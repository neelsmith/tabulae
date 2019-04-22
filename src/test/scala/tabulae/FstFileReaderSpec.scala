
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstFileReaderSpec extends FlatSpec {


  "The FstFileReader object" should "recognize strings for a new token analysis" in {
    val fst = "> actio"
    assert(FstFileReader.isToken(fst))
  }

  it should "distinguish fst lines that are not new tokens" in {
    val fst = "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<masc><dat><sg><pos><u>ocremorph.us_a_um3</u>"
    assert(FstFileReader.isToken(fst) == false)
  }

}
