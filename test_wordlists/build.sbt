import complete.DefaultParsers._
import scala.sys.process._

import better.files.{File => ScalaFile, _}
import better.files.Dsl._

name := "wordlisttest"



/** Triples of description, function and status. */
def testList = List(
  // utilities
  ("Test nouns", testNouns(_,_,_), "pending"),
  ("Test conjugated verbs", testVerbs(_,_,_), "pending"),
  ("Test indeclinable forms", testIndecls(_,_,_), "pending"),
  ("Test irregular nouns", testIrregNouns(_,_,_), "pending"),
  ("Test irregular verbs", testIrregVerbs(_,_,_), "pending"),
  ("Test pronouns", testPronouns(_,_,_), "pending"),

  ("Test infinitives", testInfinitives(_,_,_), "pending"),
  ("Test participles", testParticiples(_,_,_), "pending"),
  ("Test verbal adjectives", testVerbalAdjectives(_,_,_), "pending"),

  ("Test adjectives", testAdjectives(_,_,_), "pending"),
  ("Test irregular adjectives", testIrregsAdjs(_,_,_), "pending"),
)


def testNouns(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testVerbs(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testIndecls(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testIrregNouns(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testIrregVerbs(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testAdjectives(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testIrregsAdjs(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }

def testPronouns(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testInfinitives(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testParticiples(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testVerbalAdjectives(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }


/** "s" or no "s"? */
def plural[T] (lst : List[T]) : String = {
  if (lst.size > 1) { "s"} else {""}
}

/** Interpret and display list of results.
*
* @param results List of test results
*/
def reportResults(results: List[Boolean]) = {//, testList : Vector[String]): Unit = {
  val distinctResults = results.distinct
  if (distinctResults.size == 1 && distinctResults(0)){
    println("\nAll tests succeeded.")
  } else {
    println("\nThere were failures.")
  }
  println(s"${results.filter(_ == true).size} passed out of ${results.size} test${plural(results)} executed.")
  val pending = testList.filter(_._3 == "pending")
  if (pending.nonEmpty) {
    println(s"\n${pending.size} test${plural(pending)} pending:")
    println(pending.map(_._1).mkString("\n"))
  }
}








lazy val wordlists = inputKey[Unit]("Unit tests")
wordlists in Test := {
  val args: Seq[String] = spaceDelimited("<arg>").parsed
  val corpusName = "testcorpus"
  val datadir = "datasets"
  val baseDir = baseDirectory.value

  val confFile = file("conf.properties").toScala
  val conf = Configuration(confFile)


  println("\nExecuting tests of build system with settings:\n\tcorpus:          " + corpusName + "\n\tdata source:     " + datadir + "\n\trepository base: " + baseDir + "\n")
  val results = for (t <- testList.filter(_._3 != "pending")) yield {
    //Utils.deleteSubdirs(baseDir / "parsers", false)

    print(t._1 + "...")
    val reslt = t._2(corpusName, conf, baseDir)
    if (reslt) { println ("success.") } else { println("failed.")}
    reslt
  }
  reportResults(results) //, testList)

}
