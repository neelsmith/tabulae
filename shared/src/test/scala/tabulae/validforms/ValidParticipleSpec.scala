
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidParticipleSpec extends FlatSpec {


  //
  "A ValidParticipleForm" should "accept forms with TVGCN for participle pos" in {
    val ptcpl = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:021012105"))
    assert(ptcpl.validUrnValue)
  }

  it should "reject non-zero values on other columns" in {
    val badPtcpl = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:121012105"))
    assert(badPtcpl.validUrnValue ==  false)
  }

  it should "reject tense-mood combos other than pres-act, pft-pass and fut-act" in {
    val ptcplUrn = ParticipleForm("ls.dummy", "ocremorph.test", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Present, Passive).formUrn
    val ptcpl = ValidForm(ptcplUrn)
    assert(ptcpl.validUrnValue == false)
  }

  it should "reject out of range values for TVGCN" in {
    try {
      val badPtcpl = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:051012105"))
    } catch {
      case e: Exception => assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:051012105 has invalid valudes for participle TVGCN"))
    }
  }

}
