
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstFileReaderSpec extends FlatSpec {


  val fstLines = Vector(
    "> actio",
    "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<masc><dat><sg><pos><u>ocremorph.us_a_um3</u>",
    "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<masc><abl><sg><pos><u>ocremorph.us_a_um5</u>",
    "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<neut><dat><sg><pos><u>ocremorph.us_a_um77</u>",
    "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<neut><abl><sg><pos><u>ocremorph.us_a_um79</u>"
  )

  "The FstFileReader object" should "recognize strings for a new token analysis" in {
    val fst = "> actio"
    assert(FstFileReader.isToken(fst))
  }

  it should "distinguish fst lines that are not new tokens" in {
    val fst = "<u>ocremorph.geoadj1</u><u>ls.n617</u>acti<adj><us_a_um><div><us_a_um><adj>o<masc><dat><sg><pos><u>ocremorph.us_a_um3</u>"
    assert(FstFileReader.isToken(fst) == false)
  }

  it should "pop off analyses from the top (front) of a Vector of FST strings" in pending  /*{

    val forms = FstFileReader.popAnalyses(fstLines.tail)
    val expectedSize = 4
    assert(forms.size == expectedSize)

    for (f <- forms ) {
      f  match {
        //case af: AdjectiveForm => assert(true)
        case _ => fail("Didn't get an adjective form from " + f)
      }
    }
  }*/

  it should "create an AnalyzedToken from FST strings" in pending /*{
    val analyzed = FstFileReader.popAnalyzedToken(fstLines)
    val expectedToken = "actio"
    assert(analyzed.token == expectedToken)

    val expectedSize = 4
    assert(analyzed.analyses.size == expectedSize)
    for (f <- analyzed.analyses ) {
      f  match {
        //case af: AdjectiveForm => assert(true)
        case _ => fail("Didn't get an adjective form from " + f)
      }
    }
  }*/

  it should "create a Vector of AnalyzedTokens from a Vector of FST Strings" in pending /* {
    val analyzedTokens = FstFileReader.parseFstLines(fstLines)
    val expectedTokens = 1
    assert(analyzedTokens.size == expectedTokens)
  }*/

  it should "create a Vector of AnalyzedTokens from a small sample file" in pending /*{
    val f = "src/test/resources/tiny.txt"
    val analyzedTokens = FstFileReader.formsFromFile(f)

    val expectedSize = 2
    assert(analyzedTokens.size == expectedSize)
  }*/

  it should "create a Vector of AnalyzedTokens for all forms in numismatic test set" in pending


  /* {
    val f = "src/test/resources/coins-no-indecl.txt"
    val analyzedTokens = FstFileReader.formsFromFile(f)

    println("TOKENS: " + analyzedTokens.size)
  }*/

  it should "recognize failed analyses" in {
    val failed = Vector("no result for abdi")
    val analyses = FstFileReader.popAnalyses(failed)
    assert (analyses.isEmpty)
  }

}
