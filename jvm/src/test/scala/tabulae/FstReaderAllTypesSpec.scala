
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class FstReaderAllTypesSpec extends FlatSpec {


  "The FstReader object" should "create a Vector of tokens from data in a file" in {
    val fstFile = "jvm/src/test/resources/words-oneeach-output.fst"
    val analyzed = FstReader.fromFile(fstFile)
    val expctedAnalyses = 17
    assert( analyzed.size == expctedAnalyses)
    for (analysis <- analyzed) {
      println(analysis.literalToken + " -> " + analysis.analyses.size + " analyses")
    }
  }




}
