
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._

import org.scalatest.FlatSpec

class AdjectiveFormIdSpec extends FlatSpec {

  "An AdjectiveForm" should "encode its form as an 8-digit string" in {
    val adjForm = AdjectiveForm("ls.n1413", "ocremorph.adj6", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Positive)
    val expected = "020002112"
    assert(adjForm.formId == expected)
  }

  it should "encode its form as a URN" in {
    val adjForm = AdjectiveForm("ls.n1413", "ocremorph.adj6", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Positive)
    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:020002112")
    assert(adjForm.formUrn == expected)
  }
}
