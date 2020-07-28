
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class InfinitiveFormIdSpec extends FlatSpec {

  "An InfinitiveForm" should "require encode its form" in {
    val infForm = InfinitiveForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Perfect, Active)
    val expected = "004010006"
    assert(infForm.formId == expected)
  }
}
