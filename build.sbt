import complete.DefaultParsers._
import scala.sys.process._
import java.io.File
import java.io.PrintWriter
import scala.io.Source




lazy val root = (project in file(".")).
    settings(
      name := "tabulae",
      organization := "edu.holycross.shot",
      version := "0.0.1",
      scalaVersion := "2.12.4",
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),
      resolvers += Resolver.jcenterRepo,
      resolvers += Resolver.bintrayRepo("neelsmith", "maven"),
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.0.1" % "test",

        "edu.holycross.shot.cite" %% "xcite" % "3.3.0"
      ),

      tutTargetDirectory := file("docs"),
      tutSourceDirectory := file("src/main/tut"),

      fst := buildFst.evaluated,
      corpus := corpusTemplateImpl.evaluated,
      utils := utilsImpl.evaluated,
      cleanAll := cleanAllImpl.value//,
      //mdebug := currentTest.value
    ).enablePlugins(TutPlugin)

lazy val fst = inputKey[Unit]("Compile complete FST system for a named corpus")
lazy val corpus = inputKey[Unit]("Generate data directory hierarchy for a new named corpus")
lazy val cleanAll = taskKey[Vector[String]]("Delete all compiled parsers")
lazy val utils = inputKey[Unit]("Build utility transducers for a named corpus")

/*
lazy val mdebug = taskKey[Unit]("Run temporary build tests")
def currentTest: Def.Initialize[Task[Unit]] = Def.task {
  val bd = baseDirectory.value
  val buildDirectory = bd / s"parsers/${corpus}"

  val configFile = file("configs/horace.properties")
  val conf = Configuration(configFile)

  println("Conf is " + conf + " from config file " + configFile)

  val dataDirectory = if (conf.datadir.head == '/') { file(conf.datadir)} else { bd / "datasets" }
  println("Data reictory from " + conf.datadir + " == "+ dataDirectory)

  val corp = "h5"
  // Compose makefiles and higher-order FST for build system
  BuildComposer(dataDirectory, bd, corp, conf.fstcompile)

  /*
  val bd = baseDirectory.value
  val buildDirectory = bd / s"parsers/${corpus}"
  println(buildDirectory)

  val  configFile = "configs/horace.properties"
  val conf = Configuration(file(configFile))
  println("Conf is " + conf + " from config file " + configFile)

  val srcDir = if (conf.datadir.head == '/') {
    file(conf.datadir)
  } else {
    bd / "datasets"
  }
  val corps = "h4"
  println(s"Install data witb src, repo, corpus \t${srcDir}\n \t${baseDirectory.value}\n\t${corps}\n")
  DataInstaller(srcDir, baseDirectory.value, corps)
  */
}
*/

/** Delete all subdirectories of a given directory.
* Return names of deleted diretories.
*
* @param dir Directory to empty out.
*/
def deleteSubdirs(dir: File, verbose: Boolean = true): Vector[String] = {
  val filesVector = dir.listFiles.toVector
  val deleted = for (f <- filesVector) yield {
    if (f.exists && f.isDirectory) {
      if (verbose) { println("\tdeleting " + f) } else {}
      IO.delete(f)
      f.toString
    } else {
      ""
    }
  }
  deleted.filter(_.nonEmpty)
}



// Delete all compiled parsers
lazy val cleanAllImpl: Def.Initialize[Task[Vector[String]]] = Def.task {
  val parserDir = baseDirectory.value / "parsers"
  deleteSubdirs(parserDir)
}



// Generate data directory hierarchy for a new named corpus.
// Writes output to ... depends on params given.
lazy val corpusTemplateImpl = Def.inputTaskDyn {
  val bdFile = baseDirectory.value
  val args = spaceDelimited("corpus>").parsed
  println(s"TEMPLATE FROM ${args} of size ${args.size}")
  args.size match {
    case 1 => {
      val destDir = baseDirectory.value / s"datasets/${args.head}"
      Def.task {
        val srcDir = baseDirectory.value / "datatemplate"
        println("\nCreate directory tree for new corpus " + args.head + " in " + destDir + "\n")
        DataTemplate(srcDir, destDir)
        println("\n\nDone.  Template is in " + destDir)
      }
    }

    case 2 => {
      def conf = Configuration(file(args(1)))
      println("CONFIG FROM " + conf)
      val destDir = if (conf.datadir.head == '/') {
        val configuredBase = new File(conf.datadir)

        val configuredDest = configuredBase / args(0)
        println("configurd destdir "+ configuredDest)
        configuredDest
      } else {
        bdFile / "datasets"
      }

      Def.task {
        UtilsInstaller(baseDirectory.value, args.head,conf)
      }
    }

    case _ => {
      println("\nWrong number of parameters.")
      templateUsage
    }
  }
}



def templateUsage: Def.Initialize[Task[Unit]] = Def.task {
  println("\n\tUsage: corpu CORPUSNAME [CONFIGFILE]\n")
  //println("\t-r option = replace (delete) existing dataset\n")
}



lazy val utilsImpl = Def.inputTaskDyn {

  val args = spaceDelimited("corpus>").parsed
  val bdFile = baseDirectory.value

  args.size match {
    case 1 => {
      def conf = Configuration(file("config.properties"))
      Def.task {
        UtilsInstaller(bdFile, args.head, conf)
      }
    }

    case 2 => {

      def conf = Configuration(file(args(1)))
      Def.task {
        UtilsInstaller(bdFile, args.head, conf)
      }

    }
  }
}


// Dynamically creates task to build parser by
// successively invoking tasks that take parameters.
//
// Can be invoked interactively either with
//   fst CORPUS
// or
//   fst CORPUS CONFIG_FILE
//
// CONFIG_FILE must be relative to project's base directory.
//
lazy val buildFst = Def.inputTaskDyn {
  val bdFile= baseDirectory.value
  val args = spaceDelimited("corpus>").parsed
  args.size match {
    case 1 => {
      val config =  bdFile / "config.properties"
      if (! config.exists()) {
        error("Configuration file " + config + " does not exist.\n")
      } else {
        val conf = Configuration(config)
        fstCompile(args.head,config)
      }
    }

    case 2 => {
      val confFile = bdFile / args(1)
      if (! confFile.exists()) {
        error("Configuration file " + confFile + " does not exist.\n")

      } else {
        println("\nCompile corpus " + args.head ) + " using configuration file " + confFile
        fstCompile(args.head, confFile)
      }
    }

    case _ => {
      println("Wrong number of parameters.")
      usage
    }
  }
}

def usage: Def.Initialize[Task[Unit]] = Def.task {
  println("\n\tUsage: fst CORPUS [CONFIGFILE] \n")
}

def error(msg: String): Def.Initialize[Task[Unit]] = Def.task {
  println(s"\n\tError: {$msg}\n")
}


// Compile FST parser
def fstCompile(corpus : String, configFile: File) : Def.Initialize[Task[Unit]] = Def.task {
  val bd = baseDirectory.value
  val buildDirectory = bd / s"parsers/${corpus}"
  val conf = Configuration(configFile)

  println("Conf is " + conf + " from config file " + configFile)

  val dataDirectory = if (conf.datadir.head == '/') { file(conf.datadir)} else { bd / "datasets" }
  println("Data reictory from " + conf.datadir + " == "+ dataDirectory)


  // Install data and rules, converting tabular data to FST
  DataInstaller(dataDirectory, bd, corpus)
  RulesInstaller(dataDirectory, bd, corpus)

  // Compose makefiles and higher-order FST for build system
  BuildComposer(dataDirectory, bd, corpus, conf.fstcompile)

  // Build it!
  val inflMakefile = buildDirectory / "inflection/makefile"
  val makeInfl = s"${conf.make} -f ${inflMakefile}"
  makeInfl !

  val makefile = buildDirectory / "makefile"
  val doit = s"${conf.make} -f ${makefile}"
  doit !
}


// Utility tasks

def buildDirectory(repoRoot: File , corpus: String) = {
  repoRoot / s"parsers/${corpus}"
}

////////////////////////////////////////////////////////////////
//
// Testing the build system

def testList = List(

  ("Test finding build directory", testBuildDirectory(_,_,_), ""),
  ("Test verifying directory", testDirCheck(_,_,_), ""),
  ("Test cleaning build directory", testCleanAll(_,_,_), ""),

  ("Test Configuration", testConfiguration(_, _, _), "pending" ),
  ("Test Corpus object", testCorpusObject(_, _, _), "" ),


  ("Test IndeclDataInstaller", testIndeclDataInstaller(_, _, _), "" ),
  ("Test IndeclRulesInstaller", testIndeclRulesInstaller(_, _, _), "" ),

  ("Test making Corpus template", testCorpusTemplate(_, _, _), "pending" ) /*,



  ("Test NounDataInstaller", testNounDataInstaller(_, _, _), "pending" ),
  ("Test VerbDataInstaller", testVerbDataInstaller(_, _, _), "pending" ),

  ("Test DataTemplate", testDataTemplate(_, _, _), "pending" ),



  ("Test NounRulesInstaller", testNounRulesInstaller(_, _, _), "pending" ),
  ("Test VerbRulesInstaller", testVerbRulesInstaller(_, _, _), "pending" ),

  ("Test RulesInstaller", testRulesInstaller(_, _, _), "pending" ),

  ("Test SymbolsComposer", testSymbolsComposer(_, _, _), "pending" ),
  ("Test InflectionComposer", testInflectionComposer(_, _, _), "pending" ),
  ("Test MakefileComposer", testMakefileComposer(_, _, _), "pending" ),
  ("Test ParserComposer", testParserComposer(_, _, _), "pending" ),

  ("Test AcceptorComposer", testAcceptorComposer(_, _, _), "pending" ),
  ("Test BuildComposer", testBuildComposer(_, _, _), "pending" ),


  ("Test compiling FST", testFstBuild(_, _, _), "pending" ),
  ("Test compiling utilities", testUtilsBuild(_, _, _), "pending" )
  */
)
def testBuildDirectory(corpus: String, conf: Configuration, repoRoot : File) = {
  val expected = repoRoot / s"parsers/${corpus}"
  (buildDirectory(repoRoot, corpus) == expected)
}


def testDirCheck(corpus: String, conf: Configuration, repoRoot : File) = {
  val corpusDir = DataInstaller.dir(repoRoot / s"parsers/${corpus}")
  (corpusDir.isDirectory && corpusDir.exists)
}

def testCleanAll(corpus: String, conf: Configuration, repoRoot : File) = {

  val workSpace = repoRoot / "parsers"
  val verbose = false
  val initialClean = deleteSubdirs(workSpace, verbose)
  val examples = List("a","b","c")
  for (ex <- examples) {
    val corpus = workSpace / ex
    corpus.mkdir
  }
  val expected = examples.size

  (deleteSubdirs(workSpace, verbose).size == expected)
}

def testCorpusObject(corpusName: String, conf: Configuration, repoRoot : File) = {
  val src = file(conf.datadir)
  val corpus =  Corpus(src, repoRoot, corpusName)
  (corpus.dir.exists)
}

def testConfiguration(corpus: String, conf: Configuration, repoRoot : File) = {
  println("Test configuration object")
  // Should throw Exception if any of these don't exist
//fstcompile: String, fstinfl: String, make: String, datadir
  false
}





def testIndeclDataInstaller(corpusName: String, conf: Configuration, repoRoot : File) = {

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
  val dataSource = DataInstaller.dir(file(conf.datadir))
  val corpus = DataInstaller.dir(dataSource / corpusName)
  val stems = DataInstaller.dir(corpus / "stems-tables")
  val indeclSource = DataInstaller.dir(stems / "indeclinables")
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
  val dataSource = DataInstaller.dir(file(conf.datadir))
  val corpus = DataInstaller.dir(dataSource / corpusName)
  val rules = DataInstaller.dir(corpus / "rules-tables")
  val indeclSource = DataInstaller.dir(rules / "indeclinables")
  val testData  = indeclSource / "madeuptestdata.cex"
  val text = s"header line, omitted in parsing\n${goodLine}"
  new PrintWriter(testData){write(text); close;}

  val fstFromDir = IndeclRulesInstaller.fstForIndeclRules(indeclSource)
  val readDirOk = fstFromDir == "$indeclinfl$ = " + expected + "\n\n$indeclinfl$\n"

  // clean up:
  testData.delete()

  (caughtBadLine && goodParse && readDirOk)
}

def testCorpusTemplate(corpus: String, conf: Configuration, baseDir : File) : Boolean = {
  val buildDirectory = baseDir / s"parsers/${corpus}"

  val dataDirectory = if (conf.datadir.head == '/') { file(conf.datadir)} else { baseDir / "datasets" }
  println("Data directory  " +  dataDirectory + buildDirectory )

  BuildComposer(dataDirectory, baseDir, corpus, conf.fstcompile)
  val expectedAlphabet = baseDir / "parsers/x/symbols/alphabet.fst"
  expectedAlphabet.exists
}

def testFstBuild(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}

def testUtilsBuild(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}
def plural[T] (lst : List[T]) : String = {
  if (lst.size > 1) { "s"} else {""}
}

def reportResults(results: List[Boolean]): Unit = {
  val distinctResults = results.distinct
  if (distinctResults.size == 1 && distinctResults(0)){
    println("\nAll tests.succeeded.")
  } else {
    println("\nThere were failures.")
  }
  println(s"${results.filter(_ == true).size} passed out of ${results.size} test${plural(results)} executed.")
  val pending = testList.filter(_._3 == "pending")
  if (pending.nonEmpty) {
    println(s"${pending.size} test${plural(pending)} pending.")
  }
}

lazy val allBuildTests = inputKey[Unit]("Test using output of args")
allBuildTests in Test := {
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
            //println(s"(Before ${t._1}, delete all parsers)")
            cleanAll.value

            print(t._1 + "...")
            val reslt = t._2(corpusName, conf, baseDir)
            if (reslt) { println ("success.") } else { println("failed.\n")}
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
            //println(s"(Before ${t._1}, delete all parsers)")
            cleanAll.value
            print(t._1 + "...")

            val reslt = t._2(corpusName, conf, baseDir)
            if (reslt) { println ("success.") } else { println("failed.\n")}
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
