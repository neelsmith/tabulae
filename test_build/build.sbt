import complete.DefaultParsers._
import scala.sys.process._
import scala.io.Source
import java.io.PrintWriter


name := "bldtest"

/** Triples of description, function and status. */
def testList = List(
  ("Test finding build directory", testBuildDirectory(_,_,_), ""),
  ("Test verifying directory", testDirCheck(_,_,_), ""),
  ("Test cleaning build directory", testCleanAll(_,_,_), ""),
  ("Test Corpus object", testCorpusObject(_, _, _), "" ),
  ("Test installing data for indeclinables", testIndeclDataInstaller(_, _, _), "" ),
  ("Test installing rules for indeclinables", testIndeclRulesInstaller(_, _, _), "" ),
  ("Test installing the alphabet", testAlphabetInstall(_, _, _), "" ),
  ("Test composing symbols.fst", testMainSymbolsComposer(_, _, _), "" ),
  ("Test composing files in symbols dir", testSymbolsDir(_, _, _), "" ),
)

/** "s" or no "s"? */
def plural[T] (lst : List[T]) : String = {
  if (lst.size > 1) { "s"} else {""}
}

/** Interpret and display list of results.
*
* @param results List of test results
*/
def reportResults(results: List[Boolean]): Unit = {
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

def testBuildDirectory(corpus: String, conf: Configuration, repoRoot : File): Boolean = {
  val expected = repoRoot / s"parsers/${corpus}"
  println("Expected build dir " + expected)
  (Utils.buildDirectory(repoRoot, corpus) == expected)
}

def testDirCheck(corpus: String, conf: Configuration, repoRoot : File) = {
  val corpusDir = Utils.dir(repoRoot / s"parsers/${corpus}")
  (corpusDir.isDirectory && corpusDir.exists)
}

def testCleanAll(corpus: String, conf: Configuration, repoRoot : File) = {
  val workSpace = repoRoot / "parsers"
  val verbose = false
  val initialClean = Utils.deleteSubdirs(workSpace, verbose)
  val examples = List("a","b","c")
  for (ex <- examples) {
    val corpus = workSpace / ex
    corpus.mkdir
  }
  val expected = examples.size

  (Utils.deleteSubdirs(workSpace, verbose).size == expected)
}

// test creating corpus in local workspace
def testCorpusObject(corpusName: String, conf: Configuration, repoRoot : File) = {
  val src = file("test_build/datasets")
  println("Create corpus with settings " + src + ", " + repoRoot + ", " + corpusName)
  val corpus =  Corpus(src, repoRoot, corpusName)
  val corpDir = corpus.dir
  val nameMatches = corpDir.toString == s"test_build/datasets/${corpusName}"
  val madeOk = corpDir.exists
  // tidy up
  corpDir.delete
  madeOk && nameMatches
}

def testIndeclDataInstaller(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {

  //  Test conversion of delimited text to FST.
  // 1:  should object to bad data
  val caughtBadLine = try {
    val fst = IndeclDataInstaller.indeclLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
  // 2: should correctly convert good data.
  val goodLine = "StemUrn#LexicalEntity#Stem#PoS"
  val goodFst = IndeclDataInstaller.indeclLineToFst(goodLine)
  val expected = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  val goodParse =  (goodFst ==  expected)

  // 3: should create FST for all files in a directory
  val dataSource = Utils.dir(file(conf.datadir))
  val corpus = Utils.dir(dataSource / corpusName)
  val stems = Utils.dir(corpus / "stems-tables")
  val indeclSource = Utils.dir(stems / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}

  val fstFromDir = IndeclDataInstaller.fstForIndeclData(indeclSource)
  val readDirOk = fstFromDir == s"${expected}\n"

  // 4.  Test file copying in apply function
  // Write some test data in the source work space:
  IndeclDataInstaller(dataSource, repoRoot, corpusName)

  // check the results:
  val resultFile = repoRoot / s"parsers/${corpusName}/lexica/lexicon-indeclinables.fst"
  val output  = Source.fromFile(resultFile).getLines.toVector
  val outputGood = output(0) == expected

  // clean up:
  testData.delete()

  (caughtBadLine && goodParse && outputGood && readDirOk)
}

def testIndeclRulesInstaller(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =  {
  //  Test conversion of delimited text to FST.
  // 1:  should object to bad data
  val caughtBadLine = try {
    val fst = IndeclRulesInstaller.indeclRuleToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
  // 2: should correctly convert good data.
  val goodLine = "testdata.rule1#nunc"
  val goodFst = IndeclRulesInstaller.indeclRuleToFst(goodLine)
  val expected = "<nunc><indecl><u>testdata" + "\\" + ".rule1</u>"
  val goodParse =  (goodFst ==  expected)

  // 3: should create FST for all files in a directory
  val dataSource = Utils.dir(file(conf.datadir))
  val corpus = Utils.dir(dataSource / corpusName)
  val rules = Utils.dir(corpus / "rules-tables")
  val indeclSource = Utils.dir(rules / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}

  val fstFromDir = IndeclRulesInstaller.fstForIndeclRules(indeclSource)
  val readDirOk = fstFromDir == "$indeclinfl$ = " + expected + "\n\n$indeclinfl$\n"

  // clean up:
  testData.delete()

  (caughtBadLine && goodParse && readDirOk)
}

def testAlphabetInstall(corpusName: String, conf: Configuration, repoRoot : File) : Boolean = {
  val dataSrc = file("./test_build/datasets")


  BuildComposer.installAlphabet(dataSrc, repoRoot, "minimum")
  val workSpace =  repoRoot /  "parsers/minimum"
  val expectedFile = repoRoot / "parsers/minimum/symbols/alphabet.fst"
  val itsAlive = expectedFile.exists

  val alphabet = Source.fromFile(expectedFile).getLines.toVector
  val expectedLine = "#consonant# = bcdfghjklmnpqrstvxz"

  // tidy up
  println("Delete " + workSpace)
  IO.delete(workSpace)


  (itsAlive && alphabet(1) == expectedLine)
}

def testMainSymbolsComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = repoRoot / s"parsers/${corpusName}"
  SymbolsComposer.composeMainFile(projectDir)

  val expectedFile = repoRoot / s"parsers/${corpusName}/symbols.fst"
  val symbols = Source.fromFile(expectedFile).getLines.toVector
  val expectedLine = "% symbols.fst"
  (expectedFile.exists && symbols(0) == expectedLine)
}
def testSymbolsDir(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = repoRoot / s"parsers/${corpusName}"
  val src =  file("./")

  SymbolsComposer.copySecondaryFiles(src, projectDir)
  val expectedNames = Set("markup.fst", "phonology.fst", "morphsymbols.fst",	"stemtypes.fst")
  val actualFiles =  (projectDir / "symbols") ** "*.fst"
  //println(s"Looked in ${projectDir}")
  println("Actually found \n" + actualFiles.toString)
  expectedNames == actualFiles.get.map(_.getName).toSet
}
lazy val testAll = inputKey[Unit]("Test using output of args")
testAll in Test := {
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

    case 2 => {
      try {
        val conf = Configuration(file(args(1)))
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
      println("Usage: allBuildTests CORPUS [CONFIG_FILE]")
    }
  }
}
