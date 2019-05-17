package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import org.scalatest.FlatSpec




class FormulaUnitPrepSpec extends FlatSpec {

  val fst = "> ex\n<u>ocremorph.n16519</u><u>ls.n16519</u>ex<indecl><indeclprep><div><indeclprep><indecl><u>indeclinfl.1</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val tkn = analyzedTokens(0)

   "An AnalyzedToken" should "recognize the a preposition as an indeclinable form" in pending/* {

    val indeclVector = tkn.indeclPos
    assert(indeclVector.size == 1)
    assert(indeclVector(0) == Preposition)
  }*/

  it should "recognize the part of speech as a preposition" in  pending/*{
    val fu : FormulaUnit = FormulaUnit(tkn)
    assert(fu.prepToken)
  }
*/

}
