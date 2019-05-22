
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec


class AnalyzedTokenObjectSpec extends FlatSpec {

  val fstStr = """> designatvs
<u>ocremorph.n13382c</u><u>ls.n13382</u><#>designat<verb><pftpass><div><pftpass><ptcpl>vs<masc><nom><sg><pft><pass><u>ocremorph.pft_perfppl1</u>
> agrippina
<u>ocremorph.n1640</u><u>ls.n1640</u>agrippin<noun><fem><a_ae><div><a_ae><noun>a<fem><nom><sg><u>ocremorph.a_ae1</u>
<u>ocremorph.n1640</u><u>ls.n1640</u>agrippin<noun><fem><a_ae><div><a_ae><noun>a<fem><abl><sg><u>ocremorph.a_ae5</u>
"""

  val fst = fstStr.split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)



  "The AnalyzedToken object" should "write a CEX representation of a vector of AnalyzedTokens" in {
    println("CEX:\n" + AnalyzedToken.vectorCex(analyzedTokens))
  }

}
