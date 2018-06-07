import complete.DefaultParsers._
import scala.sys.process._
import java.io.File
import java.io.PrintWriter
import scala.io.Source
import better.files.{File => ScalaFile, _}
import better.files.Dsl._

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
        "com.github.pathikrit" %% "better-files" % "3.5.0",

        "edu.holycross.shot.cite" %% "xcite" % "3.3.0",
        "edu.holycross.shot" %% "latphone" % "1.5.0"

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
lazy val testWordLists = (project in file("test_wordlists"))

lazy val testPoS = (project in file("test_pos"))


lazy val fst = inputKey[Unit]("Compile complete FST system for a named corpus")
lazy val corpus = inputKey[Unit]("Generate data directory hierarchy for a new named corpus")
lazy val cleanAll = taskKey[Vector[String]]("Delete all compiled parsers")
lazy val utils = inputKey[Unit]("Build utility transducers for a named corpus")

// Delete all compiled parsers
lazy val cleanAllImpl: Def.Initialize[Task[Vector[String]]] = Def.task {
  val parserDir = baseDirectory.value / "parsers"
  //Utils.deleteSubdirs(parserDir)
  println("REWORK THIS")
  Vector.empty[String]
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
        DataTemplate(srcDir.toScala, destDir.toScala)
        println("\n\nDone.  Template is in " + destDir)
      }
    }

    case 2 => {
      val confFile = file(args(1)).toScala
      def conf = Configuration(confFile)
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
        UtilsInstaller(baseDirectory.value.toScala, args.head,conf)
      }
    }

    case _ => {
      println("\nWrong number of parameters.")
      templateUsage
    }
  }
}

def templateUsage: Def.Initialize[Task[Unit]] = Def.task {
  println("\n\tUsage: corpus CORPUSNAME [CONFIGFILE]\n")
  //println("\t-r option = replace (delete) existing dataset\n")
}

lazy val utilsImpl = Def.inputTaskDyn {

  val args = spaceDelimited("corpus>").parsed
  val bdFile = baseDirectory.value

  args.size match {
    case 1 => {
      val confFile = file("conf.properties").toScala
      def conf = Configuration(confFile)
      Def.task {
        UtilsInstaller(bdFile.toScala, args.head, conf)
      }
    }

    case 2 => {
      val confFile = file("conf.properties").toScala
      def conf = Configuration(confFile)
      Def.task {
        UtilsInstaller(bdFile.toScala, args.head, conf)
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
      val config =  (bdFile / "conf.properties").toScala
      if (! config.exists()) {
        error("Configuration file " + config + " does not exist.\n")
      } else {
        val conf = Configuration(config)
        fstCompile(args.head,config)
      }
    }

    case 2 => {
      val confFile = (bdFile / args(1)).toScala
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

/*
def fstCompileImp(dataDirectory: File, baseDir: File, corpus: String, conf: Configuration)  = {
  // Install data and rules, converting tabular data to FST
  DataInstaller(dataDirectory, baseDir, corpus)
  RulesInstaller(dataDirectory, baseDir, corpus)

  // Compose makefiles and higher-order FST for build system
  BuildComposer(dataDirectory, baseDir, corpus, conf.fstcompile)


  // Build it!
  val buildDirectory = baseDir / s"parsers/${corpus}"
  val inflMakefile = buildDirectory / "inflection/makefile"
  val makeInfl = s"${conf.make} -f ${inflMakefile}"
  makeInfl !

  val makefile = buildDirectory / "makefile"
  val doit = s"${conf.make} -f ${makefile}"
  doit !
} */

// Compile FST parser
def fstCompile(corpus : String, configFile: ScalaFile) : Def.Initialize[Task[Unit]] = Def.task {
  val bd = baseDirectory.value
  //

  val conf = Configuration(configFile)

  println("Conf is " + conf + " from config file " + configFile)

  val dataDirectory = if (conf.datadir.head == '/') { file(conf.datadir)} else { bd / "datasets" }
  println("Data reictory from " + conf.datadir + " == "+ dataDirectory)
  FstCompiler.compile(dataDirectory.toScala, bd.toScala, corpus, conf)
/*

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
  */
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
  ("Test writing adjective acceptor string", testAdjectiveAcceptor(_, _, _), "pending" ),



  ("Test writing verb stems", testWriteVerbStems(_, _, _), "pending" ),





  ("Test composing verb makefile", testVerbMakefileComposer(_, _, _), "" ),

  ("Test making Corpus template", testCorpusTemplate(_, _, _), "pending" ) ,

  ("Test DataTemplate", testDataTemplate(_, _, _), "pending" ),


  ("Test compiling utilities", testUtilsBuild(_, _, _), "pending" ),
)







def testConfiguration(corpus: String, conf: Configuration, repoRoot : File) = {
  println("Test configuration object")
  // Should throw Exception if any of these don't exist
//fstcompile: String, fstinfl: String, make: String, datadir
  false
}


def testAcceptorCopying(corpusName: String, conf: Configuration, repoRoot : File) = {/*

  // Make directories;
  val projectDir = repoRoot / s"parsers/${corpusName}"
  Utils.dir(projectDir)
  val acceptorDir = projectDir / "acceptors"
  Utils.dir(acceptorDir)

  AcceptorComposer.copySecondaryAcceptors(repoRoot, corpusName)
  val fst = (acceptorDir) ** "*.fst"
  fst.get.size > 0
  */
}
def testAcceptorRewrite(corpusName: String, conf: Configuration, repoRoot : File) = {
/*
  val testOutDir = repoRoot / "parsers"
  Utils.dir(testOutDir)
  val testOut = testOutDir / "testfile.fst"
  new PrintWriter(testOut){write("@workdir@\n@workdir@\nUnmodified line\n"); close;}
  AcceptorComposer.rewriteFile(testOut, testOutDir)
  val lines = Source.fromFile(testOut).getLines.toVector.filter(_.nonEmpty)

  //clean up:
  testOut.delete
  lines(0) == testOutDir.toString + "/"
  */
}

def testWriteVerbAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
  /*
  val projectDir = repoRoot / "parsers"
  Utils.dir(projectDir)
  val corpus = projectDir / corpusName
  Utils.dir(corpus)
  AcceptorComposer.composeVerbAcceptor(corpus)

  val verbFile = corpus / "verb.fst"
  val lines = Source.fromFile(verbFile).getLines.toVector.filter(_.nonEmpty)
  val expected = "#include \""  + corpus + "/symbols.fst\""

  lines(0).trim == expected.trim
  */
}
def testWriteVerbStems(corpusName: String, conf: Configuration, repoRoot : File) = {
  /*
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

  emptyOk  && expandedOk */
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
def testAdjectiveAcceptor(corpusName: String, conf: Configuration, repoRoot : File) = {
  false
}


def testVerbMakefileComposer(corpusName: String, conf: Configuration, repoRoot : File) = {
  /*
  val projectDir = Utils.dir(file(s"parsers/${corpusName}"))
  val compiler = conf.fstcompile

  // install some verb data
  AcceptorComposer.copySecondaryAcceptors(repoRoot, corpusName)
  val fst = MakefileComposer.composeVerbStemMake(projectDir, compiler)
  val lines  = fst.split("\n")

  val beginning = s"parsers/${corpusName}/acceptors/verbstems.a: "
  lines(0).startsWith(beginning) && lines(0).size > (beginning.size + 3)
  */
}

def testCorpusTemplate(corpus: String, conf: Configuration, baseDir : File) : Boolean = {
  /*
  val buildDirectory = baseDir / s"parsers/${corpus}"

  val dataDirectory = if (conf.datadir.head == '/') { file(conf.datadir)} else { baseDir / "datasets" }
  println("Data directory  " +  dataDirectory + buildDirectory )

  BuildComposer(dataDirectory, baseDir, corpus, conf.fstcompile)
  val expectedAlphabet = baseDir / "parsers/x/symbols/alphabet.fst"

  val moretests = false


  expectedAlphabet.exists && moretests
  */false
}

def testUtilsBuild(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}

def testDataTemplate(corpusName: String, conf: Configuration, baseDir : File) : Boolean = {
  false
}
