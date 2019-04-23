
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class IndeclRuleParsingSpec extends FlatSpec {


  "The FstRule object" should  "recognize indeclinable forms" in {
    val ruleFst = "<indeclconj><indecl><u>indeclinfl.2</u>"

    // fstBuilder.append(s"<u>${stemUrn}</u><u>${lexEntity}</u>${stem}<${pos}>")

    //<u>ocremorph.indecl2</u><u>ls.n16278</u>et<indeclconj><div><indeclconj><indecl><u>indeclinfl.2</u>


    val rule = FstRule(ruleFst)
    rule match {
      case ir: IndeclRule => {
        assert(ir.ruleId == "indeclinfl.2")
        assert(ir.pos == "indeclconj")
      }
      case _ => fail("Should have formed an IndeclRule")
    }
  }


}
