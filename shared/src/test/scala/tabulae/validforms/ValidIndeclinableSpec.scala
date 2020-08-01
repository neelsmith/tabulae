
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidIndeclinableSpec extends FlatSpec {

  "A ValidIndeclinableForm" should "accept forms with PoS indication" in {
    val indecl = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:00000000A"))
    assert(indecl.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badIndecl = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:10000000A"))
    assert(badIndecl.validUrnValue == false)
  }

  it should "reject out of range values for PoS" in pending

}
