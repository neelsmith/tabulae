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



  it should "recognize valid URNs for participle forms" in {
    //urn:cite2:tabulae:morphforms.v1:014021305#participle: perfect passive masculine dative singular
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:014021305")
    val form = ValidForm(urn)
    form match {
      case p: ValidParticipleForm => {
        assert(p.tense == Perfect)
        assert(p.voice == Passive)
        assert(p.gender == Masculine)
        assert(p.grammaticalCase == Dative)
        assert(p.grammaticalNumber == Singular)
      }
      case _ => fail("Should have created a participle form")
    }
  }

  it should "recognize valid URNs for infinitive verb forms" in {
    //urn:cite2:tabulae:morphforms.v1:004010006#infinitive: perfect active
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:004010006")
    val form = ValidForm(urn)
    form match {
      case p: ValidInfinitiveForm => {
        assert(p.tense == Perfect)
        assert(p.voice == Active)
      }
      case _ => fail("Should have created an infinitive form")
    }
  }

  it should "recognize valid URNs for gerundive  forms" in {
    //urn:cite2:tabulae:morphforms.v1:010002307#gerundive: feminine dative singular
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:010002307")
    val form = ValidForm(urn)
    form match {
      case g: ValidGerundiveForm => {
        assert(g.gender == Feminine)
        assert(g.grammaticalCase == Dative)
        assert(g.grammaticalNumber == Singular)
      }
      case _ => fail("Should have created a gerundive form")
    }
  }

  it should "recognize valid URNs for gerund forms" in {
    //urn:cite2:tabulae:morphforms.v1:000000208#gerund: genitive
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000208")
    val form = ValidForm(urn)
    form match {
      case g: ValidGerundForm => {
        assert(g.grammaticalCase == Genitive)
      }
      case _ => fail("Should have created a gerund form")
    }
  }
  it should "recognize valid URNs for supine forms" in {
    // urn:cite2:tabulae:morphforms.v1:000000509#supine: ablative
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000509")
    val form = ValidForm(urn)
    form match {
      case s: ValidSupineForm => {
        assert(s.grammaticalCase == Ablative)
      }
      case _ => fail("Should have created a supine form")
    }
  }
  it should "recognize valid URNs for uninflected conjunctions" in {
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:00000000A")
    val form = ValidForm(urn)
    form match {
      case vuf: ValidUninflectedForm => {
        assert(vuf.indeclinablePoS == Conjunction)
      }
      case _ => fail("Should have created an uninflected form")
    }
  }
  it should "recognize valid URNs for uninflected numerals" in {
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:00000000D")
    val form = ValidForm(urn)
    form match {
      case vuf: ValidUninflectedForm => {
        assert(vuf.indeclinablePoS == Numeral)
      }
      case _ => fail("Should have created an uninflected form")
    }
  }
  it should "recognize valid URNs for uninflected prepositions" in {
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:00000000B")
    val form = ValidForm(urn)
    form match {
      case vuf: ValidUninflectedForm => {
        assert(vuf.indeclinablePoS == Preposition)
      }
      case _ => fail("Should have created an uninflected form")
    }
  }
  it should "recognize valid URNs for uninflected exclamations" in {
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:00000000C")
    val form = ValidForm(urn)
    form match {
      case vuf: ValidUninflectedForm => {
        assert(vuf.indeclinablePoS == Exclamation)
      }
      case _ => fail("Should have created an uninflected form")
    }
  }

  /*
  urn:cite2:tabulae:morphforms.v1:00000000A#uninflected conjunction
urn:cite2:tabulae:morphforms.v1:00000000B#uninflected preposition
urn:cite2:tabulae:morphforms.v1:00000000C#uninflected exclamation
urn:cite2:tabulae:morphforms.v1:00000000D#uninflected number
  */
}
