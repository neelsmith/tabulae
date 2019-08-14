
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar


class MakefileComposerSpec extends FlatSpec {


  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)

  "The MakefileComposer object" should  "ensure that acceptors directory includes some .fst source" in pending
  it should "ensure that symbols.fst exists" in pending

  it should "ensure that symbols/phonology.fst exists" in pending
  it should "ensure that inflection.fst exists" in pending
  it should "ensure that acceptor.fst exists" in pending
  it should "compose the central makefile" in pending
  it should "compose the makefile for the inflection directory" in pending
}
