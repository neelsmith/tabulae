
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class AcceptorComposerSpec extends FlatSpec {

  val parserRoot = File("src/test/resources/sample-parser")
  val noFst = File("src/test/resources/no-fst")

  "The AcceptorComposer object" should "write the central acceptor.fst file" in pending
  it should "object if there are no FST sources" in pending


  it should "recognize when verbs should be included" in pending /*{
    assert(AcceptorComposer.includeVerbs(parserRoot))
  }*/

  it should "recognize when verbs should not be included" in pending /*{
    assert(AcceptorComposer.includeVerbs(noFst) == false)
  }*/
  it should "compose acceptor's FST statements for verbs" in pending /*{
    val verbAcceptorFst = AcceptorComposer.verbAcceptor(parserRoot)

    val expected = "% Conjugated verb form acceptor\n$=verbclass$ = [#verbclass#]\n$squashverburn$ = <u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$  $separator$+$=verbclass$ <verb>[#stemchars#]* [#person#] [#number#] [#tense#] [#mood#] [#voice#]<u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u>"

    assert(verbAcceptorFst.trim == expected)
  }*/

  it should "compose acceptor's FST statements for nouns" in pending
  it should "compose acceptor's FST statements for adjectives" in pending
  it should "compose acceptor's FST statements for adverbs" in pending
  it should "compose acceptor's FST statements for indeclinables" in pending
  it should "compose acceptor's FST statements for infinitives" in pending
  it should "compose acceptor's FST statements for participles" in pending
  it should "compose acceptor's FST statements for gerundives" in pending
  it should "compose acceptor's FST statements for gerunds" in pending
  it should "compose acceptor's FST statements for supines" in pending



  it should "compose acceptor's FST statements for irregular verbs" in pending
  it should "compose acceptor's FST statements for irregular infinitives" in pending
  it should "compose acceptor's FST statements for irregular participles" in pending
  it should "compose acceptor's FST statements for irregular gerunds" in pending
  it should "compose acceptor's FST statements for irregular gerundives" in pending
  it should "compose acceptor's FST statements for irregular supines" in pending
  it should "compose acceptor's FST statements for irregular nouns" in pending
  it should "compose acceptor's FST statements for irregular adverbs" in pending
  it should "compose acceptor's FST statements for irregular pronouns" in pending
  it should "compose acceptor's FST statements for irregular adjectives" in pending






}
