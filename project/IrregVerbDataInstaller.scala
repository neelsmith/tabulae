import sbt._
import scala.io.Source
import java.io.PrintWriter


object IrregVerbDataInstaller {

  /**
  */
  def apply(dataSource: File, repo: File, corpus: String) = {
    val lexDirectory = repo / s"parsers/${corpus}/lexica"
    if (! lexDirectory.exists) { lexDirectory.mkdir}

    val verbSourceDir = dataSource / s"${corpus}/stems-tables/verbs-simplex"
    val fst = fstForVerbData(verbSourceDir)
    if (fst.nonEmpty){
      val fstFile = lexDirectory / "lexicon-verbs.fst"
      new PrintWriter(fstFile){write(fst); close;}
    } else {}
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForVerbData(dir: File) : String = {
    val verbOpt = (dir) ** "*cex"
    val verbFiles = verbOpt.get
    val fstLines = for (f <- verbFiles) yield {
      // omit empty lines and header
      val dataLines = Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1)
      VerbDataInstaller.verbLinesToFst(dataLines)
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
