
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class IndeclinableFormIdSpec extends FlatSpec {

  "An IndeclinableForm " should "require a part of speech" in {
    val indeclForm = IndeclinableForm("lemmarule.x","stemrule.x","infrle.x", Conjunction)
    val posName   = indeclForm.pos.toString
    assert(posName == "Conjunction")

    val expected = "00000000A"
    assert(indeclForm.formId == expected)
  }
}
