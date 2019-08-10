
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class RulesInstallerSpec extends FlatSpec {


  "The RulesInstaller object" should "install inflectional from a Vector of corpus names" in {
    val repo = File("src/test/resources")
    val datasource = repo / "datasets"
    val c = Vector("analytical_types")
    // Ensure target directory is empty before testing:
    val targetDir = repo /"parsers" /  c.mkString("-") / "inflection"
    if (targetDir.exists) {
      targetDir.delete()
      mkdirs(targetDir)
    }

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

  }
}
