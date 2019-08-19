
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class GerundFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular gerund forms from FST source" in pending /* {
    val adverbFst = "> care\n<u>proof.adj1</u><u>lexent.n6903</u>car<adj><us_a_um><div><us_a_um><adv>e<pos><u>proof.us_a_um1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(adverbFst)
    val parse = parsed(0)
    assert(parse.literalToken == "care")
    assert(parse.analyses.size == 1)

    val infForm: AdverbForm = parse.analyses(0) match {
      case adv: AdverbForm => adv
      case _ => fail("Nope, that wasn't an adverb.")
    }
    assert(infForm.degree == Positive)
  }*/


}
