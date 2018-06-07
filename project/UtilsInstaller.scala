
import better.files.{File => ScalaFile, _}
import better.files.Dsl._

import scala.sys.process._



object UtilsInstaller {

  def apply(repo: ScalaFile, corpus: String, conf: Configuration): Unit = {
    println(s"Install utility transducers in ${repo} for ${corpus}")

    val projectDir = repo/"parsers"/corpus
    if (! projectDir.exists){mkdirs(projectDir)}

    cpUtils(repo, corpus)
    writeMakefile(projectDir)

    val rawlex =  s"${conf.fstcompile} ${projectDir}/utils/rawlex.fst ${projectDir}/utils/rawlex.a"
    rawlex !
    val rawmorph = s"${conf.fstcompile} ${projectDir}/utils/rawmorph.fst ${projectDir}/utils/rawmorph.a"
    rawmorph !

    val mkfile = projectDir / "utils/makefile"
    s"${conf.make} -f ${mkfile }" !

  }

  def writeMakefile(dir: ScalaFile): Unit = {
    val makeText = StringBuilder.newBuilder
    makeText.append(s"${dir}/utils/rawaccepted.a: ${dir}/symbols.fst ${dir}/symbols/phonology.fst ${dir}/inflection.a ${dir}/acceptor.a\n")
    makeText.append("%.a: %.fst\n")
    makeText.append("\tfst-compiler $< $@\n")
    (dir / "utils/makefile").overwrite(makeText.toString)
  }


  def cpUtils(repo: ScalaFile, corpus: String): Unit = {
    val projectDir = repo/"parsers"/corpus

    val src = repo/"fst/utils"
    val dest = projectDir/"utils"

    val lexica = lexiconFiles(repo/"parsers"/corpus/"lexica")

     val fstFiles = src.glob("*.fst").toVector
     for (f <- fstFiles) {
       f.copyToDirectory(dest)
     }
     val copies = dest.glob("*.fst").toVector
     for (c <- copies) {
       val lines = c.lines.toVector
      val rewritten = lines.map(_.
        replaceAll("@workdir@", projectDir.toString + "/").
        replaceAll("@lexica@", lexica.mkString(" | "))).
        mkString("\n")
      c.overwrite(rewritten)
     }
  }

  def lexiconFiles(dir: ScalaFile): Vector[String] = {
    val files = dir.glob("*.fst").toVector
    files.map(f => "\"" + f.toString() + "\"")
  }


}
