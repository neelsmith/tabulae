
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


/*> aeternae
<u>ocremorph.adj6</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><nom><pl><pos><u>ocremorph.us_a_um42</u>
<u>ocremorph.adj6</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><gen><sg><pos><u>ocremorph.us_a_um38</u>
<u>ocremorph.adj6</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><dat><sg><pos><u>ocremorph.us_a_um39</u>
<u>ocremorph.adj6</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><voc><pl><pos><u>ocremorph.us_a_um48</u>*/
class AdjectiveFormSpec extends FlatSpec {

  "An AdjectiveForm" should "require GCND in constructor" in {
    val adjForm = AdjectiveForm("ls.n1413", "ocremorph.adj6", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Positive)
    adjForm match {
      case adjForm: AdjectiveForm => assert(true)
      case _ => fail("Should have instantiated a AdjectiveForm")
    }
  }
  it should "implement all functions of the LemmatizedForm trait" in {
    val form =  AdjectiveForm("ls.n1413", "ocremorph.adj6", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Positive)
    assert(form.lemmaId == "ls.n1413")
    assert(form.stemId == "ocremorph.adj6")
    assert(form.ruleId == "ocremorph.us_a_um42")
    assert(form.posLabel == "adjective")
  }

}
