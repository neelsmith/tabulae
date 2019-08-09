
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}

class TabulaeDataSetSpec extends FlatSpec {


  "A  TabulaeDataSet object" should "be built from datasource, repository and corpus name" in {
    val repo = File(".")
    val datasource = repo / "datasets"
    val c = "analytical-types"
    val dataSet = TabulaeDataSet(repo, datasource, c)
    assert(dataSet.corpus == c)

  }
}
