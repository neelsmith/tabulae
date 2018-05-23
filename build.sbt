import complete.DefaultParsers._
import scala.sys.process._

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
      cleanAll := cleanAllImpl.value //,
      //mdebug := currentTest.value
    ).enablePlugins(TutPlugin)

lazy val fst = inputKey[Unit]("Compile complete FST system for a named corpus")
lazy val corpus = inputKey[Unit]("Generate data directory hierarchy for a new named corpus")
lazy val cleanAll = taskKey[Unit]("Delete all compiled parsers")
lazy val utils = inputKey[Unit]("Build utility transducers for a named corpus")


/*
lazy val mdebug = taskKey[Unit]("Run temporary build tests")
def currentTest: Def.Initialize[Task[Unit]] = Def.task {
  val src =  new File("parsers/dev1/acceptors/verb"
  val fileOpt = (src) ** "*fst"
}
*/
  /*

  val corpus = "vienna_lit"
  val configFile = file("config.properties")




  val buildDirectory = baseDirectory.value / s"parsers/${corpus}"
  val conf = Configuration(configFile)

  // Install data and rules, converting tabular data to FST
*/

   ///DataInstaller(baseDirectory.value, corpus)
/*   RulesInstaller(baseDirectory.value, corpus)



   // Compose makefiles and higher-order FST for build system
   BuildComposer(baseDirectory.value, corpus, "/usr/local/bin/fst-compiler")

   // Build it!
   val inflMakefile = buildDirectory / "inflection/makefile"
   val makeInfl = s"${conf.make} -f ${inflMakefile}"
   makeInfl !

}   */

// Delete all compiled parsers
lazy val cleanAllImpl: Def.Initialize[Task[Unit]] = Def.task {
  val parserDir = baseDirectory.value / "parsers"
  val filesVector = parserDir.listFiles.toVector
  for (f <- filesVector) {
    if (f.exists && f.isDirectory) {
      println("Deleting " + f)
      IO.delete(f)
    } else {
      // pass over f
    }
  }
}


// Generate data directory hierarchy for a new named corpus.
// Writes output to `datasets/CORPUS`.
lazy val corpusTemplateImpl = Def.inputTaskDyn {
  val bdFile = baseDirectory.value
  val args = spaceDelimited("corpus>").parsed
  println(s"${args.size} from ${args}")
  args.size match {
    case 1 => {
      val destDir = baseDirectory.value / s"datasets/${args.head}"
      if (destDir.exists()) {
        error(s"file exists: ${destDir}")
      } else {
        Def.task {
          val srcDir = baseDirectory.value / "datatemplate"
          println("\nCreate directory tree for new corpus " + args.head + "\n")
          DataTemplate(srcDir, destDir)
          println("\n\nDone.  Template is in " + destDir)
        }
      }
    }
    case 2 => {
      // NO
      def conf = Configuration(file(args(1)))
      val destDir = baseDirectory.value / s"${conf.datadir}/${args(1)}"
      if(args(0) == "-r") {
        if (destDir.exists()) {
          IO.delete(destDir)
          println("Deleted " + destDir)
        } else { }
      }
      Def.task {
        val srcDir = baseDirectory.value / "datatemplate"
        println("\nCreate directory tree for new corpus " + args.head + "\n")
        DataTemplate(srcDir, destDir)
        println("\n\nDone.  Template is in " + destDir)
      }
    }

    case _ => {
      println("\nWrong number of parameters.")
      templateUsage
    }
  }
}



def templateUsage: Def.Initialize[Task[Unit]] = Def.task {
  println("\n\tUsage: corpus [-r] CORPUSNAME\n")
  println("\t-r option = replace (delete) existing dataset\n")
}



lazy val utilsImpl = Def.inputTaskDyn {
  val args = spaceDelimited("corpus>").parsed
  if (args.size != 1) {
    error("No corpus named\n\tUsage: utils CORPUS")
  } else {
    Def.task {
      def conf = Configuration(file("config.properties"))
      UtilsInstaller(baseDirectory.value, args.head,conf)
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
