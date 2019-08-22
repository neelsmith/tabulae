
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class VerbFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular conjugated verb forms from FST source" in {
    val verbFst = "> monstro\n<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><verb>o<1st><sg><pres><indic><act><u>proof.are_presind1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(verbFst)
    val parse = parsed(0)
    assert(parse.literalToken == "monstro")
    assert(parse.analyses.size == 1)

    val vForm: VerbForm = parse.analyses(0) match {
      case verb: VerbForm => verb
      case _ => fail("Nope, that wasn't a verb.")
    }


    assert(vForm.person == First)
    assert(vForm.grammaticalNumber == Singular)
    assert(vForm.tense == Present)
    assert(vForm.mood == Indicative)
    assert(vForm.voice == Active)
  }

  it should "instantiate a VerbForm from FST source for irregular verbs" in {
    val verbFst = "> it\n<u>proof.irrv1</u><u>lexent.n15868</u><#>it<3rd><sg><pres><indic><act><irregcverb><div><irregcverb><u>irreginfl.3</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(verbFst)
    val parse = parsed(0)
    assert(parse.literalToken == "it")
    assert(parse.analyses.size == 1)

    val vForm: VerbForm = parse.analyses(0) match {
      case verb: VerbForm => verb
      case _ => fail("Nope, that wasn't a verb.")
    }
    assert(vForm.person == Third)
    assert(vForm.grammaticalNumber == Singular)
    assert(vForm.tense == Present)
    assert(vForm.mood == Indicative)
    assert(vForm.voice == Active)
  }

  it should "recognize the pluperfect tense for crying out loud" in {
    val pluptf = "> conspexisset\n<u>livymorph.comp14_10</u><u>ls.n10586</u><#>conspex<verb><pftact><div><pftact><verb>isset<3rd><sg><plupft><subj><act><u>latcommon.pftact_plupft9</u>".split("\n").toVector

    val parsed = FstReader.parseFstLines(pluptf)
    val parse = parsed(0)
    assert(parse.literalToken == "conspexisset")
    assert(parse.analyses.size == 1)

    val vForm: VerbForm = parse.analyses(0) match {
      case verb: VerbForm => verb
      case _ => fail("Nope, that wasn't a verb.")
    }
    assert(vForm.tense == Pluperfect)
  }

}
