
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class PronounFormIdSpec extends FlatSpec {

  "A  PronounForm" should "require encode its form" in {
    val pronounForm = PronounForm("ls.n20640", "proof.irrpron1","proof.irreginfl7", Masculine, Nominative, Singular)

    val expected = "01000110"
    assert(pronounForm.formId == expected)
  }
}
