
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar


class MakefileComposerSpec extends FlatSpec {


  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  val datasets = File("src/test/resources/datasets/")
  val corpora =  Vector("analytical_types")
  val fstSrc = datasets / "fst"
  val compiler = "/path/to/sfst-compiler"

  def installLexica(datasets: File, corpora: Vector[String], projectDir: File) = {
    val lexDir = projectDir / "lexica"
    if (! lexDir.exists) {mkdirs(lexDir)}
    val targetFile = lexDir / "lexicon-verbs.fst"
    VerbDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"VerbDataInstaller:  did not create " + targetFile)
  }
  def installRules(datasets: File, corpora: Vector[String], projectDir: File) = {
    val inflDir = projectDir / "inflection"
    if (! inflDir.exists) { mkdirs(inflDir)}
    val targetFile = inflDir / "verbinfl.fst"
    VerbRulesInstaller(datasets, corpora, targetFile)
  }


  "The MakefileComposer object" should  "ensure that inflection directory includes some .fst source" in {
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to compose makefiles:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir
    SymbolsComposer(symbolsDir, fstSrc)

    // remove inflection directory:
    (projectDir / "inflection").delete()
    try {
      MakefileComposer(projectDir, compiler)
      fail("Should not have made makefiles")
    } catch {
      case t : Throwable => {
        val expected = "MakefileComposer: no inflection rules installed"
        assert(t.toString.contains(expected))
      }
    }




  }


  it should "ensure that symbols.fst exists" in pending

  it should "ensure that symbols/phonology.fst exists" in pending
  it should "ensure that inflection.fst exists" in pending
  it should "ensure that acceptor.fst exists" in pending
  it should "compose the central makefile" in pending
  it should "compose the makefile for the inflection directory" in pending
}
