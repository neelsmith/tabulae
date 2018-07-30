import complete.DefaultParsers._
import scala.sys.process._

import better.files.{File => ScalaFile, _}
import better.files.Dsl._

name := "irregtest"


/** Triples of description, function and status. */
def testList = List(

  // irregular verbs:
  ("Test converting bad stem data to fst for verbs", testBadIrregVerbStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for irregular verbs", testIrregVerbStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for irregular verbs", testIrregVerbStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for irregular verb stem data installer", testIrregVerbStemDataApplied(_, _, _), "" ),

  //irreg adverbs:
  ("Test converting bad stem data to fst for adverbs", testBadIrregAdvStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for irregular adverbs", testIrregAdvStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for irregular adverbs", testIrregAdvStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for adverb stem data installer", testIrregAdvStemDataApplied(_, _, _), "" ),

  // irreg nouns:
  ("Test converting bad stem data to fst for nouns", testBadIrregNounStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for irregular nouns", testIrregNounStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for irregular nouns", testIrregNounStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for nouns stem data installer", testIrregNounStemDataApplied(_, _, _), "" ),

  // irreg pronouns
  ("Test converting bad stem data to fst for pronouns", testBadIrregPronounStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for irregular pronouns", testIrregPronounStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for irregular pronouns", testIrregPronounStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for pronouns stem data installer", testIrregPronounStemDataApplied(_, _, _), "" ),

  // irreg adjs:
  ("Test converting bad stem data to fst for irregular adjectives", testBadIrregAdjectiveStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for irregular adjectives", testIrregAdjectiveStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for irregular adjectives", testIrregAdjectiveStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for adjectives stem data installer", testIrregAdjectiveStemDataApplied(_, _, _), "" ),


  // irreg infinitives
  ("Test converting bad stem data to fst for infinitives", testBadIrregInfinStemDataConvert(_, _, _), "" ),
  ("Test converting stem data to fst for irregular infinitives", testIrregInfinStemDataConvert(_, _, _), "" ),
  ("Test converting stem files in directory to fst for irregular infinitives", testIrregInfinStemFstFromDir(_, _, _), "" ),
  ("Test converting apply method for irregular infinitives stem data installer", testIrregInfinStemDataApplied(_, _, _), "" ),

  // irreg participles
/*
("Test converting bad stem data to fst for infinitives", testBadIrregInfinStemDataConvert(_, _, _), "" ),
("Test converting stem data to fst for irregular infinitives", testIrregInfinStemDataConvert(_, _, _), "" ),
("Test converting stem files in directory to fst for irregular infinitives", testIrregInfinStemFstFromDir(_, _, _), "" ),
("Test converting apply method for irregular infinitives stem data installer", testIrregInfinStemDataApplied(_, _, _), "" ),

*/
  // irreg gerunds

("Test converting bad stem data to fst for gerunds", testBadIrregGerundStemDataConvert(_, _, _), "" ),
("Test converting stem data to fst for irregular gerunds", testIrregGerundStemDataConvert(_, _, _), "" ),
("Test converting stem files in directory to fst for irregular gerunds", testIrregGerundStemFstFromDir(_, _, _), "pending" ),
("Test converting apply method for irregular infinitives stem data gerunds", testIrregGerundStemDataApplied(_, _, _), "pending" ),

  // irreg gerundives
/*
("Test converting bad stem data to fst for infinitives", testBadIrregInfinStemDataConvert(_, _, _), "" ),
("Test converting stem data to fst for irregular infinitives", testIrregInfinStemDataConvert(_, _, _), "" ),
("Test converting stem files in directory to fst for irregular infinitives", testIrregInfinStemFstFromDir(_, _, _), "" ),
("Test converting apply method for irregular infinitives stem data installer", testIrregInfinStemDataApplied(_, _, _), "" ),

*/
  // irreg supines
/*
("Test converting bad stem data to fst for infinitives", testBadIrregInfinStemDataConvert(_, _, _), "" ),
("Test converting stem data to fst for irregular infinitives", testIrregInfinStemDataConvert(_, _, _), "" ),
("Test converting stem files in directory to fst for irregular infinitives", testIrregInfinStemFstFromDir(_, _, _), "" ),
("Test converting apply method for irregular infinitives stem data installer", testIrregInfinStemDataApplied(_, _, _), "" ),

*/


  ("Test installing stem data for irregular compound verbs", testInstallIrregCompoundVerbs(_, _, _), "pending" ),


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
    for (png <- pending){
      println("\t" + png._1)
    }
    println("\n")
  }
}


////////////////// Tests //////////////////////////////
//

def testInstallIrregCompoundVerbs(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  /*
  val verbsDir = mkdirs(repo/"datasets"/corpusName/"stems-tables/verbs-simplex")
  installVerbStemTable(verbsDir)
  val compoundsDir = mkdirs(repo/"datasets"/corpusName/"stems-tables/verbs-compound")
  installCompoundStemTable(compoundsDir)

  val target = mkdirs("parsers"/corpusName/"lexica")
  val resultFile = target/"lexicon-compoundverbs.fst"
  CompoundVerbDataInstaller(verbsDir, compoundsDir, resultFile)
  val output = resultFile.lines.toVector
  println("READ FROM FILE:\n\n" + output(0) + "\n\n" )

  val expected = "<u>ag\\.comp1</u><u>demolexent\\.1</u><#>peram<verb><conj1>"

  (expected == output(0).trim)
  */
  false
}


/////////  All irregulars

// irreg verbs
def testBadIrregVerbStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  try {
    val fst = IrregVerbDataInstaller.verbLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}
def testIrregVerbStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {

  val goodLine = "ag.irrv1#lexent.n46529#sum#1st#sg#pres#indic#act"
  val goodFst = IrregVerbDataInstaller.verbLineToFst(goodLine)
  val expected = "<u>ag\\.irrv1</u><u>lexent\\.n46529</u><#>sum<1st><sg><pres><indic><act><irregcverb>"
  goodFst.trim ==  expected
}
def testIrregVerbStemFstFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean =  {

    // Should create FST for all files in a directory
    val goodLine = "ag.irrv1#lexent.n46529#sum#1st#sg#pres#indic#act"
    val goodFst = IrregVerbDataInstaller.verbLineToFst(goodLine)

    val verbSource = mkdirs(repo/"datasets"/corpusName/"irregular-stems/verbs")
    val testData = verbSource/"madeuptestdata.cex"
    val text = s"header line, omitted in parsing\n${goodLine}"
    testData.overwrite(text)

    val fstFromDir = IrregVerbDataInstaller.fstForIrregVerbData(verbSource)
    // Tidy up
    (repo/"datasets").delete()
    val expected = "<u>ag\\.irrv1</u><u>lexent\\.n46529</u><#>sum<1st><sg><pres><indic><act><irregcverb>"
    fstFromDir.trim == expected

}
def testIrregVerbStemDataApplied(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean =  {
  val ds = mkdir(repo/"datasets")
  val cdir = mkdir(ds/corpusName)
  val irregDir = mkdir(cdir/"irregular-stems")
  val verbsDir = mkdir(irregDir/"verbs")
  val goodLine = "ag.irrv1#lexent.n46529#sum#1st#sg#pres#indic#act"
  val goodFst = IrregVerbDataInstaller.verbLineToFst(goodLine)
  val testData = verbsDir/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)

  val destDir = mkdirs(repo/"parsers"/corpusName/"lexica")
  // Write some test data in the source work space:
  val resultFile = destDir/"lexicon-irreg-verbs.fst"
  IrregVerbDataInstaller(verbsDir, resultFile)

  // check the results:
  val output = resultFile.lines.toVector

  // clean up:
  (repo/"datasets").delete()

  val expected = "<u>ag\\.irrv1</u><u>lexent\\.n46529</u><#>sum<1st><sg><pres><indic><act><irregcverb>"

  val rslt = output(0) == expected
  rslt
}

// irreg adverbs
def testBadIrregAdvStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  try {
    val fst = IrregAdverbDataInstaller.adverbLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}
def testIrregAdvStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {

  val goodLine = "ag.irradv1#lexent.n31151#non#pos"
  val goodFst = IrregAdverbDataInstaller.adverbLineToFst(goodLine)
  val expected = "<u>ag\\.irradv1</u><u>lexent\\.n31151</u>non<pos><irregadv>"
  goodFst.trim ==  expected

}
def testIrregAdvStemFstFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  // Should create FST for all files in a directory
  val goodLine = "ag.irradv1#lexent.n31151#non#pos"
  val goodFst = IrregAdverbDataInstaller.adverbLineToFst(goodLine)
  val adverbSource = mkdirs(repo/"datasets"/corpusName/"irregular-stems/adverbs")
  val testData = adverbSource/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)

  val fstFromDir = IrregAdverbDataInstaller.fstForIrregAdverbData(adverbSource)
  // Tidy up
  (repo/"datasets").delete()
  val expected = "<u>ag\\.irradv1</u><u>lexent\\.n31151</u>non<pos><irregadv>"
  fstFromDir.trim == expected
}
def testIrregAdvStemDataApplied(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val ds = mkdir(repo/"datasets")
  val cdir = mkdir(ds/corpusName)
  val irregDir = mkdir(cdir/"irregular-stems")
  val adverbsDir = mkdir(irregDir/"adverbs")
  val goodLine = "ag.irradv1#lexent.n31151#non#pos"
  val goodFst = IrregAdverbDataInstaller.adverbLineToFst(goodLine)

  val testData = adverbsDir/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)

  val destDir = mkdirs(repo/"parsers"/corpusName/"lexica")
  // Write some test data in the source work space:
  val resultFile = destDir/"lexicon-irreg-verbs.fst"
  IrregAdverbDataInstaller(adverbsDir, resultFile)

  // check the results:
  val output = resultFile.lines.toVector

  // clean up:
  (repo/"datasets").delete()

  val expected = "<u>ag\\.irradv1</u><u>lexent\\.n31151</u>non<pos><irregadv>"
  val rslt = output(0) == expected
  rslt
}

// irreg nouns
def testBadIrregNounStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  try {
    val fst = IrregNounDataInstaller.nounLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}

def testIrregNounStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irrn1m#lexent.n5575#bos#masc#nom#sg"
  val goodFst = IrregNounDataInstaller.nounLineToFst(goodLine)
  val expected = "<u>ag\\.irrn1m</u><u>lexent\\.n5575</u>bos<masc><nom><sg><irregnoun>"
  goodFst.trim ==  expected
}

def testIrregNounStemFstFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irrn1m#lexent.n5575#bos#masc#nom#sg"
  val goodFst = IrregNounDataInstaller.nounLineToFst(goodLine)

  val nounSource = mkdirs(repo/"datasets"/corpusName/"irregular-stems/nouns")
  val testData = nounSource/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)
  val fstFromDir = IrregNounDataInstaller.fstForIrregNounData(nounSource)
  // Tidy up
  (repo/"datasets").delete()
  val expected = "<u>ag\\.irrn1m</u><u>lexent\\.n5575</u>bos<masc><nom><sg><irregnoun>"
  fstFromDir.trim == expected
}
def testIrregNounStemDataApplied(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val ds = mkdir(repo/"datasets")
  val cdir = mkdir(ds/corpusName)
  val irregDir = mkdir(cdir/"irregular-stems")
  val nounsDir = mkdir(irregDir/"nouns")
  val goodLine = "ag.irrn1m#lexent.n5575#bos#masc#nom#sg"
  val goodFst = IrregNounDataInstaller.nounLineToFst(goodLine)


  val testData = nounsDir/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)

  val destDir = mkdirs(repo/"parsers"/corpusName/"lexica")
  // Write some test data in the source work space:
  val resultFile = destDir/"lexicon-irreg-nouns.fst"
  IrregNounDataInstaller(nounsDir, resultFile)

  // check the results:
  val output = resultFile.lines.toVector

  // clean up:
  (repo/"datasets").delete()

  val expected = "<u>ag\\.irrn1m</u><u>lexent\\.n5575</u>bos<masc><nom><sg><irregnoun>"
  val rslt = output(0) == expected
  rslt
}

// irreg pronouns
def testBadIrregPronounStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  try {
    val fst = IrregPronounDataInstaller.pronounLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}

def testIrregPronounStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irrpron1#lexent.n20640#hic#masc#nom#sg"
  val goodFst = IrregPronounDataInstaller.pronounLineToFst(goodLine)
  val expected = "<u>ag\\.irrpron1</u><u>lexent\\.n20640</u>hic<masc><nom><sg><irregpron>"
  goodFst.trim ==  expected
}

def testIrregPronounStemFstFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irrpron1#lexent.n20640#hic#masc#nom#sg"
  val goodFst = IrregPronounDataInstaller.pronounLineToFst(goodLine)
  val expected = "<u>ag\\.irrpron1</u><u>lexent\\.n20640</u>hic<masc><nom><sg><irregpron>"

  val pronounSource = mkdirs(repo/"datasets"/corpusName/"irregular-stems/pronouns")
  val testData = pronounSource/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)
  val fstFromDir = IrregPronounDataInstaller.fstForIrregPronounData(pronounSource)
  // Tidy up
  (repo/"datasets").delete()

  fstFromDir.trim == expected
}
def testIrregPronounStemDataApplied(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irrpron1#lexent.n20640#hic#masc#nom#sg"
  val goodFst = IrregPronounDataInstaller.pronounLineToFst(goodLine)
  val expected = "<u>ag\\.irrpron1</u><u>lexent\\.n20640</u>hic<masc><nom><sg><irregpron>"


  val ds = mkdir(repo/"datasets")
  val cdir = mkdir(ds/corpusName)
  val irregDir = mkdir(cdir/"irregular-stems")
  val nounsDir = mkdir(irregDir/"pronouns")

  val testData = nounsDir/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)

  val destDir = mkdirs(repo/"parsers"/corpusName/"lexica")
  // Write some test data in the source work space:
  val resultFile = destDir/"lexicon-irreg-pronouns.fst"
  IrregPronounDataInstaller(nounsDir, resultFile)

  // check the results:
  val output = resultFile.lines.toVector

  // clean up:
  (repo/"datasets").delete()

  val rslt = output(0) == expected
  rslt
}


/// irreg adjs
def testBadIrregAdjectiveStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  try {
    val fst = IrregAdjectiveDataInstaller.adjectiveLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}
def testIrregAdjectiveStemDataConvert(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean =  {
  val goodLine = "ag.irradj1#lexent.n48627#totus#masc#nom#sg#pos"
  val goodFst = IrregAdjectiveDataInstaller.adjectiveLineToFst(goodLine)
  val expected = "<u>ag\\.irradj1</u><u>lexent\\.n48627</u>totus<masc><nom><sg><pos><irregadj>"
  goodFst.trim ==  expected
}
def testIrregAdjectiveStemFstFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irradj1#lexent.n48627#totus#masc#nom#sg#pos"
  val goodFst = IrregAdjectiveDataInstaller.adjectiveLineToFst(goodLine)
  val expected = "<u>ag\\.irradj1</u><u>lexent\\.n48627</u>totus<masc><nom><sg><pos><irregadj>"

  val adjSource = mkdirs(repo/"datasets"/corpusName/"irregular-stems/adjectives")
  val testData = adjSource/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)
  val fstFromDir = IrregAdjectiveDataInstaller.fstForIrregAdjectiveData(adjSource)
  // Tidy up
  (repo/"datasets").delete()

  fstFromDir.trim == expected
}
def testIrregAdjectiveStemDataApplied(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
    val goodLine = "ag.irradj1#lexent.n48627#totus#masc#nom#sg#pos"
    val goodFst = IrregAdjectiveDataInstaller.adjectiveLineToFst(goodLine)
    val expected = "<u>ag\\.irradj1</u><u>lexent\\.n48627</u>totus<masc><nom><sg><pos><irregadj>"


    val ds = mkdir(repo/"datasets")
    val cdir = mkdir(ds/corpusName)
    val irregDir = mkdir(cdir/"irregular-stems")
    val nounsDir = mkdir(irregDir/"adjectives")

    val testData = nounsDir/"madeuptestdata.cex"
    val text = s"header line, omitted in parsing\n${goodLine}"
    testData.overwrite(text)

    val destDir = mkdirs(repo/"parsers"/corpusName/"lexica")
    // Write some test data in the source work space:
    val resultFile = destDir/"lexicon-irreg-adjectives.fst"
    IrregAdjectiveDataInstaller(nounsDir, resultFile)

    // check the results:
    val output = resultFile.lines.toVector

    // clean up:
    (repo/"datasets").delete()

    val rslt = output(0) == expected
    rslt
}



// inifintive verb forms
def testBadIrregInfinStemDataConvert (corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  try {
    val fst = IrregInfinitiveDataInstaller.infinitiveLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}
def testIrregInfinStemDataConvert (corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irrinf1#lexent.n46529#esse#pres#act"
  val goodFst = IrregInfinitiveDataInstaller.infinitiveLineToFst(goodLine)
  val expected = "<u>ag\\.irrinf1</u><u>lexent\\.n46529</u>esse<pres><act><irreginfin>"
  goodFst.trim ==  expected
}

def testIrregInfinStemFstFromDir (corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irrinf1#lexent.n46529#esse#pres#act"
  val goodFst = IrregInfinitiveDataInstaller.infinitiveLineToFst(goodLine)
  val expected = "<u>ag\\.irrinf1</u><u>lexent\\.n46529</u>esse<pres><act><irreginfin>"

  val infSource = mkdirs(repo/"datasets"/corpusName/"irregular-stems/infinitives")
  val testData = infSource/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)

  val fstFromDir = IrregInfinitiveDataInstaller.fstForIrregInfinitiveData(infSource)
  // Tidy up
  (repo/"datasets").delete()

  fstFromDir.trim == expected

}
def testIrregInfinStemDataApplied (corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "ag.irrinf1#lexent.n46529#esse#pres#act"
  val goodFst = IrregInfinitiveDataInstaller.infinitiveLineToFst(goodLine)
  val expected = "<u>ag\\.irrinf1</u><u>lexent\\.n46529</u>esse<pres><act><irreginfin>"


  val ds = mkdir(repo/"datasets")
  val cdir = mkdir(ds/corpusName)
  val irregDir = mkdir(cdir/"irregular-stems")
  val infinsDir = mkdir(irregDir/"inifinitives")

  val testData = infinsDir/"madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  testData.overwrite(text)

  val destDir = mkdirs(repo/"parsers"/corpusName/"lexica")
  // Write some test data in the source work space:
  val resultFile = destDir/"lexicon-irreg-infinitives.fst"
  IrregInfinitiveDataInstaller(infinsDir, resultFile)

  // check the results:
  val output = resultFile.lines.toVector

  // clean up:
  (repo/"datasets").delete()

  val rslt = output(0) == expected
  rslt
}


// gerunds

def testBadIrregGerundStemDataConvert (corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {

  try {
    val fst = IrregGerundDataInstaller.gerundLineToFst("Not a real line")
    false
  } catch {
    case t : Throwable => true
  }
}
def testIrregGerundStemDataConvert (corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  val goodLine = "proof.irrigrd1#lexent.n14599#dandi#gen"
  val goodFst = IrregGerundDataInstaller.gerundLineToFst(goodLine)
  val expected = "<u>proof\\.irrigrd1</u><u>lexent\\.n14599</u>dandi<gen><irreggrnd>"
  //val expected = "<u>ag\\.irrinf1</u><u>lexent\\.n46529</u>esse<pres><act><irreginfin>"
  println("EXP/ACTUAL\n" + expected + "\n" + goodFst.trim)
  goodFst.trim ==  expected
}
def  testIrregGerundStemFstFromDir(corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  false
}
def testIrregGerundStemDataApplied (corpusName: String, conf: Configuration, repo :  ScalaFile):  Boolean = {
  false
}

lazy val irregTests = inputKey[Unit]("Unit tests")
irregTests in Test := {
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
