import better.files._
import better.files.File._
import better.files.Dsl._
import java.io.{File => JFile}


/** Object for converting tabular source to FST statements.
*/
object ParticipleRulesInstaller {

  /** Write FST rules for all participle data in a directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
  * @param targetFile File to write FST statements to.
  */
  def apply(srcDir: File, targetFile: File): Unit = {
    val participleFst = fstForParticipleRules(srcDir)
      if(participleFst.nonEmpty) {
        targetFile.overwrite(participleFst)
      } else {}
  }


  /** Compose FST statements for all tables of
  * participle data found in a directory.
  *
  * @param srcDir Directory with lexical tables.
  */
  def fstForParticipleRules(srcDir: File) : String = {

    val participlesFiles = srcDir.glob("*.cex").toVector
    val rules = participlesFiles.flatMap(f =>
      f.lines.toVector.filter(_.nonEmpty).drop(1))
    val fst = participleRulesToFst(rules.toVector)
    if (fst.nonEmpty) {
      "$participleinfl$ = " + fst + "\n\n$participleinfl$\n"
    } else {
      ""
    }
  }

  /** Compose FST for a single delimited-text line of a lexical
  * data table.
  *
  * @param line Line of data.
  */
  def participleRuleToFst(line: String) : String = {
    val cols = line.split("#")
    //"lverbinfl.are_ptcpl1#conj1#ans#masc#nom#sg#pres#act"
    if (cols.size < 8) {
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
      val tense = cols(6)
      val voice = cols(7)

      fst.append(s" <${inflClass}><ptcpl>${inflString}<${grammGender}><${grammCase}><${grammNumber}><${tense}><${voice}><u>${ruleUrn}</u>").toString
    }
  }

  /** Compose a single FST string for a Vector of rules
  * stated as one line of delimited-text each.
  *
  * @param data Vector of rules strings.
  */
  def participleRulesToFst(data: Vector[String]) : String = {
    data.map(participleRuleToFst(_)).mkString(" |\\\n")
  }

}
