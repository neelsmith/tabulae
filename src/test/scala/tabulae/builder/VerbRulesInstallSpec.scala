
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar



class VerbRulesInstallSpec extends FlatSpec {

  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The VerbRulesInstaller object" should "install verb data from a single source" in pending /* {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")
    val parserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      File("src/test/resources/parsers").delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"VerbDataInstaller:  could not create " + targetDir)

    val targetFile = targetDir / "lexicon-verbs.fst"
    println("Target " + targetFile)

    //dataSource: File, corpusList: Vector[String], targetFile: File
    VerbDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists,"VerbDataInstaller:  did not create " + targetFile)

    val expected = "<u>proof\\.v1</u><u>lexent\\.n29616</u><#>monstr<verb><conj1>"
    assert(targetFile.lines.toVector(0) == expected)

    try {
      File("src/test/resources/parsers").delete()
      println("Deleted temp dir.")
    } catch {
      case t: Throwable => {
        println("Failed to delete parsers dir. " + t)
      }
    }

  }

*/

  it should "do nothing if no verb data are present in a given corpus" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("no-lexica")
    val parserDir = File("src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    val targetFile = targetDir / "lexicon-verbs.fst"
    if (targetDir.exists) {
      File("src/test/resources/parsers").delete()
    }
    mkdirs(targetDir)

    VerbRulesInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists == false)
  }

  it should "return an empty string if no data are found in the source directory" in pending /*{
    val emptyDir = File("src/test/resources/no-fst")
    val output = VerbDataInstaller.fstForVerbData(emptyDir)
    assert(output.isEmpty)
  }*/

  it should "return an empty string if asked to create FST strings from a non-existent directory" in pending /* {
    val emptyDir = File("src/test/resources/no-fst")
    val fst = VerbDataInstaller.fstForVerbData(emptyDir)
    assert(fst.isEmpty)
  }*/


  it should "composite data from multiple sources" in pending /*{
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types", "shared")
    val parserDir = File("src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = parserDir / "lexica"
    if (targetDir.exists) {
      val parserDir = File("src/test/resources/parsers")
      parserDir.delete()
      println("Removed " + parserDir  + ": " + (parserDir.exists == false))
    }
    mkdirs(targetDir)
    assert(targetDir.exists, "VerbDataInstallerSpec: could not create " + targetDir)
    val targetFile = targetDir / "lexicon-verbs.fst"
    //dataSource: File, corpusList: Vector[String], targetFile: File
    VerbDataInstaller(datasets, corpora, targetFile)
    assert(targetFile.exists, "VerbDataInstallerSpec: VerbDataInstaller did not create " + targetFile)

    val expectedLines = Vector("<u>proof\\.v1</u><u>lexent\\.n29616</u><#>monstr<verb><conj1>", "<u>proof\\.v2</u><u>lexent\\.n2280</u><#>am<verb><conj1>")

    assert(targetFile.lines.toVector.filter(_.nonEmpty) == expectedLines)

    try {
    parserDir.delete()
    } catch {
      case t: Throwable => {
        println("Failed to delete parsers dir. " + t)
      }
    }

  }*/

}
