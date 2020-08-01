
package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import org.scalatest.FlatSpec

class IndeclinableFormIdSpec extends FlatSpec {

  "An IndeclinableForm " should "require a part of speech" in {
    val indeclForm = IndeclinableForm("lemmarule.x","stemrule.x","infrle.x", Conjunction)
    val posName   = indeclForm.pos.toString
    assert(posName == "Conjunction")

    val expected = "00000000A"
    assert(indeclForm.formId == expected)
  }

  it should "create a URN for its form" in {
    val indeclForm = IndeclinableForm("lemmarule.x","stemrule.x","infrle.x", Conjunction)
    val expected = Cite2Urn("urn:cite2:tabulae:morphforms.v1:00000000A")
    assert(indeclForm.formUrn == expected)
  }
}
