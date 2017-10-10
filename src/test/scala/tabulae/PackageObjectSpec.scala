
package edu.holycross.shot.tabulae

import org.scalatest.FlatSpec

class PackageObjectSpec extends FlatSpec {

  val longFst = "agricola<lo>"
  val longAscii = "agricola_"

  val shortFst = "ponti<sh>fex"
  val shortAscii = "ponti^fex"

  "The kanones package object" should "convert FST symbols to ASCII" in {
    assert(fstToAscii(longFst) == longAscii)
    assert(fstToAscii(shortFst) == shortAscii)
  }

  it should "convert ASCII to FST symbols" in {
    assert (asciiToFst(longAscii) == longFst)
    assert (asciiToFst(shortAscii) == shortFst)
  }

}
