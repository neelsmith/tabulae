
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

class AcceptorComposerSpec extends FlatSpec {


  "The AcceptorComposer object" should "write the central acceptor.fst file" in pending

  it should "compose intermediate .fst files for verbs" in pending

  it should "compose intermediate .fst files for indeclinables" in pending
  it should "compose intermediate .fst files for infinitives" in pending
  it should "compose intermediate .fst files for participles" in pending
  it should "compose intermediate .fst files for gerundives" in pending
  it should "compose intermediate .fst files for gerunds" in pending
  it should "compose intermediate .fst files for supines" in pending

  it should "compose intermediate .fst files for nouns" in pending
  it should "compose intermediate .fst files for adjectives" in pending
  it should "compose intermediate .fst files for adverbs" in pending

  // And for all the irregulars, too:
  /*
  irregVerbAcceptor(projectDir),
  irregInfinitiveAcceptor(projectDir),
  irregParticipleAcceptor(projectDir),
  irregGerundAcceptor(projectDir),
  irregGerundiveAcceptor(projectDir),
  irregSupineAcceptor(projectDir),

  irregNounAcceptor(projectDir),
  irregAdverbAcceptor(projectDir),
  irregPronounAcceptor(projectDir),
  irregAdjectiveAcceptor(projectDir)*/

  it should "object if there are no FST sources" in pending


}
