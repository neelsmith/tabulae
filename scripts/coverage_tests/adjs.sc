import edu.holycross.shot.tabulae.builder._
import edu.holycross.shot.tabulae._

import sys.process._
import scala.language.postfixOps

import better.files._
import java.io.{File => JFile}
import better.files.Dsl._


val macInstall = File("/usr/local/bin/")
val linuxInstall = File("/usr/bin/")

val compiler = if ( (macInstall / "fst-compiler-utf8").exists) {
  macInstall / "fst-compiler-utf8"
} else {
  linuxInstall / "fst-compiler-utf8"
}
val fstinfl = if ( (macInstall / "fst-infl").exists) {
  macInstall / "fst-infl"
} else {
  linuxInstall / "fst-infl"
}
val make = "/usr/bin/make"


val repo = File(".")
val datasets: File = repo / "datasets"
val corpusList = Vector("coverage-adjective-rules", "coverage-adjective-stems")
val conf =  Configuration(compiler.toString, fstinfl.toString, make, datasets.toString)
val parserDir = repo / "jvm/src/test/resources/parsers"
val fst = repo / "fst"
val tempDir = parserDir / corpusList.mkString("-")

def compile = {

  try {
    FstCompiler.compile(datasets, corpusList, parserDir, fst, conf)
    println("\nCompilation completed.\nParser is available in " +  parserDir + "/" + corpusList.mkString("-") + "/latin.a")
  } catch {
    case t: Throwable => println("Error trying to compile:\n" + t.toString)
  }
}

def tidyAdj = {
  tempDir.delete()
}



/**  Parse words listed in a file, and return their analyses
* as a String.
*
* @param wordsFile File with words to parse listed one per line.
* @param parser Name of corpus-specific parser, a subdirectory of
* tabulae/parsers.
*/
def parse(wordsFile : String) : String = {
  def compiled = parserDir / corpusList.mkString("-") / "latin.a"
  val cmd = fstinfl + " " + compiled + "  " + wordsFile
  cmd !!
}



val testList = Vector("er_ra_rum")

def testAdjective = {
  if (! tempDir.exists) {
    compile
  }

  val wordsDir = repo / "scripts/coverage_tests/wordlists-adjective"
  val results = for (test <- testList) yield {
    val testFile = wordsDir / s"${test}.txt"
    val fst = parse(testFile.toString)
    val parsed = FstReader.parseFstLines(fst.split("\n").toVector)


    val summary = s"${test} class. Tokens: ${parsed.size}. Parsed:  ${parsed.filter(_.analyses.nonEmpty).size}."
    val failed = parsed.filter(_.analyses.isEmpty).map(_.token)

    if (failed.nonEmpty) {
        summary + " Failed on:\n" + failed.map(tkn => "\t" + tkn).mkString("\n")
    } else {
      summary
    }

  }
  println("\nResults:\n-------\n\n" + results.mkString("\n"))

  println(s"\nTotal classes tested: ${results.size}")

}
