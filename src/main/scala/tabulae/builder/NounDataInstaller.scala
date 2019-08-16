package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._
import java.io.{File => JFile}




object NounDataInstaller {

  /** Write FST rules for all noun stem data in a given directory
  * of tabular files.
  *
  * @param dataSource Root directory for corpus-specific data sources.
  * @param corpusList List of corpora within dataSource directory.
  * @param targetFile File to write FST statements to.  The directory
  * containing targetFile must already exist.
  */
  def apply(dataSource: File, corpusList: Vector[String], targetFile: File) = {
    val srcData = for (corpus <- corpusList) yield {
      val nounsDir = dataSource / corpus / "stems-tables/nouns"
      if (! nounsDir.exists) {
        mkdirs(nounsDir)
      }
      val data = fstForNounData(nounsDir)
      data
    }
    val fst = srcData.filter(_.nonEmpty).mkString("\n")
    if (fst.nonEmpty) {
      // Directory containing targetFile must already exist!
      targetFile.overwrite(fst)
    } else {}
    /*
      val nounFst = fstForNounData(srcDir)
      if (nounFst.nonEmpty) {
        targetFile.overwrite(nounFst)
      } else {}*/
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

  /** Create FST string for noun tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForNounData(dir: File) : String = {
    val nounFiles = dir.glob("*.cex").toVector
    val fstLines = for (f <- nounFiles) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1) //Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1)
      nounLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

}
