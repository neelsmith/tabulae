package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}

class NounRulesInstallerSpec extends FlatSpec {

  "The NounRulesInstaller object" should "respond to bad data with an exception" in {
    try {
      val fst = NounRulesInstaller.nounRuleToFst("Not a real line")
      fail("Should have failed")
    } catch {
      case t : Throwable => {
        val start = "java.lang.Exception: Wrong number of columns 1."
        assert(t.toString.startsWith(start))
      }
    }
  }

}
