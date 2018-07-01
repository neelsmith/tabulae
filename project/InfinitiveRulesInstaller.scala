
import better.files._
import better.files.File._
import better.files.Dsl._


/** Object for converting tabular source to FST statements.
*/
object InfinitiveRulesInstaller {

  /** Write FST rules for all infinitive data in a directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
  * @param targetFile File to write FST statements to.
  */
  def apply(srcDir: File, targetFile: File): Unit = {
    val infinitiveFst = fstForInfinitiveRules(srcDir)
    if(infinitiveFst.nonEmpty) {
      targetFile.overwrite(infinitiveFst)
    } else {}
  }


  /** Compose FST statements for all tables of
  * infinitive data found in a directory.
  *
  * @param srcDir Directory with lexical tables.
  */
  def fstForInfinitiveRules(srcDir: File) : String = {
    val fileList = srcDir.glob("*.cex").toVector
    if (fileList.isEmpty) {
      ""
    } else {
      val rules = fileList.flatMap(f => f.lines.toVector.filter(_.nonEmpty).drop(1) ) //Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1))

      val fst = infinitiveRulesToFst(rules)

      if (fst.nonEmpty) {
        "$infinitiveinfl$ = " + fst + "\n\n$infinitiveinfl$\n"
      } else {
        ""
      }
    }
  }

  /** Compose FST for a single delimited-text line of a lexical
  * data table.
  *
  * @param line Line of data.
  */
  def infinitiveRuleToFst(line: String) : String = {
    val cols = line.split("#")
    //lverbinfl.are_inf1#conj1#are#pres#act
    if (cols.size < 5) {
      println(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {
      val fst = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").
        replaceAll("\\.","\\\\.")  //infinitiveinfl.w_pp3_aor_indic3b
      val inflClass = cols(1).replaceAll("_","\\_")  //#w_pp3
      val inflString = cols(2) //.toFstAlphabet(cols(2)) // #e
      val tense = cols(3)
      val voice = cols(4)

      fst.append(s" <${inflClass}><infin>${inflString}<${tense}><${voice}><u>${ruleUrn}</u>").toString
    }
  }

  /** Compose a single FST string for a Vector of rules
  * stated as one line of delimited-text each.
  *
  * @param data Vector of rules strings.
  */
  def infinitiveRulesToFst(data: Vector[String]) : String = {
    data.map(infinitiveRuleToFst(_)).mkString(" |\\\n")
  }

}
