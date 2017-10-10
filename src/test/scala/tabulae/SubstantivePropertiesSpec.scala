
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class SustantivePropertiesSpec extends FlatSpec {


  "The  GrammaticalCase trait" should "include all Latin cases" in {

    val nominativeExample : GrammaticalCase = Nominative
    val isNominative = nominativeExample match {
      case Nominative => true
      case Genitive => false
      case Dative => false
      case Accusative => false
      case Ablative => false
      case Vocative => false
    }
    assert(isNominative)
  }


  "The Gender trait" should "include all Latin genders" in {
    val neuterExample: Gender = Neuter
    val isNeuter = neuterExample match {
      case Masculine => false
      case Feminine => false
      case Neuter => true
    }
    assert(isNeuter)
  }


  "The GrammaticalNumber trait" should "include all Latin numbers" in {
    val pluralExample: GrammaticalNumber = Plural
    val isPlural = pluralExample match {
      case Singular => false
      case Plural => true
    }
    assert(isPlural)
  }

  "The Degree trait" should "include all degrees of Latin adjectives " in {
    val superlativeExample: Degree = Superlative
    val isSuperlative = superlativeExample match {
      case Positive => false
      case Comparative => false
      case Superlative => true
    }
    assert(isSuperlative)
  }
}
