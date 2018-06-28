import better.files._
import better.files.File._
import better.files.Dsl._


/** An object for reading data about indeclinable stems,
* and writing it in SFST notation.
*/
object IndeclDataInstaller {

  /** Creates FST file for each CEX file of
  * noun stems.
  *
  * @param dataSource Root directory of morphological data set.
  * @param targetFile File to write FST statements to.
  */
  def apply(dataSource: File, targetFile: File) = {
    val indeclFst = fstForIndeclData(dataSource)
    if (indeclFst.nonEmpty) {
      targetFile.overwrite(indeclFst)
    } else {}

  }


  /** Create a single FST string from all CEX
  * files in a given directory.
  *
  * @param dir Directory with CEX data.
  */
  def fstForIndeclData(dir: File) : String = {
    val indeclFiles = dir.glob("*.cex").toVector
    val fstLines = for (f <- indeclFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      indeclLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

  /** Translates one line of CEX data documenting a noun stem
  * to a corresponding single line of FST.
  *
  * @param line CEX line to translate.
  */
  def indeclLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 4) {
      //StemUrn#LexicalEntity#Stem#PoS
      println(s"Wrong number of columns ${cols.size}.\nCould not parse indeclinable data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse indeclinable data line:\n${line}")
    } else {
      val fstBuilder = StringBuilder.newBuilder
      val stemUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")

      val lexEntity = cols(1).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val stem = cols(2)
      val pos = cols(3)


      fstBuilder.append(s"<u>${stemUrn}</u><u>${lexEntity}</u>${stem}<${pos}>")
      fstBuilder.toString
    }
  }

  /** Convert a Vector of data for indeclinables in CEX form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one noun stem in CEX form.
  */
  def indeclLinesToFst(data: Vector[String]) : String = {
    data.map(indeclLineToFst(_)).mkString("\n") + "\n"
  }





}
