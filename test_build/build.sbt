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

  ("Test composing final acceptor acceptor.fst", testMainAcceptorComposer(_, _, _), "pending" ),

  ("Test composing parser", testParserComposer(_, _, _), "pending" ),

  ("Test composing inflection makefile", testInflectionMakefileComposer(_, _, _), "pending" ),
  ("Test composing main makefile", testMainMakefileComposer(_, _, _), "pending" ),

  // Top-level inflectional rules
  ("Test composing inflection.fst", testInflectionComposer(_, _, _), "pending" ),

)


/** Triples of description, function and status. */
def integrationList = {
  List(
    ("Test compiling// FST parser", testFstBuild(_, _, _), "" ),
    ("Test compiling// FST parser", testBuildWithVerb(_, _, _), "" ),
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

/** Install a sample rule file for indeclinables.
*
* @param corpusDir Directory for corpus dataset where file should be installed.
*/
def installIndeclRuleTable(corpusDir: File ): Unit = {
  val goodLine = "StemUrn#LexicalEntity#Stem#PoS"
  val stems = corpusDir / "stems-tables"
  val indeclSource = stems / "indeclinables"
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  //new PrintWriter(testData){write(text); close;}
}

def installIndeclRuleFst(corpusDir:  File) : Unit = {
  val lexica = corpusDir / "lexica"
  if (! lexica.exists) { lexica.mkdir}
  val rulesFst = lexica  / "lexicon-indeclinables.fst"
  val entry = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  //new PrintWriter(rulesFst){write(entry);close;}
}
def installVerbRuleFst(corpusDir:  File) : Unit = {
  /*
  val lexica = corpusDir / "lexica"
  if (! lexica.exists) { lexica.mkdir}
  val rulesFst = lexica  / "lexicon-indeclinables.fst"
  val entry = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  new PrintWriter(rulesFst){write(entry);close;}
  */
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
  //

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

/*
////// Indeclinables
def testBadIndeclDataConvert(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
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
def testIndeclDataConvert(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
  // should correctly convert good data.
  val goodLine = "StemUrn#LexicalEntity#Stem#PoS"
  val goodFst = IndeclDataInstaller.indeclLineToFst(goodLine)
  val expected = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  goodFst ==  expected
}
def testIndeclFstFromDir(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
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

  fail
}
def testIndeclApplied(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
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

def testBadIndeclRulesConvert(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean =  {
  //  Test conversion of delimited text to FST.
  // Should object to bad data
  try {
    val fst = IndeclRulesInstaller.indeclRuleToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}
def testConvertIndeclRules(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean =  {
  // Should correctly convert good data.
  val goodLine = "testdata.rule1#nunc"
  val goodFst = IndeclRulesInstaller.indeclRuleToFst(goodLine)
  val expected = "<nunc><indecl><u>testdata" + "\\" + ".rule1</u>"
  goodFst ==  expected
}

def testIndeclRulesFromDir(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean =
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

def testIndeclAcceptor(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
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
*/

/*
def testApplyIndeclRulesInstall(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
  // install data
  println("Install ")

  val projectDir = repoRoot/"parsers"/corpusName
  installIndeclRuleTable(projectDir)
  Utils.dir(projectDir)
  val lexDir = Utils.dir(projectDir / "lexica")
  val indeclLexicon= lexDir  / "lexicon-indeclinables.fst"


  val dataDir = Utils.dir(repoRoot / s"datasets")
  val corpus = Utils.dir(dataDir / corpusName)
  val rules = Utils.dir(corpus / "rules-tables")
  val indecls = Utils.dir(rules / "indeclinables")
  val testData  = indecls / "madeuptestdata.cex"
  val hdr = "Unparsed header line\n"
  val goodLine = "testdata.rule1#nunc\n"
  new PrintWriter(testData){write(hdr + goodLine); close;}

  println("Indecls dir is " + indecls)
  IndeclRulesInstaller(indecls, indeclLexicon)

  val lines = Source.fromFile(indeclLexicon).getLines.toVector
  println(lines.mkString("\n"))

  false
} */

////// Verbs
/*

def testApplyVerbAcceptor(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
  // install data
  /*
  val lexDir = Utils.dir(projectDir / s"datasets")
  val verbLexicon= lexDir  / "lexicon-verbs.fst"
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  new PrintWriter(verbLexicon){write(goodFst);close;}


  val data =
  VerbRulesInstaller()*/
  false
}
*/

///


/*
///////////   Nouns
def testBadNounStemDataConvert(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testNounStemDataConvert(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testNounStemFstFromDir(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testNounStemDataApplied(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
*/


//def testNounAcceptor(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }

////

/*
/////////// Adjectives
def testBadAdjectiveStemDataConvert(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testAdjectiveStemDataConvert(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testAdjectiveStemFstFromDir(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testAdjectiveStemDataApplied(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testBadAdjectiveInflRulesConvert(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testConvertAdjectiveInflRules(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testAdjectiveInflRulesFromDir(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
def testAdjectiveAcceptor(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= { false }
*/
/////
/*
def installVerbRuleTable(verbsDir:  ScalaFile) : Unit = {
  val verbFile = verbsDir/"madeupdata.cex"
  val goodLine = "RuleUrn#InflectionClasses#Ending#Person#Number#Tense#Mood#Voice\nlverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act\n"
  verbFile.overwrite(goodLine)
}
*/
def testInflectionComposer(corpusName: String, conf: Configuration, repo : ScalaFile) :  Boolean= {
/*
  val verbData = mkdirs(repo/"datasets"/corpusName/"rules-tables/verbs")
  installVerbRuleTable(verbData)

  RulesInstaller(repo/"datasets", repo, corpusName)
  InflectionComposer(repo/"parsers"/corpusName)

  val outputFile = repo/"parsers"/corpusName/"inflection.fst"
  val actualLines = outputFile.lines.toVector.filter(_.nonEmpty)

  // tidy uip
  println("DELETE CORPUS " + repo/"datasets"/corpusName)
  (repo/"datasets"/corpusName).delete()

  val expectedStart  = "$ending$ = " + "\"<" + repoRoot + "/parsers/" + corpusName + "/inflection/indeclinfl.a>\""
  (outputFile.exists && actualLines(3).trim.startsWith(expectedStart) )
  */false
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

  // Install one data file:
  val datasets = repo/"parsers"
  val corpusData = mkdirs(datasets/corpusName)

  //installIndeclRuleFst(corpusData)


  // 1. Should omit indeclinables if not data present.
  val projectDir = repo/"parsers"/corpusName
  AcceptorComposer.composeMainAcceptor(projectDir)
  val acceptor = projectDir/"acceptor.fst"
  val lines = acceptor.lines.toVector.filter(_.nonEmpty)
  val expected = "$acceptor$ = $squashindeclurn$"
  lines(5).trim == expected.trim
}

def testParserComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {
  /*val projectDir = repoRoot / s"parsers/${corpusName}"
  if (!projectDir.exists) {projectDir.mkdir}
  ParserComposer(projectDir)

  val parserFst = projectDir / "latin.fst"
  val lines = "" //Source.fromFile(parserFst).getLines.toVector.filter(_.nonEmpty)

  // tidy up
  //parserFst.delete

  val expected = "%% latin.fst : a Finite State Transducer for ancient latin morphology"
  //lines(0).trim == expected*/
  false
}

def testInflectionMakefileComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {


  val projectDir = mkdirs(repo/"parsers"/corpusName)

  val compiler = conf.fstcompile
  MakefileComposer.composeInflectionMake(projectDir, compiler)

  val inflDir = projectDir/"inflection"
  val mkfile = inflDir/"makefile"

  mkfile.exists
}

def testMainMakefileComposer(corpusName: String, conf: Configuration, repo : ScalaFile) = {

  val projectDir = mkdirs(repo/"parsers"/corpusName)

  // install some data
  //installIndeclRuleFst(projectDir)

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
  */false
}

def testBuildWithVerb(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean = {
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

def testParserOutput(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}

//// Irreg. verbs
/*
def testBadIrregVerbStemDataConvert(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  //  Test conversion of delimited text to FST.
  //  should object to bad data
  try {
    val fst = VerbDataInstaller.verbLineToFst("Not a real line")
    println("Should never have seent this... " + fst)
    false
  } catch {
    case t : Throwable => true
  }
  false
}
def testIrregVerbStemDataConvert(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}
def testIrregVerbStemFstFromDir(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}
def testIrregVerbStemDataApplied(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}


// inflectional rules
def testBadIrregVerbInflRulesConvert(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}
def testConvertIrregVerbInflRules(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}
def testIrregVerbInflRulesFromDir(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}

// acceptor
def testIrregVerbAcceptor(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}
*/


/*(

////// Verbs
def testBadVerbStemDataConvert(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {

}
def testVerbStemDataConvert(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
  // should correctly convert good data.
  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val goodFst = VerbDataInstaller.verbLineToFst(goodLine)
  val expected = "<u>ag\\.v1</u><u>lexent\\.n2280</u><#>am<verb><conj1>"
  goodFst.trim ==  expected
}
def testVerbStemFstFromDir(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
  // Should create FST for all files in a directory
    val goodLine = "ag.v1#lexent.n2280#am#conj1"

  val dataSource = repoRoot / "datasets"
  val corpus = Utils.dir(dataSource / corpusName)
  val stems = Utils.dir(corpus / "stems-tables")
  val verbSource = Utils.dir(stems / "verbs-simplex")
  val testData  = verbSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}

  val fstFromDir = VerbDataInstaller.fstForVerbData(verbSource)

  // Tidy up
  IO.delete(corpus)
  val expected = "<u>ag\\.v1</u><u>lexent\\.n2280</u><#>am<verb><conj1>"
  fstFromDir.trim == expected
}
def testVerbStemDataApplied(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
  // Install one data file:

  val dataSource = repoRoot / "datasets"
  val corpus = Utils.dir(dataSource / corpusName)
  installVerbStemTable(corpus)
  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val stems = Utils.dir(corpus / "stems-tables")
  val verbSource = Utils.dir(stems / "verbs-simplex")
  val testData  = verbSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}


  val workDir = Utils.dir(repoRoot / s"parsers/${corpusName}")
  val lexDir = Utils.dir(workDir / "lexica")
  // Test file copying in apply function
  // Write some test data in the source work space:
  VerbDataInstaller(dataSource, repoRoot, corpusName)

  // check the results:
  val resultFile = repoRoot / s"parsers/${corpusName}/lexica/lexicon-verbs.fst"
  val output  = Source.fromFile(resultFile).getLines.toVector

  // clean up:
  IO.delete( repoRoot / s"parsers/${corpusName}")
  IO.delete( repoRoot / s"datasets/${corpusName}")

  val expected = "<u>ag\\.v1</u><u>lexent\\.n2280</u><#>am<verb><conj1>"
  output(0) == expected
}

def testBadVerbsInflRulesConvert(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean =  {
  //  Test conversion of delimited text to FST.
  // Should object to bad data
  try {
    val fst = VerbRulesInstaller.verbRuleToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}

def testConvertVerbsInflRules(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean =  {
  // Should correctly convert good data.
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  val expected = "<conj1><verb>o<1st><sg><pres><indic><act><u>lverbinfl\\.are\\_presind1</u>"
  goodFst.trim ==  expected
}


def testVerbInflRulesFromDir(corpusName: String, conf: Configuration, repo : ScalaFile) : Boolean =
{
  // Install inflectional table of data
  val corpus = Utils.dir(repoRoot / s"datasets/${corpusName}")
  val rules  = Utils.dir(corpus / "rules-tables")
  val verbSource = Utils.dir(rules / "verbs")
  installVerbRuleTable(corpus)

  val fstFromDir = VerbRulesInstaller.fstForVerbRules(verbSource)
  val lines = fstFromDir.split("\n")
  val expected = "$verbinfl$ =  <conj1><verb>o<1st><sg><pres><indic><act><u>lverbinfl\\.are\\_presind1</u>"

  lines(0) == expected
}


def testVerbAcceptor(corpusName: String, conf: Configuration, repo : ScalaFile):  Boolean = {
  val projectDir = Utils.dir(repoRoot / s"parsers/${corpusName}")

  // 1. Should  return empty string if no data:
  val emptyFst = AcceptorComposer.verbAcceptor(projectDir)
  val emptiedOk = emptyFst.isEmpty

  // 2. Now try after building some data:
  val lexDir = Utils.dir(projectDir / "lexica")
  val verbLexicon= lexDir  / "lexicon-verbs.fst"
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  new PrintWriter(verbLexicon){write(goodFst);close;}

  val acceptorFst = AcceptorComposer.verbAcceptor(projectDir)
  val lines = acceptorFst.split("\n").toVector.filter(_.nonEmpty)
  val expected = "$=verbclass$ = [#verbclass#]"
  (emptiedOk && lines(0) == expected)
}

)
*/

lazy val listEm = inputKey[Unit]("get a list")
listEm in Test := {
  println("Do universe with " + testList)
}

lazy val integrationTests = inputKey[Unit]("Integration tests")
integrationTests in Test := {
    val args: Seq[String] = spaceDelimited("<arg>").parsed
    println("Integration tests " + args)
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
