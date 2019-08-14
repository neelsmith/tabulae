package edu.holycross.shot.tabulae.builder

import better.files.{File => ScalaFile, _}
import better.files.Dsl._


/** Write the top-level transducer, latin.fst.
*/
object ParserComposer {

  /** Static file header. */
  val header = """
%% latin.fst : a Finite State Transducer for ancient latin morphology
%
% All symbols used in the FST:
"""

  /** Write latin.fst for parser in a given directory.
  *  This
  *
  * @param projectDir Directory where parser is to be written.
  */
  def apply(projectDir: ScalaFile) : Unit = {
    if (! projectDir.exists){throw new Exception("ParserComposer:  cannot compose parser FST for empty or non-existent directory.")}

    val lexica = projectDir / "lexica"
    if (! lexica.exists) { throw new Exception("ParserComposer:  cannot compose parser FST until lexica have been installed.")}

    val latin = StringBuilder.newBuilder
    latin.append(header)
    latin.append("#include \"" + projectDir.toString + "/symbols.fst\"\n\n" )

    latin.append("% Dynamically loaded lexica of stems:\n$stems$ = ")

    latin.append(lexiconFiles(lexica).mkString(" |\\\n") + "\n\n")

    latin.append("% Dynamically loaded inflectional rules:\n$ends$ = \"<" + projectDir.toString + "/inflection.a>\"")

    latin.append("\n\n% Morphology data is the crossing of stems and endings:\n$morph$ = $stems$ <div> $ends$\n\n")

    latin.append("% Acceptor (1) filters for content satisfying requirements for\n")
    latin.append("% morphological analysis and  (2) maps from underlying to surface form\n")
    latin.append("% by suppressing analytical symbols, and allowing only surface strings.\n")
    latin.append("$acceptor$ = \"<" + projectDir.toString + "/acceptor.a>\"\n\n" )

    latin.append("% Final transducer:\n")
    latin.append("$morph$ || $acceptor$\n\n")

    val latinFile = projectDir / "latin.fst"
    latinFile.overwrite(latin.toString)
  }


  /**  Compose a single FST string listing all lexicon
  *  files. Note that SFST efficiently uses lexicon files
  * like these directly from uncompiled FST source, so
  * latin.fst can include them without compilation.
  *
  * @param dir Root directory for all lexicon files.
  */
  def lexiconFiles(dir: ScalaFile): Vector[String] = {
    val files = dir.glob("*.fst").toVector
    files.filter(_.nonEmpty).map(f => "\"" + f.toString() + "\"").toVector
  }

}
