package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import org.scalatest.FlatSpec

// This only works if you're online:  the LewisShort object loads
// data over the internet.
class UrnDecoderSpec extends FlatSpec {

  "The LemmatizedForm object" should "should recognized valid URNs for noun forms" in  {
    val urn = Cite2Urn("urn:cite2:tabulae:morphforms.v1:020001400")

    val form = ValidForm(urn)
    form match {
      case vnf: ValidNounForm => {
        assert(vnf.gender == Masculine)
        assert(vnf.grammaticalCase == Accusative)
        assert(vnf.grammaticalNumber == Plural)
      }
      case _ => fail("Should have created a noun form")
    }

  }

}
