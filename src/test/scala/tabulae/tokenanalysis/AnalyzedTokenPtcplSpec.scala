package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._


import org.scalatest.FlatSpec




class AnalyzedTokenPtcplSpec extends FlatSpec {

  val fst = "> designatvs\n<u>ocremorph.n13382c</u><u>ls.n13382</u><#>designat<verb><pftpass><div><pftpass><ptcpl>vs<masc><nom><sg><pft><pass><u>ocremorph.pft_perfppl1</u>\n".split("\n").toVector
  val analyzedTokens = FstReader.parseFstLines(fst)
  val tkn = analyzedTokens(0)

  "An AnalyzedToken" should "recognize the category of participle tokens" in  {
    assert(tkn.ptcplToken)
  }


  it should "recognize the gender of participle forms" in {
    val genderVector = tkn.substGender
    assert(genderVector.size == 1)
    assert(genderVector(0) == Masculine)
  }

  it should "recognize the grammatical case of participle forms" in  {
    val caseVector = tkn.substCase
    assert(caseVector.size == 1)
    assert(caseVector(0) == Nominative)
  }

  it should "recognize the number of participle forms" in {
    val numberVector = tkn.grammNumber
    assert(numberVector.size == 1)
    assert(numberVector(0) == Singular)
  }

  it should "construct a GCNTriple for a participle form" in  {
    val gcnVector = tkn.gcn

    val expected = GCNTriple(Masculine, Nominative, Singular)
    assert(gcnVector(0) == expected)
  }

  it should "recognize the tense of participle forms" in  {
    val tenseVector = tkn.tense
    assert(tenseVector.size == 1)
    assert(tenseVector(0) == Perfect)
  }

  it should "recognize the voice of participle forms" in  {
    val voiceVector = tkn.voice
    assert(voiceVector.size == 1)
    assert(voiceVector(0) == Passive)
  }


}
