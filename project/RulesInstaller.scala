
import better.files._
import better.files.File._
import better.files.Dsl._

object RulesInstaller {

  /** Format compilable FST rules for a named corpus.
  *
  * @param sourceDir Base directory of data set.  Inflectional
  * rules are drawn from tables here.
  * @param repo Base directory of the Tabulae repository.  Fst with
  * inflectional rules in FST for invariant forms are drawn from here.
  * @param corpus Name of corpus.  All rules are installed in
  * a subdirectory of the repository's parsers directory named
  * for the corpus.
  */
  def apply(sourceDir: File, repo: File, corpus: String): Unit = {

    val inflDir = repo/"parsers"/corpus/"inflection"
    if (! inflDir.exists) {mkdirs(inflDir)}
    val srcCorpus = sourceDir/corpus


    val nounsSrc = srcCorpus/"rules-tables/nouns"
    val nounsFst = inflDir/"nouninfl.fst"
    //NounRulesInstaller( nounsSrc,nounsFst )

    //IndeclRulesInstaller(srcCorpus / "rules-tables/indeclinables", inflDir / "indeclinfl.fst")

    val verbsSrc = srcCorpus/"rules-tables/verbs"
    val verbsFst = inflDir/"verbinfl.fst"
    VerbRulesInstaller(verbsSrc, verbsFst )

    //IrregVerbRulesInstaller(srcCorpus / "rules-tables/verbs", inflDir / "verbinfl.fst")

    val inflFst = repo/"fst/inflection"
    installInvariants(inflFst, inflDir)
  }


  /** Copy FST files with rules for invariant forms
  * (indeclinable and irregular forms) from repository
  * FST source to parser's build area.
  *
  * @param fstSrc Directory "fst/inflection" in tabulae repository.
  * @param fstTarget Directory "inflection" in parser build area.
  */
  def installInvariants(fstSrc: File, fstTarget: File) = {
    if (! fstTarget.exists) {
      mkdirs(fstTarget)
    }
    val fsts = fstSrc.glob("*.fst").toVector
    for (fst <- fsts) {
      cp(fst, fstTarget)
    }
  }

}
