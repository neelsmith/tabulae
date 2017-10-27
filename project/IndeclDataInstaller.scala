import sbt._
import scala.io.Source
import java.io.PrintWriter


object IndeclDataInstaller {

  /** Creates FST file for each CEX file of
  * noun stems.
  *
  * @param srcDir Project directory.
  */
  def apply(dataSource: File, repo: File, corpus: String) = {
    val lexDirectory = DataInstaller.madeDir(repo / s"parsers/${corpus}/lexica")
    val indeclDir = file( s"${dataSource}/${corpus}/stems-tables/indeclinables")
    val indeclOpt = (indeclDir) ** "*cex"
    val indeclFiles = indeclOpt.get

    for (f <- indeclFiles) {
      val lexName = "lex-indeclinables-"+ f.getName().replaceFirst(".cex$", ".fst")
      val fstFile = lexDirectory /  lexName

      // omit empty lines and header
      val dataLines = Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1)
      val fstLines = IndeclDataInstaller.indeclLinesToFst(dataLines)

      new PrintWriter(fstFile) { write(fstLines); close }
    }
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
      println(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
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

  /** Convert a Vector of noun stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one noun stem in CEX form.
  */
  def indeclLinesToFst(data: Vector[String]) : String = {
    data.map(indeclLineToFst(_)).mkString("\n") + "\n"
  }





}
