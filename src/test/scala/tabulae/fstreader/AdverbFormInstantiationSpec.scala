
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class AdverbFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular adverb forms from FST source" in {
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
  }

  it should  "instantiate create AdverbForms from FST source for irregular adverbs" in {


    val adverbFst = "> bene\n<u>proof.irradv1</u><u>lexent.n5558</u>bene<pos><irregadv><div><irregadv><u>irreginfl.2</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(adverbFst)
    val parse = parsed(0)
    assert(parse.literalToken == "bene")
    assert(parse.analyses.size == 1)

    val infForm: AdverbForm = parse.analyses(0) match {
      case adv: AdverbForm => adv
      case _ => fail("Nope, that wasn't an adverb.")
    }
    assert(infForm.degree == Positive)

  }

}
