package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


class MorphologicalQuerySpec extends FlatSpec {

  "A LemmatizedForm" should "extract case from substantives" in {
    val fst = "<u>ocremorph.n25359mns</u><u>lexent.n25359</u>ivppiter<masc><nom><sg><irregnoun><div><irregnoun><u>irreginfl.0</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.substantiveCase.get == Nominative)
  }

  it should "extract gender from substantives" in {
    val fst = "<u>ocremorph.n25359mns</u><u>lexent.n25359</u>ivppiter<masc><nom><sg><irregnoun><div><irregnoun><u>irreginfl.0</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.substantiveGender.get == Masculine)
  }

  it should "extract number from substantives" in {
    val fst = "<u>ocremorph.n25359mns</u><u>lexent.n25359</u>ivppiter<masc><nom><sg><irregnoun><div><irregnoun><u>irreginfl.0</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.substantiveNumber.get == Singular)
  }

  it should "extract person from conjugated verbs" in {
    val fst = "<u>proof.irrv1</u><u>lexent.n15868</u><#>it<3rd><sg><pres><indic><act><irregcverb><div><irregcverb><u>irreginfl.3</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.verbPerson.get == Third)
  }
  it should "extract number from conjugated verbs" in {
    val fst = "<u>proof.irrv1</u><u>lexent.n15868</u><#>it<3rd><sg><pres><indic><act><irregcverb><div><irregcverb><u>irreginfl.3</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.verbNumber.get == Singular)
  }
  it should "extract tense from conjugated verbs" in {
    val fst = "<u>proof.irrv1</u><u>lexent.n15868</u><#>it<3rd><sg><pres><indic><act><irregcverb><div><irregcverb><u>irreginfl.3</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.verbTense.get == Present)
  }
  it should "extract mood from conjugated verbs" in {
    val fst = "<u>proof.irrv1</u><u>lexent.n15868</u><#>it<3rd><sg><pres><indic><act><irregcverb><div><irregcverb><u>irreginfl.3</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.verbMood.get == Indicative)
  }
  it should "extract voice from conjugated verbs" in {
    val fst = "<u>proof.irrv1</u><u>lexent.n15868</u><#>it<3rd><sg><pres><indic><act><irregcverb><div><irregcverb><u>irreginfl.3</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.verbVoice.get == Active)
  }

  it should "extract degree from adverbs" in {
    val fst = "<u>proof.irradv1</u><u>lexent.n5558</u>bene<pos><irregadv><div><irregadv><u>irreginfl.2</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.adverbDegree.get == Positive)
  }


  it should "extract part of speech from indeclinable forms" in {
    val fst = "<u>proof.indecl1</u><u>lexent.n11873</u>cum<indecl><indeclconj><div><indeclconj><indecl><u>indeclinfl.2</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.indeclinablePartOfSpeech.get == Conjunction)
  }

  it should "extract tense from infinitive forms" in {
    val fst = "<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><infin>avisse<pft><act><u>proof.are_inf1</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.infinitiveTense.get == Perfect)
  }
  it should "extract voice from infinitive forms" in {
    val fst = "<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><infin>avisse<pft><act><u>proof.are_inf1</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.infinitiveVoice.get == Active)
  }



  it should "extract tense from participle forms" in {
    val fst = "<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><ptcpl>ans<masc><nom><sg><pres><act><u>proof.are_ptcpl1</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.participleTense.get == Present)
  }
  it should "extract voice from participle forms" in {
    val fst = "<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><ptcpl>ans<masc><nom><sg><pres><act><u>proof.are_ptcpl1</u>"
    val f = LemmatizedForm(fst)
    assert(f.get.participleVoice.get == Active)
  }

}
