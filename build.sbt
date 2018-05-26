import complete.DefaultParsers._
import scala.sys.process._
import java.io.File
import java.io.PrintWriter
import scala.io.Source


val commonSettings = Seq(
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
      cleanAll := cleanAllImpl.value
    )

lazy val root = (project in file(".")).
    settings( commonSettings:_*).enablePlugins(TutPlugin)

lazy val testBuild = (project in file("test_build"))

lazy val fst = inputKey[Unit]("Compile complete FST system for a named corpus")
lazy val corpus = inputKey[Unit]("Generate data directory hierarchy for a new named corpus")
lazy val cleanAll = taskKey[Vector[String]]("Delete all compiled parsers")
lazy val utils = inputKey[Unit]("Build utility transducers for a named corpus")

// Delete all compiled parsers
lazy val cleanAllImpl: Def.Initialize[Task[Vector[String]]] = Def.task {
  val parserDir = baseDirectory.value / "parsers"
  Utils.deleteSubdirs(parserDir)
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


////////////////////////////////////////////////////////////////
//
// Testing the build system
//
def tbdList = List (
  ("Test Configuration object", testConfiguration(_, _, _), "pending" ),
)
def testListX = List(






  ("Test copying secondary acceptors", testAcceptorCopying(_, _, _), "pending" ),
  ("Test rewriting acceptor file", testAcceptorRewrite(_, _, _), "" ),

  ("Test writing main verb acceptor file", testWriteVerbAcceptor(_, _, _), "" ),
  ("Test writing noun acceptor string", testNounAcceptor(_, _, _), "pending" ),
  ("Test writing irregular noun acceptor string", testIrregNounAcceptor(_, _, _), "pending" ),
  ("Test writing indeclinables acceptor string", testIndeclAcceptor(_, _, _), "" ),
  ("Test writing adjective acceptor string", testAdjectiveAcceptor(_, _, _), "pending" ),
  ("Test writing top-level acceptor string", testTopLevelAcceptor(_, _, _), "" ),
  ("Test composing final acceptor acceptor.fst", testMainAcceptorComposer(_, _, _), "" ),

  ("Test writing verb stems", testWriteVerbStems(_, _, _), "pending" ),

  ("Test composing parser", testParserComposer(_, _, _), "" ),
  ("Test composing main makefile", testMainMakefileComposer(_, _, _), "" ),
  ("Test composing inflection makefile", testInflectionMakefileComposer(_, _, _), "" ),
  ("Test composing verb makefile", testVerbMakefileComposer(_, _, _), "" ),

  ("Test making Corpus template", testCorpusTemplate(_, _, _), "pending" ) ,

  ("Test DataTemplate", testDataTemplate(_, _, _), "pending" ),

  ("Test compiling and executing FST parser", testFstBuild(_, _, _), "pending" ),
  ("Test compiling utilities", testUtilsBuild(_, _, _), "pending" ),
)







def testConfiguration(corpus: String, conf: Configuration, repoRoot : File) = {
  println("Test configuration object")
  // Should throw Exception if any of these don't exist
//fstcompile: String, fstinfl: String, make: String, datadir
  false
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
def testAcceptorCopying(corpusName: String, conf: Configuration, repoRoot : File) = {
  // Make directories;
  val projectDir = repoRoot / s"parsers/${corpusName}"
  Utils.dir(projectDir)
  val acceptorDir = projectDir / "acceptors"
  Utils.dir(acceptorDir)

  AcceptorComposer.copySecondaryAcceptors(repoRoot, corpusName)
  val fst = (acceptorDir) ** "*.fst"
  fst.get.size > 0
}
def testAcceptorRewrite(corpusName: String, conf: Configuration, repoRoot : File) = {

  val testOutDir = repoRoot / "parsers"
  Utils.dir(testOutDir)
  val testOut = testOutDir / "testfile.fst"
  new PrintWriter(testOut){write("@workdir@\n@workdir@\nUnmodified line\n"); close;}
  AcceptorComposer.rewriteFile(testOut, testOutDir)
  val lines = Source.fromFile(testOut).getLines.toVector.filter(_.nonEmpty)

  //clean up:
  testOut.delete
  lines(0) == testOutDir.toString + "/"
}


def testWriteVerbAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = repoRoot / "parsers"
  Utils.dir(projectDir)
  val corpus = projectDir / corpusName
  Utils.dir(corpus)
  AcceptorComposer.composeVerbAcceptor(corpus)

  val verbFile = corpus / "verb.fst"
  val lines = Source.fromFile(verbFile).getLines.toVector.filter(_.nonEmpty)
  val expected = "#include \""  + corpus + "/symbols.fst\""

  lines(0).trim == expected.trim
}
def testWriteVerbStems(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = repoRoot / s"parsers/${corpusName}"
  Utils.dir(projectDir)
  val acceptorsDir  = projectDir / "acceptors"
  val acceptorFile = acceptorsDir / "verbstems.fst"

  // 1. Should be minimal if no data installed.
  Utils.dir(acceptorsDir)
  AcceptorComposer.composeVerbStems(projectDir)
  val linesEmpty = Source.fromFile(acceptorFile).getLines.toVector.filter(_.nonEmpty)
  val firstChars = linesEmpty.map(_(0)).distinct
  val emptyOk = firstChars.size == 1 && firstChars(0) == '%'
  // tidy up
  acceptorFile.delete

  // 2. Should accommodate data if installed.
  AcceptorComposer.copySecondaryAcceptors(repoRoot, corpusName)
  AcceptorComposer.composeVerbStems(projectDir)

  val expectedStart = "\"<" + projectDir + "/acceptors/verb/"
  val fullLines = Source.fromFile(acceptorFile).getLines.toVector
  val expandedOk =  fullLines(3).startsWith(expectedStart)

  emptyOk  && expandedOk
}
def testRewriteAcceptors(corpusName: String, conf: Configuration, repoRoot : File) = {
  false
}

def testNounAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
  false
}

def testIrregNounAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
  false
}

def testIndeclAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
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

def testAdjectiveAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
  false
}

def testTopLevelAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = Utils.dir(file(s"parsers/${corpusName}"))

  // 1.  Should have minimal pipeline when no data installed
  val minAcceptorFst = AcceptorComposer.topLevelAcceptor(projectDir)
  val lines = minAcceptorFst.split("\n").toVector.filter(_.nonEmpty)
  val expected = "$acceptor$ = $verb_pipeline$"
  val minimalOk = lines(2).trim == expected

  // 2. Should prdocued same output for top of record when
  // data  installed
  val lexDir = Utils.dir(projectDir / "lexica")
  val indeclLexicon= lexDir  / "lexicon-indeclinables.fst"
  val goodLine = "testdata.rule1#nunc"
  val goodFst = IndeclRulesInstaller.indeclRuleToFst(goodLine)
  new PrintWriter(indeclLexicon){write(goodFst);close;}
  val expandedAcceptorFst = AcceptorComposer.topLevelAcceptor(projectDir)
  val lines2 = minAcceptorFst.split("\n").toVector.filter(_.nonEmpty)
  val expandedOk = lines2(2).trim == expected

  minimalOk && expandedOk
}

def testParserComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = Utils.dir(file(s"parsers/${corpusName}"))
  ParserComposer(projectDir)

  val parserFst = projectDir / "latin.fst"
  val lines = Source.fromFile(parserFst).getLines.toVector.filter(_.nonEmpty)

  // tidy up
  parserFst.delete

  val expected = "%% latin.fst : a Finite State Transducer for ancient latin morphology"
  lines(0).trim == expected
}

def testMainMakefileComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = Utils.dir(file(s"parsers/${corpusName}"))
  val compiler = conf.fstcompile

  // install some verb data
  AcceptorComposer.copySecondaryAcceptors(repoRoot, corpusName)
  val fst = MakefileComposer.composeVerbStemMake(projectDir, compiler)
  val lines  = fst.split("\n")

  MakefileComposer.composeMainMake(projectDir, compiler)

  val mkfile = projectDir / "makefile"
  mkfile.exists
}

def testVerbMakefileComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = Utils.dir(file(s"parsers/${corpusName}"))
  val compiler = conf.fstcompile

  // install some verb data
  AcceptorComposer.copySecondaryAcceptors(repoRoot, corpusName)
  val fst = MakefileComposer.composeVerbStemMake(projectDir, compiler)
  val lines  = fst.split("\n")

  val beginning = s"parsers/${corpusName}/acceptors/verbstems.a: "
  lines(0).startsWith(beginning) && lines(0).size > (beginning.size + 3)
}

def testInflectionMakefileComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  val projectDir = Utils.dir(file(s"parsers/${corpusName}"))
  val compiler = conf.fstcompile
  MakefileComposer.composeInflectionMake(projectDir, compiler)

  val inflDir = projectDir / "inflection"
  val mkfile = inflDir / "makefile"

  mkfile.exists
}

def testCorpusTemplate(corpus: String, conf: Configuration, baseDir : File) : Boolean = {
  val buildDirectory = baseDir / s"parsers/${corpus}"

  val dataDirectory = if (conf.datadir.head == '/') { file(conf.datadir)} else { baseDir / "datasets" }
  println("Data directory  " +  dataDirectory + buildDirectory )

  BuildComposer(dataDirectory, baseDir, corpus, conf.fstcompile)
  val expectedAlphabet = baseDir / "parsers/x/symbols/alphabet.fst"

  val moretests = false


  expectedAlphabet.exists && moretests
}

// test comopiling and executing a final parser
def testFstBuild(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}

def testUtilsBuild(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}

def testDataTemplate(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}
/*
def plural[T] (lst : List[T]) : String = {
  if (lst.size > 1) { "s"} else {""}
}

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
            deleteSubdirs(baseDir / "parsers", false)

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
            deleteSubdirs(baseDir / "parsers", false)
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
} */
