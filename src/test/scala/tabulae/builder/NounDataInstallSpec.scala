package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar

class NounDataInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The NounDataInstaller object" should  "install noun data from a single source" in  {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"NounDataInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "nouninfl.fst"

    NounDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"NounDataInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("<u>proof\\.nom1</u><u>lexent\\.n39429</u>puer<noun><masc><0_i>")
    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)

    //println(targetFile.lines.toVector.filter(_.nonEmpty) )
    tempParserDir.delete()
  }



  it should "do nothing if no noun data are present in a given corpus" in  {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "nouninfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    NounDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)
    try {
      parserDir.delete()

    } catch {
      case t: Throwable => {
        println("Failed to delete parsers dir. " + t)
      }
    }
  }

  it should "return an empty string if no data are found in the source directory" in   {
    val emptyDir = File("src/test/resources/no-fst")
    val output = NounDataInstaller.fstForNounData(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in  {
    val emptyDir = File("src/test/resources/no-fst")
    val fst = NounDataInstaller.fstForNounData(emptyDir)
    assert(fst.isEmpty)
  }


  it should "composite data from multiple sources" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "NounDataInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "nouninfl.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    NounDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "NounDataInstallerSpec: NounDataInstaller did not create " + targetFile)



    val expectedLines =  Vector("<u>proof\\.nom1</u><u>lexent\\.n39429</u>puer<noun><masc><0_i>", "<u>proof\\.n2</u><u>ls\\.n26903</u>loc<noun><masc><us_i>")

    val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")
    val actualString = targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")

    //println(actualString)
    assert(actualString == expectedString)
    tempParserDir.delete()
  }

  it should "do nothing if no noun data are present in HC corpus" in  {
    val datasets = File("src/test/resources/lex-no-rules/")
    val corpora = Vector("lat24", "shared")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "nouninfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    NounDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }




}
