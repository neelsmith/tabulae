
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar


class ParserComposerSpec extends FlatSpec {


  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The ParserComposer object" should  "ensure that lexica are already installed" in pending
  it should "ensure that inflectional rules are already installed" in pending
  it should "ensure that the central symbols.fst file is already written" in pending
  it should "write the main parser file latin.fst" in pending
}
