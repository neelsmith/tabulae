
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidFiniteVerbSpec extends FlatSpec {

  "A ValidFiniteVerbForm" should "accept forms with PNTMV for verb PoS" in pending /*{
    val verbForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:314110004"))
    assert(verbForm.validUrnValue)
  }*/


  it should "reject non-zero values on other columns" in pending /* {
    val badVerbForm  = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:314110104"))
    assert(badVerbForm.validUrnValue == false)
  }*/

  it should "reject subjunctive forms in mood other than pr, impft, pft, pluptf" in pending

  it should "reject imperative forms in combinations other than 2nd pres and future, and 3rd future" in pending

  it should "reject imperative tenses other than pres and fut" in pending

  it should "reject out of range values for PNTMV" in pending

}
