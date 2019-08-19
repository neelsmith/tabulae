
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class PronounFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate pronoun forms from FST source for irregular pronouns" in  {
    val pronounFst = "> hic\n<u>proof.irrpron1</u><u>lexent.n20640</u>hic<masc><nom><sg><irregpron><div><irregpron><u>irreginfl.7</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(pronounFst)
    val parse = parsed(0)
    assert(parse.literalToken == "hic")
    assert(parse.analyses.size == 1)

    val pronounForm: PronounForm = parse.analyses(0) match {
      case pronoun: PronounForm => pronoun
      case _ => fail("Nope, that wasn't a pronoun.")
    }
    assert(pronounForm.gender == Masculine)
    assert(pronounForm.grammaticalCase == Nominative)
    assert(pronounForm.grammaticalNumber == Singular)
  }



}
