
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidAdverbSpec extends FlatSpec {

  "A ValidAdverbForm" should "accept forms with D for adverb PoS" in {
    val advForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000013"))
    assert(advForm.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badAdvForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:100000013"))
    assert(badAdvForm.validUrnValue == false)
  }

  it should "reject out of range values for D"  in {
    try {
      val badAdvForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:0000000X3"))
    } catch {
      case e: Exception => assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:0000000X3 has invalid value for adverb degree"))
    }

  }
}
