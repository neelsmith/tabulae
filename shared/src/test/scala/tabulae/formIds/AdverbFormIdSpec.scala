
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._

import org.scalatest.FlatSpec

class AdverbFormIdSpec extends FlatSpec {

  "An AdverbForm" should "encode its form" in {
      val advForm = AdverbForm("ls.n25115", "ocremorph.n25115", "irreginfl.2", Positive)
    val expected = "000000013"
    assert(advForm.formId == expected)
  }

  it should "encode its form as a URN" in {
    val advForm = AdverbForm("ls.n25115", "ocremorph.n25115", "irreginfl.2", Positive)

    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000013")
    assert(advForm.formUrn == expected)

  }

}
