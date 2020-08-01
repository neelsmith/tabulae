
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidNounSpec extends FlatSpec {

  "A ValidNounForm" should "accept forms with GCN for noun pos" in {
    val nounForm = ValidForm( Cite2Urn("urn:cite2:tabulae:morphforms.v1:010002100"))
    assert(nounForm.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badNounForm = ValidForm( Cite2Urn("urn:cite2:tabulae:morphforms.v1:110002100"))
    assert(badNounForm.validUrnValue == false)
  }

  it should "reject out of range values" in {
    try {
      val badNounForm = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:040002100"))
    } catch {
      case e: Exception => assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:040002100 has invalid values for noun GCN"))
    }
  }
}
