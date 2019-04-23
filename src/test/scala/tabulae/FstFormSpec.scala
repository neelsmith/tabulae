
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstFormSpec extends FlatSpec {


  "The Form object" should "construct a parsed noun form from FST string input" in {
    val fst = "<u>dev1.n1</u><u>lexent.n1</u>femin<noun><fem><a_ae><div><a_ae><noun>as<fem><acc><pl><u>lnouninfl.a_ae10</u>"
    val f = LemmatizedForm(fst)
    f match {
      case nf: NounForm => {
        assert (nf.gender == Feminine)
        assert (nf.grammaticalCase == Accusative)
        assert (nf.grammaticalNumber == Plural)
      }
      case _ => fail("Should have created a noun form")
    }
  }

  it should "construct a parsed conjugated verb form from FST string input" in {
    val stemFst = "<u>dev1.v1</u><u>lexent.v1</u><#>am<verb><conj1>"
    val ruleFst = "<conj1><verb>i<1st><sg><pft><indic><act><u>lverbinfl.are_pftind1</u>"
    val fst = stemFst + "<div>" + ruleFst
    val f = LemmatizedForm(fst)
    f match {
      case vf: VerbForm => {
        assert (vf.person == First)
        assert (vf.grammaticalNumber == Singular)
        assert (vf.tense == Perfect)
        assert (vf.mood == Indicative)
        assert (vf.voice == Active)
      }
      case _ => fail("Should have created a verb form")
    }
  }



  it should "construct a parsed adjectival form from FST string input" in  {
    val fst = "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<masc><dat><sg><pos><u>ocremorph.us_a_um3</u>"
    val f = LemmatizedForm(fst)
    f match {
      case adjForm: AdjectiveForm => {
        assert(adjForm.gender == Masculine)
      }
      case _ => fail("Should have created an adjectival form")
    }

  }

  it should "construct a parsed participal form from FST string input" in pending /*{
    val fst = "<u>plinymorph.verb2</u><u>lexent.n29544</u><#>mon<verb><conj2><div><conj2><ptcpl>ens<masc><nom><sg><pres><act><u>lverbinfl.ere_conj2presapl1</u>"
    val f = LemmatizedForm(fst)
    f match {
      case ptcplForm: ParticipleForm => {
        assert(ptcplForm.gender == Masculine)
      }
      case _ => fail("Should have created a participial form")
    }
  }*/
  it should "construct a parsed infinitive form from FST string input" in pending /*{
    val fst = "<u>plinymorph.verb2</u><u>lexent.n29544</u><#>mon<verb><conj2><div><conj2><infin>uisse<pft><act><u>lverbinfl.ere_inf3</u>"
    val f = LemmatizedForm(fst)
    f match {
      case infinitiveForm: InfinitiveForm => {
        assert(infinitiveForm.voice == Active)
      }
      case _ => fail("Should have created an infinitive form")
    }
  }*/
  it should "contsruct a parsed adverbial form from FST string input" in pending /* {
    val fst = "<u>plinymorph.adj90</u><u>lexent.n18204</u>finitim<adj><us_a_um><div><us_a_um><adv>e<pos><u>advnfl.us_a_um1</u>"
    val f = LemmatizedForm(fst)
    f match {
      case adverbForm: AdverbForm => {
        assert(adverbForm.degree == Positive)
      }
      case _ => fail("Should have created an adverbial form")
    }
  }*/

  it should "construct a parsed gerundive form from FST string input" in pending
  it should "construct a parsed gerund form from FST string input" in pending
  it should "construct a parsed supine form from FST string input" in pending



  it should "construct a parsed noun form from FST string input for irregular nouns" in pending
  it should "construct a parsed adjective form from FST string input for irregular adjectives" in pending
  it should "construct a parsed adverb form from FST string input for irregular adverbs" in pending
  it should "construct a parsed infinitive form from FST string input for irregular infinitive" in pending
  it should "construct a parsed participle form from FST string input for irregular participle" in pending
  it should "construct a parsed verb form from FST string input for irregular verb" in pending
}
