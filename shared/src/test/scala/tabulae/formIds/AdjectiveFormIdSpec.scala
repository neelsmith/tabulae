
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class AdjectiveFormIdSpec extends FlatSpec {

  "An AdjectiveForm" should "encode its form" in {
    val adjForm = AdjectiveForm("ls.n1413", "ocremorph.adj6", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Positive)
    val expected = "02000211"
    assert(adjForm.formId == expected)
  }
}
