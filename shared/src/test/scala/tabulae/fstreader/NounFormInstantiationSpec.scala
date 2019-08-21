
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class NounFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular noun forms from FST source" in  {
    val adverbFst = "> puer\n<u>proof.nom1</u><u>lexent.n39429</u>puer<noun><masc><0_i><div><0_i><noun><masc><nom><sg><u>proof.0_i1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(adverbFst)
    val parse = parsed(0)
    assert(parse.literalToken == "puer")
    assert(parse.analyses.size == 1)

    val nounForm: NounForm = parse.analyses(0) match {
      case noun: NounForm => noun
      case _ => fail("Nope, that wasn't a noun.")
    }
    assert(nounForm.gender == Masculine)
    assert(nounForm.grammaticalCase == Nominative)
    assert(nounForm.grammaticalNumber == Singular)
  }

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
