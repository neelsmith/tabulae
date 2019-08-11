
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._


class SymbolsComposerSpec extends FlatSpec {

  "The SymbolsComposer object" should "write fst files in the symbols subdirectory of the parser directory" in {
    val repo = File(s"src/test/resources/datasets")
    val fst = repo / "fst/symbols"
    val parsers = repo / "parsers"
    val corpusDir = parsers / "analytical_types"


    val  sc = SymbolsComposer(corpusDir, fst)

    val symbolsDir = corpusDir / "symbols"
    val expectedFiles = Vector(
      corpusDir / "symbols.fst",
      symbolsDir / "markup.fst",
      symbolsDir / "morphsymbols.fst",
      symbolsDir / "phonology.fst",
      symbolsDir / "stemtypes.fst"
    )
    for (f <- expectedFiles) {
      assert(f.exists, "SymbolsComposer did not create expected file " + f)
    }
    // tidy up
    //parsersDir.delete()
    //assert(parsersDir.exists == false)
  }



}
