
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar


class ParserComposerSpec extends FlatSpec {


  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  val datasets = File("src/test/resources/datasets/")
  val corpora =  Vector("analytical_types")
  val fstSrc = datasets / "fst"

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




  "The ParserComposer object" should  "ensure that lexica are already installed" in {
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to build a parser:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir

    SymbolsComposer(symbolsDir, fstSrc)

    // but remove lexica:
    (projectDir / "lexica").delete()
    try {
      ParserComposer(projectDir)
    } catch {
      case t: Throwable => {
        val expected = "ParserComposer:  cannot compose parser FST until lexica have been installed"
        assert(t.toString.contains(expected))
      }
    }
    tempParserDir.delete()
  }

  it should "ensure that inflectional rules are already installed" in {
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to build a parser:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir

    SymbolsComposer(symbolsDir, fstSrc)

    // but remove inflectional rules:
    (projectDir / "inflection").delete()
    try {
      ParserComposer(projectDir)
      fail("Should not have created a parser.")
    } catch {
      case t: Throwable => {
        val expected = "ParserComposer:  cannot compose parser FST until inflectional rules have been installed"
        assert(t.toString.contains(expected))
      }
    }
    tempParserDir.delete()
  }
  it should "ensure that the central symbols.fst file is already written" in {
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to build a parser:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir

    SymbolsComposer(symbolsDir, fstSrc)

    // but remove symbols file:
    (projectDir / "symbols.fst").delete()
    try {
      ParserComposer(projectDir)
      fail("Should not have created a parser.")
    } catch {
      case t: Throwable => {
        val expected = "ParserComposer:  cannot compose parser FST until FST symbols have been installed"
        assert(t.toString.contains(expected))
      }
    }
    tempParserDir.delete()
  }
  it should "write the main parser file latin.fst" in {
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to build a parser:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir
    SymbolsComposer(symbolsDir, fstSrc)

    ParserComposer(projectDir)
    val parserFst = projectDir / "latin.fst"
    assert(parserFst.exists, "ParserComposer did not write parser file " + parserFst)
    tempParserDir.delete()
  }
}
