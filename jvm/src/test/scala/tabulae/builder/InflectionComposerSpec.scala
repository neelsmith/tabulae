
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar

class InflectionComposerSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  "The InflectionComposer object" should "write inflection.fst in the parser root directory" in {
    val datasets = File("jvm/src/test/resources/datasets/")
    val corpora = Vector("analytical_types")

    val tempParserDir = File(s"jvm/src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val projectDirectory = tempParserDir / corpora.mkString("_")
    val targetDir = projectDirectory / "inflection"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"InflectionComposer:  could not create " + targetDir)
    // install some rules data:
    val verbRulesFile = targetDir / "verbinfl.fst"

  VerbRulesInstaller(datasets, corpora, verbRulesFile)
  InflectionComposer( projectDirectory )

  val topLevelInflectionFile = projectDirectory / "inflection.fst"
  assert(topLevelInflectionFile.exists)

    val content = topLevelInflectionFile.lines.mkString("\n")
    val expected = "analytical_types/inflection/verbinfl.a"
    assert(content.contains(expected))
    tempParserDir.delete()
  }

  it should "throw an exception if the project has no inflectional files" in pending /*{
    val projectDirectory = File("jvm/src/test/resources/no-fst")
    try {
      InflectionComposer( projectDirectory )
      fail("Should not have been able to proceed without failing on InflectionComposer.")
    } catch {
      case nosuchfile:  java.nio.file.NoSuchFileException => { assert(true) }
      case t: Throwable => {
        println("What's " + t + "?")
        fail("Expected NoSuchFileExecption but got " + t)
      }
    }

  }*/

  it should "write inflection.fst in the parser root directory if the file already exists" in pending /*{
    val projectDirectory = File("jvm/src/test/resources/inflection-fst-files")
    val topLevelInflectionFile = projectDirectory / "inflection.fst"

    // Create file with bogus data to overwrite:
    topLevelInflectionFile.overwrite("NOT valid fst data\n")

    InflectionComposer( projectDirectory )
    val content = topLevelInflectionFile.lines.mkString("\n")
    assert(content.contains("inflection-fst-files/inflection/adjinfl.a"))
    topLevelInflectionFile.delete()
  }*/


}
