
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidGerundiveSpec extends FlatSpec {

  "A ValidGerundForm" should "accept forms with GCN for gerundive PoS" in  {
    val gdvForm = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:010002107"))
    assert(gdvForm.validUrnValue)
  }



  it should "reject non-zero values on other columns" in {
    val badGdvForm = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:110002107"))
    assert(badGdvForm.validUrnValue == false)
  }

  it should "reject out of range values for GCN" in {
    try {
      val badGdvForm = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:0X0002107"))
    } catch {
      case e : Exception => assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:0X0002107 has invalid values for gerundive GCN"))
    }

  }
}
