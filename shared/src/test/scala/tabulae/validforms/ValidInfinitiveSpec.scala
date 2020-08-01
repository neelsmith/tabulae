
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidInfinitiveSpec extends FlatSpec {

  "A ValidInfinitiveForm" should "accept forms with TV for infinitive pos" in {
    val infForm = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:004010006"))
    assert(infForm.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badInfForm = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:104010006"))
    assert(badInfForm.validUrnValue == false)
  }

  it should "reject tenses other than pres, pft, fut" in {
    val badTense = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:002010006"))
    assert(badTense.validUrnValue ==  false)
  }

  it should "reject out of range values for TV" in {
    try {
      val badTense =   ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:00X010006"))
    } catch {
      case e: Exception => assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:00X010006 has invalid values for infinitive TV"))
    }
  }

}
