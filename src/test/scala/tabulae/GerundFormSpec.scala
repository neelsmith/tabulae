
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

//<u>ocremorph.verb2</u><u>ls.n18378</u><#>fl<verb><conj1><div><conj1><gerund>ando<dat><u>ocremorph.grd_conj1_2</u>

class GerundFormSpec extends FlatSpec {

  "A  GerundForm" should "require case in constructor" in {
    val gerundForm = GerundForm("lexentID", Dative)
    gerundForm match {
      case gf: GerundForm => assert(true)
      case _ => fail("Should have instantiated a GerundForm")
    }
  }

}
