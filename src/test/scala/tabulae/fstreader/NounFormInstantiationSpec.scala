
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class NounFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular noun forms from FST source" in pending /* {
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

  it should "instantiate NounForms from FST source for irregular nouns" in {
    val irregNounFst = "> vis\n<u>proof.irrn1m</u><u>lexent.n51113</u>vis<fem><nom><sg><irregnoun><div><irregnoun><u>irreginfl.0</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(irregNounFst)
    val parse = parsed(0)
    assert(parse.literalToken == "vis")
    assert(parse.analyses.size == 1)

    val nounForm: NounForm = parse.analyses(0) match {
      case noun: NounForm => noun
      case _ => fail("Nope, that wasn't a noun.")
    }
    assert(nounForm.gender == Feminine)
    assert(nounForm.grammaticalCase == Nominative)
    assert(nounForm.grammaticalNumber == Singular)

  }


}
