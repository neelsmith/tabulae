
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._

import org.scalatest.FlatSpec

class SupineFormIdSpec extends FlatSpec {

  "A SupineForm" should "encode its form" in {
    //val gerundForm = GerundForm("lexentID", Dative)
    val supineForm = SupineForm("ls.demo","ocremorph.stem","livymorph.form", Ablative)
    val expected = "000000509"
    assert(supineForm.formId == expected)
  }
  it should "encode its form as a URN" in {
    val supineForm = SupineForm("ls.demo","ocremorph.stem","livymorph.form", Ablative)
    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000509")
    assert(supineForm.formUrn == expected)
  }

}
