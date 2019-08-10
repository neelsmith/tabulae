package edu.holycross.shot.tabulae.builder


import scala.sys.process._

import better.files.{File => ScalaFile, _}
import better.files.Dsl._


object FstCompiler {


  /** Compile lexica, morphological rule sets and core parser logic
  *
  * @param dataDirectory Base directory for finding source data from which
  * the parser will be built.
  * @param baseDir A writable working directory where the binary parser
  * will be written.  Specifically, the parser will be written within a subdirectory named `corpus` of a subdirectory named "`parsers`" of
  * `baseDir`.
  * @param corpus  Name of "corpus", used as name of subdirectory where
  * binary parser will be written.
  */
  def compileAll(dataDirectory: ScalaFile, baseDir: ScalaFile, corpusList: Vector[String], conf: Configuration) : Unit = {
    // Install data and rules, converting tabular data to FST
    //println(s"\n\n  Install data for ${corpus} in ${dataDirectory}...")
    DataInstaller(dataDirectory, baseDir, corpusList)

    //println(s"Install rules for ${corpus} in ${dataDirectory}...")
    RulesInstaller(dataDirectory, baseDir, corpusList)

    //println("Compose build")
    // Compose makefiles and higher-order FST for build system
    BuildComposer(dataDirectory, baseDir, corpusList, conf.fstcompile)

    // Build it!
    val buildDirectory = baseDir / "parsers" / corpusList(0)
    val inflMakefile = buildDirectory / "inflection/makefile"
    val makeInfl = s"${conf.make} -f ${inflMakefile}"
    makeInfl !

    val makefile = buildDirectory / "makefile"
    val doit = s"${conf.make} -f ${makefile}"
    doit !
  }


  /**  Compose a parser in FST from tabular source data, and compile it to binary form.
  *
  * @param dataDirectory Base directory for finding source data from which
  * the parser will be built.
  * @param baseDir A writable working directory where the binary parser
  * will be written.  Specifically, the parser will be written within a subdirectory named `corpus` of a subdirectory named "`parsers`" of
  * `baseDir`.
  * @param corpus  Name of "corpus", used as name of subdirectory where
  * binary parser will be written.
  * @param conf Configuration object for compiling a parser.
  */
  def compile(dataDirectory: ScalaFile, baseDir: ScalaFile, corpusList: Vector[String], conf: Configuration, replaceExisting: Boolean = true) : Unit = {

    val projectDir = baseDir / "parsers" / corpusList(0)
    if (projectDir.exists) {
      replaceExisting match {
        case true => {
          projectDir.delete()
          compileAll(dataDirectory, baseDir, corpusList, conf)
        }
        case false => {
          println("Directory " + projectDir + " exists, and setting to delete exising parser is false.")
          println("Cowardly refusing to continue.")
        }
      }
    } else {
      compileAll(dataDirectory, baseDir, corpusList, conf)
    }
  }
}
