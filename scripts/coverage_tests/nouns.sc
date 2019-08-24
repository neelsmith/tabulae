import edu.holycross.shot.tabulae.builder._

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
val corpusList = Vector("coverage-noun-rules", "coverage-noun-stems")

def compile = {



  val conf =  Configuration(compiler.toString, fstinfl.toString, make, datasets.toString)
  val parserDir = repo / "src/test/resources/parsers"
  val fst = repo / "fst"
  try {
    FstCompiler.compile(datasets, corpusList, parserDir, fst, conf)

    println("\nCompilation completed.\nParser is available in " +  parserDir + "/" + corpusList.mkString("-") + "/latin.a")
  } catch {
    case t: Throwable => println("Error trying to compile:\n" + t.toString)
  }
}

def tidy = {

  val parserDir = repo / "src/test/resources/parsers"
  val tempDir = parserDir / corpusList.mkString("-")
  tempDir.delete()
}



/**  Parse words listed in a file, and return their analyses
* as a String.
*
* @param wordsFile File with words to parse listed one per line.
* @param parser Name of corpus-specific parser, a subdirectory of
* tabulae/parsers.
*/
def parse(wordsFile : String, parser: String = "analytical-types") : String = {
  def compiled = s"parsers/${parser}/latin.a"
  val cmd = fstinfl + " " + compiled + "  " + wordsFile
  cmd !!
}

println("\n\nCompile a parser:\n")
println("\tcompile\n")
println("Parse a word list:\n")
println("\tparse(WORDSFILE, [CORPUS])")
