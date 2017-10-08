import complete.DefaultParsers._

lazy val root = (project in file(".")).
    settings(
      name := "tabulae",
      organization := "edu.holycross.shot",
      version := "0.0.1",
      scalaVersion := "2.12.3",
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),
      resolvers += Resolver.jcenterRepo,
      resolvers += Resolver.bintrayRepo("neelsmith", "maven"),
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.0.1" % "test",

        "edu.holycross.shot.cite" %% "xcite" % "2.7.1"
      ),

      //fst := buildFst.evaluated,
      corpusTemplate := corpusTemplateImpl.evaluated,
      //utils := utilsImpl.evaluated,
      //cleanAll := cleanAllImpl.value,

      //kdebug := currentTest.value
    )

lazy val fst = inputKey[Unit]("Compile complete FST system for a named corpus")
lazy val corpusTemplate = inputKey[Unit]("Generate data directory hierarchy for a new named corpus")
lazy val cleanAll = taskKey[Unit]("Delete all compiled parsers")
lazy val utils = inputKey[Unit]("Build utility transducers for a named corpus")



//lazy val kdebug = taskKey[Unit]("Run temporary build tests")
/*def currentTest: Def.Initialize[Task[Unit]] = Def.task {
  val corpus = "vienna_lit"
  val configFile = file("config.properties")
*/


  /*
  val buildDirectory = baseDirectory.value / s"parsers/${corpus}"
  val conf = Configuration(configFile)

  // Install data and rules, converting tabular data to FST
*/

   ///DataInstaller(baseDirectory.value, corpus)
/*   RulesInstaller(baseDirectory.value, corpus)



   // Compose makefiles and higher-order FST for build system
   BuildComposer(baseDirectory.value, corpus, "/usr/local/bin/fst-compiler")
   */
/*
   // Build it!
   val inflMakefile = buildDirectory / "inflection/makefile"
   val makeInfl = s"${conf.make} -f ${inflMakefile}"
   makeInfl !
   */
//}

// Delete all compiled parsers
/*
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
*/

// Generate data directory hierarchy for a new named corpus.
// Writes output to `datasets/CORPUS`.
lazy val corpusTemplateImpl = Def.inputTaskDyn {
  val bdFile = baseDirectory.value
  val args = spaceDelimited("corpus>").parsed

  args.size match {
    case 1 => {

      val srcDir = file(args.head)
      val destDir = bdFile / s"datasets/${args.head}"

      if (destDir.exists()) {
        error(s"file exists: ${destDir}")

      } else {
        Def.task {
          val srcDir = bdFile / "datatemplate"
          println("\nCreate directory tree for new corpus " + args.head + "\n")
          DataTemplate(srcDir, destDir)
          println("\n\nDone.  Template is in " + destDir)
        }
      }
    }

    case 2 => {

      if(args(0) == "-r") {
        val destDir = bdFile / s"datasets/${args(1)}"
        if (destDir.exists()) {
          IO.delete(destDir)
          println("Deleted " + destDir)
        } else {}
        Def.task {
          val srcDir = bdFile / "datatemplate"
          println("\nCreate directory tree for new corpus " + args.head + "\n")
          DataTemplate(srcDir, destDir)
          println("\n\nDone.  Template is in " + destDir)
        }

      } else {
        println("Syntax error.")
        templateUsage
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


/*
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
lazy val buildFst = Def.inputTaskDyn {
  val args = spaceDelimited("corpus>").parsed

  args.size match {
    case 1 => {
      val src = baseDirectory.value / s"datasets/${args.head}"
      if (! src.exists()) {
        error("Source dataset " + src + " does not exist.\n")
      } else {
        println("\nCompile corpus " + args.head + " with default configuration from config.properties\n")
        fstCompile(args.head, baseDirectory.value / "config.properties")
      }

    }
    case 2 => {
      val src = baseDirectory.value / s"datasets/${args.head}"
      val confFile = baseDirectory.value / args(1)
      if (! src.exists()) {
        error("Source dataset " + src + " does not exist.\n")
      } else if (! confFile.exists()) {
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
*/
def usage: Def.Initialize[Task[Unit]] = Def.task {
  println("\n\tUsage: fst CORPUS [CONFIGFILE] \n")
}

def error(msg: String): Def.Initialize[Task[Unit]] = Def.task {
  println(s"\n\tError: {$msg}\n")
}

/*
// Compile FST parser
def fstCompile(corpus : String, configFile: File) : Def.Initialize[Task[Unit]] = Def.task {
  val buildDirectory = baseDirectory.value / s"parsers/${corpus}"
  val conf = Configuration(configFile)

  // Install data and rules, converting tabular data to FST
  DataInstaller(baseDirectory.value, corpus)
  RulesInstaller(baseDirectory.value, corpus)

  // Compose makefiles and higher-order FST for build system
  BuildComposer(baseDirectory.value, corpus, "/usr/local/bin/fst-compiler")

  // Build it!
  val inflMakefile = buildDirectory / "inflection/makefile"
  val makeInfl = s"${conf.make} -f ${inflMakefile}"
  makeInfl !
  val makefile = buildDirectory / "makefile"
  val doit = s"${conf.make} -f ${makefile}"
  doit !
}
*/
