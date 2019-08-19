
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class ConjugatedPropertiesSpec extends FlatSpec {

  "The  Person trait" should "include all Latin persons" in {

    val firstPersonExample : Person = First
    val isFirst = firstPersonExample match {
      case First => true
      case Second => false
      case Third => false
    }
    assert(isFirst)
  }

  "The  Tense trait" should "include all Latin tenses" in {

    val presentExample : Tense = Present
    val isPresent = presentExample match {
      case Present => true
      case Imperfect => false
      case Perfect => false
      case Pluperfect => false

      case Future => false
      case FuturePerfect => false
    }
    assert(isPresent)
  }


  "The  Mood trait" should "include all Latin moods" in {
    val indicativeExample : Mood = Indicative
    val isIndicative = indicativeExample match {
      case Indicative => true
      case Subjunctive => false
      case Imperative => false
    }
    assert(isIndicative)
  }

  "The  Voice trait" should "include all Latin voices" in {
    val activeExample : Voice = Active
    val isActive = activeExample match {
      case Active => true
      case Passive => false

    }
    assert(isActive)
  }
}
