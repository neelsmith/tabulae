
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidFiniteVerbSpec extends FlatSpec {

  "A ValidFiniteVerbForm" should "accept forms with PNTMV for verb PoS" in{
    val verbForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:314110004"))
    assert(verbForm.validUrnValue)
  }


  it should "reject non-zero values on other columns" in {
    val badVerbForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:314110104"))
    assert(badVerbForm.validUrnValue == false)
  }

  it should "reject subjunctive forms in mood other than pr, impft, pft, pluptf" in {
    val badSubjunctive = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Second, Singular, Future, Subjunctive, Active)
    val badForm = ValidForm(badSubjunctive.formUrn)

    badForm match {
      case fv: ValidFiniteVerbForm => {
        assert(fv.validSubjunctive == false)
        assert(fv.validUrnValue == false)
      }
      case _ => fail("Should have made finite verb form")
    }
  }



  it should "reject imperative forms in combinations other than 2nd pres and future, and 3rd future" in {
    val badImperative = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", First, Singular, Present, Imperative, Active)
    val badForm = ValidForm(badImperative.formUrn)

    badForm match {
      case fv: ValidFiniteVerbForm => {
        assert(fv.validImperative == false)
        assert(fv.validUrnValue == false)
      }
      case _ => fail("Should have made finite verb form")
    }
  }
  it should "reject imperative forms in tenses other than present and future" in {
    val badImperative = VerbForm("ls.n17516","ocremorph.n17516b","livymorph.pftact_pft3", Second, Singular, Imperfect, Imperative, Active)
    val badForm = ValidForm(badImperative.formUrn)

    badForm match {
      case fv: ValidFiniteVerbForm => {
        assert(fv.validImperative == false)
        assert(fv.validUrnValue == false)
      }
      case _ => fail("Should have made finite verb form")
    }

  }
  it should "reject out of range values for PNTMV" in {
    try {
      val verbForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:X14110004"))
    } catch {
      case e: Exception => assert(e.toString.contains("URN urn:cite2:tabulae:morphforms.v1:X14110004 contains invalid values for finitive verb PNTMV"))
    }

  }

}
