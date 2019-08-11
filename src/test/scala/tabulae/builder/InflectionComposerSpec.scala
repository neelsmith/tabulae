
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class InflectionComposerSpec extends FlatSpec {

  "The InflectionComposer object" should "write inflection.fst in the parser root directory" in {
    val projectDirectory = File("src/test/resources/inflection-fst-files")
    val topLevelInflectionFile = projectDirectory / "inflection.fst"
    // Ensure file does not exist:
    if (topLevelInflectionFile.exists) {
      topLevelInflectionFile.delete()
    }
    InflectionComposer( projectDirectory )
    assert(topLevelInflectionFile.exists)

    val content = topLevelInflectionFile.lines.mkString("\n")
    assert(content.contains("inflection-fst-files/inflection/adjinfl.a"))
    topLevelInflectionFile.delete()
  }

  it should "throw an exception if the project has no inflectional files" in {
    val projectDirectory = File("src/test/resources/no-fst")
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

  }

  it should "write inflection.fst in the parser root directory if the file already exists" in {
    val projectDirectory = File("src/test/resources/inflection-fst-files")
    val topLevelInflectionFile = projectDirectory / "inflection.fst"

    // Create file with bogus data to overwrite:
    topLevelInflectionFile.overwrite("NOT valid fst data\n")

    InflectionComposer( projectDirectory )
    val content = topLevelInflectionFile.lines.mkString("\n")
    assert(content.contains("inflection-fst-files/inflection/adjinfl.a"))
    topLevelInflectionFile.delete()
  }


}
