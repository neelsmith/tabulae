
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class IrregStemParsingSpec extends FlatSpec {
/*
    val stemFst = "<u>pliny.indecl1</u><u>lexent.tbd</u>cum<indecl><indeclconj>"

    val ruleFst = "<indeclconj><indecl><u>lindeclinfl.1</u>"

    val fst = stemFst + "<div>" +  ruleFst
*/

    val stemFst = "<u>ocremorph.n25359mns</u><u>lexent.n25359</u>ivppiter<masc><nom><sg><irregnoun>"

    val ruleFst = "<irregnoun><u>irreginfl.0</u>"

    val fst = stemFst + "<div>" +  ruleFst


    //val irregNounFst = "<u>ocremorph.n25359mns</u><u>lexent.n25359</u>ivppiter<masc><nom><sg><irregnoun><div><irregnoun><u>irreginfl.0</u>"

  "The FstRule object" should  "recognize irregular noun forms" in {
    println("parse " + fst)
    val f = LemmatizedForm(fst)
    println("PARSED " + f)
    //${pos}>")

    //<u>ocremorph.indecl2</u><u>ls.n16278</u>et<indeclconj><div><indeclconj><indecl><u>indeclinfl.2</u>
/*

    val rule = FstRule(ruleFst)
    rule match {
      case ir: IndeclRule => {
        assert(ir.ruleId == "indeclinfl.2")
        assert(ir.pos == "indeclconj")
      }
      case _ => fail("Should have formed an IndeclRule")
    }*/
  }


}
