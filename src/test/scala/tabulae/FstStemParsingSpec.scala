
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstStemParsingSpec extends FlatSpec {


  "The FstStem object" should "parse the stem part of an FST reply into an FstStem object" in  {
    val stemFst = "<u>dev1.n1</u><u>lexent.n1</u>femin<noun><fem><a_ae>"

    val stemObj = FstStem(stemFst)
    stemObj match {
      case nounObj: NounStem => {
        assert(nounObj.stemId == "dev1.n1")
        assert(nounObj.lexentId == "lexent.n1")
        assert(nounObj.stem == "femin")
        assert(nounObj.inflClass == "a_ae")
        assert(nounObj.gender == "fem")
      }
      case _ => fail("Should have created NounStem")
    }
  }

}
