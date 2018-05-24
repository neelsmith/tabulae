import sbt._
import scala.io.Source
import java.io.PrintWriter


object DataInstaller {

/*
Users/nsmith/Desktop/horace
 	/data/repos/latin/tabulae
	h4
*/
  def apply(dataSource: File, repo: File, corpus: String): Unit = {
    println(s"Convert morphological lexicon tables in ${dataSource} to FST")

    val parsers =  DataInstaller.dir(repo / "parsers")
    val corpDir = DataInstaller.dir(parsers / corpus)

    val lexDir = DataInstaller.dir(corpDir / "lexica")
    val inflDir = DataInstaller.dir(corpDir / "inflection")


    //NounDataInstaller(dataSource, repo, corpus)
    IndeclDataInstaller(dataSource,repo, corpus)
    //VerbDataInstaller(dataSource, repo, corpus)
  }


  /** Make sure directory exists.
  *
  * @param d Directory to check.
  */
  def dir(d: File) : File = {
    if (! d.isDirectory) {d.mkdir} else {}
    require(d.isDirectory, s"File ${d} is not a directory")
    if (! d.exists) {d.mkdir}
    d
  }
}
