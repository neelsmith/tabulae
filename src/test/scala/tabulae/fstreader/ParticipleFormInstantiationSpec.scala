
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class ParticipleFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular participle forms from FST source" in  {
    val adverbFst = "> monstrans\n<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><ptcpl>ans<masc><nom><sg><pres><act><u>proof.are_ptcpl1</u>".split("\n").toVector

    val parsed = FstReader.parseFstLines(adverbFst)
    val parse = parsed(0)
    assert(parse.literalToken == "monstrans")
    assert(parse.analyses.size == 1)

    val ptcplForm: ParticipleForm = parse.analyses(0) match {
      case ptcpl: ParticipleForm => ptcpl
      case _ => fail("Nope, that wasn't a participle.")
    }
    assert(ptcplForm.gender == Masculine)
    assert(ptcplForm.grammaticalCase == Nominative)
    assert(ptcplForm.grammaticalNumber == Singular)
  }


}
