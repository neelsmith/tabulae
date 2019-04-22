
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class EmptyStringRuleParsingSpec extends FlatSpec {

//<masc><nom><sg><u>ocremorph.0_is1</u>
//<u>ocremorph.persn20</u><u>ls.n6080</u>caesar<noun><masc><0_is><div>
//<0_is><noun><masc><nom><sg><u>ocremorph.0_is1</u>

  "The FstRule object" should "recognize nouns forms with null string for ending" in  {
    val ruleFst = "<0_is><noun><masc><nom><sg><u>ocremorph.0_is1</u>"
    val rule = FstRule(ruleFst)
    rule match {
      case nr: NounRule => {
        assert(nr.ruleId == "ocremorph.0_is1")
        assert(nr.gender == "masc")
        assert(nr.grammaticalCase == "nom")
        assert(nr.grammaticalNumber == "sg")
        assert(nr.declClass == "0_is")
        assert(nr.ending == "")

      }
    }
  }


}
