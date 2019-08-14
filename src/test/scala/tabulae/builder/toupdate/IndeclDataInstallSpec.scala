package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}

class IndeclDataInstallSpec extends FlatSpec {

  "The IndeclRulesInstaller object" should "respond to bad data with an exception" in pending /*{
    try {
      val fst = IndeclDataInstaller.indeclLineToFst("Not a real line")
      fail("Should have failed")
    } catch {
      case t : Throwable => {
        val start = "java.lang.Exception: Wrong number of columns 1."
        assert(t.toString.startsWith(start))
      }
    }
  }*/

  it should "format a valid data line correctly as FST" in pending /*{
    val fst = IndeclDataInstaller.indeclLineToFst("ocremorph.indecl2#ls.n16278#et#indeclconj")
    println(fst)

  } */

}
