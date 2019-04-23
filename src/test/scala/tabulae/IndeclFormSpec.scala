
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class IndeclFormSpec extends FlatSpec {


  "The Form object" should  "construct a parsed indeclinable form from FST string input" in  {
    val ruleFst = "<indeclconj><indecl><u>lindeclinfl.1</u>"
    val stemFst = "<u>pliny.indecl1</u><u>lexent.tbd</u>cum<indecl><indeclconj>"

    val fst = stemFst + "<div>" +  ruleFst
    val f = LemmatizedForm(fst)
    f match {
      case indeclForm: IndeclinableForm => {
        assert(indeclForm.pos == Conjunction)
      }
      case _ => fail("Should have created an indeclinable form")
    }
  }

}
