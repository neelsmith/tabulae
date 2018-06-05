import sbt._
import scala.sys.process._


import better.files.{File => ScalaFile, _}
import better.files.Dsl._


object FstCompiler {

  def compile(dataDirectory: ScalaFile, baseDir: ScalaFile, corpus: String, conf: Configuration) : Unit = {
    // Install data and rules, converting tabular data to FST
    //println(s"\n\n  Install data for ${corpus} in ${dataDirectory}...")
    DataInstaller(dataDirectory, baseDir, corpus)

    //println(s"Install rules for ${corpus} in ${dataDirectory}...")
    RulesInstaller(dataDirectory, baseDir, corpus)

    //println("Compose build")
    // Compose makefiles and higher-order FST for build system
    BuildComposer(dataDirectory.toJava, baseDir.toJava, corpus, conf.fstcompile)


    // Build it!
    val buildDirectory = baseDir/"parsers"/corpus
    val inflMakefile = buildDirectory/"inflection/makefile"
    val makeInfl = s"${conf.make} -f ${inflMakefile}"
    makeInfl !

    val makefile = buildDirectory / "makefile"
    val doit = s"${conf.make} -f ${makefile}"
    doit !

  }
}
