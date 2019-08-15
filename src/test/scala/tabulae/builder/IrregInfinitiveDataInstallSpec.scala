
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar



class IrregInfinitiveDataInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The IrregInfinitiveDataInstaller object" should "install infinitive data from a single source" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"IrregInfinitiveDataInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "lexicon-irreginfinitives.fst"

    IrregInfinitiveDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"IrregInfinitiveDataInstaller:  did not create " + targetFile)

    val expected = "<u>proof\\.irrinf1</u><u>lexent\\.n15868</u>isse<pft><act><irreginfin>"


    assert(targetFile.lines.toVector(0) == expected)
    parserDir.delete()
  }



  it should "do nothing if no verb data are present in a given corpus" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-irreginfinitives.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    IrregInfinitiveDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false)
    parserDir.delete()
  }

  it should "return an empty string if no data are found in the source directory" in  {
    val emptyDir = File("src/test/resources/no-fst")
    val output = IrregInfinitiveDataInstaller.fstForIrregInfinitiveData(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in {
    val emptyDir = File("src/test/resources/no-fst")
    val fst = IrregInfinitiveDataInstaller.fstForIrregInfinitiveData(emptyDir)
    assert(fst.isEmpty)
  }


  it should "composite data from multiple sources" in  {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "IrregInfinitiveDataInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "lexicon-irreginfinitives.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    IrregInfinitiveDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "IrregInfinitiveDataInstallerSpec: IrregInfinitiveDataInstaller did not create " + targetFile)

    val expectedLines = Vector("<u>proof\\.irrinf1</u><u>lexent\\.n15868</u>isse<pft><act><irreginfin>", "<u>proof\\.irrinf2</u><u>lexent\\.n15868</u>ire<pres><act><irreginfin>")


    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)
    parserDir.delete()
  }

  it should "do nothing if no verb data are present in one or more extant corpora" in  {
    val datasets = File("src/test/resources/named-empty-copora")

    val corpora = Vector("shared", "lat24")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-irreginfinitives.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    IrregInfinitiveDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Oops, made " + targetFile)
    parserDir.delete()
  }

}
