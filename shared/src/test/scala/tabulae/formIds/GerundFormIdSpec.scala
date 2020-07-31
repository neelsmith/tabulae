
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._

import org.scalatest.FlatSpec

class GerundFormIdSpec extends FlatSpec {

  "A GerundForm" should "encode its form" in {
    //val gerundForm = GerundForm("lexentID", Dative)
      val gerundForm = GerundForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Dative)
    val expected = "000000308"
    assert(gerundForm.formId == expected)
  }
  it should "encode its form as a URN" in {
    val gerundForm = GerundForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Dative)
    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000308")
    assert(gerundForm.formUrn == expected)
  }
}
