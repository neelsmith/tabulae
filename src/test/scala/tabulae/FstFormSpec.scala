
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstFormSpec extends FlatSpec {
  val res1 = "<u>dev1.n1</u><u>lexent.n1</u>femin<noun><fem><a_ae>::<a_ae><noun>as<fem><acc><pl><u>lnouninfl.a_ae10</u>"

    "The Form object" should "construct Forms from FST input" in {

    val f = Form(res1)
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
