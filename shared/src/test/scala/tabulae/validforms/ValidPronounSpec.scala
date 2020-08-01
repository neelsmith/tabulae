
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidPronounSpec extends FlatSpec {

  "A ValidPronounForm" should "accept forms with GCN for pronoun pos" in {
    val pron = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:010001101"))
    assert(pron.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badPron = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:110001101"))
    assert(badPron.validUrnValue == false)
  }

  it should "reject invalid values for GCN" in {
    try {
      val badPron = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:040001101"))
    } catch {
      case e: Exception => assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:040001101 has invalid values for pronoun GCN"))
    }
  }

}
