
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstStemParsingSpec extends FlatSpec {


  "The FstStem object" should "parse the stem part of an FST reply into an FstStem object" in pending/* {
    val stemFst = "<u>smyth.n47039_0</u><u>lexent.n47039</u>ni<lo>k<noun><fem><h_hs><stemultacc>"

    val stemObj = FstStem(stemFst)
    stemObj match {
      case nounObj: NounStem => {
        assert(nounObj.stemId == "smyth.n47039_0")
        assert(nounObj.lexentId == "lexent.n47039")
        assert(nounObj.stem == "ni<lo>k")
        assert(nounObj.inflClass == "h_hs")
        assert(nounObj.gender == "fem")
        assert(nounObj.accent == "stemultacc")
      }
      case _ => fail("Should have created NounStem")
    }

  }*/

}
