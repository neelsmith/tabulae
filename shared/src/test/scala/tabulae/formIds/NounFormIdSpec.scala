
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class NounFormIdSpec extends FlatSpec {

  "A  NounForm" should "require encode its form" in {
    val nounForm = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    val expected = "01000210"
    assert(nounForm.formId == expected)
  }
}
