
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar

class SymbolsComposerSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  "The SymbolsComposer object" should "write fst files in the symbols subdirectory of the parser directory" in {
    val tempParserDir =  File("jvm/src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"

    val projectDir = tempParserDir / "analytical_types"
    val fst = File("jvm/src/test/resources/datasets/fst")
    SymbolsComposer(projectDir, fst)

    val symbolsDir = projectDir / "symbols"
    val expectedFiles = Vector(
      projectDir / "symbols.fst",
      symbolsDir / "markup.fst",
      symbolsDir / "morphsymbols.fst",
      symbolsDir / "phonology.fst",
      symbolsDir / "stemtypes.fst"
    )
    for (f <- expectedFiles) {
      assert(f.exists, "SymbolsComposer did not create expected file " + f)
    }
    // tidy up
    tempParserDir.delete()

  }


  it should "rewrite @workdir@ variable with full path to working directory" in pending


}
