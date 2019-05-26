package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._


import org.scalatest.FlatSpec




class AnalyzedTokenAdjSpec extends FlatSpec {

  val fst = "> aeternae\n<u>ocremorph.n1413</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><nom><pl><pos><u>ocremorph.us_a_um42</u>\n<u>ocremorph.n1413</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><gen><sg><pos><u>ocremorph.us_a_um38</u>\n<u>ocremorph.n1413</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><dat><sg><pos><u>ocremorph.us_a_um39</u>\n<u>ocremorph.n1413</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><voc><pl><pos><u>ocremorph.us_a_um48</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val firstToken = analyzedTokens(0)

  "An AnalyzedToken" should "recognize the category of adjective tokens" in {
    assert(firstToken.adjToken)
  }


  it should "recognize the gender of adjective forms" in  {
    val genderVector = firstToken.substGender
    assert(genderVector.size == 1)
    assert(genderVector(0) == Feminine)
  }

  it should "recognize the grammatical case of adjective forms" in   {
    val caseVector = firstToken.substCase
    assert(caseVector.size == 3)
    assert(caseVector(0) == Nominative)
  }

  it should "recognize the number of adjective forms" in {
    val numberVector = firstToken.grammNumber
    assert(numberVector.size == 2)
    assert(numberVector(0) == Plural)
  }

  it should "construct a GCNTriple for a adjective form" in  {
    val gcnVector = firstToken.gcn

    val expected = GCNTriple(Feminine, Nominative, Plural)
    assert(gcnVector(0) == expected)
  }


}
