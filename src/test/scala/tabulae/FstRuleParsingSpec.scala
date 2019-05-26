
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstRuleParsingSpec extends FlatSpec {


  "The FstRule object" should "recognize nouns forms" in  {
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
  it should "recognize nouns forms with null string for ending" in  {
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

  it should "recognize adjective forms" in {

    val ruleFst = "<us_a_um><adj>o<masc><dat><sg><pos><u>ocremorph.us_a_um3</u>"
    val rule = FstRule(ruleFst)
    rule match {
      case ar: AdjectiveRule => {
      }
      case _ => fail("Should have formed an AdjectiveRule")
    }
  }
  it should "recognize adverbial forms" in pending

  it should "recognize conjugated verb forms" in {
    val ruleFst = "<conj1><verb>i<1st><sg><pft><indic><act><u>lverbinfl.are_pftind1</u>"
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

  it should "recognize infinitive forms of the verb" in pending



  it should "recognize forms of the gerund" in {
    val ruleFst = "<conj1><gerund>ando<dat><u>ocremorph.grd_conj1_2</u>"
    val rule = FstRule(ruleFst)
    rule match {
      case gr: GerundRule => {
      }
      case _ => fail("Should have formed a GerundRule")
    }
  }

  it should "recognize participial forms" in {
    val ruleFst = "<pftpass><ptcpl>i<masc><nom><pl><pft><pass><u>ocremorph.pft_perfppl7</u>"
    val rule = FstRule(ruleFst)
    rule match {
      case pr: ParticipleRule => {
        assert(pr.ruleId == "ocremorph.pft_perfppl7")
        assert(pr.gender == "masc")
        assert(pr.grammaticalCase == "nom")
        assert(pr.grammaticalNumber == "pl")
        assert(pr.tense == "pft")
        assert(pr.voice == "pass")
      }
      case _ => fail("Should have formed a ParticipleRule")
    }
  }


  it should "recognize gerundive forms" in pending

  it should "recognize indeclinable forms" in  {
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
