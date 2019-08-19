
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar



class GerundRulesInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The GerundRulesInstaller object" should "install gerund data from a single source" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"GerundRulesInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "gerundinfl.fst"

    GerundRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"GerundRulesInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("$gerundinfl$ =  <conj1><gerund>andum<nom><u>proof\\.gerundconj1\\_1</u> |\\",  "<conj1><gerund>andum<acc><u>proof\\.gerundconj1\\_2</u>", "$gerundinfl$")

    assert(targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","") == expectedLines.mkString("\n").replaceAll(" ",""))
    tempParserDir.delete()
  }



  it should "do nothing if no verb data are present in a given corpus" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    val targetFile = targetDir / "gerundinfl.fst"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)

    GerundRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    tempParserDir.delete()
  }

  it should "return an empty string if no data are found in the source directory" in  {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val output = GerundRulesInstaller.fstForGerundRules(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in  {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val fst = GerundRulesInstaller.fstForGerundRules(emptyDir)
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
    assert(targetDir.exists, "GerundRulesInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "gerundinfl.fst"

    GerundRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "GerundRulesInstallerSpec: GerundRulesInstaller did not create " + targetFile)


    val expectedLines = Vector(
      "$gerundinfl$=<conj1><gerund>andum<nom><u>proof\\.gerundconj1\\_1</u>|\\",
      "<conj1><gerund>andum<acc><u>proof\\.gerundconj1\\_2</u>|\\",
      "<conj1><gerund>andi<gen><u>proof\\.gerundconj1\\_3</u>",
      "$gerundinfl$"
    )

    val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")
    val actualString = targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")

    assert(actualString == expectedString)
    tempParserDir.delete()
  }

  it should "do nothing if no verb data are present in multiple named corpora" in {
    val datasets = File("jvm/src/test/resources/lex-no-rules/")
    val corpora = Vector("lat24", "shared")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "infininfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    GerundRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }

}
