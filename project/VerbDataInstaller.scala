import sbt._
import scala.io.Source
import java.io.PrintWriter


object VerbDataInstaller {


  /**
  */
  def apply(dataSource: File, repo: File, corpus: String) = {
    val lexDirectory = DataInstaller.dir(repo / s"parsers/${corpus}/lexica")

    val dir =  dataSource / s"${corpus}/stems-tables/verbs-simplex"
    val rulesOpt = (dir) ** "*cex"
    val rulesFiles = rulesOpt.get

    println("\tbuilding simplex verb stems from " + dir)

    for (f <- rulesFiles) {
      val fstFile = lexDirectory / "lex-verbs-" + f.getName().replaceFirst(".cex$", ".fst")
      // omit empty lines and header
      val dataLines = Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1)
      val fstLines = VerbDataInstaller.verbLinesToFst(dataLines)
      new PrintWriter(fstFile) { write(fstLines); close }
    }
  }


// model of src cex
// smyth.n23658_2#lexent.n23658#deic#w_pp3#
// model of target fst:
// <u>smyth.n62274_0</u><u>lexent.n62274</u><#>leip<verb><w_pp1>

  def verbLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 4) {
      println("Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
    } else {

      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString = "<#>" + cols(2) //.toFstAlphabet(cols(2))
      val princPart = cols(3)

      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<verb><${princPart}>")

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
