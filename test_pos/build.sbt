import complete.DefaultParsers._
import scala.sys.process._

import better.files.{File => ScalaFile, _}
import better.files.Dsl._

name := "postest"


/** Triples of description, function and status. */
def testList = List(

  // Test all inflectional rules installers
  ("Test copying FST inflection rules for invariants", testInvariantCopy(_,_,_), ""),

  // inflectional rules for verbs
  ("Test converting bad inflectional rules for verbs", testBadVerbsInflRulesConvert(_, _, _), "" ),
  ("Test converting  inflectional rules for verbs", testConvertVerbInflRules(_, _, _), "" ),
  ("Test converting  inflectional rules for verbs from files in dir", testVerbInflRulesFromDir(_, _, _), "" ),


  // verb stems
  ("Test converting bad stem data to fst for verbs", testBadVerbStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for verbs", testVerbStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for verbs", testVerbStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for verb stem data installer", testVerbStemDataApplied(_, _, _), "" ),

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

def installVerbRuleTable(verbsDir:  ScalaFile) : Unit = {
  val verbFile = verbsDir/"madeupdata.cex"
  val goodLine = "RuleUrn#InflectionClasses#Ending#Person#Number#Tense#Mood#Voice\nlverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act\n"
  verbFile.overwrite(goodLine)

}
def installVerbStemTable(verbsDir:  ScalaFile) : Unit = {
  val verbFile = verbsDir/"madeupdata.cex"
  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val text = s"header line, omitted in parsing\n${goodLine}"
  verbFile.overwrite(text)
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


def testBadVerbsInflRulesConvert(corpusName: String, conf: Configuration, repoRoot : File): Boolean = {
  //  Test conversion of delimited text to FST.
  // Should object to bad data
  try {
    val fst = VerbRulesInstaller.verbRuleToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}

def testConvertVerbInflRules(corpusName: String, conf: Configuration, repoRoot : File): Boolean = {
  // Should correctly convert good data.
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  val expected = "<conj1><verb>o<1st><sg><pres><indic><act><u>lverbinfl\\.are\\_presind1</u>"
  goodFst.trim ==  expected
}

def testVerbInflRulesFromDir(corpusName: String, conf: Configuration, repoRoot : File): Boolean = {
  // Install inflectional table of data
  val repo = repoRoot.toScala
  val verbData = mkdirs(repo/"datasets"/corpusName/"rules-tables/verbs")
  installVerbRuleTable(verbData)

  val fstFromDir = VerbRulesInstaller.fstForVerbRules(verbData)

  val lines = fstFromDir.split("\n").toVector
  val expected = "$verbinfl$ =  <conj1><verb>o<1st><sg><pres><indic><act><u>lverbinfl\\.are\\_presind1</u>"
  // tidy up
  (repo/"datasets").delete()

  lines(0) == expected
}


def testBadVerbStemDataConvert(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  //  Test conversion of delimited text to FST.
  //  should object to bad data
  try {
    val fst = VerbDataInstaller.verbLineToFst("Not a real line")
    println("Should never have seent this... " + fst)
    false
  } catch {
    case t : Throwable => true
  }
}
def testVerbStemDataConvert(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  // should correctly convert good data.
  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val goodFst = VerbDataInstaller.verbLineToFst(goodLine)
  val expected = "<u>ag\\.v1</u><u>lexent\\.n2280</u><#>am<verb><conj1>"
  goodFst.trim ==  expected
}
def testVerbStemFstFromDir(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  val repo = repoRoot.toScala

  // Should create FST for all files in a directory
  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val verbSource = mkdirs(repo/"datasets"/corpusName/"stems-tables/verbs-simplex")
  val testData = verbSource/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)
  val fstFromDir = VerbDataInstaller.fstForVerbData(verbSource)
  // Tidy up
  //   (repo/"datasets").delete()
  val expected = "<u>ag\\.v1</u><u>lexent\\.n2280</u><#>am<verb><conj1>"
  fstFromDir.trim == expected

}
def testVerbStemDataApplied(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  // Install one data file:
  val repo = repoRoot.toScala
  val verbsDir = repo/"datasets"/corpusName/"stems-tables/verbs-simplex"
  installVerbStemTable(verbsDir)

  val destDir = mkdirs(repo/"parsers"/corpusName/"lexica")

  // Write some test data in the source work space:
  VerbDataInstaller(verbsDir, destDir/"lexicon-verbs.fst")

  // check the results:
  val resultFile = repo/"parsers"/corpusName/"lexica/lexicon-verbs.fst"
  val output = resultFile.lines.toVector

  // clean up:
  (repo/"datasets").delete()

  val expected = "<u>ag\\.v1</u><u>lexent\\.n2280</u><#>am<verb><conj1>"
  output(0) == expected
}


def testRulesInstaller(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= {

  // Write some test data in the source work space:
  // Install inflectional table of data
  val repo = repoRoot.toScala
  val verbData = mkdirs(repo/"datasets"/corpusName/"rules-tables/verbs")
  installVerbRuleTable(verbData)

  RulesInstaller(repo/"datasets", repo, corpusName)

  val actualFiles =  (repo/"parsers"/corpusName/"inflection").glob("*.fst")
  val actualSet = actualFiles.map(_.name).toSet
  println("Actual file set:  " + actualSet)
  val expectedSet = Set("verbinfl.fst", "irreginfl.fst", "indeclinfl.fst")

  expectedSet  ==  actualSet
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
