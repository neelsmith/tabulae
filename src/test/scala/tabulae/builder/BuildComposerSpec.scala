
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class BuildComposerSpec extends FlatSpec {


  val repo = File("src/test/resources")
  val dataSource = repo / "datasets"
  val corpusList = Vector("analytical_types")

  "The BuildComposer object" should "do a lot of things by invoking other composers..." in pending

  it should "install the alphabet.fst file" in {
    val target = repo / "parsers" / corpusList.mkString("-") / "symbols/alphabet.fst"
    // Test when doesn't exist yet:
    if (target.exists) {
      (repo / "parsers").delete()
    }
    BuildComposer.installAlphabet(dataSource, repo, corpusList)
    assert(target.exists)
    (repo / "parsers").delete()
  }

  it should "overwrite the alphabet.fst file if it already exists" in {
    val targetDir = repo / "parsers" / corpusList.mkString("-") / "symbols"
    if (targetDir.exists) {
      targetDir.delete()
    }
    mkdirs(targetDir)
    val alphabetFile = targetDir / "alphabet.fst"
    alphabetFile.overwrite("Fake data in pre-existing file.\n")

    BuildComposer.installAlphabet(dataSource, repo, corpusList)

    val expectedStart = "% Characters for Latin character set with distinct i/j, u/v"

    val alphabetText = alphabetFile.lines.mkString("\n")
    assert(alphabetText.startsWith(expectedStart))
    (repo / "parsers").delete()
  }
}
