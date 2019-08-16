
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar



class ParticipleRulesInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The ParticipleRulesInstaller object" should "install participle data from a single source" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"ParticipleRulesInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "ptcplinfl.fst"

    ParticipleRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"ParticipleRulesInstaller:  did not create destination file " + targetFile)

    val expectedLines = Vector("$participleinfl$ =  <conj1><ptcpl>ans<masc><nom><sg><pres><act><u>proof\\.are\\_ptcpl1</u>", "$participleinfl$")
    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)

    tempParserDir.delete()
  }



  it should "do nothing if no verb data are present in a given corpus" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "ptcplinfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    ParticipleRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }

  it should "return an empty string if no data are found in the source directory" in  {
    val emptyDir = File("src/test/resources/no-fst")
    val output = ParticipleRulesInstaller.fstForParticipleRules(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in {
    val emptyDir = File("src/test/resources/no-fst")
    val fst = ParticipleRulesInstaller.fstForParticipleRules(emptyDir)
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
    assert(targetDir.exists, "ParticipleRulesInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "ptcplinfl.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    ParticipleRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "ParticipleRulesInstallerSpec: ParticipleRulesInstaller did not create " + targetFile)


    //val expectedLines = Vector("$infinitiveinfl$=<conj1><infin>avisse<pft><act><u>proof\\.are\\_inf1</u>|\\", "<conj1><infin>are<pre><act><u>proof\\.are\\_inf2</u>", "$infinitiveinfl$")

    val expectedLines = Vector(
      "$participleinfl$=<conj1><ptcpl>ans<masc><nom><sg><pres><act><u>proof\\.are\\_ptcpl1</u>|\\",
      "<conj1><ptcpl>antis<masc><gen><sg><pres><act><u>proof\\.are\\_ptcpl2</u>", "$participleinfl$"
    )

    val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")
    val actualString = targetFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")
    //println(actualString)
    assert(actualString == expectedString)
    tempParserDir.delete()
  }

  it should "do nothing if no verb data are present in multiple named corpora" in {
    val datasets = File("src/test/resources/lex-no-rules/")
    val corpora = Vector("lat24", "shared")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "inflection"
    val targetFile = targetDir / "ptcplinfl.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    ParticipleRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Somehow wound up with file " + targetFile)

    parserDir.delete()
  }

}
