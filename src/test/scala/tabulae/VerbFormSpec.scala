
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class VerbFormSpec extends FlatSpec {

  "A  VerbForm" should "require PNTMV in constructor" in {
    val verbForm = VerbForm("lexentID", First, Singular, Perfect, Indicative, Active)
    verbForm match {
      case vf: VerbForm => assert(true)
      case _ => fail("Should have instantiated a VerbForm")
    }
  }

  it should "be recognized as an instance of a Form" in {
    val form = VerbForm("lexentID", First, Singular, Perfect, Indicative, Active)

    form match {
      case vf: VerbForm => assert(true)
      case _ => fail("Should have created a VerbForm")
    }
  }

}
