
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class CompleteBuildSpec extends FlatSpec {


  "The FstCompiler object" should "compile a binary FST parser" in {
    val repo = File("src/test/resources")
    val datasource = repo / "datasets"
    val c = "analytical_types"
    //val c = "indecls"
    val conf = Configuration("/usr/local/bin/fst-compiler-utf8", "/usr/local/bin/fst-infl",  "/usr/bin/make", "datasets")


    val target = File("src/test/resources/parsers/analytical_types/latin.a")
    if (target.exists()) {
      println("Removing previous binary...")
      rm(target)
      assert(target.exists == false, "Unable to remove target file " + target)
    }
    FstCompiler.compile(datasource, repo, c, conf, true)
    assert(target.exists)
  }
}
