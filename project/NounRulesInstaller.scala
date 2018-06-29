import better.files._
import better.files.File._
import better.files.Dsl._
import java.io.{File => JFile}


/** Object for converting tabular source to FST statements.
*/
object NounRulesInstaller {

  /** Write FST rules for all noun data in a directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
  * @param targetFile File to write FST statements to.
  */
  def apply(srcDir: File, targetFile: File): Unit = {
    val nounFst = fstForNounRules(srcDir)
    println(s"Write ${nounFst.size} chars of noun rules to " + targetFile)
    //new PrintWriter(targetFile) { write(nounFst ); close }
  }


  /** Compose FST statements for all tables of
  * noun data found in a directory.
  *
  * @param srcDir Directory with lexical tables.
  */
  def fstForNounRules(srcDir: File) : String = {

    val nounsFiles = srcDir.glob("*.cex").toVector
    println("\tbuilding inflection rules for nouns from " + srcDir)

    val rules = nounsFiles.flatMap(f =>
      f.lines.toVector.filter(_.nonEmpty).drop(1))
    val fst = nounRulesToFst(rules.toVector)
    if (fst.nonEmpty) {
      "$nouninfl$ = " + fst + "\n\n$nouninfl$\n"
    } else {
      ""
    }
  }

  /** Compose FST for a single delimited-text line of a lexical
  * data table.
  *
  * @param line Line of data.
  */
  def nounRuleToFst(line: String) : String = {
    val cols = line.split("#")
    if (cols.size < 6) {
      println(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
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

      fst.append(s" <${inflClass}><noun>${inflString}<${grammGender}><${grammCase}><${grammNumber}><u>${ruleUrn}</u>").toString
    }
  }

  /** Compose a single FST string for a Vector of rules
  * stated as one line of delimited-text each.
  *
  * @param data Vector of rules strings.
  */
  def nounRulesToFst(data: Vector[String]) : String = {
    data.map(nounRuleToFst(_)).mkString(" |\\\n")
  }

}
