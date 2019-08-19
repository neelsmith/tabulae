package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar

class AdverbRulesInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The AdverbRulesInstaller object" should  "install adverb rules from a single source" in  {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"AdverbRulesInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "adverbinfl.fst"

    AdverbRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"AdverbRulesInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("$adverbinfl$ =  <us_a_um><adv>e<pos><u>proof\\.us\\_a\\_um1</u>", "$adverbinfl$")
    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)
    //println(targetFile.lines.toVector.filter(_.nonEmpty))
    tempParserDir.delete()
  }



  it should "do nothing if no adverb data are present in a given corpus" in  {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "adverbinfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    AdverbRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)
    try {
      parserDir.delete()

    } catch {
      case t: Throwable => {
        println("Failed to delete parsers dir. " + t)
      }
    }
  }

  it should "return an empty string if no data are found in the source directory" in {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val output = AdverbRulesInstaller.fstForAdverbRules(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in  {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val fst = AdverbRulesInstaller.fstForAdverbRules(emptyDir)
    assert(fst.isEmpty)
  }


  it should "composite data from multiple sources" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "AdverbRulesInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "adverbinfl.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    AdverbRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "AdverbRulesInstallerSpec: AdverbRulesInstaller did not create " + targetFile)



    val expectedLines =  Vector("$adverbinfl$=<us_a_um><adv>e<pos><u>proof\\.us\\_a\\_um1</u>|\\",
      "<us_a_um><adv>ius<comp><u>proof\\.us\\_a\\_um1</u>",
      "$adverbinfl$")

    val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")
    val actualString = targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")

    assert(actualString == expectedString)
    //println(actualString)
    tempParserDir.delete()
  }

  it should "do nothing if no adverb data are present in HC corpus" in {
    val datasets = File("jvm/src/test/resources/lex-no-rules/")
    val corpora = Vector("lat24", "shared")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "adverbinfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    AdverbRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }




}
