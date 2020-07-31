
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidSupineSpec extends FlatSpec {

  "A ValidSupineForm" should "accept forms with C for supine PoS" in {
    val gerundForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000308"))
    assert(gerundForm.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badGerundForm  =      ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:100000308"))
    assert(badGerundForm.validUrnValue == false)
  }

  it should "reject values other than acc, abl for case" in pending

  it should "reject plural number" in pending

}
