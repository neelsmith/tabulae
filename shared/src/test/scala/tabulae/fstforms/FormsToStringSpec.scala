
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


class FormsToStringSpec extends FlatSpec {

  "A LemmatizedForm" should "override toString for nouns" in {
    val nounForm = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    val expected = "noun: feminine, nominative, singular"
    assert(nounForm.toString == expected)
  }

  it should "override toString for pronouns" in {
    val pronounForm = PronounForm("ls.n20640", "proof.irrpron1","proof.irreginfl7", Masculine, Nominative, Singular)

    val expected = "pronoun: masculine, nominative, singular"
    assert(pronounForm.toString == expected)
  }

  it should "override toString for adjectives" in {
    val adjForm =  AdjectiveForm("ls.n1413", "ocremorph.adj6", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Positive)

    val expected = "adjective: feminine, nominative, plural, positive"
    assert(adjForm.toString == expected)
  }

  it should "override toString for adverbs" in {
    val advForm = AdverbForm("ls.n25115", "ocremorph.n25115", "irreginfl.2", Positive)
    val expected = "adverb: positive degree"
    assert(advForm.toString == expected)
  }
  it should "override toString for indeclinable forms" in {
    val indeclForm = IndeclinableForm("proof.indecl1","ls.n11873","proof.indeclinfl2",Conjunction)
    val expected = "uninflected form: conjunction"
    assert(indeclForm.toString == expected)
  }

  it should "override toString for conjugated verbs" in {
    val verbForm = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Third, Singular, Perfect, Indicative, Active)
    val expected = "verb: third, singular, perfect, indicative, active"
    assert(verbForm.toString == expected)
  }


  it should "override toString for infinitives" in {
    //<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><infin>avisse<pft><act><u>proof.are_inf1</u>

    val infinitiveForm = InfinitiveForm("lexent.n29616","proof.v1","proof.are_inf1",  Perfect, Active)
    val expected = "infinitive: perfect, active"
    assert(infinitiveForm.toString == expected)
  }


  it should "override toString for participles" in {
//<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><ptcpl>ans<masc><nom><sg><pres><act><u>proof.are_ptcpl1</u>
    val ptcplForm = ParticipleForm("lexent.n29616","proof.v1","proof.are_inf1",  Masculine, Nominative, Singular, Present, Active)
    val expected = "participle: masculine, nominative, singular, present, active"
    assert(ptcplForm.toString == expected)
  }
  it should "override toString for gerundives" in {
    val grndvForm = GerundiveForm("lexent.n29616","proof.v1","proof.are_inf1",  Masculine, Nominative, Singular)
    val expected = "gerundive: masculine, nominative, singular"
    assert(grndvForm.toString == expected)
  }
  it should "override toString for gerunds" in {
    val gerundForm = GerundForm("lexent.n29616","proof.v1","proof.are_inf1",  Genitive)
    val expected = "gerund: genitive"
    assert(gerundForm.toString == expected)
  }
  it should "override toString for supines" in {
    val supineForm = SupineForm("lexent.n29616","proof.v1","proof.are_inf1",  Ablative)
    val expected = "supine: ablative"
    assert(supineForm.toString == expected)
  }



}
