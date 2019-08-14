
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar

class CompleteBuildSpec extends FlatSpec {


  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  "The FstCompiler object" should "compile a binary FST parser" in {
    val datasets = File("src/test/resources/datasets")
    val c = Vector("analytical_types")
    val tempParserDir =  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val fst = datasets / "fst"
    val conf = Configuration("/usr/local/bin/fst-compiler-utf8", "/usr/local/bin/fst-infl",  "/usr/bin/make", "datasets")
    val projectDir = tempParserDir / c.mkString("-")
    val target = projectDir / "latin.a"
    if (target.exists()) {
      println("Removing previous binary...")
      rm(target)
      assert(target.exists == false, "Unable to remove target file " + target)
    }
    FstCompiler.compile(datasets, c, tempParserDir, fst, conf)

    assert(target.exists)

    //tempParserDir.delete()
  }
}
