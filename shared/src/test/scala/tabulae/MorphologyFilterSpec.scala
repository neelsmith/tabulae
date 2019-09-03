package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


class MorphologyFilterSpec extends FlatSpec {


  val nounForm = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
  val verbForm = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Third, Singular, Perfect, Indicative, Active)
  val adverbForm = AdverbForm("ls.n25115", "ocremorph.n25115", "irreginfl.2", Positive)

  "A MorphologyFilter" should "agree with anything if it has no parameters" in {
    val noProperties = MorphologyFilter()
    assert(noProperties.agrees(nounForm))
    assert(noProperties.agrees(verbForm))
  }

  it should "check part of speech (analytical type)" in {
    val mf = MorphologyFilter(pos = Some("noun"))
    assert(mf.posAgrees(nounForm))
    assert(mf.posAgrees(verbForm) == false)
  }

  it should "check person" in {
    val mf = MorphologyFilter(person = Some(Third))
    assert(mf.personAgrees(nounForm) == false)
    assert(mf.personAgrees(verbForm))
  }
  it should "check number" in {
    val mf = MorphologyFilter(grammaticalNumber = Some(Singular))
    assert(mf.numberAgrees(nounForm))
    assert(mf.numberAgrees(verbForm))
  }
  it should "check tense" in {
    val mf = MorphologyFilter(tense = Some(Perfect))
    assert(mf.tenseAgrees(nounForm) == false)
    assert(mf.tenseAgrees(verbForm))
  }

  it should "check mood" in {
    val mf = MorphologyFilter(mood = Some(Indicative))
    assert(mf.moodAgrees(nounForm) == false)
    assert(mf.moodAgrees(verbForm))
  }

  it should "check voice" in {
    val mf = MorphologyFilter(voice = Some(Active))
    assert(mf.voiceAgrees(nounForm) == false)
    assert(mf.voiceAgrees(verbForm))
  }

  it should "check gender" in {
    val mf = MorphologyFilter(gender = Some(Feminine))
    assert(mf.genderAgrees(nounForm))
    assert(mf.genderAgrees(verbForm) == false)
  }


  it should "check case" in {
    val mf = MorphologyFilter(grammaticalCase = Some(Nominative))
    assert(mf.caseAgrees(nounForm))
    assert(mf.caseAgrees(verbForm) == false)
  }

  it should "check degree" in {
    val mf = MorphologyFilter(degree = Some(Positive))
    assert(mf.degreeAgrees(adverbForm))
    assert(mf.degreeAgrees(verbForm) == false)
  }
  it should "check indeclinable PoS" in {
    val indeclForm = IndeclinableForm("proof.indecl1", "lexent.n11873", "indeclinfl.2", Conjunction)
    val mf = MorphologyFilter(indeclinablePoS = Some(Conjunction))
    assert(mf.indeclinableAgrees(indeclForm))
    assert(mf.indeclinableAgrees(verbForm) == false)
  }
}
