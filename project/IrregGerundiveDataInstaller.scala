import better.files._
import better.files.File._
import better.files.Dsl._


object IrregGerundiveDataInstaller {

  /** Creates FST file for each CEX file of
  * irregular verb stems.
  *
  * @param dataSource Root directory of morphological data set.
  * @param targetFile File to write FST statements to.
  */
  def apply(dataSource: File, targetFile: File) = {
    val irregGerundiveFst = fstForIrregGerundiveData(dataSource)
    if (irregGerundiveFst.nonEmpty) {
      targetFile.overwrite(irregGerundiveFst)
    } else {}
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForIrregGerundiveData(dir: File) : String = {
    val gerundiveFiles = dir.glob("*.cex").toVector

    val fstLines = for (f <- gerundiveFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      gerundiveLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

  def gerundiveLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 6) {
      println(s"${cols.size} is the wrong number of columns for a gerundive\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {

      //proof.irrigrdv3#lexent.n15868#eundum#neut#acc#sg
      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString =  cols(2)
      val gender =  cols(3)
      val grammCase = cols(4)
      val num = cols(5)

      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<${gender}><${grammCase}><${num}><irreggrndv>")
      fstBuilder.toString
    }
  }

  /** Convert a Vector of gerundive stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one infinitive stem in CEX form.
  */
  def gerundiveLinesToFst(data: Vector[String]) : String = {
    data.map(gerundiveLineToFst(_)).mkString("\n") + "\n"
  }


}
