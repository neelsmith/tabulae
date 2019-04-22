
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class AdjectiveFormSpec extends FlatSpec {

  "An AdjectiveForm" should "require GCND in constructor" in {
    val adjForm = AdjectiveForm(Neuter, Nominative, Singular, Positive)
    adjForm match {
      case adjForm: AdjectiveForm => assert(true)
      case _ => fail("Should have instantiated a AdjectiveForm")
    }
  }


}
