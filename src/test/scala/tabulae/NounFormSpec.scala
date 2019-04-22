
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class NounFormSpec extends FlatSpec {

  "A  NounForm" should "require GCN in constructor" in {
    val nounForm = NounForm("lexentID", Neuter, Nominative, Singular)
    nounForm match {
      case nf: NounForm => assert(true)
      case _ => fail("Should have instantiated a NounForm")
    }
  }

  it should "be recognized as an instance of a Form" in {
    val form: LemmatizedForm = NounForm("lexentID", Neuter, Nominative, Singular)
    form match {
      case nf: NounForm => assert(true)
      case _ => fail("Should have created a NounForm")
    }
  }

}
