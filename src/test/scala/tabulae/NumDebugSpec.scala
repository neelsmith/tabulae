
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class NumDebugSpec extends FlatSpec {



  "The FstFileReader object" should "create a Vector of AnalyzedTokens for all forms in numismatic test set" in   {
    val f = "src/test/resources/coins-no-indecl-no-irreg.txt"

    val analyzedTokens = FstFileReader.formsFromFile(f)
    val expectedTokens = 214
    assert(analyzedTokens.size == expectedTokens)
  }

  it should "deal with single analyses" in pending/*{


  val fstLines = """> et
<u>ocremorph.indecl2</u><u>ls.n16278</u>et<indeclconj><div><indeclconj><u>indeclinfl.2</u>
> exercitvs
<u>ocremorph.51</u><u>ls.n16824</u>exercit<noun><masc><us_us><div><us_us><noun>vs<masc><nom><sg><u>ocremorph.us_us1</u>
<u>ocremorph.51</u><u>ls.n16824</u>exercit<noun><masc><us_us><div><us_us><noun>vs<masc><acc><pl><u>ocremorph.us_us9</u>
<u>ocremorph.51</u><u>ls.n16824</u>exercit<noun><masc><us_us><div><us_us><noun>vs<masc><gen><sg><u>ocremorph.us_us2</u>
<u>ocremorph.51</u><u>ls.n16824</u>exercit<noun><masc><us_us><div><us_us><noun>vs<masc><voc><sg><u>ocremorph.us_us11</u>
<u>ocremorph.51</u><u>ls.n16824</u>exercit<noun><masc><us_us><div><us_us><noun>vs<masc><voc><pl><u>ocremorph.us_us12</u>
""".split("\n").toVector
val parses = FstFileReader.parseFstLines(fstLines)
println(parses)
}*/

}
