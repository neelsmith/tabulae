package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


class ValidFormObjectSpec extends FlatSpec {

  "The ValidForm object" should "have a map labelling 479 valid forms" in {
    val expectedSize = 479
    assert(ValidForm.labels.size == expectedSize)
  }
}
