
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import java.util.Calendar

class OneEachSpec extends FlatSpec {


  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  // This can still fail sometimes?
  "The FstCompiler object" should "compile a binary FST parser" in {
    val datasets = File("src/test/resources/datasets")
    val c = Vector("one-each")
    val tempParserDir = File("src/test/resources/parsers/stress") //  File("src/test/resources/parsers") / s"dummyparser-${r.nextInt(1000)}"
    val fst = datasets / "fst"

    val projectDir = tempParserDir / c.mkString("-")
    val target = projectDir / "latin.a"
    if (target.exists()) {
      println("Removing previous binary...")
      rm(target)
      assert(target.exists == false, "Unable to remove target file " + target)
    }

    if (File("/usr/local/bin/fst-compiler-utf8").exists) {
      val conf_mac = Configuration("/usr/local/bin/fst-compiler-utf8", "/usr/local/bin/fst-infl",  "/usr/bin/make", "datasets")
      FstCompiler.compile(datasets, c, tempParserDir, fst, conf_mac)

    } else {
      val conf_linux = Configuration("/usr/bin/fst-compiler-utf8", "/usr/bin/fst-infl",  "/usr/bin/make", "datasets")
      FstCompiler.compile(datasets, c, tempParserDir, fst, conf_linux)
    }

    assert(target.exists, "Failed to compile binary parser " + target)
    //tempParserDir.delete()
  }
}
