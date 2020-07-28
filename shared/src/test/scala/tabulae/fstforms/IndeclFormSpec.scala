
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class IndeclFormSpec extends FlatSpec {

/*

scala> indeclForm.pos
res0: edu.holycross.shot.tabulae.IndeclinablePoS = Conjunction
*/
  "An IndeclinableForm" should "require part of speech " in {
    val indeclForm = IndeclinableForm("lemmarule.x","stemrule.x","infrle.x", Conjunction)
    val posName   = indeclForm.pos.toString

    val code = posName match {
      case "Conjunction" => assert(true)
      case "Preposition" => fail("Should have been Conjunction")
      case "Exclamation" => fail("Should have been Conjunction")
      case "Numeral" => fail("Should have been Conjunction")
    }
  }

  it should "implement all functions of the LemmatizedForm trait" in pending

}
