import sbt._
import scala.io.Source
import java.io.PrintWriter


object DataInstaller {


  def apply(repo: File, corpus: String): Unit = {
    println(s"Convert morphological lexicon tables in ${repo} to FST")

    val parsers =  DataInstaller.madeDir(repo / "parsers")
    val projectDir = DataInstaller.madeDir(parsers/ corpus)
    val lexDir = DataInstaller.madeDir(projectDir / "lexica")


      val corpDir = DataInstaller.madeDir(parsers / corpus)
      val inflDir = DataInstaller.madeDir(corpDir / "inflection")


    NounDataInstaller(repo, corpus)
    //IndeclDataInstaller(repo, corpus)
    //VerbDataInstaller(repo, corpus)
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
}
