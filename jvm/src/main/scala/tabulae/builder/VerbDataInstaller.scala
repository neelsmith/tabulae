package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._


object VerbDataInstaller {

  /** Write FST rules for all verb stem data in a directory
  * of tabular files.
  *
  * @param dataSource Root directory for corpus-specific data sources.
  * @param corpusList List of corpora within dataSource directory.
  * @param targetFile File to write FST statements to.  The directory
  * containing targetFile must already exist.
  */
  def apply(dataSource: File, corpusList: Vector[String], targetFile: File) = {
    val srcData = for (corpus <- corpusList) yield {
      val verbsDir = dataSource / corpus / "stems-tables/verbs-simplex"
      if (! verbsDir.exists) {
        mkdirs(verbsDir)
      }
      val data = fstForVerbData(verbsDir)
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
  def fstForVerbData(dir: File) : String = {
    if (! dir.exists) {
      ""
    } else {
      val verbFiles = dir.glob("*.cex").toVector
      val fstLines = for (f <- verbFiles) yield {
        // omit empty lines and header
        val dataLines = f.lines.toVector.filter(_.nonEmpty).drop(1) //Source.fromFile(f).getLines.toVector.filter(_.nonEmpty).drop(1)
        VerbDataInstaller.verbLinesToFst(dataLines)
      }
      fstLines.mkString("\n")
    }
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
