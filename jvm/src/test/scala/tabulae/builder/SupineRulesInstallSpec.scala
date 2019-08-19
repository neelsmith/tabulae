
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar



class SupineRulesInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The SupineRulesInstaller object" should "install supine data from a single source" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"SupineRulesInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "supineinfl.fst"

    SupineRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"SupineRulesInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("$supineinfl$ =  <conj1><supine>atu<abl><u>proof\\.conj1\\_1</u>", "$supineinfl$")
    assert(expectedLines == targetFile.lines.toVector.filter(_.nonEmpty))
    tempParserDir.delete()
  }



  it should "do nothing if no verb data are present in a given corpus" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    val targetFile = targetDir / "supineinfl.fst"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)

    SupineRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    tempParserDir.delete()
  }

  it should "return an empty string if no data are found in the source directory" in  {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val output = SupineRulesInstaller.fstForSupineRules(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in  {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val fst = SupineRulesInstaller.fstForSupineRules(emptyDir)
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
    assert(targetDir.exists, "SupineRulesInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "supineinfl.fst"

    SupineRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "SupineRulesInstallerSpec: SupineRulesInstaller did not create " + targetFile)


    val expectedLines = Vector(
      "$supineinfl$ =  <conj1><supine>atu<abl><u>proof\\.conj1\\_1</u> |\\",  "<conj1><supine>atum<acc><u>proof\\.conj1sup2</u>",
      "$supineinfl$"
    )
    assert(expectedLines == targetFile.lines.toVector.filter(_.nonEmpty).map(_.trim))
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

    SupineRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }

}
