import better.files.{File => ScalaFile, _}
import better.files.Dsl._

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
  def apply(repo: ScalaFile, corpus: String): Unit = {
    val projectDir =  repo/"parsers"/corpus
    composeMainAcceptor(projectDir)
  }

  /** Write acceptor.fst, the final transducer in the
  * the FST chain.  Should only include acceptors for
  * components that actually appear in the project's data.
  *
  * @param projectDir The directory for the corpus-specific
  * parser where acceptor.fst should be written.
  */
  def composeMainAcceptor(projectDir: ScalaFile): Unit = {
    if (! projectDir.exists()) {mkdirs(projectDir)}
    val fst = StringBuilder.newBuilder
    // automatically included
    fst.append("#include \"" + projectDir.toString + "/symbols.fst\"\n")
    //fst.append(nounAcceptor(projectDir) + "\n")
    //fst.append(irregNounAcceptor(projectDir) + "\n")

    // MANAGE IN A FOR COMPREHENSION
    fst.append(indeclAcceptor(projectDir) + "\n")
    fst.append(verbAcceptor(projectDir) + "\n")
    fst.append(irregVerbAcceptor(projectDir) + "\n")

    fst.append("\n\n" + topLevelAcceptor(projectDir) + "\n")

    val acceptorFile = projectDir/"acceptor.fst"
    acceptorFile.overwrite(fst.toString)
  }




  /** Rewrite a single file by replacing all occurrences of
  * the variable name `@workDir` with the string value for the
  * work directory.
  *
  * @param f File to rewrite.
  * @param workDir Actual directory where corpus-specific
  * parser is to be built.
  */
  def rewriteFile(f: ScalaFile, workDir: ScalaFile): Unit = {
    val lines = f.lines.toVector
    val rewritten = lines.map(_.replaceAll("@workdir@", workDir.toString + "/")).mkString("\n")
    f.overwrite(rewritten)
  }


  def irregVerbAcceptor(dir : ScalaFile): String = {
    if (includeIrregVerbs(dir) ) {
      """
$squashirregverburn$ =  <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+[#person#] [#number#] [#tense#] [#mood#] [#voice#]<irregcverb><div><irregcverb><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
    } else {""}
  }


  /** String defining final step of main verb acceptor. */
  def verbAcceptor(dir : ScalaFile): String = {
    if (includeVerbs(dir) ) {
    """
$=verbclass$ = [#verbclass#]
$squashverburn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$  $separator$+$=verbclass$ <verb>[#stemchars#]* [#person#] [#number#] [#tense#] [#mood#] [#voice#]<u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
//$squashverburn$

} else { "" }
}
  /** String defining final noun acceptor transducer.*/
  def nounAcceptor(dir : ScalaFile): String = {
    """
% Noun acceptor:
$=nounclass$ = [#nounclass#]
$squashnounurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<noun>$=gender$ $=nounclass$   $separator$+ $=nounclass$  <noun> [#stemchars#]* $=gender$ $case$ $number$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
}

  /** String defining final acceptor transducer for irregular nouns.*/
  def irregNounAcceptor(dir : ScalaFile): String = {
    """
% Irregular noun acceptor
$squashirregnounurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+ $gender$ $case$ $number$ <irregnoun>  $separator$+ <irregnoun><noun><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
}

/** String defining final acceptor transducer for indeclinable forms.*/
def indeclAcceptor (dir : ScalaFile): String = {
  if (includeIndecls(dir) ) {  """
% Indeclinable form acceptor:
$=indeclclass$ = [#indecl#]
$squashindeclurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+  $=indeclclass$ <div>   $=indeclclass$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

/** String defining final adjective acceptor transducer.  */
def adjAcceptor(dir : ScalaFile): String = {
  """
% adjective acceptor:
$=adjectiveclass$ = [#adjectiveclass#]
$squashadjurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<adj> $=adjectiveclass$   $separator$+ $=adjectiveclass$  <adj> [#stemchars#]* $=gender$ $case$ $number$ $degree$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
}

  /** True if parser lexica include data for indeclinables.
  *
  * @param dir Directory for corpus data set.
  */
  def includeIndecls(dir: ScalaFile): Boolean = {
    val indeclSource = dir/"lexica/lexicon-indeclinables.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }


  /** True if parser lexica include entries for verbs.
  *
  * @param dir Root directory of work space (repo/parsers/CORPUS).
  *
  */
  def includeVerbs(dir: ScalaFile): Boolean = {
    val lexica = dir/"lexica"
    val verbsSource = lexica/"lexicon-verbs.fst"
    verbsSource.exists && verbsSource.lines.nonEmpty
  }

  def includeIrregVerbs(dir: ScalaFile): Boolean = {
    val indeclSource = dir/"lexica/lexicon-irregverbs.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }

  /** Compose FST for union of transducers squashing URNs.
  *
  * @param dir Directory for corpus data set.
  */
  def unionOfSquashers(dir: ScalaFile) : String = {
    val fst = StringBuilder.newBuilder
    fst.append("% Union of all URN squashers.\n\n$acceptor$ = ")
    //fst.append("% Union of all URN squashers:\n%%$acceptor$ = $verb_pipeline$ | $squashnounurn$ | $squashirregnounurn$ | $squashindeclurn$ \n\n$acceptor$ = $verb_pipeline$ ")

    def typesList = List(
      (includeVerbs(_),"$squashverburn$" ),
      (includeIndecls(_),"$squashindeclurn$" ),
      (includeIrregVerbs(_), "$squashirregverburn$")
    )
    val xducerList = for (xducer <- typesList) yield {
      if (xducer._1(dir)) { xducer._2} else {""}
    }
    val online = xducerList.filter(_.nonEmpty)
    if (online.isEmpty) {
      throw new Exception("AcceptorComposer:  no acceptors recognized.")
    } else {
      fst.append(xducerList.filter(_.nonEmpty).mkString(" | "))
      // |  $squashnounurn$ | $squashindeclurn$  %%| $squashadjurn$
      fst.append("\n")
      fst.toString
    }
  }

  /** String defining union of acceptors for each distinct
  * analytical pattern, followed by a transducer removing
  * all analysis-level symbols.
  *
  * @param dir Directory for corpus data set.
  */
  def topLevelAcceptor(dir : ScalaFile): String = {
    val constructed  = unionOfSquashers(dir)
  val trail = """
%% Put all symbols in 2 categories:  pass
%% surface symbols through, suppress analytical symbols.
#analysissymbol# = #editorial# #urntag# #indecl# #pos# #morphtag# #stemtype#  #separator#
#surfacesymbol# = #letter# #diacritic#
ALPHABET = [#surfacesymbol#] [#analysissymbol#]:<>
$stripsym$ = .+

%% The canonical pipeline: (morph data) -> acceptor -> parser/stripper
$acceptor$ || $stripsym$
"""
    constructed + trail
  }

}
