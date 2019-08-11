
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class VerbDataInstallerSpec extends FlatSpec {

  "The VerbDataInstaller object" should "install verb data" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    //analytical_types/rules-tables/verbs
    val targetDir = File("src/test/resources/parsers/dummyparser/lexica")
    if (targetDir.exists) {
      File("src/test/resources/parsers").delete()
    }
    mkdirs(targetDir)
    val targetFile = targetDir / "lexicon-verbs.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    VerbDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists)

    val expected = "<u>proof\\.v1</u><u>lexent\\.n29616</u><#>monstr<verb><conj1>"
    assert(targetFile.lines.toVector(0) == expected)

    File("src/test/resources/parsers").delete()
  }

  it should "do nothing if no verb data are present in a given corpus" in {
    val datasets = File("src/test/resources/datasets/")
      val corpora = Vector("no-lexica")

  }

}
