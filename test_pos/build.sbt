import complete.DefaultParsers._
import scala.sys.process._
/*
import better.files._
import better.files.File._
import java.io.{File => JFile}*/

import better.files.{File => ScalaFile, _}
import better.files.Dsl._
import java.io.File

name := "postest"



/** Triples of description, function and status. */
def testList = List(
  ("Test copying FST inflection rules for invariants", testInvariantCopy(_,_,_), ""),
  ("Test composing all inflectional rules via RulesInstaller", testRulesInstaller(_, _, _), "" ),
)

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


////////////////// Tests //////////////////////////////
//

def testInvariantCopy(corpusName: String, conf: Configuration, repoRoot : File): Boolean = {
  val repo = repoRoot.toScala
  val parser = repo/"parsers"/corpusName
  val inflTarget = parser/"inflection"
  val inflSrc = repo/"fst/inflection"

  RulesInstaller.installInvariants(inflSrc,inflTarget )

  val expectedFiles = Set (
    inflTarget/"indeclinfl.fst",
    inflTarget/"irreginfl.fst"
  )
  val actualFiles = inflTarget.glob("*.fst").toSet
  actualFiles == expectedFiles
}


def testRulesInstaller(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= {
  /*
  val goodLine = "testdata.rule1#nunc"
  // Write some test data to work with:
  val dataSource = repoRoot / "datasets"
  val corpus = Utils.dir(dataSource / corpusName)
  // are we leaving junk from another test lying around?
  val parserDir = repoRoot / s"parsers/${corpusName}"
  IO.delete(parserDir)
  IO.delete(corpus)
  Utils.dir(parserDir)
  Utils.dir(corpus)

  val stems = Utils.dir(corpus / "rules-tables")
  val indeclSource = Utils.dir(stems / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}

  val expected = "<nunc><indecl><u>testdata" + "\\" + ".rule1</u>"
  val fstFromDir = IndeclRulesInstaller.fstForIndeclRules(indeclSource)


  RulesInstaller(dataSource, repoRoot, corpusName)
  val target =  file (s"${repoRoot}/parsers/${corpusName}/inflection")
  val installedSource = target ** "*.fst"
  val actualSet = installedSource.get.map(_.getName).toSet
  val expectedSet = Set("indeclinfl.fst")
  expectedSet  ==  actualSet*/
  false
}

lazy val posTests = inputKey[Unit]("Unit tests")
posTests in Test := {
  val args: Seq[String] = spaceDelimited("<arg>").parsed

  args.size match {
      //runBuildTests(args(0), conf, baseDirectory.value)
    case 1 => {
      try {
        val conf = Configuration(file("conf.properties"))

        val f = file(conf.datadir)

        if (f.exists) {
          val corpusName = args(0)
          val baseDir = baseDirectory.value
          println("\nExecuting tests of build system with settings:\n\tcorpus:          " + corpusName + "\n\tdata source:     " + conf.datadir + "\n\trepository base: " + baseDir + "\n")

          val results = for (t <- testList.filter(_._3 != "pending")) yield {

            Utils.deleteSubdirs(baseDir / "parsers", false)
            print(t._1 + "...")
            val reslt = t._2(corpusName, conf, baseDir)
            if (reslt) { println ("success.") } else { println("failed.")}
            reslt
          }
          reportResults(results) //, testList)

        } else {
          println("Failed.")
          println(s"No configuration file ${conf.datadir} exists.")
        }

      } catch {
        case t: Throwable => {
          println("Failed.")
          println(t)
        }
      }
    }

    case _ =>  {
      println(s"Wrong number args (${args.size}): ${args}")
      println("Usage: unitTests CORPUS [CONFIG_FILE]")
    }
  }
}
