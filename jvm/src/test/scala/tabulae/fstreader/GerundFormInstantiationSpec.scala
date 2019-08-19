
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class GerundFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular gerund forms from FST source" in {
    val adverbFst = "> monstrandum\n<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><gerund>andum<nom><u>proof.gerundconj1_1</u>\n<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><gerund>andum<acc><u>proof.gerundconj1_2</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(adverbFst)
    val parse = parsed(0)
    assert(parse.literalToken == "monstrandum")
    assert(parse.analyses.size == 2)

    val grndForm: GerundForm = parse.analyses(0) match {
      case grnd: GerundForm => grnd
      case _ => fail("Nope, that wasn't a gerund.")
    }
    assert(grndForm.grammaticalCase == Nominative)
  }


}
