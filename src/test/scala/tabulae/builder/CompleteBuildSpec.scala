
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}

class CompleteBuildSpec extends FlatSpec {


  "The FstCompiler object" should "compile a binary FST parser" in {
    //ataDirectory: ScalaFile, baseDir: ScalaFile, corpus: String, conf: Configuration)

    val repo = File(".")
    val datasource = repo/"datasets"
    val c = "analytical-types"

    val conf = Configuration("/usr/local/bin/fst-compiler-utf8", "/usr/local/bin/fst-infl",  "/usr/bin/make", "datasets")


    FstCompiler.compile(datasource, repo, c, conf, true)

  }
}
