
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class AdverbFormIdSpec extends FlatSpec {

  "An AdverbForm" should "encode its form" in {
      val advForm = AdverbForm("ls.n25115", "ocremorph.n25115", "irreginfl.2", Positive)
    val expected = "00000001"
    assert(advForm.formId == expected)
  }
}
