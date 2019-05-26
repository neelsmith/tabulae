
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class IrregStemParsingSpec extends FlatSpec {

  "The FstRule object" should  "recognize irregular noun forms" in {
      val stemFst = "<u>ocremorph.n25359mns</u><u>lexent.n25359</u>ivppiter<masc><nom><sg><irregnoun>"
      val ruleFst = "<irregnoun><u>irreginfl.0</u>"
      val fst = stemFst + "<div>" +  ruleFst

      val f = LemmatizedForm(fst)
      f match {
        case nf: NounForm => {
          assert(nf.gender == Masculine)
        }
        case _ => fail("Expected to get a NouNForm from " + fst)
      }
    }

    it should "recognize irregular adjective forms" in pending
    it should "recognize irregular conjugated verb forms" in pending
}
