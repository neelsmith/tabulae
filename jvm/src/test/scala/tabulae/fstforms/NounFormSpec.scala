
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class NounFormSpec extends FlatSpec {
//<u>ocremorph.n27</u><u>ls.n1315</u>aeqvita<noun><fem><s_tis><div><s_tis><noun>s<fem><nom><sg><u>ocremorph.stis13</u>

  "A  NounForm" should "require GCN in constructor" in {
    val nounForm = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    nounForm match {
      case nf: NounForm => assert(true)
      case _ => fail("Should have instantiated a NounForm")
    }
  }

  it should "be recognized as an instance of a Form" in  {
    val form = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    form match {
      case nf: NounForm => assert(true)
      case _ => fail("Should have created a NounForm")
    }
  }
  it should "implement all functions of the LemmatizedForm trait" in {
    val form = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    assert(form.lemmaId == "ls.n1315")
    assert(form.stemId == "ocremorph.n27")
    assert(form.ruleId == "ocremorph.stis13")
    assert(form.posLabel == "noun")
  }
}
