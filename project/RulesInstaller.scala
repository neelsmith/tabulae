import sbt._
import scala.io.Source
import java.io.PrintWriter

object RulesInstaller {

  /** Format compilable FST rules for a named corpus.
  *
  * @param repo Base directory of the Tabulae repository.
  * @param corpus Name of corpus
  */
  def apply(repo: File, corpus: String): Unit = {
    println(s"\nConvert inflectional rules tables in ${repo} to FST")

    val parsers =  DataInstaller.madeDir(repo / "parsers")
    val corpDir = DataInstaller.madeDir(parsers / corpus)
    val inflDir = DataInstaller.madeDir(corpDir / "inflection")


    println("Install infl rules into " + inflDir)
    val srcCorpus = repo / s"datasets/${corpus}"


    NounRulesInstaller( srcCorpus / "rules-tables/nouns", inflDir / "nouninfl.fst")

  }


}
