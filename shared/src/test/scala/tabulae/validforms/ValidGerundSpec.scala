
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidGerundSpec extends FlatSpec {

  "A ValidGerundForm" should "accept forms with C for gerund PoS" in {
    val gerundForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000308"))
    assert(gerundForm.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badGerundForm  =      ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:100000308"))
    assert(badGerundForm.validUrnValue == false)
  }

  it should "reject values of nominative for C" in {
    val badGerundForm  =      ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:100000108"))
    assert(badGerundForm.validUrnValue == false)

  }

  it should "should reject out of range values for C" in {
    try {
      val gerundForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000X08"))
    } catch {
      case e : Exception => {
        assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:000000X08 has invalid values for gerund case"))
      }
    }

  }

}
