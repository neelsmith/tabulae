
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar


// CHANGE TO USE RANDOMIZED TEMP FILE NAMES
class RulesInstallerSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  "The RulesInstaller object" should "install inflectional from a Vector of corpus names" in  {
    val datasets = File("src/test/resources/datasets")
    val c = Vector("analytical_types")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val fst = File("src/test/resources/datasets/fst")

    val projectDir = tempParserDir / c.mkString("-")


    // Ensure target directory is empty before testing:
    val targetDir =  projectDir / "inflection"
    if (targetDir.exists) {
      targetDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "Unable to create new inflection directory " + targetDir)

    val  ri = RulesInstaller(datasets, c, tempParserDir, fst)

    val expectedFiles = Vector(
    targetDir / "adjinfl.fst",
    targetDir / "advinfl.fst",
    targetDir / "gerundinfl.fst",
    targetDir / "gerundiveinfl.fst",
    targetDir / "infininfl.fst",

    targetDir / "nouninfl.fst",
    targetDir / "ptcplinfl.fst",
    targetDir / "supineinfl.fst",


    targetDir / "indeclinfl.fst",
    targetDir / "irreginfl.fst",
    targetDir / "verbinfl.fst")
    for (f <- expectedFiles) {
      assert(f.exists, "Did not find expected file " + f)
    }
    //tidy up
    tempParserDir.delete()
  }

  it should "install correctly from more than one source" in {
    val datasets = File("src/test/resources/datasets")
    val c = Vector("analytical_types", "shared")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val fst = File("src/test/resources/datasets/fst")

    val projectDir = tempParserDir / c.mkString("-")
    // Ensure target directory is empty before testing:
    val targetDir =  projectDir / "inflection"
    if (targetDir.exists) {
      targetDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "Unable to create new inflection directory " + targetDir)

    val  ri = RulesInstaller(datasets, c, tempParserDir, fst)
    // VerbRulesInstaller is not working proprerly

      val expectedLines =  Vector("$verbinfl$ =  <conj1><verb>o<1st><sg><pres><indic><act><u>proof\\.are\\_presind1</u> |\\",
      "<conj1><verb>as<2nd><sg><pres><indic><act><u>proof\\.are\\_presind2</u>",
      "$verbinfl$")

      val expectedString =  expectedLines.mkString("\n").replaceAll(" ","")

      val verbFile = targetDir / "verbinfl.fst"
      val actualString = verbFile.lines.toVector.filter(_.nonEmpty).mkString("\n").replaceAll(" ","")

      assert(actualString == expectedString)
      tempParserDir.delete()
  }

  it should "create subdirectories as necessary for installation" in  {
    val datasets = File("src/test/resources/datasets")
    val c = Vector("analytical_types")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val fst = File("src/test/resources/datasets/fst")

    val projectDir = tempParserDir / c.mkString("-")

    // Ensure target directory does not exist:
    val targetDir = projectDir / "inflection"
    if (targetDir.exists) {
      targetDir.delete()
      assert(targetDir.exists == false, "Unable to delete previous dir " + targetDir)
    }

    val  ri = RulesInstaller(datasets, c, tempParserDir, fst)
    assert(targetDir.exists, "RulesInstaller did not create target directory " + targetDir)
    // tidy up
    tempParserDir.delete()
  }

  it should "install fst for invariants" in {
    // Install these two files in target inflection directory:
    //indeclinfl.fst
    // irreginfl.fst
    val c = Vector("analytical_types")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val fstSrc = File("src/test/resources/datasets/fst/inflection")
    val fstTarget = tempParserDir / c.mkString("-") / "inflection"

    RulesInstaller.installInvariants(fstSrc, fstTarget)


    val indeclFst = fstTarget / "indeclinfl.fst"
    assert(indeclFst.exists, "RulesInstaller did not created file " + indeclFst)

    val irregFst = fstTarget / "irreginfl.fst"
    assert(irregFst.exists, "RulesInstaller did not created file " + irregFst)

    tempParserDir.delete()
  }
}
