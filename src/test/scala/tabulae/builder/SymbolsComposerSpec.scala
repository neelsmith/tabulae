
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class SymbolsComposerSpec extends FlatSpec {


  "The SymbolsComposer object" should "write fst files in the symbols directory of the parser source" in {
    val repo = File("src/test/resources")
    val corpusList = Vector("analytical_types")


    val projectDir = repo / "parsers" / corpusList.mkString("-")
    val targetDir = projectDir / "symbols"
    if (targetDir.exists) {
      targetDir.delete()
    }

    val  sc = SymbolsComposer(repo, corpusList)

    val expectedFiles = Vector(
      targetDir / "markup.fst",
      targetDir / "morphsymbols.fst",
      targetDir / "phonology.fst",
      targetDir / "stemtypes.fst"
    )
    for (f <- expectedFiles) {
      assert(f.exists)
    }
    // tidy up
    (repo / "parsers").delete()
  }



}
