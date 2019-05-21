
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class VerbFormSpec extends FlatSpec {


  //val fst = "> fecit\n<u>ocremorph.n17516b</u><u>ls.n17516</u><#>fec<verb><pftact><div><pftact><verb>it<3rd><sg><pft><indic><act><u>livymorph.pftact_pft3</u>\n"

  "A  VerbForm" should "require PNTMV in constructor" in {
    val verbForm = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Third, Singular, Perfect, Indicative, Active)
    verbForm match {
      case vf: VerbForm => assert(true)
      case _ => fail("Should have instantiated a VerbForm")
    }
  }

  it should "be recognized as an instance of a Form" in {
    val form =  VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Third, Singular, Perfect, Indicative, Active)

    form match {
      case vf: VerbForm => assert(true)
      case _ => fail("Should have created a VerbForm")
    }
  }

  it should "implement all functions of the LemmatizedForm trait" in {
    val form =  VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Third, Singular, Perfect, Indicative, Active)
    assert(form.lemmaId == "ls.n17516")
    assert(form.stemId == "ocremorph.n17516b")
    assert(form.ruleId == "livymorph.pftact_pft3")
    assert(form.posLabel == "verb")
  }
}
