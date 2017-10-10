
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class PackageObjectSpec extends FlatSpec {

  val fst = "ni<lo>kh h<ro> e<sm>n th<isub> a<sm>po<#>dosei"
  val ascii = "ni_kh h( e)n th| a)po-dosei"

  "The kanones package object" should "convert FST symbols to ASCII" in pending /* {
      assert(fstToAscii(fst) == ascii)
  }

  it should "convert ASCII to FST symbols" in {
    assert (asciiToFst(ascii) == fst)
  }
*/
}
