
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class GerundiveFormIdSpec extends FlatSpec {

  "A GerundiveForm" should "encode its form" in {
    val gdvForm =  GerundiveForm("ls.fake", "ocremorph.dummy", "ocremorph.test", Feminine, Nominative, Singular)


    val expected = "010002107"
    assert(gdvForm.formId == expected)
  }
}
