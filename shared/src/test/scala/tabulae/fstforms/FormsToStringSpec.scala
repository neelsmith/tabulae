
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


class FormsToStringSpec extends FlatSpec {

  "A LemmatizedForm" should "override toString for adjectives" in {
    val adjForm =  AdjectiveForm("ls.n1413", "ocremorph.adj6", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Positive)

    val expected = "adjective: feminine, nominative, plural, positive"
    assert(adjForm.toString == expected)
  }

}
/*
case v: VerbForm => "verb"
case n: NounForm => "noun"
case pron: PronounForm => "pronoun"
case adj: AdjectiveForm => "adjective"
case ptcpl: ParticipleForm => "participle"
case gnd: GerundForm => "gerund"
case gndv: GerundiveForm => "gerundive"
case adv: AdverbForm => "adverb"
case indecl: IndeclinableForm => "indeclinable"
case inf: InfinitiveForm => "infinitive"
case sup: SupineForm => "supine"
*/
