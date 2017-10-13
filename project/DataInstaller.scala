import sbt._
import scala.io.Source
import java.io.PrintWriter


object DataInstaller {


  def apply(dataSource: File, repo: File, corpus: String): Unit = {
    println(s"Convert morphological lexicon tables in ${dataSource} to FST")

    val parsers =  DataInstaller.madeDir(repo / "parsers")
    val corpDir = DataInstaller.madeDir(parsers / corpus)

    val lexDir = DataInstaller.madeDir(corpDir / "lexica")
    val inflDir = DataInstaller.madeDir(corpDir / "inflection")


    NounDataInstaller(dataSource, repo, corpus)
    //IndeclDataInstaller(repo, corpus)
    VerbDataInstaller(dataSource, repo, corpus)
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
