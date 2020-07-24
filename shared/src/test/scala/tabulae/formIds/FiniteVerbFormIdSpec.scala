
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FiniteVerbFormIdSpec extends FlatSpec {

  "A VerbForm" should "encode its form" in {
    val verbForm = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Third, Singular, Perfect, Indicative, Active)
    val expected = "31411000"
    assert(verbForm.formId == expected)

  }
}
