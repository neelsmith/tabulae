import better.files._
import better.files.File._
import better.files.Dsl._
import java.io.{File => JFile}



object NounDataInstaller {



  /** Creates FST file for each CEX file of
  * noun stems.
  *
  * @param srcDir Project directory.
  */
  def apply(dataSource: File, repo: File, corpus: String) = {
    /*

    val lexDirectory = Utils.dir(repo / s"parsers/${corpus}/lexica")
    val nounsDir = file( s"${dataSource}/${corpus}/stems-tables/nouns")
    val nounsOpt = (nounsDir) ** "*cex"
    val nounsFiles = nounsOpt.get

    for (f <- nounsFiles) {
      val lexName = "lex-nouns-"+ f.getName().replaceFirst(".cex$", ".fst")
      val fstFile = lexDirectory /  lexName

      // omit empty lines and header
      val dataLines = Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1)
      val fstLines = NounDataInstaller.nounLinesToFst(dataLines)

      new PrintWriter(fstFile) { write(fstLines); close }
    }*/
  }

  /** Translates one line of CEX data documenting a noun stem
  * to a corresponding single line of FST.
  *
  * @param line CEX line to translate.
  */
  def nounLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 5) {
      //StemUrn#LexicalEntity#Stem#Gender#Class
      println(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n s${line}")
    } else {
      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexEntity = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val  stem = cols(2)
      val  gender = cols(3)
      val declClass = cols(4)

      //fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<verb><${princPart}>")

      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexEntity}</u>${stem}<noun><${gender}><${declClass}>")
      fstBuilder.toString

      //<u>vienna\.n3\_1</u><u>lexent\.n3</u>t<noun><fem><h_hs><inflacc>
    }
  }

  /** Convert a Vector of noun stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one noun stem in CEX form.
  */
  def nounLinesToFst(data: Vector[String]) : String = {
    data.map(nounLineToFst(_)).mkString("\n") + "\n"
  }





}
