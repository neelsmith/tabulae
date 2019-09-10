package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

// This only works if you're online:  the LewisShort object loads
// data over the internet.
class LewisShortSpec extends FlatSpec {

  "The LewisShort object" should "label ID values" in {
    val expected = "ls.n49895:urbs"
    assert(LewisShort.label("ls.n49895") == "ls.n49895:urbs")
  }

  it should "tolerate IDs not part of Lewis-Short" in {
    val expected = "elsewhere.not-lewis-short"
    assert(LewisShort.label(expected) == expected)
  }

  it should "find an ID value for a unique label" in {
    val expected = "ls.n49895"
    val actual = LewisShort.guessId("urbs").get
    assert(actual == expected)
  }

  it should "return None for non-existent IDs" in {
    val actual = LewisShort.guessId("not Latin")
    assert(actual == None)
  }
}
