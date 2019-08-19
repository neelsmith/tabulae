
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

  "The CompoundVerbDataInstaller object" should "install verb data from a single source" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "lexica"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"CompoundVerbDataInstaller:  could not create " + targetDir)


    CompoundVerbDataInstaller(datasets, corpora, targetDir)
    val targetFile = targetDir / "lexicon-compoundverbs.fst"
    assert(targetFile.exists,"CompoundVerbDataInstaller:  did not create " + targetFile)

    val expected = "<u>proof\\.comp1\\_0</u><u>lexent\\.n13047</u><#>demonstr<verb><conj1>"
    assert(targetFile.lines.toVector(0) == expected)

    tempParserDir.delete()
  }

  it should "know how to merge two maps of compound forms" in {
    val map1 = Map("lexent.n13047" -> Vector("proof.comp1#lexent.n13047#de#lexent.n29616"))
    val map2 = Map("lexent.n23008" -> Vector("proof.comp2#lexent.n23008#in#lexent.n15868"))
    val merged = CompoundVerbDataInstaller.mergeMaps(Vector(map1, map2))
    assert(merged.size == 2)
  }

  it should "do nothing if no verb data are present in one or more extant corpora" in   {
    val datasets = File("jvm/src/test/resources/named-empty-copora")

    val corpora = Vector("shared", "lat24")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    CompoundVerbDataInstaller(datasets, corpora, targetDir)
    assert(targetDir.isEmpty, "Oops, made some files in " + targetDir)
    parserDir.delete()
  }

  it should "do nothing if no verb data are present in a given corpus" in   {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)

    CompoundVerbDataInstaller(datasets, corpora, targetDir)
    assert(targetDir.isEmpty,"Oops, put some files in " + targetDir)
    parserDir.delete()
  }


  it should "composite data from multiple sources" in  {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val parserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      parserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "CompoundVerbDataInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "lexicon-compoundverbs.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    CompoundVerbDataInstaller(datasets, corpora, targetDir)
    assert(targetFile.exists, "CompoundVerbDataInstallerSpec: CompoundVerbDataInstaller did not create " + targetFile)

    val expectedLines = Vector("<u>proof\\.comp1\\_0</u><u>lexent\\.n13047</u><#>demonstr<verb><conj1>","<u>proof\\.comp21\\_1</u><u>lexent\\.n40697</u><#>redam<verb><conj1>")

    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)
    //println(targetFile.lines.toVector.filter(_.nonEmpty))
    parserDir.delete()


  }


  it should "object if compound verb entries refer to simplex verbs not in the project" in pending

}
