import better.files._
import better.files.File._
import better.files.Dsl._
import java.io.{File => JFile}



object AdjectiveDataInstaller {

  /** Write FST rules for all adjective stem data in a given directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
  * @param targetFile File to write FST statements to.
  */
  def apply(srcDir: File, targetFile: File) = {
      val adjectiveFst = fstForAdjectiveData(srcDir)
      if (adjectiveFst.nonEmpty) {
        targetFile.overwrite(adjectiveFst)
      } else {}
  }

  /** Translates one line of CEX data documenting a adjective stem
  * to a corresponding single line of FST.
  *
  * @param line CEX line to translate.
  */
  def adjectiveLineToFst(line: String) : String = {
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

      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexEntity}</u>${stem}<adjective><${gender}><${declClass}>")
      fstBuilder.toString

      //<u>vienna\.n3\_1</u><u>lexent\.n3</u>t<adjective><fem><h_hs><inflacc>
    }
  }

  /** Convert a Vector of adjective stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one adjective stem in CEX form.
  */
  def adjectiveLinesToFst(data: Vector[String]) : String = {
    data.map(adjectiveLineToFst(_)).mkString("\n") + "\n"
  }

  /** Create FST string for adjective tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForAdjectiveData(dir: File) : String = {
    val adjectiveFiles = dir.glob("*.cex").toVector
    val fstLines = for (f <- adjectiveFiles) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1) //Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1)
      adjectiveLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

}
