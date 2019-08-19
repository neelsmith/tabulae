
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

  val datasets = File("jvm/src/test/resources/datasets/")
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
    val tempParserDir =  File("jvm/src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
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
        val expected = "MakefileComposer: cannot compose main makefile until inflection.fst"
        assert(t.toString.contains(expected))
      }
    }
    tempParserDir.delete()
  }


  it should "ensure that symbols.fst exists" in {
    val tempParserDir =  File("jvm/src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to compose makefiles:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir
    SymbolsComposer(symbolsDir, fstSrc)

    // remove symbols.fst:
    (projectDir / "symbols.fst").delete()
    try {
      MakefileComposer(projectDir, compiler)
      fail("Should not have made makefiles")
    } catch {
      case t : Throwable => {
        val expected = "MakefileComposer: cannot compose main makefile until symbols.fst is installed"
        assert(t.toString.contains(expected))
      }
    }
    tempParserDir.delete()
  }

  it should "ensure that symbols/phonology.fst exists" in {
    val tempParserDir =  File("jvm/src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to compose makefiles:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir
    SymbolsComposer(symbolsDir, fstSrc)

    // remove phonology.fst:
    (projectDir / "symbols/phonology.fst").delete()
    try {
      MakefileComposer(projectDir, compiler)
      fail("Should not have made makefiles")
    } catch {
      case t : Throwable => {
        val expected = "MakefileComposer: cannot compose main makefile until symbols/phonology.fst is installed"
        assert(t.toString.contains(expected))
      }
    }
    tempParserDir.delete()
  }

  // NOT YET INSTALLED
  it should "ensure that inflection.fst exists" in  pending /*{
    val tempParserDir =  File("jvm/src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to compose makefiles:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir
    SymbolsComposer(symbolsDir, fstSrc)
    // remove inflection.fst:
    //(projectDir / "inflection.fst").delete()
    println("Does inflection.fst exist? " + (projectDir / "inflection.fst").exists)
    try {
      MakefileComposer(projectDir, compiler)
      fail("Should not have made makefiles")
    } catch {
      case t : Throwable => {
        val expected = "MakefileComposer: cannot compose main makefile until inflection.fst is installed"
        assert(t.toString.contains(expected))
      }
    }
    tempParserDir.delete()
  }*/
  // NOT YET INSTALLED
  it should "ensure that acceptor.fst exists" in pending
  it should "compose the central makefile" in {
    val tempParserDir =  File("jvm/src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to compose makefiles:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir
    SymbolsComposer(symbolsDir, fstSrc)
    InflectionComposer( projectDir )

    MakefileComposer(projectDir, compiler)
    assert((projectDir / "makefile").exists)
    tempParserDir.delete()
  }
  it should "compose the makefile for the inflection directory" in {
    val tempParserDir =  File("jvm/src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val projectDir = tempParserDir / corpora.mkString("-")
    mkdirs(projectDir)

    // install enough data to compose makefiles:
    // lexicon, rules, and symbols.
    installLexica(datasets, corpora, projectDir)
    installRules(datasets, corpora, projectDir)
    val symbolsDir = projectDir
    SymbolsComposer(symbolsDir, fstSrc)
    InflectionComposer( projectDir )

    MakefileComposer(projectDir, compiler)
    assert((projectDir / "inflection/makefile").exists)
    tempParserDir.delete()
  }
}
