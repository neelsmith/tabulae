
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._

import org.scalatest.FlatSpec

class ValidAdjectiveFormSpec extends FlatSpec {

  "A ValidAdjectiveForm" should "accept forms with GCND for adj. PoS" in {
    val adjForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:020002112"))
    assert(adjForm.validUrnValue)
  }

  it should "reject non-zero values on other columns" in {
    val badAdjForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:120002112"))
    assert(badAdjForm.validUrnValue == false)
  }

}
