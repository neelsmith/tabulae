
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

}
