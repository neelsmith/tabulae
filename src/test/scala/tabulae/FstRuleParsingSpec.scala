
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



}
