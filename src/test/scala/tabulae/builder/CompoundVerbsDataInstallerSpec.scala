
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class CompoundVerbsDataInstallerSpec extends FlatSpec {


  "The DataInstaller object" should "install compound verb data" in {
    val repo = File("src/test/resources")
    val datasource = repo / "datasets"
    val c = Vector("analytical_types")
    // Ensure target directory is empty before testing:
    val targetDir = repo / "parsers" /  c.mkString("-") / "lexica"
    if (targetDir.exists) {
      targetDir.delete()
      mkdirs(targetDir)
    }

    val projectDir = repo / "parsers" / c.mkString("-")
    projectDir.delete()
  }

}
