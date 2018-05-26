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

  ("Test installing the alphabet", testAlphabetInstall(_, _, _), "" ),
  ("Test composing symbols.fst", testMainSymbolsComposer(_, _, _), "" ),
  ("Test composing files in symbols dir", testSymbolsDir(_, _, _), "" ),
  ("Test composing phonology symbols", testPhonologyComposer(_, _, _), "" ),

  ("Test converting bad data to fst for indeclinable", testBadIndeclDataConvert(_, _, _), "" ),
  ("Test converting tabular data to fst for indeclinable", testIndeclDataConvert(_, _, _), "" ),
  ("Test converting files in directorty to fst for indeclinable", testIndeclFstFromDir(_, _, _), "" ),
  ("Test converting apply method for Indeclinable data installed", testIndeclApplied(_, _, _), "" ),

  ("Test converting bad rules for indeclinables", testBadIndeclRulesConvert(_, _, _), "" ),
  ("Test converting  rules for indeclinables", testConvertIndeclRules(_, _, _), "" ),
  ("Test converting  rules for indeclinables from files in dir", testIndeclRulesFromDir(_, _, _), "" ),
  ("Test composing all rules via RulesInstaller", testRulesInstaller(_, _, _), "" ),
  ("Test composing inflection.fst", testInflectionComposer(_, _, _), "" ),

  ("Test writing indeclinables acceptor string", testIndeclAcceptor(_, _, _), "" ),

  ("Test writing union of squashers string", testUnionOfSquashers(_, _, _), "" ),
  ("Test writing top-level acceptor string", testTopLevelAcceptor(_, _, _), "" ),
  ("Test composing final acceptor acceptor.fst", testMainAcceptorComposer(_, _, _), "pending" ),
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

/** Install a sample rule file for indeclinables.
*
* @param corpusDir Directory for corpus dataset where file should be installed.
*/
def installIndeclRuleTable(corpusDir: File ): Unit = {
  val goodLine = "StemUrn#LexicalEntity#Stem#PoS"
  val stems = Utils.dir(corpusDir / "stems-tables")
  val indeclSource = Utils.dir(stems / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}
}


def installIndeclRuleFst(corpusDir:  File) : Unit = {
  val lexica = corpusDir / "lexica"
  if (! lexica.exists) { lexica.mkdir}
  val rulesFst = lexica  / "lexicon-indeclinables.fst"
  val entry = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  new PrintWriter(rulesFst){write(entry);close;}
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
  val corpus =  Corpus(src, repoRoot, corpusName)
  val corpDir = corpus.dir
  val nameMatches = corpDir.toString == s"test_build/datasets/${corpusName}"
  val madeOk = corpDir.exists
  // tidy up
  corpDir.delete
  madeOk && nameMatches
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
  expectedNames == actualFiles.get.map(_.getName).toSet
}

def testPhonologyComposer(corpusName: String, conf: Configuration, repoRoot : File) = {

  val projectDir = file(s"test_build/parsers/${corpusName}")
  val phono = projectDir / "symbols/phonology.fst"

  // First install raw source.  Phonology file should have unexpanded macro:
  SymbolsComposer.copySecondaryFiles(file( "fst/symbols"), projectDir / "symbols")

  val rawLines = Source.fromFile(phono).getLines.toVector
  val expectedRaw = """#include "@workdir@symbols/alphabet.fst""""
  (rawLines(7))
  // Then rewrite phonology with expanded paths:
  SymbolsComposer.rewritePhonologyFile(phono, projectDir)
  val cookedLines = Source.fromFile(phono).getLines.toVector

  //tidy up
  //IO.delete(projectDir)

  val expectedCooked = s"""#include "${projectDir}/symbols/alphabet.fst""""
  (rawLines(7) == expectedRaw && cookedLines(7) == expectedCooked)
}

def testBadIndeclDataConvert(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  //  Test conversion of delimited text to FST.
  //  should object to bad data
  try {
    val fst = IndeclDataInstaller.indeclLineToFst("Not a real line")
    println("Should never have seent this... " + fst)
    false
  } catch {
    case t : Throwable => true
  }
}
def testIndeclDataConvert(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  // should correctly convert good data.
  val goodLine = "StemUrn#LexicalEntity#Stem#PoS"
  val goodFst = IndeclDataInstaller.indeclLineToFst(goodLine)
  val expected = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  goodFst ==  expected
}
def testIndeclFstFromDir(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  // Should create FST for all files in a directory
  val goodLine = "StemUrn#LexicalEntity#Stem#PoS"

  val dataSource = file ("./test_build/datasets")
  val corpus = Utils.dir(dataSource / corpusName)
  val stems = Utils.dir(corpus / "stems-tables")
  val indeclSource = Utils.dir(stems / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}

  val fstFromDir = IndeclDataInstaller.fstForIndeclData(indeclSource)
  // Tidy up
  IO.delete(corpus)
  val expected = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  fstFromDir == s"${expected}\n"
}
def testIndeclApplied(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  // Install one data file:
  val goodLine = "StemUrn#LexicalEntity#Stem#PoS"
  val dataSource = file ("./test_build/datasets")
  val corpus = Utils.dir(dataSource / corpusName)
  val stems = Utils.dir(corpus / "stems-tables")
  val indeclSource = Utils.dir(stems / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}

  // Test file copying in apply function
  // Write some test data in the source work space:
  val workSpace  = file("./test_build")
  IndeclDataInstaller(dataSource, workSpace, corpusName)

  // check the results:
  val resultFile = workSpace / s"parsers/${corpusName}/lexica/lexicon-indeclinables.fst"
  val output  = Source.fromFile(resultFile).getLines.toVector

  // clean up:
  IO.delete( workSpace / s"parsers/${corpusName}")
  IO.delete( workSpace / s"datasets/${corpusName}")

  val expected = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  output(0) == expected
}

def testBadIndeclRulesConvert(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =  {
  //  Test conversion of delimited text to FST.
  // Should object to bad data
  try {
    val fst = IndeclRulesInstaller.indeclRuleToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}
def testConvertIndeclRules(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =  {
  // Should correctly convert good data.
  val goodLine = "testdata.rule1#nunc"
  val goodFst = IndeclRulesInstaller.indeclRuleToFst(goodLine)
  val expected = "<nunc><indecl><u>testdata" + "\\" + ".rule1</u>"
  goodFst ==  expected
}

def testIndeclRulesFromDir(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =
{
  val goodLine = "testdata.rule1#nunc"
  val dataSource = file ("./test_build/datasets")
  val corpus = Utils.dir(dataSource / corpusName)
  val stems = Utils.dir(corpus / "rules-tables")
  val indeclSource = Utils.dir(stems / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}

  val expected = "<nunc><indecl><u>testdata" + "\\" + ".rule1</u>"
  val fstFromDir = IndeclRulesInstaller.fstForIndeclRules(indeclSource)
  val readDirOk = fstFromDir == "$indeclinfl$ = " + expected + "\n\n$indeclinfl$\n"

  // clean up:
  IO.delete(corpus)

  readDirOk
}

def testRulesInstaller(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= {
  // Write some test data to work with:
  val goodLine = "testdata.rule1#nunc"
  val dataSource = file ("./test_build/datasets")
  val corpus = Utils.dir(dataSource / corpusName)
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
  expectedSet  ==  actualSet
}

def testInflectionComposer(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= {
  val goodLine = "testdata.rule1#nunc"
  // Create and install some test data:
  val dataSource = repoRoot / "datasets"
  val corpus = Utils.dir(dataSource / corpusName)
  val stems = Utils.dir(corpus / "rules-tables")
  val indeclSource = Utils.dir(stems / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}
  RulesInstaller(dataSource, repoRoot, corpusName)

  //
  InflectionComposer(repoRoot / s"parsers/${corpusName}")
  val expectedFile = repoRoot / s"parsers/${corpusName}/inflection.fst"
  val lines = Source.fromFile(expectedFile).getLines.toVector.filter(_.nonEmpty)
  val expectedLine  = "$ending$ = " + "\"<" + repoRoot + "/parsers/" + corpusName + "/inflection/indeclinfl.a>\""

  // tidy uip
  IO.delete(corpus)

  (expectedFile.exists && lines(3).trim ==  expectedLine)

}
def testIndeclAcceptor(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  val projectDir = repoRoot / s"parsers/${corpusName}"
  Utils.dir(projectDir)

  // 1. Should  return empty string if no data:
  val emptyFst = AcceptorComposer.indeclAcceptor(projectDir)
  val emptiedOk = emptyFst.isEmpty

  // 2. Now try after building some data:
  val lexDir = Utils.dir(projectDir / "lexica")
  val indeclLexicon= lexDir  / "lexicon-indeclinables.fst"
  val goodLine = "testdata.rule1#nunc"
  val goodFst = IndeclRulesInstaller.indeclRuleToFst(goodLine)
  new PrintWriter(indeclLexicon){write(goodFst);close;}

  val indeclFst = AcceptorComposer.indeclAcceptor(projectDir)
  val lines = indeclFst.split("\n").toVector.filter(_.nonEmpty)
  val expected = "% Indeclinable form acceptor:"

  (emptiedOk && lines(0) == expected)
}

def testApplyIndeclAcceptor(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  // install data
  val projectDir = repoRoot / s"parsers/${corpusName}"
  Utils.dir(projectDir)
  val lexDir = Utils.dir(projectDir / "lexica")
  val indeclLexicon= lexDir  / "lexicon-indeclinables.fst"
  val goodLine = "testdata.rule1#nunc"
  val goodFst = IndeclRulesInstaller.indeclRuleToFst(goodLine)
  new PrintWriter(indeclLexicon){write(goodFst);close;}

  //IndeclRulesInstaller
  false
}

def testUnionOfSquashers(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= {
  val corpusDir = repoRoot / s"parsers/${corpusName}"
  if (! corpusDir.exists) {corpusDir.mkdir}

  // 1. should throw Exception if no data.
  val noData =  try {
    AcceptorComposer.unionOfSquashers(corpusDir)
    false
  } catch {
    case t: Throwable => true
  }
  // 2. Install some data.
  installIndeclRuleFst(corpusDir)
  val actual = AcceptorComposer.unionOfSquashers(corpusDir).split("\n").filter(_.nonEmpty)
  val expected  =   "$acceptor$ = $squashindeclurn$"

  (noData && actual(1).trim == expected)
}


def testTopLevelAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
  // Install one data file:
  val datasets = repoRoot / "parsers"
  val corpusData = datasets / corpusName
  if (! corpusData.exists) {corpusData.mkdir}
  installIndeclRuleFst(corpusData)

  val expandedAcceptorFst = AcceptorComposer.topLevelAcceptor(corpusData)
  val lines = expandedAcceptorFst.split("\n").toVector.filter(_.nonEmpty)

  val expected = "$acceptor$ = $squashindeclurn$"

  // tidy
  IO.delete(corpusData)
  lines(1).trim == expected
}

def testMainAcceptorComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = file(s"parsers/${corpusName}")

  // 1. Should omit indeclinables if not data present.
  AcceptorComposer.composeMainAcceptor(projectDir)
  val acceptor = projectDir / "acceptor.fst"
  val lines = Source.fromFile(acceptor).getLines.toVector.filter(_.nonEmpty)
  val expected = "$acceptor$ = $verb_pipeline$"
  val emptyOk = lines(4).trim == expected.trim

  // 2. Should include indeclinables if data are present.
  val lexica = projectDir / "lexica"
  Utils.dir(lexica)
  val indeclLexicon= lexica  / "lexicon-indeclinables.fst"
  val goodLine = "testdata.rule1#nunc"
  val goodFst = IndeclRulesInstaller.indeclRuleToFst(goodLine)
  new PrintWriter(indeclLexicon){write(goodFst);close;}

  AcceptorComposer.composeMainAcceptor(projectDir)
  val lines2 = Source.fromFile(acceptor).getLines.toVector.filter(_.nonEmpty)
  val expected2 = "$=indeclclass$ = [#indeclclass#]"
  val dataOk = expected2.trim == lines2(2).trim


  emptyOk && dataOk
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
