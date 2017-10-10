
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstRuleParsingSpec extends FlatSpec {


  "The FstRule object" should "parse the stem part of an FST reply into an FstRule object" in pending/* {
    val ruleFst = "<h_hs><noun>h<fem><voc><sg><u>nouninfl.h_hs5</u>"

    val rule = FstRule(ruleFst)
    rule match {
      case nr: NounRule => {
        assert(nr.ruleId == "nouninfl.h_hs5")
        assert(nr.gender == "fem")
        assert(nr.grammaticalCase == "voc")
        assert(nr.grammaticalNumber == "sg")
        assert(nr.declClass == "h_hs")
        assert(nr.ending == "h")

      }
    }
  }*/



}
