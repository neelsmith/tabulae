
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


class AnalyzedTokenCexSpec extends FlatSpec {

  val fst = "> designatvs\n<u>ocremorph.n13382c</u><u>ls.n13382</u><#>designat<verb><pftpass><div><pftpass><ptcpl>vs<masc><nom><sg><pft><pass><u>ocremorph.pft_perfppl1</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val tkn = analyzedTokens(0)


  "An AnalyzedToken" should "write a CEX representation of itself" in {
    //assert(tkn.cex == expected)
    val expected = "designatvs#lexical#ls.n13382#ocremorph.n13382c#ocremorph.pft_perfppl1#Masculine#Nominative#Singular###Perfect##Passive"
    assert(tkn.cex == expected)
  }

}
