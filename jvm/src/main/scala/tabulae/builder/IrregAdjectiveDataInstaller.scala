package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._



object IrregAdjectiveDataInstaller {

  /** Creates FST file for each CEX file of
  * irregular verb stems.
  *
  * @param dataSource Root directory for corpus-specific data sources.
  * @param corpusList List of corpora within dataSource directory.
  * @param targetFile File to write FST statements to.  The directory
  * containing targetFile must already exist.
  */
  def apply(dataSource: File, corpusList: Vector[String], targetFile: File) = {
    val srcData = for (corpus <- corpusList) yield {
      val irregAdjectivesDir = dataSource / corpus / "irregular-stems/adjectives"
      if (! irregAdjectivesDir.exists) {
        mkdirs(irregAdjectivesDir)
      }
      val data = fstForIrregAdjectiveData(irregAdjectivesDir)
      data
    }
    val fst = srcData.filter(_.nonEmpty).mkString("\n")
    if (fst.nonEmpty) {
      // Directory containing targetFile must already exist!
      targetFile.overwrite(fst)
    } else {}
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory withn tables of verb data.
  */
  def fstForIrregAdjectiveData(dir: File) : String = {
    val adjectiveFiles = dir.glob("*.cex").toVector

    val fstLines = for (f <- adjectiveFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      adjectiveLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

  def adjectiveLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 7) {
      println(s"${cols.size} is the wrong number of columns for an adjective\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {

      //ag.irrn1m#lexent.n5575#bos#masc#nom#sg
      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString =  cols(2)
      val gender = cols(3)
      val cse = cols(4)
      val num = cols(5)
      val deg = cols(6)

      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<${gender}><${cse}><${num}><${deg}><irregadj>")
      fstBuilder.toString
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


}
