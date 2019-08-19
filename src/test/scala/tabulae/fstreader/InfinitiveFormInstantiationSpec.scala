
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class InfinitiveFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular infinitive forms from FST source" in {
    val infinFst = "> monstravisse\n<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><infin>avisse<pft><act><u>proof.are_inf1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(infinFst)
    val parse = parsed(0)
    assert(parse.literalToken == "monstravisse")
    assert(parse.analyses.size == 1)

    val infForm: InfinitiveForm = parse.analyses(0) match {
      case inf: InfinitiveForm => inf
      case _ => fail("Nope, that wasn't an infinitive.")
    }
    assert(infForm.tense == Perfect)
    assert(infForm.voice == Active)
  }


}
