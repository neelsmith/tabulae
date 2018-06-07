import complete.DefaultParsers._
import scala.sys.process._


import better.files.{File => ScalaFile, _}
import better.files.Dsl._

name := "bldtest"

/** Triples of description, function and status. */
def testList = List(
  // utilities
  ("Test Corpus object", testCorpusObject(_, _, _), "" ),
  // FST symbol system
  ("Test installing the alphabet", testAlphabetInstall(_, _, _), "" ),
  ("Test composing files in symbols dir", testSymbolsDir(_, _, _), "" ),
  ("Test composing symbols.fst", testMainSymbolsComposer(_, _, _), "" ),
  ("Test composing phonology symbols", testPhonologyComposer(_, _, _), "" ),

  // Top-level acceptors
  ("Test empty union of squashers", testEmptySquashers(_, _, _), "" ),
  ("Test writing union of squashers string", testUnionOfSquashers(_, _, _), "" ),
  ("Test writing top-level acceptor string", testTopLevelAcceptor(_, _, _), "" ),
  ("Test composing final acceptor acceptor.fst", testMainAcceptorComposer(_, _, _), "" ),

  ("Test composing inflection.fst", testInflectionComposer(_, _, _), "" ),

  ("Test composing empty parser", testEmptyParserComposer(_, _, _), "" ),
  ("Test composing parser for empty lexica", testParserComposerForEmptyLexica(_, _, _), "" ),

  ("Test composing parser", testParserComposer(_, _, _), "" ),

  ("Test composing inflection makefile for empty directory", testEmptyInflectionMakefileComposer(_, _, _), "" ),
  ("Test composing inflection makefile", testInflectionMakefileComposer(_, _, _), "" ),
  ("Test composing main makefile", testMainMakefileComposer(_, _, _), "" ),


  // These may not be relevant
  ("Test assemblingAcceptorsDir", testAcceptorDirectory(_, _, _), "pending" ),
  ("Test composing makefile with empty acceptors", testMainMakefileComposerEmptyAcceptors(_, _, _), "pending" ),
)


/** Triples of description, function and status. */
def integrationList = {
  List(
    ("Test compiling// FST parser", testFstBuild(_, _, _), "pending" ),
    ("Test compiling// FST parser", testBuildWithVerb(_, _, _), "pending" ),
    ("Test output of FST parser", testParserOutput(_, _, _), "pending" ),
  )
}


/** "s" or no "s"? */
def plural[T] (lst : List[T]) : String = {
  if (lst.size > 1) { "s"} else {""}
}

/** Interpret and display list of results.
*
* @param results List of test results
*/
def reportResults(results: List[Boolean]) : Unit = {
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


def installVerbStemTable(corpusDir:  ScalaFile) : Unit = {

  val stems = corpusDir/"stems-tables"
  val verbs = stems/"verbs-simplex"
  if (! verbs.exists) {mkdirs(verbs)}
  val verbFile = verbs/"madeupdata.cex"

  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val text = s"header line, omitted in parsing\n${goodLine}"
  verbFile.overwrite(text)
}
def installVerbRuleTable(verbsDir:  ScalaFile) : Unit = {
  val verbFile = verbsDir/"madeupdata.cex"
  val goodLine = "RuleUrn#InflectionClasses#Ending#Person#Number#Tense#Mood#Voice\nlverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act\n"
  verbFile.overwrite(goodLine)
}
////////////////// Tests //////////////////////////////
//

// test creating corpus in local workspace
def testCorpusObject(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val src = file"test_build/datasets"
  val corpus =  Corpus(src, repo, corpusName)
  val corpDir = corpus.dir
  val madeOk = corpDir.exists
  val expectedDir = repo/"datasets"/corpusName
  // tidy up
  corpDir.delete()

  madeOk && (corpDir == expectedDir)
}

def testAlphabetInstall(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean = {
  val dataSrc = repo/"datasets"
  BuildComposer.installAlphabet(dataSrc, repo, "minimum")

  val expectedFile = repo/"parsers/minimum/symbols/alphabet.fst"
  val alphabetLines = expectedFile.lines.toVector
  val expectedLine = "#consonant# = bcdfghjklmnpqrstvxz"

  alphabetLines(1) == expectedLine
}

def testMainSymbolsComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = repo/"parsers"/corpusName
  SymbolsComposer.composeMainFile(projectDir)

  val expectedFile = repo/"parsers"/corpusName/"symbols.fst"
  val symbolLines = expectedFile.lines.toVector
  val expectedLine = "% symbols.fst"
  symbolLines(0) == expectedLine
}
def testSymbolsDir(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val targetDir = repo/"parsers"/corpusName/"symbols"
  val src =  file("fst/symbols").toScala

  SymbolsComposer.copyFst(src, targetDir)
  val expectedNames = Set("markup.fst", "phonology.fst", "morphsymbols.fst",	"stemtypes.fst")
  val actualFiles =  targetDir.glob("*.fst").toVector
  expectedNames == actualFiles.map(_.name).toSet
}

def testPhonologyComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = repo/"parsers"/corpusName
  val phono = projectDir/"symbols/phonology.fst"

  // First install raw source.  Phonology file should have unexpanded macro:
  val targetDir = repo/"parsers"/corpusName/"symbols"
  val src =  file("fst/symbols").toScala
  SymbolsComposer.copyFst(src,targetDir)

  val rawLines = phono.lines.toVector
  val expectedRaw = """#include "@workdir@symbols/alphabet.fst""""
  //(rawLines(7))
  // Then rewrite phonology with expanded paths:
  SymbolsComposer.rewritePhonologyFile(phono, projectDir)
  val cookedLines = phono.lines.toVector

  //tidy up
  projectDir.delete()

  val expectedCooked = s"""#include "${projectDir}/symbols/alphabet.fst""""
  (rawLines(7) == expectedRaw && cookedLines(7) == expectedCooked)
}

def testInflectionComposer(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= {
  val verbData = mkdirs(repo/"datasets"/corpusName/"rules-tables/verbs")
  installVerbRuleTable(verbData)

  RulesInstaller(repo/"datasets", repo, corpusName)
  InflectionComposer(repo/"parsers"/corpusName)

  val outputFile = repo/"parsers"/corpusName/"inflection.fst"
  val actualLines = outputFile.lines.toVector.filter(_.nonEmpty)

  // tidy up
  (repo/"datasets"/corpusName).delete()

  val expectedStart  = "$ending$ = " + "\"<" + repo + "/parsers/" + corpusName + "/inflection/indeclinfl.a>\""
  (outputFile.exists && actualLines(3).trim.startsWith(expectedStart) )

}

def testEmptySquashers(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= {
  val corpusDir = mkdirs(repo/"parsers"/corpusName)

  // 1. should throw Exception if no data.
  val noData =  try {
    AcceptorComposer.unionOfSquashers(corpusDir)
    false
  } catch {
    case t: Throwable => true
  }
  noData
}

def testUnionOfSquashers(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= {
  val corpusDir = mkdirs(repo/"parsers"/corpusName)

  // Install some verb stem data.
  val verbData = repo/"datasets"/corpusName/"stems-tables/verbs"
  if (!verbData.exists) {mkdirs(verbData)}
  installVerbStemTable(repo/"datasets"/corpusName)
  DataInstaller(repo/"datasets", repo, corpusName)
  val actual = AcceptorComposer.unionOfSquashers(corpusDir).split("\n").filter(_.nonEmpty).toVector
  val expected  =   "$acceptor$ = $squashverburn$"
  // tidy up:
  (repo/"datasets"/corpusName).delete()
  actual(1).trim == expected
}

def testTopLevelAcceptor(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  // Install one data file:
  val datasets = repo/"parsers"
  val corpusData = mkdirs(datasets/corpusName)

  // Install some verb stem data.
  val verbData = repo/"datasets"/corpusName/"stems-tables/verbs"
  if (!verbData.exists) {mkdirs(verbData)}
  installVerbStemTable(repo/"datasets"/corpusName)
  DataInstaller(repo/"datasets", repo, corpusName)

  val expandedAcceptorFst = AcceptorComposer.topLevelAcceptor(corpusData)
  val lines = expandedAcceptorFst.split("\n").toVector.filter(_.nonEmpty)

  val expected = "$acceptor$ = $squashverburn$"

  // tidy
  corpusData.delete()

  lines(1).trim == expected
}

def testMainAcceptorComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val datasets = repo/"parsers"
  val corpusData = mkdirs(datasets/corpusName)

  // Install some verb stem data.
  val verbData = repo/"datasets"/corpusName/"stems-tables/verbs"
  if (!verbData.exists) {mkdirs(verbData)}
  installVerbStemTable(repo/"datasets"/corpusName)
  DataInstaller(repo/"datasets", repo, corpusName)

  // 1. Should omit indeclinables if not data present.
  val projectDir = repo/"parsers"/corpusName
  AcceptorComposer.composeMainAcceptor(projectDir)
  val acceptor = projectDir/"acceptor.fst"
  val lines = acceptor.lines.toVector.filter(_.nonEmpty)

  // tidy
  (repo/"datasets"/corpusName).delete()

  val expected = "$acceptor$ = $squashverburn$"
  lines(4).trim == expected.trim
}

def testEmptyParserComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = repo/"parsers"/corpusName

  try {
    ParserComposer(projectDir)
    false
  } catch {
    case t: Throwable => true
  }
}

def testParserComposerForEmptyLexica(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = mkdirs(repo/"parsers"/corpusName)
  try {
    ParserComposer(projectDir)
    false
  } catch {
    case t: Throwable => true
  }
}

def testParserComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = repo/"parsers"/corpusName
  if (!projectDir.exists) {mkdirs(projectDir)}

  // Only works if we've installed a lexicon with some data...
  val verbData = repo/"datasets"/corpusName/"stems-tables/verbs"
  if (!verbData.exists) {mkdirs(verbData)}
  installVerbStemTable(repo/"datasets"/corpusName)
  DataInstaller(repo/"datasets", repo, corpusName)

  ParserComposer(projectDir)

  val parserFst = projectDir/"latin.fst"
  val lines = parserFst.lines.toVector.filter(_.nonEmpty)

  // tidy up
  (repo/"datasets"/corpusName).delete()

  val expected = "%% latin.fst : a Finite State Transducer for ancient latin morphology"
  lines(0).trim == expected
}

def testEmptyInflectionMakefileComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = mkdirs(repo/"parsers"/corpusName)
  val compiler = conf.fstcompile
  try {
    MakefileComposer.composeInflectionMake(projectDir, compiler)
    false
  } catch {
    case t: Throwable => true
  }
}

def testInflectionMakefileComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = mkdirs(repo/"parsers"/corpusName)
  val compiler = conf.fstcompile

  // install some inflectional rules;
  val verbData = mkdirs(repo/"datasets"/corpusName/"rules-tables/verbs")
  installVerbRuleTable(verbData)
  RulesInstaller(repo/"datasets", repo, corpusName)

  MakefileComposer.composeInflectionMake(projectDir, compiler)

  val mkfile = projectDir/"inflection/makefile"
  mkfile.exists
}

def testAcceptorDirectory(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = mkdirs(repo/"parsers"/corpusName)

  // install some data
  val lexDir = projectDir/"lexica"
  mkdirs(lexDir)
  val verbLexicon= lexDir/"lexicon-verbs.fst"
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  verbLexicon.overwrite(goodFst)

  AcceptorComposer(repo, corpusName)

  false
}

def testMainMakefileComposerEmptyAcceptors(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = mkdirs(repo/"parsers"/corpusName)
  val compiler = conf.fstcompile
  try {
    MakefileComposer.composeMainMake(projectDir, compiler)
    false
  } catch {
    case t: Throwable => true
  }
}

def testMainMakefileComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  val projectDir = mkdirs(repo/"parsers"/corpusName)

  // install some data
  val lexDir = projectDir/"lexica"
  mkdirs(lexDir)
  val verbLexicon= lexDir/"lexicon-verbs.fst"
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  verbLexicon.overwrite(goodFst)
  val acceptorFst = AcceptorComposer(repo, corpusName)


  val compiler = conf.fstcompile
  MakefileComposer.composeMainMake(projectDir, compiler)

  val mkfile = projectDir/"makefile"
  mkfile.exists
}

// test comopiling and executing a final parser
def testFstBuild(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean = {
  /*
    val cName = "minimum"
    val dataDirectory = repo/"datasets"
    val conf = Configuration("/usr/local/bin/fst-compiler", "/usr/local/bin/fst-infl", "/usr/bin/make")

    val target = repo/"parsers"/cName
    //IO.delete(target)

    FstCompiler.compile(dataDirectory, repo, cName, conf)

    val parser = repoRoot/"/parsers/minimum/latin.a"
    parser.exists
    */
  false
}


def testBuildWithVerb(corpusName: String, conf: Configuration, repo : ScalaFile) = {
/*
  val cName = "minimum+verb"
  val dataDirectory = repo/"datasets"
  val conf = Configuration("/usr/local/bin/fst-compiler", "/usr/local/bin/fst-infl", "/usr/bin/make")

  val target = repoRoot/"parsers"/cName
  //IO.delete(target)
  //Utils.dir(target)
  FstCompiler.compile(dataDirectory, repo, cName, conf)

  val parser = repoRoot/"/parsers/minimum+verb/latin.a"
  parser.exists
  */ false
}

def testParserOutput(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  false
}


lazy val listEm = inputKey[Unit]("get a list")
listEm in Test := {
  println("Do universe with " + testList)
}

lazy val integrationTests = inputKey[Unit]("Integration tests")
integrationTests in Test := {
  val args: Seq[String] = spaceDelimited("<arg>").parsed

  args.size match {
    case 1 => {
      try {
        val confFile = file("conf.properties").toScala
        val conf = Configuration(confFile)
        val f = file(conf.datadir).toScala

        if (f.exists) {
          val corpusName = args(0)
          val baseDir = baseDirectory.value.toScala
          println("\nExecuting tests of build system with settings:" +
            "\n\tcorpus:          " + corpusName +
            "\n\tdata source:     " + conf.datadir +
            "\n\trepository base: " + baseDir +
            "\n"
          )
          val results = for (t <- integrationList.filter(_._3 != "pending")) yield {
            val subdirs = (baseDir/"parsers").children.filter(_.isDirectory)
            for (d <- subdirs) {
              d.delete()
            }

            print(t._1 + "...")
            val reslt = t._2(corpusName, conf, baseDir)
            if (reslt) { println ("success.") } else { println("failed.")}
            reslt
          }
          reportResults(results)

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
      println("Usage: unitTests CORPUS")
    }
  }
}

lazy val unitTests = inputKey[Unit]("Unit tests")
unitTests in Test := {
  val args: Seq[String] = spaceDelimited("<arg>").parsed

  args.size match {
    case 1 => {
      try {
        val confFile = file("conf.properties").toScala
        val conf = Configuration(confFile)
        val f = file(conf.datadir).toScala

        if (f.exists) {
          val corpusName = args(0)
          val baseDir = baseDirectory.value.toScala
          println("\nExecuting tests of build system with settings:" +
            "\n\tcorpus:          " + corpusName +
            "\n\tdata source:     " + conf.datadir +
            "\n\trepository base: " + baseDir +
            "\n"
          )
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
          reportResults(results)

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
      println("Usage: unitTests CORPUS")
    }
  }
}
