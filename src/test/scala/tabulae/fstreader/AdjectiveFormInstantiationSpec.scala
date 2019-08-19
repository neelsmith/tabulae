
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class AdjectiveFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular adjective forms from FST source" in {
    val adjFst = "> carus\n<u>proof.adj1</u><u>lexent.n6903</u>car<adj><us_a_um><div><us_a_um><adj>us<masc><nom><sg><pos><u>adjinfl.us_a_um1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(adjFst)
    val parse = parsed(0)
    assert(parse.literalToken == "carus")
    assert(parse.analyses.size == 1)

    val adjForm: AdjectiveForm = parse.analyses(0) match {
      case adj: AdjectiveForm => adj
      case _ => fail("Nope, that wasn't an adjective.")
    }

    assert(adjForm.gender == Masculine)
    assert(adjForm.grammaticalCase == Nominative)
    assert(adjForm.grammaticalNumber == Singular)
    assert(adjForm.degree == Positive)
  }




  it should "instantiate Adjectiveforms from FST source for irregular adjectives" in {
    val adjFst = "> totius\n<u>proof.irradj1</u><u>lexent.n48627</u>totius<masc><gen><sg><pos><irregadj><div><irregadj><u>irreginfl.1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(adjFst)
    val parse = parsed(0)
    assert(parse.literalToken == "totius")
    assert(parse.analyses.size == 1)

    val adjForm: AdjectiveForm = parse.analyses(0) match {
      case adj: AdjectiveForm => adj
      case _ => fail("Nope, that wasn't an adjective.")
    }

    assert(adjForm.gender == Masculine)
    assert(adjForm.grammaticalCase == Genitive)
    assert(adjForm.grammaticalNumber == Singular)
    assert(adjForm.degree == Positive)

  }


}
