package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar

class NounRulesInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The NounRulesInstaller object" should  "install noun rules from a single source" in  {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"NounRulesInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "nouninfl.fst"

    NounRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"NounRulesInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("$nouninfl$ =  <0_i><noun><masc><nom><sg><u>proof\\.0\\_i1</u>", "$nouninfl$")
    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)
    //println(targetFile.lines.toVector.filter(_.nonEmpty))
    tempParserDir.delete()
  }



  it should "do nothing if no noun data are present in a given corpus" in  {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "nouninfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    NounRulesInstaller(datasets, corpora, targetFile)
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
    val output = NounRulesInstaller.fstForNounRules(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in  {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val fst = NounRulesInstaller.fstForNounRules(emptyDir)
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
    assert(targetDir.exists, "NounRulesInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "nouninfl.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    NounRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "NounRulesInstallerSpec: NounRulesInstaller did not create " + targetFile)



    val expectedLines =  Vector("$nouninfl$=<0_i><noun><masc><nom><sg><u>proof\\.0\\_i1</u>|\\", "<0_i><noun>i<gen><nom><sg><u>proof\\.0\\_i2</u>", "$nouninfl$")

    val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")
    val actualString = targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")

    assert(actualString == expectedString)
    //println(actualString)
    tempParserDir.delete()
  }

  it should "do nothing if no noun data are present in HC corpus" in {
    val datasets = File("jvm/src/test/resources/lex-no-rules/")
    val corpora = Vector("lat24", "shared")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "nouninfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    NounRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }




}
