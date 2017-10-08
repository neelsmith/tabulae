import sbt._
import scala.io.Source
import java.io.PrintWriter

object RulesInstaller {


  /** Format compilable FST rules for a named corpus.
  *
  * @param repo Base directory of the Kanónes repository.
  * @param corpus Name of corpus
  */
  def apply(repo: File, corpus: String): Unit = {
    println(s"\nConvert inflectional rules tables in ${repo} to FST")
/*

    val inflDir = DataInstaller.madeDir(repo / s"parsers/${corpus}/inflection")
    val srcCorpus = repo / s"datasets/${corpus}"

    NounRulesInstaller(srcCorpus / "rules-tables/nouns", inflDir / "nouninfl.fst")
    IndeclRulesInstaller(srcCorpus / "rules-tables/indeclinable", inflDir / "indeclinfl.fst")
    VerbRulesInstaller(srcCorpus / "rules-tables/verbs", inflDir / "verbinfl.fst")
    */
  }


}
