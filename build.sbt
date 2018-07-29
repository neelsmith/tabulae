import complete.DefaultParsers._
import scala.sys.process._

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
      parse := parseWords.evaluated,
      corpus := corpusTemplateImpl.evaluated,
      utils := utilsImpl.evaluated,
      cleanAll := cleanAllImpl.value,
      debug := debugMe.value

    )

lazy val root = (project in file(".")).
    settings( commonSettings:_*).enablePlugins(TutPlugin)

lazy val testBuild = (project in file("test_build"))
lazy val testWordLists = (project in file("test_wordlists"))

lazy val testPoS = (project in file("test_pos"))
lazy val testIrreg = (project in file("test_irreg"))


lazy val testPoS2 = (project in file("test_pos2"))


lazy val fst = inputKey[Unit]("Compile complete FST system for a named corpus")
lazy val parse = inputKey[Unit]("Run a binary parse against a word list")
lazy val corpus = inputKey[Unit]("Generate data directory hierarchy for a new named corpus")
lazy val cleanAll = taskKey[Vector[String]]("Delete all compiled parsers")
lazy val utils = inputKey[Unit]("Build utility transducers for a named corpus")
lazy val debug = taskKey[Vector[String]]("Run some debugging script")

lazy val debugMe: Def.Initialize[Task[Vector[String]]] = Def.task {
  val goodLine = "ag.irrinf1#lexent.n46529#esse#pres#act"
  val goodFst = IrregInfinitiveDataInstaller.infinitiveLineToFst(goodLine)
  val expected = "<u>ag\\.irrinf1</u><u>lexent\\.n46529</u>esse<pres><act><irreginfin>"
  val worked = (goodFst.trim ==  expected)
  println("GOT " + goodFst)
  Vector(goodFst,goodLine)
}


// Delete all compiled parsers
lazy val cleanAllImpl: Def.Initialize[Task[Vector[String]]] = Def.task {
  val parserDir = baseDirectory.value / "parsers"
  val subdirs = (parserDir.toScala).children.filter(_.isDirectory)
  for (d <- subdirs) {
    d.delete()
  }
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


lazy val parseWords = Def.inputTaskDyn {
  val bdFile= baseDirectory.value
  val args = spaceDelimited("corpus>").parsed
  Def.task {
    println("Args to parse:  " + args)
    // need 3? corpus, conffile, wordlist
    val confFile = file("conf.properties").toScala
    val conf = Configuration(confFile)
    println("Conf is " + conf + " from config file " + args(1))
  }
}


def usage: Def.Initialize[Task[Unit]] = Def.task {
  println("\n\tUsage: fst CORPUS [CONFIGFILE] \n")
}

def error(msg: String): Def.Initialize[Task[Unit]] = Def.task {
  println(s"\n\tError: {$msg}\n")
}

// Compile FST parser
def fstCompile(corpus : String, configFile: ScalaFile, replace: Boolean = true) : Def.Initialize[Task[Unit]] = Def.task {
  val bd = baseDirectory.value
  val conf = Configuration(configFile)

  println("Conf is " + conf + " from config file " + configFile)

  val dataDirectory = if (conf.datadir.head == '/') { file(conf.datadir)} else { bd / "datasets" }
  println("Data directory from " + conf.datadir + " == "+ dataDirectory)
  FstCompiler.compile(dataDirectory.toScala, bd.toScala, corpus, conf, replace)
}
