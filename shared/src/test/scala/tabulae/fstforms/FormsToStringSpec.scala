
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


class FormsToStringSpec extends FlatSpec {

  "A LemmatizedForm" should "override toString for nouns" in {
    val nounForm = NounForm("ls.n1315", "ocremorph.n27","ocremorph.stis13", Feminine, Nominative, Singular)
    val expected = "noun: feminine, nominative, singular"
    assert(nounForm.toString == expected)
  }

  it should "override toString for pronouns" in {

  }

  it should "override toString for adjectives" in {
    val adjForm =  AdjectiveForm("ls.n1413", "ocremorph.adj6", "ocremorph.us_a_um42", Feminine, Nominative, Plural, Positive)

    val expected = "adjective: feminine, nominative, plural, positive"
    assert(adjForm.toString == expected)
  }

  it should "override toString for adverbs" in pending
  it should "override toString for indeclinable forms" in pending

  it should "override toString for conjugated verbs" in pending
  it should "override toString for infinitives" in pending
  it should "override toString for participles" in pending
  it should "override toString for gerundives" in pending
  it should "override toString for gerunds" in pending
  it should "override toString for supines" in pending



}
