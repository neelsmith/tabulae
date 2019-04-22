
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

  it should "pop off analyses from the top (front) of a Vector of FST strings" in {
    val fstLines = Vector(
      "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<masc><dat><sg><pos><u>ocremorph.us_a_um3</u>",
      "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<masc><abl><sg><pos><u>ocremorph.us_a_um5</u>",
      "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<neut><dat><sg><pos><u>ocremorph.us_a_um77</u>",
      "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<neut><abl><sg><pos><u>ocremorph.us_a_um79</u>"
    )
    val forms = FstFileReader.popAnalyses(fstLines)
    println(forms)

  }

}
