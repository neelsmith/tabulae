
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class IndeclinablePropertiesSpec extends FlatSpec {

  "The  IndeclinablePoS trait" should "include all relevant parts of speech" in {
    val conjunctionExample : IndeclinablePoS = Conjunction
    val isConjunction = conjunctionExample match {
      case Conjunction => true
      case Preposition => false
      case Exclamation => false

    }
    assert(isConjunction)
  }

/*

case object Conjunction extends IndeclinablePoS

case object Preposition extends IndeclinablePoS

case object Exclamation extends IndeclinablePoS
*/
}
