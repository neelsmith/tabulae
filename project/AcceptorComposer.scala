import sbt._
import java.io.PrintWriter
import scala.io.Source

import Path.rebase

/** Factory object for composing and writing to a file the top-level
* acceptor transducer, acceptor.fst, in the root of the project FST build.
*
* The acceptor transducer is the final stage in the morphological pipeline.
* See documentation on the tabulae github repository for details.
*/
object AcceptorComposer {

  /** Compose acceptor.fst and the intermediate fst files
  * it depends on for a named corpus.
  *
  * @param repo Root of Kanónes repository.
  * @param corpus Corpus to build acceptor for.
  */
  def apply(repo: File, corpus: String): Unit = {
    val projectDir =  repo / s"parsers/${corpus}"
    composeMainAcceptor(projectDir)
    println("\nSecondary generators are necessary for verbs to distinguish prin.part as well as inflectional category")
    copySecondaryAcceptors(repo, corpus)
    rewriteSecondaryAcceptors(projectDir)

    composeVerbStems(projectDir)
    composeVerbAcceptor(projectDir)
  }

  /** Write verb.fst, the top-level transducer for verbs in the
  * the FST chain.  Squashed URN representations are generated for
  * underlying patterns like:
  * <u>1.1</u><u>2.2</u>am<verb><are_vb>::<are_vb><verb>i<1st><sg><pft><indic><act><u>3.3</u>
  *
  * @param projectDir The directory for the corpus-specific
  * parser where acceptor.fst should be written.
  */
  def composeVerbAcceptor(projectDir: File): Unit = {
    val fst = StringBuilder.newBuilder
    fst.append("#include \"" + projectDir.toString + "/symbols.fst\"\n\n")
    fst.append("%%%\n%%% Adjust stem  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n%%%\n")
    fst.append("$stems_acceptors$ = \"<" +  projectDir.toString +     "/acceptors/verbstems.a>\"\n")


    fst.append("%%%\n%%% The URN squasher for verbs %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n%%%\n")
    fst.append("$=verbclass$ = [#verbclass#]\n")
    fst.append("$squashverburn$ = <u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$\\:\\:$=verbclass$ <verb>[#stemchars#]* [#person#] [#number#] [#tense#] [#mood#] [#voice#]<u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u>\n\n")

    fst.append("$stems_acceptors$ ||  $squashverburn$\n")


    //fst.append(mainVerbAcceptor)
    val acceptorFile = projectDir / "verb.fst"
    new PrintWriter(acceptorFile) { write(fst.toString); close }
  }

  /** Write verb_stems.fst, the union of all acceptors
  * in acceptors/verb, for a corpus-specific parser.
  *
  * @param projectDir  The directory for the corpus-specific
  * parser where verb_stems.fst should be written.
  */
  def composeVerbStems(projectDir: File): Unit = {

    val src = projectDir / "acceptors/verb"
    val fileOpt = (src) ** "*fst"
    val fileList = fileOpt.get

    val fileNames = for (f <- fileList) yield {
      "\"<" + f.getAbsolutePath().replaceFirst(".fst$", ".a") + ">\""
    }
    val heading = "% verbstems.fst\n% A transducer to generate principal part stems for inflected forms of verbs.\n\n"
    val acceptorFile = projectDir / "acceptors/verbstems.fst"
    new PrintWriter(acceptorFile) { write(heading + fileNames.mkString(" || ")); close }
  }

  /** Write acceptor.fst, the final transducer in the
  * the FST chain.  Should only include acceptors for
  * components that actually appear in the project's data.
  *
  * @param projectDir The directory for the corpus-specific
  * parser where acceptor.fst should be written.
  */
  def composeMainAcceptor(projectDir: File): Unit = {
    val dir = DataInstaller.dir(projectDir)
    val fst = StringBuilder.newBuilder
    // automatically included
    fst.append("#include \"" + dir.toString + "/symbols.fst\"\n")

    fst.append(nounAcceptor(projectDir) + "\n")
    fst.append(indeclAcceptor(projectDir) + "\n")

    fst.append(irregNounAcceptor(projectDir) + "\n")
    fst.append("$verb_pipeline$ = \"<" + dir.toString + "/verb.a>\"\n")

    fst.append("\n\n" + topLevelAcceptor(projectDir) + "\n")


    val acceptorFile = projectDir / "acceptor.fst"
    new PrintWriter(acceptorFile) { write(fst.toString); close }
    println("Wrote " + acceptorFile)
  }


  /** Copy all .fst files within the `fst/acceptors` branch
  * of the tree to the corresponding location in the build area
  * for a named corpus.
  *
  * @param repo Root of the Kanónes repository.
  * @param corpus Corpus to build
  */
  def copySecondaryAcceptors(repo: File, corpus: String): Unit = {
    val src = repo / "fst/acceptors"
    val dest = DataInstaller.dir(repo / s"parsers/${corpus}/acceptors")
    println("\tcopying secondary acceptors from " + src)
     val fst = (src) ** "*.fst"
     val fstFiles = fst.get
     val mappings: Seq[(File,File)] = fstFiles pair rebase(src, dest)
     for (m <- mappings) {
       IO.copyFile(m._1, m._2)
     }
  }


  /** Rewrite a single file by replacing all occurrences of
  * the variable name `@workDir` with the string value for the
  * work directory.
  *
  * @param f File to rewrite.
  * @param workDir Actual directory where corpus-specific
  * parser is to be built.
  */
  def rewriteFile(f: File, workDir: File): Unit = {
    val lines = Source.fromFile(f).getLines.toVector
    val rewritten = lines.map(_.replaceAll("@workdir@", workDir.toString + "/")).mkString("\n")
    new PrintWriter(f) { write(rewritten); close }
  }

  /** Filter secondary acceptor files, replacing ant-style
  * variable with actual value.
  *
  * @param projectDir Directory where corpus-specific parser
  * is to be built.
  */
  def rewriteSecondaryAcceptors(projectDir: File) : Unit = {
    val dir = projectDir / "acceptors"
    val fst = (dir) ** "*.fst"
    val fstFiles = fst.get
    println("\tfiltering acceptor files in " + dir)
    for (f <- fstFiles) {
      rewriteFile(f, projectDir)
    }
  }


  /** String defining final step of main verb acceptor. */
  def mainVerbAcceptor(dir : File): String = {
    """
$=verbclass$ = [#verbclass#]
$squashverburn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$  $separator$+$=verbclass$ <verb>[#stemchars#]* [#person#] [#number#] [#tense#] [#mood#] [#voice#]<u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>

$stem_acceptors$ || $aug$ || $squashverburn$
"""

}
  /** String defining final noun acceptor transducer.*/
  def nounAcceptor(dir : File): String = {
    """
% Noun acceptor:
$=nounclass$ = [#nounclass#]
$squashnounurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<noun>$=gender$ $=nounclass$   $separator$+ $=nounclass$  <noun> [#stemchars#]* $=gender$ $case$ $number$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
}

  /** String defining final acceptor transducer for irregular nouns.*/
  def irregNounAcceptor(dir : File): String = {
    """
% Irregular noun acceptor
$squashirregnounurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+ $gender$ $case$ $number$ <irregnoun>  $separator$+ <irregnoun><noun><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
}

/** String defining final acceptor transducer for indeclinable forms.*/
def indeclAcceptor (dir : File): String = {

  //val indeclSource = dir / "inflection/inde") ** "*.fst"
  val indeclSource = dir / "lexica/lexicon-indeclinables.fst"
  if (indeclSource.exists) {
   """
% Indeclinable form acceptor:
$=indeclclass$ = [#indeclclass#]
$squashindeclurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ <indecl> $=indeclclass$  $separator$+  $=indeclclass$ <indecl> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

/** String defining final adjective acceptor transducer.  */
def adjAcceptor(dir : File): String = {
  """
% adjective acceptor:
$=adjectiveclass$ = [#adjectiveclass#]
$squashadjurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<adj> $=adjectiveclass$   $separator$+ $=adjectiveclass$  <adj> [#stemchars#]* $=gender$ $case$ $number$ $degree$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
}
  /** String defining union of acceptors for each distinct
  * analytical pattern, followed by a transducer removing
  * all analysis-level symbols.*/

  def topLevelAcceptor(dir : File): String = {
    """
% Union of all URN squashers:
%%$acceptor$ = $verb_pipeline$ | $squashnounurn$ | $squashirregnounurn$ | $squashindeclurn$

$acceptor$ = $verb_pipeline$ |  $squashnounurn$ | $squashindeclurn$  %%| $squashadjurn$

%% Put all symbols in 2 categories:  pass
%% surface symbols through, suppress analytical symbols.
#analysissymbol# = #editorial# #urntag# <noun><verb><indecl><ptcpl><infin><vadj><adj><adv> #morphtag# #stemtype#  #separator#
#surfacesymbol# = #letter# #diacritic#
ALPHABET = [#surfacesymbol#] [#analysissymbol#]:<>
$stripsym$ = .+

%% The canonical pipeline: (morph data) -> acceptor -> parser/stripper
$acceptor$ || $stripsym$
"""
}

}
