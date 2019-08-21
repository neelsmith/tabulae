
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class PronounFormSpec extends FlatSpec {
//<u>proof.irrpron1</u><u>lexent.n20640</u>hic<masc><nom><sg><irregpron>
//<div>
//<irregpron><u>irreginfl.7</u>

  "A  PronounForm" should "require GCN in constructor" in {
    val pronounForm = PronounForm("ls.n20640", "proof.irrpron1","proof.irreginfl7", Masculine, Nominative, Singular)
    pronounForm match {
      case prf: PronounForm => assert(true)
      case _ => fail("Should have instantiated a NounForm")
    }
  }

  it should "be recognized as an instance of a Form" in pending /* {
    val form = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    form match {
      case nf: NounForm => assert(true)
      case _ => fail("Should have created a NounForm")
    }
  }*/
  it should "implement all functions of the LemmatizedForm trait" in pending /*{
    val form = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    assert(form.lemmaId == "ls.n1315")
    assert(form.stemId == "ocremorph.n27")
    assert(form.ruleId == "ocremorph.stis13")
    assert(form.posLabel == "noun")
  }*/
}
