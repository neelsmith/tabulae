import better.files._
import better.files.File._
import better.files.Dsl._
import java.io.{File => JFile}


/** Object for converting tabular source to FST statements.
*/
object AdverbRulesInstaller {

  /** Write FST rules for all adverb data in a directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
  * @param targetFile File to write FST statements to.
  */
  def apply(srcDir: File, targetFile: File): Unit = {
    val adverbFst = fstForAdverbRules(srcDir)
    if(adverbFst.nonEmpty) {
      targetFile.overwrite(adverbFst)
    } else {}
  }


  /** Compose FST statements for all tables of
  * adverb data found in a directory.
  *
  * @param srcDir Directory with lexical tables.
  */
  def fstForAdverbRules(srcDir: File) : String = {
    val adverbsFiles = srcDir.glob("*.cex").toVector
    val rules = adverbsFiles.flatMap(f =>
      f.lines.toVector.filter(_.nonEmpty).drop(1))
    val fst = adverbRulesToFst(rules.toVector)
    if (fst.nonEmpty) {
      "$adverbinfl$ = " + fst + "\n\n$adverbinfl$\n"
    } else {
      ""
    }
  }

  /** Compose FST for a single delimited-text line of a lexical
  * data table.
  *
  * @param line Line of data.
  */
  def adverbRuleToFst(line: String) : String = {
    //advnfl.us_a_um1#us_a_um#e#pos
    val cols = line.split("#")
    if (cols.size < 4) {
      println(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
    } else {

      val fst = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").
        replaceAll("\\.","\\\\.")
      val inflClass = cols(1).replaceAll("_","\\_")
      val inflString = cols(2)
      val degree = cols(3)

      fst.append(s" <${inflClass}><adv>${inflString}<${degree}><u>${ruleUrn}</u>").toString
    }
  }

  /** Compose a single FST string for a Vector of rules
  * stated as one line of delimited-text each.
  *
  * @param data Vector of rules strings.
  */
  def adverbRulesToFst(data: Vector[String]) : String = {
    data.map(adverbRuleToFst(_)).mkString(" |\\\n")
  }

}
