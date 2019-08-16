package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar

class AdjectiveDataInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The AdjectiveDataInstaller object" should  "install noun data from a single source" in  {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"AdjectiveDataInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "nouninfl.fst"

    AdjectiveDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"AdjectiveDataInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("<u>proof\\.adj1</u><u>lexent\\.n6903</u>car<adj><us_a_um>")
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

    AdjectiveDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)
    try {
      parserDir.delete()

    } catch {
      case t: Throwable => {
        println("Failed to delete parsers dir. " + t)
      }
    }
  }

  it should "return an empty string if no data are found in the source directory" in  {
    val emptyDir = File("src/test/resources/no-fst")
    val output = AdjectiveDataInstaller.fstForAdjectiveData(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in {
    val emptyDir = File("src/test/resources/no-fst")
    val fst = AdjectiveDataInstaller.fstForAdjectiveData(emptyDir)
    assert(fst.isEmpty)
  }


  it should "composite data from multiple sources" in  {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "AdjectiveDataInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "nouninfl.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    AdjectiveDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "AdjectiveDataInstallerSpec: AdjectiveDataInstaller did not create " + targetFile)



    val expectedLines =  Vector("<u>proof\\.adj1</u><u>lexent\\.n6903</u>car<adj><us_a_um>", "<u>proof\\.adj1</u><u>ls\\.n4306</u>atro<adj><x_cis>")

    val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")
    val actualString = targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")

    //println(actualString)
    assert(actualString == expectedString)
    tempParserDir.delete()
  }

  it should "do nothing if no noun data are present in HC corpus" in {
    val datasets = File("src/test/resources/lex-no-rules/")
    val corpora = Vector("lat24", "shared")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "nouninfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    AdjectiveDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }




}
