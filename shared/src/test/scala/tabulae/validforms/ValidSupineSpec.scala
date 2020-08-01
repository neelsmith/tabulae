
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidSupineSpec extends FlatSpec {

  "A ValidSupineForm" should "accept forms with C for supine PoS" in {
    val supineForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000409"))
    assert(supineForm.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badSupineForm  =      ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:100000509"))
    assert(badSupineForm.validUrnValue == false)
  }


  it should "reject values other than acc, abl for case" in {
    val supineForm = SupineForm("ls.demo","ocremorph.stem","livymorph.form", Nominative)
    val badSupine = ValidForm(supineForm.formUrn)
    assert(badSupine.validUrnValue == false)
  }

  it should "reject out of range values for C" in {
    try {
      val badSupine = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000X09"))
    } catch {
      case e: Exception => {

        assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:000000X09 has invalid valid for supine case"))
      }
    }






  }


}
