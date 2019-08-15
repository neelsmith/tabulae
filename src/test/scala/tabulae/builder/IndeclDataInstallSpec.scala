package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}

import better.files.Dsl._

import java.util.Calendar

class IndeclDataInstallSpec extends FlatSpec {
  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  "The IndeclRulesInstaller object" should "install indeclinable data from a single source" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "lexica"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"IndeclDataInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "lexicon-indeclinables.fst"

    IndeclDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"IndeclDataInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("<u>proof\\.indecl1</u><u>lexent\\.n11873</u>cum<indecl><indeclconj>")
    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)

    //println("WROTE : " + targetFile.lines.toVector.filter(_.nonEmpty) )

    tempParserDir.delete()
  }



  it should "do nothing if no indeclinable data are present in a given corpus" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-indeclinables.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    IndeclDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)
    parserDir.delete()

  }

  it should "return an empty string if no data are found in the source directory" in {
    val emptyDir = File("src/test/resources/no-fst")
    val output = IndeclDataInstaller.fstForIndeclData(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in  {
    val emptyDir = File("src/test/resources/no-fst")
    val fst = IndeclDataInstaller.fstForIndeclData(emptyDir)
    assert(fst.isEmpty)
  }


  it should "composite data from multiple sources" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "lexica"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "IndeclDataInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "lexicon-indeclinables.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    IndeclDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "IndeclDataInstallerSpec: IndeclDataInstaller did not create " + targetFile)

    val expectedLines =  Vector("<u>proof\\.indecl1</u><u>lexent\\.n11873</u>cum<indecl><indeclconj>","<u>proof\\.indecl2</u><u>ls\\.n45746</u>sub<indecl><indeclprep>")

    val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")
    val actualString = targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")

    assert(actualString == expectedString)
    tempParserDir.delete()
  }

  it should "do nothing if no indeclinable data are present in HC corpus" in {
    val datasets = File("src/test/resources/lex-no-rules/")
    val corpora = Vector("lat24", "shared")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-indeclinables.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    IndeclDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }

}
