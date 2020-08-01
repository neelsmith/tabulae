package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._

import org.scalatest.FlatSpec

class PronounFormIdSpec extends FlatSpec {

  "A  PronounForm" should "require encode its form" in {
    val pronounForm = PronounForm("ls.n20640", "proof.irrpron1","proof.irreginfl7", Masculine, Nominative, Singular)

    val expected = "010001101"
    assert(pronounForm.formId == expected)
  }

  it should "encode its form as a URN" in {
    val pronounForm = PronounForm("ls.n20640", "proof.irrpron1","proof.irreginfl7", Masculine, Nominative, Singular)
    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:010001101")
    assert(pronounForm.formUrn == expected)
  }
}
