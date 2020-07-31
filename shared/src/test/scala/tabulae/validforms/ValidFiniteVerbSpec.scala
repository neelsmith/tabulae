
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidFiniteVerbSpec extends FlatSpec {

  "A ValidAdverbForm" should "accept forms with D for adverb PoS" in {
    val verbForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:314110004"))
    assert(verbForm.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badVerbForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:314110104"))
    assert(badVerbForm.validUrnValue == false)
  }

}
