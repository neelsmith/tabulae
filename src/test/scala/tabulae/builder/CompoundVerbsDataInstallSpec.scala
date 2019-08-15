
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar

class CompoundVerbsDataInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  "The CompoundVerbDataInstaller object" should "install verb data from a single source" in pending /* {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"CompoundVerbDataInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "lexicon-verbs.fst"

    CompoundVerbDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"CompoundVerbDataInstaller:  did not create " + targetFile)

    val expected = "<u>proof\\.v1</u><u>lexent\\.n29616</u><#>monstr<verb><conj1>"
    assert(targetFile.lines.toVector(0) == expected)

    parserDir.delete()
  }*/



  it should "do nothing if no verb data are present in a given corpus" in  pending /* {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-verbs.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    CompoundVerbDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false)
    parserDir.delete()
  }*/

  it should "return an empty string if no data are found in the source directory" in  pending /*  {
    val emptyDir = File("src/test/resources/no-fst")
    val output = CompoundVerbDataInstaller.fstForVerbData(emptyDir)
    assert(output.isEmpty)
  }*/

  it should "return an empty string if asked to create FST strings from a non-existent directory" in  pending /* {
    val emptyDir = File("src/test/resources/no-fst")
    val fst = CompoundVerbDataInstaller.fstForVerbData(emptyDir)
    assert(fst.isEmpty)
  }*/


  it should "composite data from multiple sources" in  pending /* {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "CompoundVerbDataInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "lexicon-verbs.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    CompoundVerbDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "CompoundVerbDataInstallerSpec: CompoundVerbDataInstaller did not create " + targetFile)

    val expectedLines = Vector("<u>proof\\.v1</u><u>lexent\\.n29616</u><#>monstr<verb><conj1>", "<u>proof\\.v2</u><u>lexent\\.n2280</u><#>am<verb><conj1>")

    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)

    parserDir.delete()


  }*/

  it should "do nothing if no verb data are present in one or more extant corpora" in  pending /*  {
    val datasets = File("src/test/resources/named-empty-copora")

    val corpora = Vector("shared", "lat24")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-verbs.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    CompoundVerbDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Oops, made " + targetFile)
    parserDir.delete()
  }*/

}
