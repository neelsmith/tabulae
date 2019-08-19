
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class SupineFormInstantiationSpec extends FlatSpec {


  "The FstReader object" should  "instantiate regular supine forms from FST source" in  {
    val supineFst = "> monstratu\n<u>proof.v1</u><u>lexent.n29616</u><#>monstr<verb><conj1><div><conj1><supine>atu<abl><u>proof.conj1_1</u>".split("\n").toVector
    println("\n\n\n")
    val parsed = FstReader.parseFstLines(supineFst)
    val parse = parsed(0)
    assert(parse.literalToken == "monstratu")
    assert(parse.analyses.size == 1)

    val supForm: SupineForm = parse.analyses(0) match {
      case supine: SupineForm => supine
      case _ => fail("Nope, that wasn't a supine.")
    }
    assert(supForm.grammaticalCase == Ablative)
  }


}
