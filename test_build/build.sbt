import complete.DefaultParsers._
import scala.sys.process._
import scala.io.Source
import java.io.PrintWriter


name := "bldtest"



/** Triples of description, function and status. */
def testList = List(
  // utilities
  ("Test finding build directory", testBuildDirectory(_,_,_), ""),
  ("Test verifying directory", testDirCheck(_,_,_), ""),
  ("Test cleaning build directory", testCleanAll(_,_,_), ""),
  ("Test Corpus object", testCorpusObject(_, _, _), "" ),

  // FST symbol system
  ("Test installing the alphabet", testAlphabetInstall(_, _, _), "" ),
  ("Test composing symbols.fst", testMainSymbolsComposer(_, _, _), "" ),
  ("Test composing files in symbols dir", testSymbolsDir(_, _, _), "" ),
  ("Test composing phonology symbols", testPhonologyComposer(_, _, _), "" ),

  ("Test copying FST for invariant rules", testInvariantFst(_, _, _), "" ),
  //////////////// Indeclinables.
  // Stem data
  ("Test converting bad data to fst for indeclinable", testBadIndeclDataConvert(_, _, _), "" ),
  ("Test converting tabular data to fst for indeclinable", testIndeclDataConvert(_, _, _), "" ),
  ("Test converting files in directorty to fst for indeclinable", testIndeclFstFromDir(_, _, _), "" ),
  ("Test converting apply method for Indeclinable data installed", testIndeclApplied(_, _, _), "" ),
  // Inflectional rules
  ("Test converting bad inflectional rules for indeclinables", testBadIndeclRulesConvert(_, _, _), "" ),
  ("Test converting  inflectional rules for indeclinables", testConvertIndeclRules(_, _, _), "" ),
  ("Test converting inflectional rules for indeclinables from files in dir", testIndeclRulesFromDir(_, _, _), "" ),
  //  Acceptor:
  ("Test writing indeclinables acceptor string", testIndeclAcceptor(_, _, _), "" ),

  //////////////// Verbs.
  // stems
  ("Test converting bad stem data to fst for verbs", testBadVerbStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for verbs", testVerbStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for verbs", testVerbStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for verb stem data installer", testVerbStemDataApplied(_, _, _), "" ),
  // inflectional rules
  ("Test converting bad inflectional rules for verbs", testBadVerbsInflRulesConvert(_, _, _), "" ),
  ("Test converting  inflectional rules for verbs", testConvertVerbsInflRules(_, _, _), "" ),
  ("Test converting  inflectional rules for verbs from files in dir", testVerbInflRulesFromDir(_, _, _), "" ),
  // acceptor
  ("Test writing verbs acceptor string", testVerbAcceptor(_, _, _), "" ),

  //////////////// Nouns.
  // stems
  ("Test converting bad stem data to fst for nouns", testBadNounStemDataConvert(_, _, _), "pending" ),
  ("Test converting stem data to fst for nouns", testNounStemDataConvert(_, _, _), "pending" ),
  ("Test converting stem files in directory to fst for nouns", testNounStemFstFromDir(_, _, _), "pending" ),
  ("Test converting apply method for noun stem data installer", testNounStemDataApplied(_, _, _), "pending" ),

  // inflectional rules
  ("Test converting bad inflectional rules for nouns", testBadNounInflRulesConvert(_, _, _), "pending" ),
  ("Test converting  inflectional rules for nouns", testConvertNounInflRules(_, _, _), "pending" ),
  ("Test converting  inflectional rules for nouns from files in dir", testNounInflRulesFromDir(_, _, _), "pending" ),
  // acceptor
  ("Test writing nouns acceptor string", testNounAcceptor(_, _, _), "pending" ),

  //////////////// Adjectives.
  // stems
  ("Test converting bad stem data to fst for adjectives", testBadAdjectiveStemDataConvert(_, _, _), "pending" ),
  ("Test converting stem data to fst for adjectives", testAdjectiveStemDataConvert(_, _, _), "pending" ),
  ("Test converting stem files in directory to fst for adjectives", testAdjectiveStemFstFromDir(_, _, _), "pending" ),
  ("Test converting apply method for adjectives stem data installer", testAdjectiveStemDataApplied(_, _, _), "pending" ),

  // inflectional rules
  ("Test converting bad inflectional rules for adjectives", testBadAdjectiveInflRulesConvert(_, _, _), "pending" ),
  ("Test converting  inflectional rules for adjectives", testConvertAdjectiveInflRules(_, _, _), "pending" ),
  ("Test converting  inflectional rules for adjectives from files in dir", testAdjectiveInflRulesFromDir(_, _, _), "pending" ),
  // acceptor
  ("Test writing adjectives acceptor string", testAdjectiveAcceptor(_, _, _), "pending" ),




  //////////////// Irreg. verbs.
  // stems
  ("Test converting bad stem data to fst for irregular verbs", testBadIrregVerbStemDataConvert(_, _, _), "pending" ),
  ("Test converting stem data to fst for irregular verbs", testIrregVerbStemDataConvert(_, _, _), "pending" ),
  ("Test converting stem files in directory to fst for irregular verbs", testIrregVerbStemFstFromDir(_, _, _), "pending" ),
  ("Test converting apply method for irregular verbs stem data installer", testIrregVerbStemDataApplied(_, _, _), "pending" ),

  // inflectional rules
  ("Test converting bad inflectional rules for irregular verbs", testBadIrregVerbInflRulesConvert(_, _, _), "pending" ),
  ("Test converting  inflectional rules for irregular verbs", testConvertIrregVerbInflRules(_, _, _), "pending" ),
  ("Test converting  inflectional rules for irregular verbs from files in dir", testIrregVerbInflRulesFromDir(_, _, _), "pending" ),
  // acceptor
  ("Test writing adjectives acceptor string", testIrregVerbAcceptor(_, _, _), "pending" ),


  // Top-level inflectional rules

  ("Test composing inflection.fst", testInflectionComposer(_, _, _), "" ),


  // Top-level acceptors
  ("Test writing union of squashers string", testUnionOfSquashers(_, _, _), "" ),
  ("Test writing top-level acceptor string", testTopLevelAcceptor(_, _, _), "" ),
  ("Test composing final acceptor acceptor.fst", testMainAcceptorComposer(_, _, _), "" ),



  ("Test composing parser", testParserComposer(_, _, _), "" ),

  ("Test composing inflection makefile", testInflectionMakefileComposer(_, _, _), "" ),
  ("Test composing main makefile", testMainMakefileComposer(_, _, _), "" ),



  // do we need these?
  ("Test apply function of acceptor for verbs", testApplyVerbAcceptor(_, _, _), "pending" ),
  ("Test apply function of acceptor for indeclinables", testApplyIndeclRulesInstall(_, _, _), "pending" ),

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



def installVerbRuleFst(corpusDir:  File) : Unit = {
  /*
  val lexica = corpusDir / "lexica"
  if (! lexica.exists) { lexica.mkdir}
  val rulesFst = lexica  / "lexicon-indeclinables.fst"
  val entry = "<u>StemUrn</u><u>LexicalEntity</u>Stem<indecl><PoS>"
  new PrintWriter(rulesFst){write(entry);close;}
  */
}
def installVerbStemTable(corpusDir:  File) : Unit = {
  val stems = Utils.dir(corpusDir / "stems-tables")
  val verbs = Utils.dir(stems / "verbs-simplex")
  val verbFile = verbs / "madeupdata.cex"

  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(verbFile){write(text); close;}
}
def installVerbRuleTable(corpusDir:  File) : Unit = {
  val rules = Utils.dir(corpusDir / "rules-tables")
  val verbs = Utils.dir(rules / "verbs")
  val verbFile = verbs / "madeupdata.cex"
  val goodLine = "RuleUrn#InflectionClasses#Ending#Person#Number#Tense#Mood#Voice\nlverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act\n"

  new PrintWriter(verbFile){write(goodLine); close;}
}
////////////////// Tests //////////////////////////////
//

def testBuildDirectory(corpus: String, conf: Configuration, repoRoot : File): Boolean = {
  val expected = repoRoot / s"parsers/${corpus}"
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


////// Indeclinables
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

def testApplyIndeclRulesInstall(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  // install data
  println("Install ")
/*
  val projectDir = repoRoot / s"parsers/${corpusName}"
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
  */
  false
}

////// Verbs
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
def testVerbStemDataApplied(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
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

def testBadVerbsInflRulesConvert(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =  {
  //  Test conversion of delimited text to FST.
  // Should object to bad data
  try {
    val fst = VerbRulesInstaller.verbRuleToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}

def testConvertVerbsInflRules(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =  {
  // Should correctly convert good data.
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  val expected = "<conj1><verb>o<1st><sg><pres><indic><act><u>lverbinfl\\.are\\_presind1</u>"
  goodFst.trim ==  expected
}


def testVerbInflRulesFromDir(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =
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


def testVerbAcceptor(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
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

def testApplyVerbAcceptor(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
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

///



///////////   Nouns
def testBadNounStemDataConvert(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testNounStemDataConvert(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testNounStemFstFromDir(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testNounStemDataApplied(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testBadNounInflRulesConvert(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testConvertNounInflRules(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testNounInflRulesFromDir(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testNounAcceptor(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }

////


/////////// Adjectives


def testBadAdjectiveStemDataConvert(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testAdjectiveStemDataConvert(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testAdjectiveStemFstFromDir(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testAdjectiveStemDataApplied(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testBadAdjectiveInflRulesConvert(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testConvertAdjectiveInflRules(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testAdjectiveInflRulesFromDir(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }
def testAdjectiveAcceptor(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= { false }

/////


def testInflectionComposer(corpusName: String, conf: Configuration, repoRoot : File) :  Boolean= {
  val goodLine = "testdata.rule1#nunc"
  // Create and install some test data:
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
  /*RulesInstaller(dataSource, repoRoot, corpusName)


  InflectionComposer(repoRoot / s"parsers/${corpusName}")
  val expectedFile = repoRoot / s"parsers/${corpusName}/inflection.fst"
  val lines = Source.fromFile(expectedFile).getLines.toVector.filter(_.nonEmpty)
  val expectedLine  = "$ending$ = " + "\"<" + repoRoot + "/parsers/" + corpusName + "/inflection/indeclinfl.a>\""

  // tidy uip
  IO.delete(corpus)

  (expectedFile.exists && lines(3).trim ==  expectedLine)
*/
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

  // Install one data file:
  val datasets = repoRoot / "parsers"
  val corpusData = datasets / corpusName
  if (! corpusData.exists) {corpusData.mkdir}
  installIndeclRuleFst(corpusData)


  // 1. Should omit indeclinables if not data present.
  val projectDir = repoRoot / s"parsers/${corpusName}"
  AcceptorComposer.composeMainAcceptor(projectDir)
  val acceptor = projectDir / "acceptor.fst"
  val lines = Source.fromFile(acceptor).getLines.toVector.filter(_.nonEmpty)
  val expected = "$acceptor$ = $squashindeclurn$"
  lines(5).trim == expected.trim
}

def testParserComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = repoRoot / s"parsers/${corpusName}"
  if (!projectDir.exists) {projectDir.mkdir}
  ParserComposer(projectDir)

  val parserFst = projectDir / "latin.fst"
  val lines = Source.fromFile(parserFst).getLines.toVector.filter(_.nonEmpty)

  // tidy up
  //parserFst.delete

  val expected = "%% latin.fst : a Finite State Transducer for ancient latin morphology"
  lines(0).trim == expected
}

def testInflectionMakefileComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = repoRoot /s"parsers/${corpusName}"
  if (!projectDir.exists){projectDir.mkdir}
  val compiler = conf.fstcompile
  MakefileComposer.composeInflectionMake(projectDir, compiler)

  val inflDir = projectDir / "inflection"
  val mkfile = inflDir / "makefile"

  mkfile.exists
}
def testMainMakefileComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  // install some data
  val projectDir = repoRoot / s"parsers/${corpusName}"
  if (!projectDir.exists){projectDir.mkdir}
  installIndeclRuleFst(projectDir)

  val compiler = conf.fstcompile
  MakefileComposer.composeMainMake(projectDir, compiler)

  val mkfile = projectDir / "makefile"
  mkfile.exists
}

// test comopiling and executing a final parser
def testFstBuild(corpusName: String, conf: Configuration, repoRoot : File) : Boolean = {
  val cName = "minimum"
  val dataDirectory = repoRoot / "datasets"
  val conf = Configuration("/usr/local/bin/fst-compiler", "/usr/local/bin/fst-infl", "/usr/bin/make")

  val target = repoRoot / s"parsers/${cName}"
  IO.delete(target)
  Utils.dir(target)
  FstCompiler.compile(dataDirectory, repoRoot, cName, conf)

  val parser = repoRoot / "/parsers/minimum/latin.a"
  parser.exists
}

def testBuildWithVerb(corpusName: String, conf: Configuration, repoRoot : File) : Boolean = {
  val cName = "minimum+verb"
  val dataDirectory = repoRoot / "datasets"
  val conf = Configuration("/usr/local/bin/fst-compiler", "/usr/local/bin/fst-infl", "/usr/bin/make")

  val target = repoRoot / s"parsers/${cName}"
  IO.delete(target)
  Utils.dir(target)
  FstCompiler.compile(dataDirectory, repoRoot, cName, conf)

  val parser = repoRoot / "/parsers/minimum+verb/latin.a"
  parser.exists
}

def testParserOutput(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}

//// Irreg. verbs

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

def testInvariantFst(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  val src = baseDir / "fst/inflection"
  false
}

/*(

////// Verbs
def testBadVerbStemDataConvert(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {

}
def testVerbStemDataConvert(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
  // should correctly convert good data.
  val goodLine = "ag.v1#lexent.n2280#am#conj1"
  val goodFst = VerbDataInstaller.verbLineToFst(goodLine)
  val expected = "<u>ag\\.v1</u><u>lexent\\.n2280</u><#>am<verb><conj1>"
  goodFst.trim ==  expected
}
def testVerbStemFstFromDir(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
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
def testVerbStemDataApplied(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
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

def testBadVerbsInflRulesConvert(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =  {
  //  Test conversion of delimited text to FST.
  // Should object to bad data
  try {
    val fst = VerbRulesInstaller.verbRuleToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}

def testConvertVerbsInflRules(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =  {
  // Should correctly convert good data.
  val goodLine = "lverbinfl.are_presind1#conj1#o#1st#sg#pres#indic#act"
  val goodFst = VerbRulesInstaller.verbRuleToFst(goodLine)
  val expected = "<conj1><verb>o<1st><sg><pres><indic><act><u>lverbinfl\\.are\\_presind1</u>"
  goodFst.trim ==  expected
}


def testVerbInflRulesFromDir(corpusName: String, conf: Configuration, repoRoot : File) : Boolean =
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


def testVerbAcceptor(corpusName: String, conf: Configuration, repoRoot : File):  Boolean = {
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

lazy val testIntegration = inputKey[Unit]("Integration tests")
testIntegration in Test := {
    val args: Seq[String] = spaceDelimited("<arg>").parsed
    println("Integration tests " + args)

    args.size match {
      case 1 => {
        val conf = Configuration(file("conf.properties"))
        val f = file(conf.datadir)

        if (f.exists) {
          val corpusName = args(0)
          val baseDir = baseDirectory.value
          println("\nExecuting tests of build system with settings:\n\tcorpus:          " + corpusName + "\n\tdata source:     " + conf.datadir + "\n\trepository base: " + baseDir + "\n")
          val results = for (t <- integrationList.filter(_._3 != "pending")) yield {
            Utils.deleteSubdirs(baseDir / "parsers", false)

            print(t._1 + "...")
            val reslt = t._2(corpusName, conf, baseDir)
            if (reslt) { println ("success.") } else { println("failed.")}
            reslt
          }
          reportResults(results)//,integrationList)

        } else {
          println("Failed.")
          println(s"No configuration file ${conf.datadir} exists.")
        }
      }
      case _ =>  {
        println(s"Wrong number args (${args.size}): ${args}")
        println("Usage: unitTests CORPUS [CONFIG_FILE]")
      }
    }
}

lazy val unitTests = inputKey[Unit]("Unit tests")
unitTests in Test := {
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
          reportResults(results)//, testList)

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
