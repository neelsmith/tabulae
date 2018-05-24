import sbt._
import scala.io.Source
import java.io.PrintWriter


/** Object for converting tabular source to FST statements.
*/
object IndeclRulesInstaller {

  /** Write FST rules for all noun data in a directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
  * @param targetFile File to write FST statements to.
  */
  def apply(srcDir: File, targetFile: File): Unit = {
    val indeclFst = fstForIndeclRules(srcDir)
    //println(s"Write ${indeclFst.size} chars of rules for indeclinables to " + targetFile)
    new PrintWriter(targetFile) { write(indeclFst ); close }
  }


  /** Compose FST statements for all tables of
  * noun data found in a directory.
  *
  * @param srcDir Directory with lexical tables.
  */
  def fstForIndeclRules(srcDir: File) : String = {
    val indeclOpt = (srcDir) ** "*cex"
    val indeclFiles = indeclOpt.get
    println("\tbuilding inflection rules for indeclinables from " + srcDir)

    val rules = indeclFiles.flatMap(f => Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1))
    //println("GOT THESE RULES FOR INDECLS: " + rules)
    val fst = indeclRulesToFst(rules.toVector)

    if (fst.nonEmpty) {
      "$indeclinfl$ = " + fst + "\n\n$indeclinfl$\n"
    } else {
      ""
    }
  }

  /** Compose FST for a single delimited-text line of a lexical
  * data table.
  *
  * @param line Line of data.
  */
  def indeclRuleToFst(line: String) : String = {
    val cols = line.split("#")
    if (cols.size < 2) {
      println(s"Wrong number of columns ${cols.size}.\nCould not parse rule:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse rule:\n${line}")
    } else {
      val fst = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").
        replaceAll("\\.","\\\\.")
      val pos = cols(1)

      fst.append(s"<${pos}><indecl><u>${ruleUrn}</u>").toString
    }
  }

  /** Compose a single FST string for a Vector of rules
  * stated as one line of delimited-text each.
  *
  * @param data Vector of rules strings.
  */
  def indeclRulesToFst(data: Vector[String]) : String = {
    data.map(indeclRuleToFst(_)).mkString(" |\\\n")
  }

}
