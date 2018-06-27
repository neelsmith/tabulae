import complete.DefaultParsers._
import scala.sys.process._

import better.files.{File => ScalaFile, _}
import better.files.Dsl._

name := "postest"


/** Triples of description, function and status. */
def testList = List(

  // Test all inflectional rules installers
  ("Test copying FST inflection rules for invariants", testInvariantCopy(_,_,_), ""),

  ("Test converting bad stem data for invariants", testBadInvariantStemData(_, _, _), "" ),
  ("Test converting  stem data for invariants", testConvertInvariantStem(_, _, _), "" ),
 /*
  // inflectional rules for verbs
  ("Test converting bad inflectional rules for verbs", testBadVerbsInflRulesConvert(_, _, _), "" ),
  ("Test converting  inflectional rules for verbs", testConvertVerbInflRules(_, _, _), "" ),
  ("Test converting  inflectional rules for verbs from files in dir", testVerbInflRulesFromDir(_, _, _), "" ),

  // verb stems
  ("Test converting bad stem data to fst for verbs", testBadVerbStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for verbs", testVerbStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for verbs", testVerbStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for verb stem data installer", testVerbStemDataApplied(_, _, _), "" ),



  /////////
  // inflectional rules for nouns
  ("Test converting bad inflectional rules for nouns", testBadNounsInflRulesConvert(_, _, _), "pending" ),
  ("Test converting  inflectional rules for nouns", testConvertNounInflRules(_, _, _), "pending" ),
  ("Test converting  inflectional rules for nouns from files in dir", testNounInflRulesFromDir(_, _, _), "pending" ),

  // noun stems
  ("Test converting bad stem data to fst for nouns", testBadNounStemDataConvert(_, _, _), "pending" ),
  ("Test converting stem data to fst for nouns", testNounStemDataConvert(_, _, _), "pending" ),
  ("Test converting stem files in directory to fst for nouns", testNounStemFstFromDir(_, _, _), "pending" ),
  ("Test converting apply method for noun stem data installer", testNounStemDataApplied(_, _, _), "pending" ),


  ("Test composing all inflectional rules via RulesInstaller", testRulesInstaller(_, _, _), "" ),

  // acceptor
  ("Test writing verbs acceptor string", testVerbAcceptor(_, _, _), "" ),
  ("Test writing nouns acceptor string", testNounAcceptor(_, _, _), "pending" ),

*/

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
// Invariants: rules
def testInvariantCopy(corpusName: String, conf: Configuration, repo : ScalaFile): Boolean = {

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


// Invariants: stems
def testBadInvariantStemData(corpusName: String, conf: Configuration, repo : ScalaFile): Boolean = {
  try {
    val fst = IndeclDataInstaller.indeclLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}
def testConvertInvariantStem(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  // should correctly convert good data.
  //  //StemUrn#LexicalEntity#Stem#PoS
  // cum n11872 prep
  val goodLine = "demo.n1#lexent.n11872#cum#indeclprep"
  val goodFst = IndeclDataInstaller.indeclLineToFst(goodLine)
  println("GOOD FST == " + goodFst)
  val expected = "<u>demo\\.n1</u><u>lexent\\.n11872</u>cum<indeclprep>"
  goodFst.trim ==  expected
}
/*




def testBadVerbStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
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






def testBadVerbsInflRulesConvert(corpusName: String, conf: Configuration, repo :  ScalaFile): Boolean = {
  //  Test conversion of delimited text to FST.
  // Should object to bad data
  try {
    val fst = VerbRulesInstaller.verbRuleToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}

def testConvertVerbInflRules(corpusName: String, conf: Configuration, repo :  ScalaFile): Boolean = {
  // Should correctly convert good data.
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  val expected = "<conj1><verb>o<1st><sg><pres><indic><act><u>lverbinfl\\.are\\_presind1</u>"
  goodFst.trim ==  expected
}

def testVerbInflRulesFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile): Boolean = {
  // Install inflectional table of data

  val verbData = mkdirs(repo/"datasets"/corpusName/"rules-tables/verbs")
  installVerbRuleTable(verbData)

  val fstFromDir = VerbRulesInstaller.fstForVerbRules(verbData)

  val lines = fstFromDir.split("\n").toVector
  val expected = "$verbinfl$ =  <conj1><verb>o<1st><sg><pres><indic><act><u>lverbinfl\\.are\\_presind1</u>"
  // tidy up
  (repo/"datasets").delete()

  lines(0) == expected
}


def testVerb
StemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  // should correctly convert good data.
  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val goodFst = VerbDataInstaller.verbLineToFst(goodLine)
  val expected = "<u>ag\\.v1</u><u>lexent\\.n2280</u><#>am<verb><conj1>"
  goodFst.trim ==  expected
}
def testVerbStemFstFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {


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
def testVerbStemDataApplied(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  // Install one data file:

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


def testBadNounsInflRulesConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = { false }
def testConvertNounInflRules(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = { false }
def testNounInflRulesFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = { false }

def testBadNounStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = { false }
def testNounStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = { false }
def testNounStemFstFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = { false }
def testNounStemDataApplied(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = { false }


def testNounAcceptor(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = { false }

def testRulesInstaller(corpusName: String, conf: Configuration, repo :  ScalaFile) :  Boolean= {
  // Write some test data in the source work space:
  // Install inflectional table of data
  val verbData = repo/"datasets"/corpusName/"rules-tables/verbs"
  if (!verbData.exists) {mkdirs(verbData)}
  installVerbRuleTable(verbData)

  RulesInstaller(repo/"datasets", repo, corpusName)

  val actualFiles =  (repo/"parsers"/corpusName/"inflection").glob("*.fst")
  val actualSet = actualFiles.map(_.name).toSet
  val expectedSet = Set("verbinfl.fst", "irreginfl.fst", "indeclinfl.fst")

  expectedSet  ==  actualSet
}

def testVerbAcceptor(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
  val projectDir = repo/"parsers"/corpusName

  // 1. Should  return empty string if no data:
  val emptyFst = AcceptorComposer.verbAcceptor(projectDir)
  val emptiedOk = emptyFst.isEmpty

  // 2. Now try after building some data:
  val lexDir = projectDir/"lexica"
  mkdirs(lexDir)
  val verbLexicon= lexDir/"lexicon-verbs.fst"
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  verbLexicon.overwrite(goodFst)

  val acceptorFst = AcceptorComposer.verbAcceptor(projectDir)
  val lines = acceptorFst.split("\n").toVector.filter(_.nonEmpty)
  val expected = "$=verbclass$ = [#verbclass#]"
  (emptiedOk && lines(0) == expected)
}
*/

lazy val posTests = inputKey[Unit]("Unit tests")
posTests in Test := {
  val args: Seq[String] = spaceDelimited("<arg>").parsed

  args.size match {
      //runBuildTests(args(0), conf, baseDirectory.value)
    case 1 => {
      try {
        val confFile = file("conf.properties").toScala
        val conf = Configuration(confFile)

        val f = file(conf.datadir).toScala

        if (f.exists) {
          val corpusName = args(0)
          val baseDir = baseDirectory.value.toScala
          println("\nExecuting tests of build system with settings:\n\tcorpus:          " + corpusName + "\n\tdata source:     " + conf.datadir + "\n\trepository base: " + baseDir + "\n")

          val results = for (t <- testList.filter(_._3 != "pending")) yield {
            val subdirs = (baseDir/"parsers").children.filter(_.isDirectory)
            for (d <- subdirs) {
              d.delete()
            }

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
