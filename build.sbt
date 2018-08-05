import complete.DefaultParsers._
import scala.sys.process._

import better.files.{File => ScalaFile, _}
import better.files.Dsl._

val commonSettings = Seq(
      name := "tabulae",
      organization := "edu.holycross.shot",
      version := "1.0.0",
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


      cleanAll := cleanAllImpl.value

    )

lazy val root = (project in file(".")).
    settings( commonSettings:_*).enablePlugins(TutPlugin)

lazy val cleanAll = taskKey[Vector[String]]("Delete all compiled parsers")





// Delete all compiled parsers
lazy val cleanAllImpl: Def.Initialize[Task[Vector[String]]] = Def.task {
  val parserDir = baseDirectory.value / "parsers"
  val subdirs = (parserDir.toScala).children.filter(_.isDirectory)
  for (d <- subdirs) {
    d.delete()
  }
  Vector.empty[String]
}

/*
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
*/

/*
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
      def conf = edu.holycross.shot.tabulae.builder.Configuration(confFile)
      Def.task {
        UtilsInstaller(bdFile.toScala, args.head, conf)
      }
    }
  }
}
*/
