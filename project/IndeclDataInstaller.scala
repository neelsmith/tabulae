import sbt._
import scala.io.Source
import java.io.PrintWriter


object IndeclDataInstaller {

  /** Creates FST file for each CEX file of
  * noun stems.
  *
  * @param dataSource Root directory of morphological data set.
  * @param repo Root directory  of tabulae repository.
  * @param corpus Name of corpus
  */
  def apply(dataSource: File, repo: File, corpusName: String) = {
    val corpus = Utils.dir(repo / s"parsers/${corpusName}")
    val lexDirectory = Utils.dir(corpus / "lexica")

    val indeclSourceDir = file( s"${dataSource}/${corpusName}/stems-tables/indeclinables")
    val fst = fstForIndeclData(indeclSourceDir)

    val fstFile = lexDirectory / "lexicon-indeclinables.fst"
    new PrintWriter(fstFile) { write(fst); close }
  }


  /** Create a single FST string from all CEX
  * files in a given directory.
  *
  * @param dir Directory with CEX data.
  */
  def fstForIndeclData(dir: File) : String = {
    val indeclOpt = (dir) ** "*cex"
    val indeclFiles = indeclOpt.get

    val fstLines = for (f <- indeclFiles) yield {
      // omit empty lines and header
      val dataLines = Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1)
      IndeclDataInstaller.indeclLinesToFst(dataLines)
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


      fstBuilder.append(s"<u>${stemUrn}</u><u>${lexEntity}</u>${stem}<indecl><${pos}>")
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
