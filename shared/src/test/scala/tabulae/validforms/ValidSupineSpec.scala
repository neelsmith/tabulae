
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
    val nom = SupineForm("ls.demo","ocremorph.stem","livymorph.form", Nominative)
    val badNom = ValidForm(nom.formUrn)
    assert(badNom.validUrnValue == false)

    val gen = SupineForm("ls.demo","ocremorph.stem","livymorph.form", Genitive)
    val badGen = ValidForm(gen.formUrn)
    assert(badGen.validUrnValue == false)

    val dat = SupineForm("ls.demo","ocremorph.stem","livymorph.form", Dative)
    val badDat = ValidForm(dat.formUrn)
    assert(badDat.validUrnValue == false)

    val acc = SupineForm("ls.demo","ocremorph.stem","livymorph.form", Accusative)
    val accForm = ValidForm(acc.formUrn)
    assert(accForm.validUrnValue)

    val abl = SupineForm("ls.demo","ocremorph.stem","livymorph.form", Ablative)
    val ablForm = ValidForm(abl.formUrn)
    assert(ablForm.validUrnValue)

  }

  it should "reject out of range values for C" in {
    try {
      val badSupine = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000X09"))
    } catch {
      case e: Exception => {

        assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:000000X09 has invalid value for supine case"))
      }
    }
  }

  "The ValidForm object" should "correctly label valid supine forms" in {
    assert( ValidForm.labels("urn:cite2:tabulae:morphforms.v1:000000409") == "supine: accusative")
    assert( ValidForm.labels("urn:cite2:tabulae:morphforms.v1:000000509") == "supine: ablative")
  }


}
