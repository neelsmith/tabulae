package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import org.scalatest.FlatSpec




class AnalyzedTokenVerbSpec extends FlatSpec {

  val fst = "> fecit\n<u>ocremorph.n17516b</u><u>ls.n17516</u><#>fec<verb><pftact><div><pftact><verb>it<3rd><sg><pft><indic><act><u>livymorph.pftact_pft3</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val tkn = analyzedTokens(0)

  "An AnalyzedToken" should "recognize the category of verb tokens" in {
    assert(tkn.verbToken)
  }


  it should "recognize the person of a verb token" in  {
    val personVector = tkn.person
    assert(personVector.size == 1)
    assert(personVector(0) == Third)
  }

  it should "recognized the number of a verb token" in {
    val numberVector = tkn.grammNumber
    assert(numberVector.size == 1)
    assert(numberVector(0) == Singular)
  }

  it should "recognized the tense of a verb token" in {
    val tenseVector = tkn.tense
    assert(tenseVector.size == 1)
    assert(tenseVector(0) == Perfect)
  }

  it should "recognized the mood of a verb token" in {
    val moodVector = tkn.mood
    assert(moodVector.size == 1)
    assert(moodVector(0) == Indicative)
  }

  it should "recognized the voice of a verb token" in {
      val voiceVector = tkn.voice
      assert(voiceVector.size == 1)
      assert(voiceVector(0) == Active)
  }

}
