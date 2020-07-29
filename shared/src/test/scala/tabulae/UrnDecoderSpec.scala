package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import org.scalatest.FlatSpec

// This only works if you're online:  the LewisShort object loads
// data over the internet.
class UrnDecoderSpec extends FlatSpec {

  "The LemmatizedForm object" should "should recognize valid URNs for noun forms" in  {
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:020001400")

    val form = ValidForm(urn)
    form match {
      case vnf: ValidNounForm => {
        assert(vnf.gender == Masculine)
        assert(vnf.grammaticalCase == Accusative)
        assert(vnf.grammaticalNumber == Plural)
      }
      case _ => fail("Should have created a noun form")
    }
  }

  it should "recognize valid URNs for pronoun forms" in {
    // urn:cite2:tabulae:morphforms.v1:020003201#pronoun: neuter genitive plural
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:020003201")
    val form = ValidForm(urn)
    form match {
      case vpr: ValidPronounForm => {
        assert(vpr.gender == Neuter)
        assert(vpr.grammaticalCase == Genitive)
        assert(vpr.grammaticalNumber == Plural)
      }
      case _ => fail("Should have created a pronoun form")
    }
  }
  it should "recognize valid URNs for adjective forms" in {
    //urn:cite2:tabulae:morphforms.v1:020002612#adjective: feminine vocative plural positive
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:020002612")
    val form = ValidForm(urn)
    form match {
      case adj: ValidAdjectiveForm => {
        assert(adj.gender == Feminine)
        assert(adj.grammaticalCase == Vocative)
        assert(adj.grammaticalNumber == Plural)
        assert(adj.degree == Positive)
      }
      case _ => fail("Should have created an adjective form")
    }

  }
  it should "recognize valid URNs for adverb forms" in {
    //urn:cite2:tabulae:morphforms.v1:000000023#adverb: comparative degree
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000023")
    val form = ValidForm(urn)
    form match {
      case adv: ValidAdverbForm => {
        assert(adv.degree == Comparative)
      }
      case _ => fail("Should have created an adverb form")
    }
  }
  it should "recognize valid URNs for finite verb forms" in {
    // urn:cite2:tabulae:morphforms.v1:212210004#finite verb: second singular imperfect subjunctive active
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:212210004")
    val form = ValidForm(urn)
    form match {
      case v: ValidFiniteVerbForm => {
        assert(v.person == Second)
        assert(v.grammaticalNumber == Singular)
        assert(v.tense == Imperfect)
        assert(v.mood == Subjunctive)
        assert(v.voice == Active)
      }
      case _ => fail("Should have created a finite verb form")
    }

  }



  it should "recognize valid URNs for participle forms" in pending

  it should "recognize valid URNs for infinitive verb forms" in pending
  it should "recognize valid URNs for gerundive  forms" in pending
  it should "recognize valid URNs for gerund forms" in pending
  it should "recognize valid URNs for supine forms" in pending
  it should "recognize valid URNs for uninflected forms" in pending

}
