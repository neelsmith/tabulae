import sbt._
import scala.sys.process._
import java.io.File
import better.files._
import better.files.File._
import better.files.Dsl._

object FstCompiler {

  def compile(dataDirectory: File, baseDir: File, corpus: String, conf: Configuration) : Unit = {
    // Install data and rules, converting tabular data to FST
    //println(s"\n\n  Install data for ${corpus} in ${dataDirectory}...")
    DataInstaller(dataDirectory.toScala, baseDir.toScala, corpus)

    //println(s"Install rules for ${corpus} in ${dataDirectory}...")
    RulesInstaller(dataDirectory.toScala, baseDir.toScala, corpus)

    //println("Compose build")
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

  }
}
