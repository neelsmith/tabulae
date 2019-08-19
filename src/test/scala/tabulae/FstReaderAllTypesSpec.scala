
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstReaderAllTypesSpec extends FlatSpec {


  "The FstReader object" should "create a Vector of tokens from data in a file" in {
    val fstFile = "src/test/resources/words-oneeach-output.fst"
    val analyzed = FstReader.fromFile(fstFile)
    val expctedAnalyses = 17
    assert( analyzed.size == expctedAnalyses)
    for (analysis <- analyzed) {
      println(analysis.literalToken + " -> " + analysis.analyses.size + " analyses")
    }
  }

  it should "properly parse infinitive forms" in {
    val infinFst = "> monstravisse\n<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><infin>avisse<pft><act><u>proof.are_inf1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(infinFst)
  }


}
