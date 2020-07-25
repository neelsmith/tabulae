package edu.holycross.shot.tabulae


import org.scalatest.FlatSpec

import edu.holycross.shot.cite._

class UrnManagerSpec extends FlatSpec {

  "A UrnManager" should "expand IDs to URNs" in {
    val lines = Vector("id#urn#label","proof#urn:cite2:tabulae:test.v1:#Minimal set of stems for testing all stem types, rule types, irregular form types, and configuration with compounds.", "lexent#urn:cite2:linglat:lexent.v1:#Latin lexical entities.")
    val urnManager = UrnManager(lines)
    val expected = Some(Cite2Urn("urn:cite2:linglat:lexent.v1:"))
    assert(urnManager.resolve("lexent") == expected)
  }

  it should "resolve to None if no match" in {
    val lines = Vector("id#urn#label","proof#urn:cite2:tabulae:test.v1:#Minimal set of stems for testing all stem types, rule types, irregular form types, and configuration with compounds.", "lexent#urn:cite2:linglat:lexent.v1:#Latin lexical entities.")
    val urnManager = UrnManager(lines)

    assert(urnManager.resolve("nada") == None)
  }

  "The UrnManager object" should "create a UrnManager from a file" in {
    val f = "jvm/src/test/resources/datasets/analytical_types/urnregistry/collectionregistry.cex"
    val manager = UrnManager.fromFile(f)
    manager match {
      case urnManager: UrnManager => assert(true)
      case _ => fail("Did not make a UrnManager from " + f)
    }    }


    it should "create a UrnManager from a URL" in {
      val url = "https://raw.githubusercontent.com/neelsmith/tabulae/master/jvm/src/test/resources/datasets/analytical_types/urnregistry/collectionregistry.cex"

      val manager = UrnManager.fromUrl(url)
      manager match {
        case urnManager: UrnManager => assert(true)
        case _ => fail("Did not make a UrnManager from " + url)
      }
  }
}
