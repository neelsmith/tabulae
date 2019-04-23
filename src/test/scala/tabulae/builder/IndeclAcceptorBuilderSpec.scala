package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}




class IndeclAcceptorBuilderSpec extends FlatSpec {

  "The AcceptorComposer object" should "generate correct FST for indeclinables" in {
    val dir = File("src/test/resources/dummyparser")
    val fst = AcceptorComposer.indeclAcceptor(dir)

    println("FST: " + fst)
  }

}
