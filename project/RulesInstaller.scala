import sbt._
import scala.io.Source
import java.io.PrintWriter

object RulesInstaller {

  /** Format compilable FST rules for a named corpus.
  *
  * @param repo Base directory of the Tabulae repository.
  * @param corpus Name of corpus
  */
  def apply(sourceDir: File, repo: File, corpus: String): Unit = {
    println(s"\nConvert inflectional rules tables in ${repo} to FST")

    val parsers =  DataInstaller.dir(repo / "parsers")
    val corpDir = DataInstaller.dir(parsers / corpus)
    val inflDir = DataInstaller.dir(corpDir / "inflection")

    val srcCorpus = sourceDir / corpus

    //NounRulesInstaller( srcCorpus / "rules-tables/nouns", inflDir / "nouninfl.fst")
    IndeclRulesInstaller(srcCorpus / "rules-tables/indeclinables", inflDir / "indeclinfl.fst")
    //VerbRulesInstaller(srcCorpus / "rules-tables/verbs", inflDir / "verbinfl.fst")
  }


}
