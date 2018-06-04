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
    //println(s"Convert morphological lexicon tables in ${dataSource} to FST")

    val parsers =  Utils.dir(repo / "parsers")

    val corpDir = Utils.dir(parsers / corpus)

    val lexDir = Utils.dir(corpDir / "lexica")
    val inflDir = Utils.dir(corpDir / "inflection")

    //NounDataInstaller(dataSource, repo, corpus)
    //IndeclDataInstaller(dataSource,repo, corpus)
    //VerbDataInstaller(dataSource, repo, corpus)
    //IrregVerbDataInstaller(dataSource, repo, corpus)
  }



}
