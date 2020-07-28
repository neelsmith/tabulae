
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class GerundFormIdSpec extends FlatSpec {

  "A GerundForm" should "require encode its form" in {
    //val gerundForm = GerundForm("lexentID", Dative)
      val gerundForm = GerundForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Dative)
    val expected = "000000308"
    assert(gerundForm.formId == expected)
  }
}
