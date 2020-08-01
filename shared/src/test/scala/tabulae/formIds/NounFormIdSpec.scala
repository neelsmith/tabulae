
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._

import org.scalatest.FlatSpec

class NounFormIdSpec extends FlatSpec {

  "A  NounForm" should "require encode its form" in {
    val nounForm = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    val expected = "010002100"
    assert(nounForm.formId == expected)
  }

  it should "encode its form as a URN" in {
    val nounForm = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:010002100")
    assert(nounForm.formUrn == expected)
  }
}
