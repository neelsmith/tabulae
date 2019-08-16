
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
  "The DataInstaller object" should "install data from a Vector of corpus names" in {

    val datasets = File("src/test/resources/datasets")
    val c = Vector("analytical_types")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"



    // Ensure target directory is empty before testing:
    val targetDir = tempParserDir /  c.mkString("-") / "lexica"
    if (targetDir.exists) {
      targetDir.delete()
      mkdirs(targetDir)
    }


    val  di = DataInstaller(datasets, c, tempParserDir)
    val expectedFiles =  Vector(

        targetDir / "lexicon-verbs.fst",
        targetDir / "lexicon-irregverbs.fst",
        targetDir / "lexicon-compoundverbs.fst",

        targetDir / "lexicon-irreginfinitives.fst",

        targetDir / "lexicon-indeclinables.fst",
        targetDir / "lexicon-adjectives.fst",
        targetDir / "lexicon-nouns.fst",

        targetDir / "lexicon-irregnouns.fst",
        targetDir / "lexicon-irregadjectives.fst",
        targetDir / "lexicon-irregadverbs.fst",

        targetDir / "lexicon-irregpronouns.fst"

/*
      targetDir / "lexicon-irregcompoundverbs.fst",
      targetDir / "lexicon-irregcompoundinfinitives.fst",



*/

  //Maybe bogus:
/*
  targetDir / "lexicon-irreggerundives.fst",
  targetDir / "lexicon-irreggerunds.fst",

  targetDir / "lexicon-irregparticiples.fst",

  targetDir / "lexicon-irregsupines.fst",

  targetDir / "lexicon-irregcompoundgerundives.fst",
  targetDir / "lexicon-irregcompoundgerunds.fst",
  targetDir / "lexicon-irregcompoundparticiples.fst",
  targetDir / "lexicon-irregcompoundsupines.fst",
*/
    )
    for (f <- expectedFiles) {
      assert(f.exists, "Expected file " + f + " not created.")
    }
    tempParserDir.delete()
  }

  it should "create subdirectories as necessary for installation" in {
    val datasets = File("src/test/resources/datasets")
    val c = Vector("analytical_types")
    // Ensure target directory does not exist:
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val targetDir = tempParserDir /  c.mkString("-") / "lexica"
    if (targetDir.exists) {
      targetDir.delete()
    }
    val  di = DataInstaller(datasets, c, tempParserDir)
    assert(targetDir.exists, "DataInstaller did not create " + targetDir)

    tempParserDir.delete()
  }



  it should "install data correctly from multiple sources" in {

    val datasource = File("src/test/resources/datasets")
    val c = Vector("analytical_types", "shared")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"

    val targetDir = tempParserDir / c.mkString("-") / "lexica"
    if (targetDir.exists) {
      targetDir.delete()
      mkdirs(targetDir)
    }

    val  di = DataInstaller(datasource, c, tempParserDir)
    val verbsFile =   tempParserDir / "analytical_types-shared/lexica/lexicon-verbs.fst"
    assert(verbsFile.exists, "DataInstaller did not write verbs file " + verbsFile)
    val expectedVerbLines = Vector("<u>proof\\.v1</u><u>lexent\\.n29616</u><#>monstr<verb><conj1>", "<u>proof\\.v2</u><u>lexent\\.n2280</u><#>am<verb><conj1>")

    assert(verbsFile.lines.toVector.filter(_.nonEmpty) == expectedVerbLines)

    tempParserDir.delete()

  }

}
