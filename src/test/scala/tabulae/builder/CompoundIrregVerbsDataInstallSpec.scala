
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar

class CompoundIrregVerbsDataInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  "The CompoundVerbDataInstaller object" should  "install compound irregular verb forms" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("irregcompounds")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "lexica"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"CompoundVerbDataInstaller:  could not create " + targetDir)


    CompoundVerbDataInstaller(datasets, corpora, targetDir)
    val targetFile = targetDir / "lexicon-irregcompoundverbs.fst"
    //assert(targetFile.exists,"CompoundVerbDataInstaller:  did not create " + targetFile)

    val expected = "<u>proof\\.irrcomp1\\_0</u><u>lexent\\.n40719</u><#>redit<3rd><sg><pres><indic><act><irregcverb>"
    assert(targetFile.lines.toVector(0) == expected)
    //println(targetFile.lines.toVector)
    tempParserDir.delete()
  }
  it should "install compound irregular infinitives" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("irregcompounds")
    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / "lexica"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"CompoundVerbDataInstaller:  could not create " + targetDir)


    CompoundVerbDataInstaller(datasets, corpora, targetDir)
    val targetFile = targetDir / "lexicon-irregcompoundinfinitives.fst"
    //assert(targetFile.exists,"CompoundVerbDataInstaller:  did not create " + targetFile)

    val expected = "<u>proof\\.irrcomp2\\_1</u><u>lexent\\.n254</u>abesse<pres><act><irreginfin>"
    assert(targetFile.lines.toVector(0) == expected)
    //println(targetFile.lines.toVector)
    tempParserDir.delete()
  }
  it should "object if compound verb entries refer to simplex verbs not in the project" in pending

}
