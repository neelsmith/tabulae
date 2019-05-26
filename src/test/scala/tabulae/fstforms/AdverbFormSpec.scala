
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec



class AdverbFormSpec extends FlatSpec {
  //<u></u><u></u>
  //itervm<pos><irregadv>
  //<div>
  //<irregadv><u>irreginfl.2</u>

  "An AdverbForm" should "require debree in constructor" in {
    val advForm = AdverbForm("ls.n25115", "ocremorph.n25115", "irreginfl.2", Positive)

    advForm match {
      case adv: AdverbForm => assert(true)
      case _ => fail("Should have instantiated a AdjectiveForm")
    }
  }
  it should "implement all functions of the LemmatizedForm trait" in  {
    val form = AdverbForm("ls.n25115", "ocremorph.n25115", "irreginfl.2", Positive)
    assert(form.lemmaId == "ls.n25115")
    assert(form.stemId == "ocremorph.n25115")
    assert(form.ruleId == "irreginfl.2")
    assert(form.posLabel == "adverb")
  }

}
