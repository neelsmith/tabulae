import better.files._
import better.files.File._
import better.files.Dsl._
import java.io.{File => JFile}


/** Object for converting tabular source to FST statements.
*/
object AdjectiveRulesInstaller {

  /** Write FST rules for all adjective data in a directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
  * @param targetFile File to write FST statements to.
  */
  def apply(srcDir: File, targetFile: File): Unit = {
    val adjectiveFst = fstForAdjectiveRules(srcDir)
    if(adjectiveFst.nonEmpty) {
      targetFile.overwrite(adjectiveFst)
    } else {}
  }


  /** Compose FST statements for all tables of
  * adjective data found in a directory.
  *
  * @param srcDir Directory with lexical tables.
  */
  def fstForAdjectiveRules(srcDir: File) : String = {

    val adjectivesFiles = srcDir.glob("*.cex").toVector
    val rules = adjectivesFiles.flatMap(f =>
      f.lines.toVector.filter(_.nonEmpty).drop(1))
    val fst = adjectiveRulesToFst(rules.toVector)
    if (fst.nonEmpty) {
      "$adjectiveinfl$ = " + fst + "\n\n$adjectiveinfl$\n"
    } else {
      ""
    }
  }

  /** Compose FST for a single delimited-text line of a lexical
  * data table.
  *
  * @param line Line of data.
  */
  def adjectiveRuleToFst(line: String) : String = {
    val cols = line.split("#")
    if (cols.size < 7) {
      println(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
    } else {

      val fst = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").
        replaceAll("\\.","\\\\.")
      val inflClass = cols(1).replaceAll("_","\\_")
      val inflString = cols(2) // DataInstaller.toFstAlphabet(cols(2))
      val grammGender = cols(3)
      val grammCase = cols(4)
      val grammNumber = cols(5)
      val degree = cols(6)
      fst.append(s" <${inflClass}><adj>${inflString}<${grammGender}><${grammCase}><${grammNumber}><${degree}><u>${ruleUrn}</u>").toString
    }
  }

  /** Compose a single FST string for a Vector of rules
  * stated as one line of delimited-text each.
  *
  * @param data Vector of rules strings.
  */
  def adjectiveRulesToFst(data: Vector[String]) : String = {
    data.map(adjectiveRuleToFst(_)).mkString(" |\\\n")
  }

}
