
package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


import org.scalatest.FlatSpec

class ValidFormSpec extends FlatSpec {

  "The ValidForm trait" should "include a concrete implementation of labelling forms" in {
    val indecl = ValidForm(Cite2Urn("urn:cite2:tabulae:morphforms.v1:00000000A"))
    val expected = "uninflected conjunction"
    assert(indecl.label == expected)
  }
}
