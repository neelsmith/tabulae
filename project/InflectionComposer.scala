import sbt._
import java.io.PrintWriter

/** Factory object for composing and writing to a file the top-level
* transducer creating the morphological lexica, inflection.fst in the root of
* the project FST build.
*/
object InflectionComposer {


  val header = """
%% inflection.fst
% A transducer accepting all inflectional patterns.
%

$ending$ = """

  def inflectionFsts(dir: File): Vector[String] = {
    val filesOpt = (dir) ** "*infl.fst"
    val files = filesOpt.get
    files.map(f => "\"<" + f.toString().replaceFirst(".fst$", ".a") + ">\"").toVector
  }

  def apply(projectDir: File) : Unit = {

    val fstText = StringBuilder.newBuilder
    fstText.append(header)
    fstText.append( inflectionFsts(projectDir / "inflection").mkString(" |\\\n"))
    fstText.append ("\n\n$ending$\n")

    val finalText = fstText.toString
    val fstFile = projectDir / "inflection.fst"
    new PrintWriter(fstFile) { write(finalText); close }
  }

}
