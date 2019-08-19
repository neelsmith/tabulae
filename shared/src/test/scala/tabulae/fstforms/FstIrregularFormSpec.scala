
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstIrregularFormSpec extends FlatSpec {


  "The Form object" should "construct a parsed noun form from FST string input for irregular nouns" in {
    val fst = "<u>ocremorph.n25359mns</u><u>lexent.n25359</u>ivppiter<masc><nom><sg><irregnoun><div><irregnoun><u>irreginfl.0</u>"
    val f = LemmatizedForm(fst)
    f.get match {
      case nf: NounForm => {
        assert (nf.gender == Masculine)
    assert (nf.grammaticalCase == Nominative)
        assert (nf.grammaticalNumber == Singular)
      }
      case _ => fail("Should have created a noun form")
    }
  }
  it should "construct a parsed adjective form from FST string input for irregular adjectives" in pending
  it should "construct a parsed adverb form from FST string input for irregular adverbs" in {
    val fst = "<u>ocremorph.n25115</u><u>ls.n25115</u>itervm<pos><irregadv><div><irregadv><u>irreginfl.2</u>"
    val f = LemmatizedForm(fst)
    f.get match {
      case advf: AdverbForm => {
        assert (advf.degree == Positive)
      }
      case _ => fail("Should have created an adverb form")
    }
  }

  it should "construct a parsed infinitive form from FST string input for irregular infinitive" in pending
  it should "construct a parsed participle form from FST string input for irregular participle" in pending
  it should "construct a parsed verb form from FST string input for irregular verb" in pending



/* {
    val stemFst = "<u>lat24morph.advn790</u><u>ls.n790</u>adhuc<pos><irregadv>"
    val stemObj = FstStem(stemFst)
    stemObj match {
      case irreg: IrregularAdverb => {
        assert(irreg.stemId == "lat24morph.advn790")
        assert(irreg.lexEntity == "ls.n790")
        assert(irreg.degree == "pos")
      }
      case _ => fail("Should have created an AdverbStem")
    }
  }*/
  it should "parse irregular pronoun stems" in pending

}
