
package edu.holycross.shot.tabulae.builder

import org.scalatest.FlatSpec
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._
import java.util.Calendar

class AcceptorComposerSpec extends FlatSpec {
  val r = scala.util.Random
  val millis = Calendar.getInstance().getTimeInMillis()
  r.setSeed(millis)


  "The AcceptorComposer object" should "write the central acceptor.fst file for verb data" in {
    val datasets = File("src/test/resources/datasets/")
    val corpora = Vector("analytical_types")

    // write some verb data there:

    val tempParserDir = File(s"src/test/resources/parsers/dummyparser-${r.nextInt(1000)}")
    val targetDir = tempParserDir / corpora.mkString("_") / "lexica"
    if (targetDir.exists) {
      tempParserDir.delete()
    }
    mkdirs(targetDir)
    assert(targetDir.exists,"AcceptorComposer:  could not create " + targetDir)
    val targetFile = targetDir / "lexicon-verbs.fst"
    VerbDataInstaller(datasets, corpora, targetFile)

    // then try composing the acceptor:
    AcceptorComposer(tempParserDir, corpora)
    val mainAcceptor = tempParserDir / corpora.mkString("_") / "acceptor.fst"
    assert(mainAcceptor.exists, "AcceptorComposer did not create main acceptor file " + mainAcceptor)

    val actualString = mainAcceptor.lines.toVector.mkString("\n")
    val expected = "$acceptor$ = $squashverburn$ | $squashinfurn$ | $squashptcplurn$ | $squashgerundiveurn$ | $squashgerundurn$ | $squashsupineurn$"

    assert (actualString.contains(expected))

    tempParserDir.delete()
  }


  it should "object if there are no FST sources" in pending


  it should "recognize when verbs should be included" in {
    val parserRoot = File("src/test/resources/sample-parser-data")
    assert(AcceptorComposer.includeVerbs(parserRoot))
  }

  it should "recognize when verbs should not be included" in {
    val noFst = File("src/test/resources/no-fst")
    assert(AcceptorComposer.includeVerbs(noFst) == false)
  }
  it should "compose acceptor's FST statements for verbs" in {
    val parserRoot = File("src/test/resources/sample-parser-data")
    val verbAcceptorFst = AcceptorComposer.verbAcceptor(parserRoot)

    val expected = "% Conjugated verb form acceptor\n$=verbclass$ = [#verbclass#]\n$squashverburn$ = <u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$  $separator$+$=verbclass$ <verb>[#stemchars#]* [#person#] [#number#] [#tense#] [#mood#] [#voice#]<u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u>"

    assert(verbAcceptorFst.trim == expected)
  }

  it should "recognize when nouns should be included" in {
    val parserRoot = File("src/test/resources/sample-parser-data")
    assert(AcceptorComposer.includeNouns(parserRoot))
  }
  it should "recognize when nouns should not be included" in {
    val noFst = File("src/test/resources/no-fst")
    assert(AcceptorComposer.includeNouns(noFst) == false)
  }
  it should "compose acceptor's FST statements for nouns" in {
    val parserRoot = File("src/test/resources/sample-parser-data")
    val nounAcceptorFst = AcceptorComposer.nounAcceptor(parserRoot)
    println(nounAcceptorFst)
    val expected = Vector(
      "% Noun acceptor:",
      "$=nounclass$ = [#nounclass#]",
      "$squashnounurn$ = <u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<noun>$=gender$ $=nounclass$   <div> $=nounclass$  <noun> [#stemchars#]* $=gender$ $case$ $number$ <u>[#urnchar#]:<>+\\.:<>[#urnchar#]:<>+</u>"
    )
    assert (expected == nounAcceptorFst.split("\n").toVector.filter(_.nonEmpty))
  }

  
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
