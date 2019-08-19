
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class GerundiveFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular gerundive forms from FST source" in {
    val infinFst = "> monstrandarum\n<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><gerundive>andarum<fem><gen><pl><u>proof.gdv_1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(infinFst)
    val parse = parsed(0)
    assert(parse.literalToken == "monstrandarum")
    assert(parse.analyses.size == 1)

    val gdvForm: GerundiveForm = parse.analyses(0) match {
      case gdv: GerundiveForm => gdv
      case _ => fail("Nope, that wasn't a gerundive.")
    }
    assert(gdvForm.gender == Feminine)
    assert(gdvForm.grammaticalCase == Genitive)
    assert(gdvForm.grammaticalNumber == Plural)
  }


}
