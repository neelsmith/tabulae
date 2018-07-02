import better.files._
import better.files.File._
import better.files.Dsl._


object IrregInfinitiveDataInstaller {

  /** Creates FST file for each CEX file of
  * irregular verb stems.
  *
  * @param dataSource Root directory of morphological data set.
  * @param targetFile File to write FST statements to.
  */
  def apply(dataSource: File, targetFile: File) = {
    val irregInfinitiveFst = fstForIrregInfinitiveData(dataSource)
    if (irregInfinitiveFst.nonEmpty) {
      targetFile.overwrite(irregInfinitiveFst)
    } else {}
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForIrregInfinitiveData(dir: File) : String = {
    val infinitiveFiles = dir.glob("*.cex").toVector

    val fstLines = for (f <- infinitiveFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      infinitiveLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

  def infinitiveLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 5) {
      println(s"${cols.size} is the wrong number of columns for an infinitive\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {

      //ag.irrinf1#lexent.n46529#esse#pres#act
      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString =  cols(2)
      val tense = cols(3)
      val voice = cols(4)



      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<${tense}><${voice}><irreginfin>")
      fstBuilder.toString
    }
  }

  /** Convert a Vector of infinitive stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one infinitive stem in CEX form.
  */
  def infinitiveLinesToFst(data: Vector[String]) : String = {
    data.map(infinitiveLineToFst(_)).mkString("\n") + "\n"
  }


}
