
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar



class IrregPronounDataInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The IrregPronounDataInstaller object" should "install pronoun data from a single source" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"IrregPronounDataInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "lexicon-irregverbs.fst"

    IrregPronounDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"IrregPronounDataInstaller:  did not create " + targetFile)

    val expected = "<u>proof\\.irrpron1</u><u>lexent\\.n20640</u>hic<masc><nom><sg><irregpron>"
    assert(targetFile.lines.toVector(0) == expected)

    //println(targetFile.lines.toVector(0))
    parserDir.delete()
  }



  it should "do nothing if no verb data are present in a given corpus" in  {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-irregverbs.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    IrregPronounDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false)
    parserDir.delete()
  }

  it should "return an empty string if no data are found in the source directory" in  {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val output = IrregPronounDataInstaller.fstForIrregPronounData(emptyDir)
    assert(output.isEmpty)
  }

  it should "return an empty string if asked to create FST strings from a non-existent directory" in   {
    val emptyDir = File("jvm/src/test/resources/no-fst")
    val fst = IrregPronounDataInstaller.fstForIrregPronounData(emptyDir)
    assert(fst.isEmpty)
  }


  it should "composite data from multiple sources" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "IrregPronounDataInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "lexicon-irregverbs.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    IrregPronounDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "IrregPronounDataInstallerSpec: IrregPronounDataInstaller did not create " + targetFile)

    val expectedLines = Vector("<u>proof\\.irrpron1</u><u>lexent\\.n20640</u>hic<masc><nom><sg><irregpron>", "<u>proof\\.irrpron1</u><u>lexent\\.n20640</u>haec<fem><nom><sg><irregpron>")

    //println(targetFile.lines.toVector.filter(_.nonEmpty) )
    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)
    parserDir.delete()
  }

  it should "do nothing if no verb data are present in one or more extant corpora" in   {
    val datasets = File("jvm/src/test/resources/named-empty-copora")

    val corpora = Vector("shared", "lat24")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-irregverbs.fst"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    IrregPronounDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false, "Oops, made " + targetFile)
    parserDir.delete()
  }

}
