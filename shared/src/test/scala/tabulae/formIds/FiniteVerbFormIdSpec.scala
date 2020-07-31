
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class FiniteVerbFormIdSpec extends FlatSpec {

  "A VerbForm" should "encode its form" in {
    val verbForm = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Third, Singular, Perfect, Indicative, Active)
    val expected = "314110004"
    assert(verbForm.formId == expected)

  }

  it should "encode its form as a URN" in {
    val verbForm = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Third, Singular, Perfect, Indicative, Active)
    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:314110004")
    assert(verbForm.formUrn == expected)
  }
}
