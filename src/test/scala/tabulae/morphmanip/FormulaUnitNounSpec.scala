package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._



import org.scalatest.FlatSpec




class FormulaUnitNounSpec extends FlatSpec {

  val fst = "> adsertori\n<u>ocremorph.n4035</u><u>ls.n4035</u>adsertor<noun><masc><0_is><div><0_is><noun>i<masc><dat><sg><u>ocremorph.0_is3</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val tkn = analyzedTokens(0)

  "An AnalyzedToken" should "recognize an analyzed noun token" in {
    assert(tkn.nounToken)
  }


  it should "recognize the gender of nouns" in  {

    val genderVector = tkn.substGender
    assert(genderVector.size == 1)
    assert(genderVector(0) == Masculine)
  }


  it should "recognize the grammatical case of nouns" in  {
    val caseVector = tkn.substCase
    assert(caseVector.size == 1)
    assert(caseVector(0) == Dative)
  }

  it should "recognize the number of noun forms" in {
    val numberVector = tkn.grammNumber
    assert(numberVector.size == 1)
    assert(numberVector(0) == Singular)
  }

  it should "construct a GCNTriple for a noun form" in  {
    val gcnVector = tkn.gcn
    val expected = Vector(GCNTriple(Masculine, Dative, Singular))
    assert(gcnVector == expected)
  }
}
