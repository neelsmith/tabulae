
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar

class DataInstallerSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

// USE RANDOMIZED FILE NAMES FOR ALL THESE TESTS
  "The DataInstaller object" should "install data from a Vector of corpus names" in pending /* {
    val repo = File("src/test/resources")
    val datasource = repo / "datasets"
    val c = Vector("analytical_types")
    // Ensure target directory is empty before testing:
    val targetDir = repo / "parsers" /  c.mkString("-") / "lexica"
    if (targetDir.exists) {
      targetDir.delete()
      mkdirs(targetDir)
    }

    val  di = DataInstaller(datasource, repo, c)
    val expectedFiles =  Vector(
        targetDir / "lexicon-verbs.fst"

      targetDir / "lexicon-adjectives.fst",
      targetDir / "lexicon-compoundverbs.fst",
      targetDir / "lexicon-indeclinables.fst",
      targetDir / "lexicon-irregadjectives.fst",
      targetDir / "lexicon-irregadverbs.fst",
      targetDir / "lexicon-irregcompoundgerundives.fst",
      targetDir / "lexicon-irregcompoundgerunds.fst",
      targetDir / "lexicon-irregcompoundinfinitives.fst",
      targetDir / "lexicon-irregcompoundparticiples.fst",
      targetDir / "lexicon-irregcompoundsupines.fst",
      targetDir / "lexicon-irregcompoundverbs.fst",
      targetDir / "lexicon-irreggerundives.fst",
      targetDir / "lexicon-irreggerunds.fst",
      targetDir / "lexicon-irreginfinitives.fst",
      targetDir / "lexicon-irregnouns.fst",
      targetDir / "lexicon-irregparticiples.fst",
      targetDir / "lexicon-irregpronouns.fst",
      targetDir / "lexicon-irregsupines.fst",
      targetDir / "lexicon-irregverbs.fst",
      targetDir / "lexicon-nouns.fst",

    )
    for (f <- expectedFiles) {
      println("Check for file " + f.toString)
      assert(f.exists)
    }

    val projectDir = repo / "parsers" / c.mkString("-")
    projectDir.delete()
  }*/

  it should "create subdirectories as necessary for installation" in {
    val datasets = File("src/test/resources/datasets")
    val c = Vector("analytical_types")
    // Ensure target directory does not exist:
    val parserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val targetDir = parserDir /  c.mkString("-") / "lexica"
    if (targetDir.exists) {
      targetDir.delete()
    }
    val  di = DataInstaller(datasets, c, parserDir)
    assert(targetDir.exists, "DataInstaller did not create " + targetDir)

    val projectDir = parserDir / c.mkString("-")
    projectDir.delete()
  }



  it should "install data correctly from multiple sources" in pending /*{
    val repo = File("src/test/resources")
    val datasource = repo / "datasets"
    val c = Vector("analytical_types", "shared")

    val targetDir = repo / "parsers" /  c.mkString("-") / "lexica"
    if (targetDir.exists) {
      targetDir.delete()
      mkdirs(targetDir)
    }

    val  di = DataInstaller(datasource, repo, c)
    val verbsFile =   File("src/test/resources/parsers/analytical_types-shared/lexica/lexicon-verbs.fst")
    assert(verbsFile.exists)
    val expectedVerbLines = Vector("<u>proof\\.v1</u><u>lexent\\.n29616</u><#>monstr<verb><conj1>", "<u>proof\\.v2</u><u>lexent\\.n2280</u><#>am<verb><conj1>")

    assert(verbsFile.lines.toVector.filter(_.nonEmpty) == expectedVerbLines)


  }*/

}
