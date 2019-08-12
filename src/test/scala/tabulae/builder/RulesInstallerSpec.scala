
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar

class RulesInstallerSpec extends FlatSpec {


  "The RulesInstaller object" should "install inflectional from a Vector of corpus names" in pending /* {
    val repo = File("src/test/resources")
    val datasource = repo / "datasets"
    val c = Vector("analytical_types")


    // Ensure target directory is empty before testing:
    val projectDir = repo / "parsers" /  c.mkString("-")
    val targetDir =  projectDir / "inflection"
    if (targetDir.exists) {
      targetDir.delete()
      mkdirs(targetDir)
    }
    assert(targetDir.exists, "Unable to create new inflection directory " + targetDir)

    val  ri = RulesInstaller(datasource, repo, c)

    val expectedFiles = Vector(targetDir / "adjinfl.fst",
    targetDir / "advinfl.fst",
    targetDir / "gerundinfl.fst",
    targetDir / "gerundiveinfl.fst",
    targetDir / "indeclinfl.fst",
    targetDir / "infininfl.fst",
    targetDir / "irreginfl.fst",
    targetDir / "nouninfl.fst",
    targetDir / "ptcplinfl.fst",
    targetDir / "supineinfl.fst",
    targetDir / "verbinfl.fst")
    for (f <- expectedFiles) {
      assert(f.exists)
    }

    val projectDir = repo / "parsers" / c.mkString("-")
    projectDir.delete()
  }*/

  it should "install correctly from more than one source" in pending

  it should "create subdirectories as necessary for installation" in pending /* {
    val repo = File("src/test/resources")
    val datasource = repo / "datasets"
    val c = Vector("analytical_types")

    val projectDir = repo / "parsers" / c.mkString("-")
    // Ensure target directory does not exist:
    val targetDir = projectDir / "inflection"
    if (targetDir.exists) {
      targetDir.delete()
      assert(targetDir.exists == false, "Unable to delete previous dir " + targetDir)
    }
    val  ri = RulesInstaller(datasource, repo, c)
    assert(targetDir.exists)
    projectDir.delete()
  }*/
}
