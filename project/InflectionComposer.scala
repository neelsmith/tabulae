import better.files._
import better.files.File._
import better.files.Dsl._

/** Factory object for composing and writing to a file the top-level
* transducer defining inflectional rules, inflection.fst, in the root of
* the parser build directory.
*/
object InflectionComposer {

  val header = """
%% inflection.fst
% A transducer accepting all inflectional patterns.
%

$ending$ = """



  /**  Collects .fst files in a given directory and
  * composes fst identifiers for corresponding .a files.
  *
  * @param dir Directory with .fst files.
  */
  def inflectionFsts(dir: File): Vector[String] = {
    val files = dir.glob("*infl.fst").toVector
    files.map(f => "\"<" + f.toString().replaceFirst(".fst$", ".a") + ">\"")
  }


  /**  Working directory for corpus work directory.
  *
  * @param projectDir Working directory for a corpus parser,
  * e.g., REPO/parsers/CORPUS.
  */
  def apply(projectDir: File) : Unit = {
    val inflFiles = inflectionFsts(projectDir/"inflection")

    val fstText = StringBuilder.newBuilder
    fstText.append(header)
    fstText.append( inflFiles.mkString(" |\\\n"))
    fstText.append ("\n\n$ending$\n")
    val finalText = fstText.toString

    val fstFile = projectDir/"inflection.fst"
    fstFile.overwrite(finalText)
    /*

    if (indeclFiles.nonEmpty) {
      val fstText = StringBuilder.newBuilder
      fstText.append(header)
      fstText.append( indeclFiles.mkString(" |\\\n"))
      fstText.append ("\n\n$ending$\n")

      val finalText = fstText.toString

      new PrintWriter(fstFile) { write(finalText); close }
    } else {
      // No fst files for indeclinables
    }*/
  }

}
