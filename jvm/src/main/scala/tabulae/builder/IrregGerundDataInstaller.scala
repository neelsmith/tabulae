package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._


object IrregGerundDataInstaller {

  /** Creates FST file for each CEX file of
  * irregular verb stems.
  *
  * @param dataSource Root directory of morphological data set.
  * @param targetFile File to write FST statements to.
  */
  def apply(dataSource: File, targetFile: File) = {
    val irregGerundFst = fstForIrregGerundData(dataSource)
    if (irregGerundFst.nonEmpty) {
      targetFile.overwrite(irregGerundFst)
    } else {}
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def fstForIrregGerundData(dir: File) : String = {
    val gerundFiles = dir.glob("*.cex").toVector

    val fstLines = for (f <- gerundFiles.filter(_.nonEmpty)) yield {
      // omit empty lines and header
      val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1)
      gerundLinesToFst(dataLines)
    }
    fstLines.mkString("\n")
  }

  def gerundLineToFst(line: String) : String = {
    val cols = line.split("#")

    if (cols.size < 4) {
      println(s"${cols.size} is the wrong number of columns for a gerund\nCould not parse data line:\n${line}")
      throw new Exception(s"Wrong number of columns ${cols.size}.\nCould not parse data line:\n${line}")
    } else {

      //proof.irrigrd2#lexent.n15868#eundum#nom
      val fstBuilder = StringBuilder.newBuilder
      val ruleUrn = cols(0).replaceAll("_","\\\\_").replaceAll("\\.","\\\\.")
      val lexent = cols(1).replaceAll("_","\\_").replaceAll("\\.","\\\\.")
      val inflString =  cols(2)
      val grammCase = cols(3)


      fstBuilder.append(s"<u>${ruleUrn}</u><u>${lexent}</u>${inflString}<${grammCase}><irreggrnd>")
      fstBuilder.toString
    }
  }

  /** Convert a Vector of infinitive stem data in CES form to
  * a single valid FST string.
  *
  * @param data Vector of strings in which each string
  * represents one infinitive stem in CEX form.
  */
  def gerundLinesToFst(data: Vector[String]) : String = {
    data.map(gerundLineToFst(_)).mkString("\n") + "\n"
  }


}
