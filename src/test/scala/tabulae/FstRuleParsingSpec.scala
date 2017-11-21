
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstRuleParsingSpec extends FlatSpec {


  "The FstRule object" should "parse the stem part of an FST reply into an FstRule object" in  {
    val ruleFst = "<a_ae><noun>as<fem><acc><pl><u>lnouninfl.a_ae10</u>"
    val rule = FstRule(ruleFst)
    rule match {
      case nr: NounRule => {
        assert(nr.ruleId == "lnouninfl.a_ae10")
        assert(nr.gender == "fem")
        assert(nr.grammaticalCase == "acc")
        assert(nr.grammaticalNumber == "pl")
        assert(nr.declClass == "a_ae")
        assert(nr.ending == "as")

      }
    }
  }

  it should "recognize verb forms" in {
    val ruleFst = "<are_vb><verb>i<1st><sg><pft><indic><act><u>lverbinfl.are_pftind1</u>"
    val rule = FstRule(ruleFst)
    rule match {
      case vr: VerbRule => {
        assert(vr.ruleId == "lverbinfl.are_pftind1")
        assert(vr.person == "1st")
        assert(vr.grammaticalNumber == "sg")
        assert(vr.tense == "pft")
        assert(vr.mood == "indic")
        assert(vr.voice == "act")

      }
      case _ => fail("Should have formed a VerbRule")
    }
  }

  it should "recognize indeclinable forms" in {
    val ruleFst = "<conjunct><indecl><u>lindeclinfl.1</u>"
    val rule = FstRule(ruleFst)
    rule match {
      case ir: IndeclRule => {
        assert(ir.ruleId == "lindeclinfl.1")
        assert(ir.pos == "conjunct")
      }
      case _ => fail("Should have formed an IndeclRule")
    }
  }



  it should "recognize adjective forms" in pending /*{
    val ruleFst = "<conjunct><indecl><u>lindeclinfl.1</u>"
    val rule = FstRule(ruleFst)
    rule match {
      case ar: AdjectiveRule => {
      }
      case _ => fail("Should have formed an AdjectiveRule")
    }
  } */


  it should "recognize participial forms" in pending
  it should "recognize infinitive forms" in pending
  it should "recognize adverbial forms" in pending

}
