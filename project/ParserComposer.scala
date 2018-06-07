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
  *
  * @param projectDir Directory where parser is to be written.
  */
  def apply(projectDir: ScalaFile) : Unit = {
    if (! projectDir.exists){mkdirs(projectDir)}
    val latin = StringBuilder.newBuilder
    latin.append(header)
    latin.append("#include \"" + projectDir.toString + "/symbols.fst\"\n\n" )

    latin.append("% Dynamically loaded lexica of stems:\n$stems$ = ")
    latin.append(lexiconFiles(projectDir/"lexica").mkString(" |\\\n") + "\n\n")

    latin.append("% Dynamically loaded inflectional rules:\n$ends$ = \"<" + projectDir.toString + "/inflection.a>\"")

    latin.append("\n\n% Morphology data is the crossing of stems and endings:\n$morph$ = $stems$ $separator$ $separator$ $ends$\n\n")

    latin.append("% Acceptor filters for content satisfying requirements for")
    latin.append("% morphological analysis and maps from underlying to surface form.\n")
    latin.append("$acceptor$ = \"<" + projectDir.toString + "/acceptor.a>\"\n\n" )

    latin.append("% Final transducer:\n")
    latin.append("$morph$ || $acceptor$\n\n")

    val latinFile = projectDir/"latin.fst"
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
    files.map(f => "\"" + f.toString() + "\"").toVector
  }

}
