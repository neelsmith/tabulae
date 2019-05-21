
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class LemmatizedFormObjectSpec extends FlatSpec {


  val verbFst = "<u>ocremorph.n17516b</u><u>ls.n17516</u><#>fec<verb><pftact><div><pftact><verb>it<3rd><sg><pft><indic><act><u>livymorph.pftact_pft3</u>"

  val lemmatizedVerbForm = LemmatizedForm(verbFst)

  "The LemmatizedFormObjectSpec" should "make a verb from FST" in {
    lemmatizedVerbForm match {
      case vf : VerbForm => assert(true)
      case _ => fail("Did not get a VerbFrom from fst " + verbFst)
    }

  }

  it should "correctly identify the lemma ID (short URN)" in {
    assert(lemmatizedVerbForm.lemmaId == "ls.n17516")
  }

  it should "correctly identify the stem ID (short URN)" in {
    assert(lemmatizedVerbForm.stemId == "ocremorph.n17516b")
  }

  it should "correctly identify the inflectional rule ID (short URN)" in {
    assert(lemmatizedVerbForm.ruleId == "livymorph.pftact_pft3")
  }
}
