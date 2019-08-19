
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar



class VerbRulesInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The VerbRulesInstaller object" should "install verb data from a single source" in   {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"VerbRulesInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "verbinfl.fst"

    VerbRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"VerbRulesInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("$verbinfl$ =  <conj1><verb>o<1st><sg><pres><indic><act><u>proof\\.are\\_presind1</u>", "$verbinfl$")
    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)

    tempParserDir.delete()
  }



  it should "do nothing if no verb data are present in a given corpus" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "verbinfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    VerbRulesInstaller(datasets, corpora, targetFile)
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
    val output = VerbRulesInstaller.fstForVerbRules(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in  {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val fst = VerbRulesInstaller.fstForVerbRules(emptyDir)
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
    assert(targetDir.exists, "VerbRulesInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "verbinfl.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    VerbRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "VerbRulesInstallerSpec: VerbRulesInstaller did not create " + targetFile)



    val expectedLines =  Vector("$verbinfl$ =  <conj1><verb>o<1st><sg><pres><indic><act><u>proof\\.are\\_presind1</u> |\\",
    "<conj1><verb>as<2nd><sg><pres><indic><act><u>proof\\.are\\_presind2</u>",
    "$verbinfl$")

    val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")
    val actualString = targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")

    assert(actualString == expectedString)
    tempParserDir.delete()
  }

  it should "do nothing if no verb data are present in HC corpus" in {
    val datasets = File("jvm/src/test/resources/lex-no-rules/")
    val corpora = Vector("lat24", "shared")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "verbinfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    VerbRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }

}
