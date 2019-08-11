
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class SymbolsComposerSpec extends FlatSpec {


  "The SymbolsComposer object" should "write fst files in the symbols directory of the parser source" in {
    val repo = File("src/test/resources")
    val corpusList = Vector("analytical_types")

    val targetDir = repo / "parsers" /  corpusList.mkString("-") / "symbols"
    if (targetDir.exists) {
      targetDir.delete()
    }
    val  sc = SymbolsComposer(repo, corpusList)
    assert(targetDir.exists)

    val projectDir = repo / "parsers" / corpusList.mkString("-")
    projectDir.delete()
  }

}
