import better.files._
import better.files.File._
import better.files.Dsl._


object IrregVerbDataInstaller {

  /** Creates FST file for each CEX file of
  * irregular verb stems.
  *
  * @param dataSource Root directory of morphological data set.
  * @param targetFile File to write FST statements to.
  */
  def apply(dataSource: File, targetFile: File) = {
    val irregVerbFst = fstForIrregVerbData(dataSource)
    if (irregVerbFst.nonEmpty) {
      targetFile.overwrite(irregVerbFst)
    } else {}
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForIrregVerbData(dir: File) : String = {
    val verbFiles = dir.glob("*.cex").toVector

    val fstLines = for (f <- verbFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      verbLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }
// model of src cex
// smyth.n23658_2#lexent.n23658#deic#w_pp3#
// model of target fst:
// <u>smyth.n62274_0</u><u>lexent.n62274</u><#>leip<verb><w_pp1>

  def verbLineToFst(line: String) : String = {

    val cols = line.split("#")

    if (cols.size < 4) {
      println(s"${cols.size} is the wrong number of columns for a verb\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {

      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString = "<#>" + cols(2) //.toFstAlphabet(cols(2))

//"ag.irrv1#lexent.n46529#sum#1st#sg#pres#indic#act"
      val pers = cols(3)
      val num = cols(4)
      val tns = cols(5)
      val mood = cols(6)
      val vce = cols(7)
//<u>ag\.irrv1</u><u>lexent\.n46529</u><#>sum<1st><sg><pres><indic><act><irregcverb>
      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<${pers}><${num}><${tns}><${mood}><${vce}><irregcverb>")
      fstBuilder.toString
    }
  }

  /** Convert a Vector of noun stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one noun stem in CEX form.
  */
  def verbLinesToFst(data: Vector[String]) : String = {
    data.map(verbLineToFst(_)).mkString("\n") + "\n"
  }


}
