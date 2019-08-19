package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._


import org.scalatest.FlatSpec




class AnalyzedTokenIndeclSpec extends FlatSpec {

  val fst = "> de\n<u>ocremorph.n12361</u><u>ls.n12361</u>de<indecl><indeclprep><div><indeclprep><indecl><u>indeclinfl.1</u>\n".split("\n").toVector
  val analyzedTokens = FstReader.parseFstLines(fst)
  val firstToken = analyzedTokens(0)

  "An AnalyzedToken" should "recognize the category of indeclinable tokens" in {
    assert(firstToken.indeclToken)
  }


  it should "recognize the part of speech of indeclinable forms" in   {
    val posVector = firstToken.indeclPos
    assert(posVector.size == 1)
    assert(posVector(0) == Preposition)
  }

  it should "recognize prepositions in indeclinable forms" in   {
    assert(firstToken.prepToken)
  }



}
