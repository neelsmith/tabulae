
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._

import org.scalatest.FlatSpec

class GerundiveFormIdSpec extends FlatSpec {

  "A GerundiveForm" should "encode its form" in {
    val gdvForm =  GerundiveForm("ls.fake", "ocremorph.dummy", "ocremorph.test", Feminine, Nominative, Singular)


    val expected = "010002107"
    assert(gdvForm.formId == expected)
  }

  it should "encode its form as a URN" in {
    val gdvForm =  GerundiveForm("ls.fake", "ocremorph.dummy", "ocremorph.test", Feminine, Nominative, Singular)

    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:010002107")
    assert(gdvForm.formUrn == expected)
  }
}
