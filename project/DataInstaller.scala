import sbt._
import scala.io.Source
import java.io.PrintWriter


object DataInstaller {


  def apply(repo: File, corpus: String): Unit = {
    println(s"Convert morphological lexicon tables in ${repo} to FST")
    /*
    val projectDir = DataInstaller.madeDir(repo / s"parsers/${corpus}")
    val lexDir = DataInstaller.madeDir(projectDir / "lexica")
    NounDataInstaller(repo, corpus)
    IndeclDataInstaller(repo, corpus)
    VerbDataInstaller(repo, corpus)
    */
  }


  // make sure directory exists
  def madeDir (dir: File) : File = {
    if (! dir.exists()) {
      dir.mkdir()
      dir
    } else {
      dir
    }
  }

  /** Rewrite characters in s that are not part of FST's
  * alphabet to corresponding FST representation.
  *
  * @param s String to rewrite.
  */
  def toFstAlphabet( s: String) = {
    s.
     replaceAll("_", "<lo>").
     replaceAll("\\^", "<sh>").
     replaceAll("\\(", "<ro>").
     replaceAll("\\)", "<sm>").
     replaceAll("=", "\\\\=").
     replaceAll("\\|", "<isub>")
   }

}
