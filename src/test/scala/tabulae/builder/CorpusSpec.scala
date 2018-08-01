
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}

class CorpusSpec extends FlatSpec {


  "A  Corpus object" should "be built from datasource, repository and corpus name" in {
    val repo = File(".")
    val datasource = repo/"datasets"
    val c = "analytical-types"
    val corpus = Corpus(repo, datasource, c)
    assert(corpus.corpus == c)

  }
}
