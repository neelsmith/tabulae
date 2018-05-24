import sbt._
import java.io.PrintWriter
import scala.io.Source
import Path.rebase

/**
*/
object SymbolsComposer {

  // Works with a repository directory and a corpus name
  // to compose files of FST symbols.
  //
  def apply(repo: File, corpus: String) : Unit = {
    composeMainFile(repo / s"parsers/${corpus}")
    copySecondaryFiles(repo, corpus)
    rewritePhonologyFile(repo / s"parsers/${corpus}/symbols/phonology.fst", repo / s"parsers/${corpus}")
  }

  // This only works if you've already installed the source
  // files (eg, by invoking copySecondaryFiles)
  def rewritePhonologyFile(f: File, workDir: File): Unit = {
    val lines = Source.fromFile(f).getLines.toVector
    val rewritten = lines.map(_.replaceAll("@workdir@", workDir.toString + "/")).mkString("\n")
    new PrintWriter(f) { write(rewritten); close }
  }


  def copySecondaryFiles(repo: File, corpus: String) : Unit = {
    val src = repo / "fst/symbols"
    val dest = repo / s"parsers/${corpus}/symbols"

     val fst = (src) ** "*.fst"
     val fstFiles = fst.get
     val mappings: Seq[(File,File)] = fstFiles pair rebase(src, dest)
     for (m <- mappings) {
       IO.copyFile(m._1, m._2)
     }
  }

  def composeMainFile(projectDir: File): Unit = {
    println("Composing main file of all symbols in " + projectDir)
    val fst = StringBuilder.newBuilder
    fst.append("% symbols.fst\n% A single include file for all symbols used in this FST.\n\n")

    fst.append("% 1. morphological tags\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/morphsymbols.fst\"\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/stemtypes.fst\"\n\n")

    fst.append("% 2. ASCII representation of polytonic Greek\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/phonology.fst\"\n\n")

    fst.append("% 3. Editorial symbols\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/markup.fst\"\n\n")

    println("Compose a symbols file in " + projectDir)
    val symbolsFile = projectDir / "symbols.fst"

    if (! projectDir.exists) {projectDir.mkdir()} else {}
    println("Time to write symbols files " + symbolsFile)
    new PrintWriter(symbolsFile) { write(fst.toString); close }
  }

}
