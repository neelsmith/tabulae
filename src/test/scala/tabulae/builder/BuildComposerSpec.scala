
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar




class BuildComposerSpec extends FlatSpec {


  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The BuildComposer object" should  "successfully invoke the AcceptorComposer" in pending
  it should "successfully invoke the SymbolsComposer" in pending
  it should "successfully invoke the InflectionComposer" in pending
  it should "successfully invoke the ParserComposer" in pending
  it should "successfully invoke the MakefileComposer" in pending

  it should "install the alphabet.fst file" in {
    val dataSource = File("src/test/resources/datasets")
    val corpusList = Vector("analytical_types")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val fst = File("src/test/resources/datasets/fst")
    val compiler = "/usr/local/bin/fst-compiler-utf8"

    val target = tempParserDir / corpusList.mkString("-") / "symbols/alphabet.fst"
    // Test when doesn't exist yet:
    if (target.exists) {
      tempParserDir.delete()
    }
    BuildComposer.installAlphabet(dataSource, corpusList, tempParserDir)
    assert(target.exists, "BuildComposer failed to create alphabet file " + target)

    tempParserDir.delete()
  }

  it should "overwrite the alphabet.fst file if it already exists" in {
    val dataSource = File("src/test/resources/datasets")
    val corpusList = Vector("analytical_types")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val fst = File("src/test/resources/datasets/fst")
    val compiler = "/usr/local/bin/fst-compiler-utf8"

    val alphabetFile = tempParserDir / corpusList.mkString("-") / "symbols/alphabet.fst"
    // Test when doesn't exist yet:
    if (alphabetFile.exists) {
      tempParserDir.delete()
    }
    BuildComposer.installAlphabet(dataSource, corpusList, tempParserDir)

    val expectedStart = "% Characters for Latin character set with distinct i/j, u/v"

    val alphabetText = alphabetFile.lines.mkString("\n")
    assert(alphabetText.startsWith(expectedStart))
    tempParserDir.delete()
  }
}
