import better.files._
import better.files.File._
import better.files.Dsl._


object IrregParticipleDataInstaller {

  /** Creates FST file for each CEX file of
  * irregular verb stems.
  *
  * @param dataSource Root directory of morphological data set.
  * @param targetFile File to write FST statements to.
  */
  def apply(dataSource: File, targetFile: File) = {
    val irregPtcplFst = fstForIrregPtcplData(dataSource)
    println("\n\nINSTALLING IRTREG PTP FST:\n" + irregPtcplFst)

    if (irregPtcplFst.nonEmpty) {
      targetFile.overwrite(irregPtcplFst)
    } else {}
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForIrregPtcplData(dir: File) : String = {
    val participleFiles = dir.glob("*.cex").toVector

    val fstLines = for (f <- participleFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      participleLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

  def participleLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 8) {
      println(s"${cols.size} is the wrong number of columns for a participle\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {

      //proof.irrinf1#lexent.n15868#iens#masc#nom#sg#pres#act
      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString =  cols(2)
      val gender = cols(3)
      val grammCase = cols(4)
      val num = cols(5)
      val tense = cols(6)
      val voice = cols(7)

      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<${gender}><${grammCase}><${num}><${tense}><${voice}><irregptcpl>")
      fstBuilder.toString
    }
  }

  /** Convert a Vector of participle stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one participle stem in CEX form.
  */
  def participleLinesToFst(data: Vector[String]) : String = {
    data.map(participleLineToFst(_)).mkString("\n") + "\n"
  }


}
