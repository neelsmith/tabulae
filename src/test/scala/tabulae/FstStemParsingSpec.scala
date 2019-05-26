
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstStemParsingSpec extends FlatSpec {


  "The FstStem object" should "parse the stem part of an FST reply for a noun into an FstStem object" in  {
    val stemFst = "<u>dev1.n1</u><u>lexent.n1</u>femin<noun><fem><a_ae>"

    val stemObj = FstStem(stemFst)
    stemObj match {
      case nounObj: NounStem => {
        assert(nounObj.stemId == "dev1.n1")
        assert(nounObj.lexEntity == "lexent.n1")
        assert(nounObj.stem == "femin")
        assert(nounObj.inflClass == "a_ae")
        assert(nounObj.gender == "fem")
      }
      case _ => fail("Should have created NounStem")
    }
  }

  it should "parse verb stems"in {
    val stemFst = "<u>dev1.v1</u><u>lexent.v1</u><#>am<verb><conj1>"
    val stemObj = FstStem(stemFst)
    stemObj match {
      case verbObj: VerbStem => {
        assert(verbObj.stemId == "dev1.v1")
        assert(verbObj.lexEntity == "lexent.v1")
        assert(verbObj.inflClass == "conj1")
        assert(verbObj.stem == "<#>am")
      }
      case _ => fail("Should have created VerbStem")
    }
  }

  it should "parse indeclinable stems" in {
      val stemFst = "<u>pliny.indecl1</u><u>lexent.tbd</u>cum<indecl><conjunct>"
      val stemObj = FstStem(stemFst)
      stemObj match {
        case indeclObj: IndeclStem => {
          assert(indeclObj.stemId == "pliny.indecl1")
          assert(indeclObj.lexEntity == "lexent.tbd")
          assert(indeclObj.pos == "conjunct")
          assert(indeclObj.stem == "cum")
        }
        case _ => fail("Should have created IndeclStem")
      }
  }
  it should "parse adjective stems" in {
    val stemFst = "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um>"
    val stemObj = FstStem(stemFst)
    stemObj match {
      case adjObj: AdjectiveStem => {
        assert(adjObj.stemId == "ocremorph.geoadj1")
        assert(adjObj.lexEntity == "ls.n617")
        assert(adjObj.stem == "acti")
        assert(adjObj.inflClass == "us_a_um")
      }
      case _ => fail("Should have created AdjectiveStem")
    }
  }
  it should "parse pronoun stems" in pending




}
