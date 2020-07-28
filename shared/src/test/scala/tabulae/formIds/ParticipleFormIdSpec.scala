
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class ParticipleFormIdSpec extends FlatSpec {

  "A PariticipleForm" should "encode its form" in {
    val ptcplForm = ParticipleForm("ls.dummy", "ocremorph.test", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Present, Active)
    val expected = "021012105"
    assert(ptcplForm.formId == expected)
  }
}
