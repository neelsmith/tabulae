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
  * @param corpusList  List of "corpora", used as name of subdirectory where
  * binary parser will be written.
  */
  def compileAll(
    dataSets: ScalaFile,
    corpusList: Vector[String],
    parser: ScalaFile,
    fstSource: ScalaFile,
    conf: Configuration) : Unit = {
    // Install data and rules, converting tabular data to FST
    //println(s"\n\n  Install data for ${corpus} in ${dataDirectory}...")
    DataInstaller(dataSets, corpusList, parser, fstSource)

    //println(s"Install rules for ${corpus} in ${dataDirectory}...")
    RulesInstaller(dataSets, corpusList, parser, fstSource)

    //println("Compose build")
    // Compose makefiles and higher-order FST for build system
    BuildComposer(dataSets, corpusList, parser, fstSource, conf.fstcompile)


    // Build it!
    val buildDirectory = parser / corpusList.mkString("-")
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
  def compile(dataDirectory: ScalaFile,  corpusList: Vector[String], parsers: ScalaFile, fstSrc: ScalaFile, conf: Configuration, replaceExisting: Boolean = true) : Unit = {

    val projectDir = parsers / corpusList.mkString("-")
    if (projectDir.exists) {
      replaceExisting match {
        case true => {
          projectDir.delete()
          compileAll(dataDirectory,  corpusList, parsers, fstSrc, conf)
        }
        case false => {
          println("Directory " + projectDir + " exists, and setting to delete exising parser is false.")
          println("Cowardly refusing to continue.")
        }
      }
    } else {
      compileAll(dataDirectory, corpusList, parsers, fstSrc, conf)
    }
  }
}
