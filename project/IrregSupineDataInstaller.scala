import better.files._
import better.files.File._
import better.files.Dsl._


object IrregSupineDataInstaller {

  /** Creates FST file for each CEX file of
  * irregular verb stems.
  *
  * @param dataSource Root directory of morphological data set.
  * @param targetFile File to write FST statements to.
  */
  def apply(dataSource: File, targetFile: File) = {
    val irregSupineFst = fstForIrregSupineData(dataSource)
    if (irregSupineFst.nonEmpty) {
      targetFile.overwrite(irregSupineFst)
    } else {}
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForIrregSupineData(dir: File) : String = {
    val supineFiles = dir.glob("*.cex").toVector

    val fstLines = for (f <- supineFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      supineLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

  def supineLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 4) {
      println(s"${cols.size} is the wrong number of columns for a supine\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {

      //proof.irrigrd2#lexent.n15868#eundum#nom
      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString =  cols(2)
      val grammCase = cols(3)


      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<${grammCase}><irregsupn>")
      fstBuilder.toString
    }
  }

  /** Convert a Vector of infinitive stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one infinitive stem in CEX form.
  */
  def supineLinesToFst(data: Vector[String]) : String = {
    data.map(supineLineToFst(_)).mkString("\n") + "\n"
  }


}
