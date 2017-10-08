import sbt._
import java.io.PrintWriter
import scala.io.Source

/**
*/
object SymbolsComposer {


  def apply(repo: File, corpus: String) : Unit = {
    composeMainFile(repo / s"parsers/${corpus}")
    //copySecondaryFiles(repo, corpus)
    //rewritePhonologyFile(repo / s"parsers/${corpus}/symbols/phonology.fst", repo / s"parsers/${corpus}")
  }

/*
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
*/

  def composeMainFile(projectDir: File): Unit = {
    val fst = StringBuilder.newBuilder
    fst.append("% symbols.fst\n% A single include file for all symbols used in this FST.\n\n")

    fst.append("% 1. morphological tags\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/morphsymbols.fst\"\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/stemtypes.fst\"\n\n")


    fst.append("% 2. ASCII representation of polytonic Greek\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/phonology.fst\"\n\n")

    fst.append("% 3. Editorial symbols\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/markup.fst\"\n\n")

    val symbolsFile = projectDir / "symbols.fst"
    new PrintWriter(symbolsFile) { write(fst.toString); close }
  }

}
