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

    val parsers =  Utils.dir(repo / "parsers")
    val corpDir = Utils.dir(parsers / corpus)
    val inflDir = Utils.dir(corpDir / "inflection")

    val srcCorpus = sourceDir / corpus

    //NounRulesInstaller( srcCorpus / "rules-tables/nouns", inflDir / "nouninfl.fst")
    IndeclRulesInstaller(srcCorpus / "rules-tables/indeclinables", inflDir / "indeclinfl.fst")

    VerbRulesInstaller(srcCorpus / "rules-tables/verbs", inflDir / "verbinfl.fst")

    //IrregVerbRulesInstaller(srcCorpus / "rules-tables/verbs", inflDir / "verbinfl.fst")
  }


}
