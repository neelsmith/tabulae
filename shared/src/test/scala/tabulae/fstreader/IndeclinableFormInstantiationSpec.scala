
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class IndeclinableFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular indeclinable forms from FST source" in  {
    val indeclFst = "> cum\n<u>proof.indecl1</u><u>lexent.n11873</u>cum<indecl><indeclconj><div><indeclconj><indecl><u>indeclinfl.2</u>".split("\n").toVector

    val parsed = FstReader.parseFstLines(indeclFst)
    val parse = parsed(0)
    assert(parse.literalToken == "cum")
    assert(parse.analyses.size == 1)

    val ideclForm: IndeclinableForm = parse.analyses(0) match {
      case indecl: IndeclinableForm => indecl
      case _ => fail("Nope, that wasn't an indeclinable form.")
    }
    assert(ideclForm.pos == Conjunction)
  }

  it should "recognize indeclinable numerals" in {
    val indeclFst = "> sex\n<u>latcommon\\.indecln44049</u><u>ls\\.n44049</u>sex<indecl><indeclnum><div><indeclnum><indecl><u>indeclinfl\\.4</u>".split("\n").toVector
    println("\n\n\n")


    val parsed = FstReader.parseFstLines(indeclFst)
    val parse = parsed(0)
    assert(parse.literalToken == "sex")
    assert(parse.analyses.size == 1)

    val ideclForm: IndeclinableForm = parse.analyses(0) match {
      case indecl: IndeclinableForm => indecl
      case _ => fail("Nope, that wasn't an indeclinable form.")
    }
    assert(ideclForm.pos == Numeral)
  }

}
