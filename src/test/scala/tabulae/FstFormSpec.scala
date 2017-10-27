
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstFormSpec extends FlatSpec {


  "The Form object" should "construct parsed grammatical form from FST string input" in {
    val fst = "<u>dev1.n1</u><u>lexent.n1</u>femin<noun><fem><a_ae>::<a_ae><noun>as<fem><acc><pl><u>lnouninfl.a_ae10</u>"
    val f = Form(fst)
    f match {
      case nf: NounForm => {
        assert (nf.gender == Feminine)
        assert (nf.grammaticalCase == Accusative)
        assert (nf.grammaticalNumber == Plural)
      }
      case _ => fail("Should have created a noun form")
    }
  }

  it should "construct parsed verb form from FST string input" in {
    val stemFst = "<u>dev1.v1</u><u>lexent.v1</u><#>am<verb><are_vb>"
    val ruleFst = "<are_vb><verb>i<1st><sg><pft><indic><act><u>lverbinfl.are_pftind1</u>"
    val fst = stemFst + "::" + ruleFst
    val f = Form(fst)
    f match {
      case vf: VerbForm => {
        assert (vf.person == First)
        assert (vf.grammaticalNumber == Singular)
        assert (vf.tense == Perfect)
        assert (vf.mood == Indicative)
        assert (vf.voice == Active)
      }
      case _ => fail("Should have created a verb form")
    }
  }


  it should "construct a parsed indeclinable form from FST string input" in  {
    val ruleFst = "<conjunct><indecl><u>lindeclinfl.1</u>"
    val stemFst = "<u>pliny.indecl1</u><u>lexent.tbd</u>cum<indecl><conjunct>"

    val fst = stemFst + "::" +  ruleFst
    val f = Form(fst)
    f match {
      case indeclForm: IndeclinableForm => {
        assert(indeclForm.pos == Conjunction)
      }
      case _ => fail("Should have created an indeclinable form")
    }
  }

  it should "contsruct a parsed adjectival form from FST string input" in pending
  it should "contsruct a parsed participal form from FST string input" in pending
  it should "contsruct a parsed infinitive form from FST string input" in pending
  it should "contsruct a parsed adverbial form from FST string input" in pending
}
