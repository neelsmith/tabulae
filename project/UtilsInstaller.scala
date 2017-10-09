import sbt._
import scala.io.Source
import java.io.PrintWriter

import scala.sys.process._

import Path.rebase


object UtilsInstaller {


  def apply(repo: File, corpus: String, conf: Configuration): Unit = {
    println(s"Install utility transducers in ${repo} for ${corpus}")
    val projectDir = madeDir(repo / s"parsers/${corpus}")

    cpUtils(repo, corpus)
    writeMakefile(projectDir)

    val rawlex =  s"${conf.fstcompile} ${projectDir}/utils/rawlex.fst ${projectDir}/utils/rawlex.a"
    rawlex !
    val rawmorph = s"${conf.fstcompile} ${projectDir}/utils/rawmorph.fst ${projectDir}/utils/rawmorph.a"
    rawmorph !

    val mkfile = projectDir / "utils/makefile"
    s"${conf.make} -f ${mkfile }" !

  }

  def writeMakefile(dir: File): Unit = {
    val makeText = StringBuilder.newBuilder
    makeText.append(s"${dir}/utils/rawaccepted.a: ${dir}/symbols.fst ${dir}/symbols/phonology.fst ${dir}/inflection.a ${dir}/acceptor.a\n")
    makeText.append("%.a: %.fst\n")
    makeText.append("\tfst-compiler $< $@\n")
    new PrintWriter(dir / "utils/makefile") { write(makeText.toString); close }
  }


  def cpUtils(repo: File, corpus: String): Unit = {
    val projectDir = repo / s"parsers/${corpus}"

    val src = repo / "fst/utils"
    val dest = projectDir / "utils"

    val lexica = lexiconFiles(repo / s"parsers/${corpus}/lexica")

     val fst = (src) ** "*.fst"
     val fstFiles = fst.get
     val mappings: Seq[(File,File)] = fstFiles pair rebase(src, dest)
     for (m <- mappings) {
       IO.copyFile(m._1, m._2)
     }


     for (m <- mappings) {
       println("NOW FILTER FILE " + m._2)
       val lines = Source.fromFile(m._2).getLines.toVector
       val rewritten = lines.map(_.
         replaceAll("@workdir@", projectDir.toString + "/").
         replaceAll("@lexica@", lexica.mkString(" | "))).
         mkString("\n")
       new PrintWriter(m._2) { write(rewritten); close }
     }

  }



  def lexiconFiles(dir: File): Vector[String] = {
    val filesOpt = (dir) ** "*.fst"
    val files = filesOpt.get
    files.map(f => "\"" + f.toString() + "\"").toVector
  }


  def madeDir (dir: File) : File = {
    if (! dir.exists()) {
      dir.mkdir()
      dir
    } else {
      dir
    }
  }

}
