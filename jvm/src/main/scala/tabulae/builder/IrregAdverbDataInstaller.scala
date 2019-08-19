package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._


object IrregAdverbDataInstaller {


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
      val irregAdverbsDir = dataSource / corpus / "irregular-stems/adverbs"
      if (! irregAdverbsDir.exists) {
        mkdirs(irregAdverbsDir)
      }
      val data = fstForIrregAdverbData(irregAdverbsDir)
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
  * @param dir Directory with tables of verb data.
  */
  def fstForIrregAdverbData(dir: File) : String = {
    val adverbFiles = dir.glob("*.cex").toVector

    val fstLines = for (f <- adverbFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      adverbLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

  def adverbLineToFst(line: String) : String = {
    //ag.irradv1#lexent.n31151#non#pos
    val cols = line.split("#")

    if (cols.size < 4) {
      println(s"${cols.size} is the wrong number of columns for an adverb\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {

      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString =  cols(2)
      val degr = cols(3)


      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<${degr}><irregadv>")
      fstBuilder.toString
    }
  }

  /** Convert a Vector of noun stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one noun stem in CEX form.
  */
  def adverbLinesToFst(data: Vector[String]) : String = {
    data.map(adverbLineToFst(_)).mkString("\n") + "\n"
  }


}
