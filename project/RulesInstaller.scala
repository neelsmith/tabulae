
import scala.io.Source
import java.io.PrintWriter

import better.files._
import better.files.File._
import better.files.Dsl._
import java.io.{File => JFile}

object RulesInstaller {

  /** Format compilable FST rules for a named corpus.
  *
  * @param jsourceDir Base directory of data set.  Inflectional
  * rules are drawn from tables here.
  * @param jrepo Base directory of the Tabulae repository.  Fst with
  * inflectional rules in FST for invariant forms are drawn from here.
  * @param corpus Name of corpus.  All rules are installed in
  * a subdirectory of the repository's parsers directory named
  * for the corpus.
  */
  def apply(jsourceDir: JFile, jrepo: JFile, corpus: String): Unit = {

    val repo = jrepo.toScala
    val sourceDir = jsourceDir.toScala
/*
    val parsers =  repo/"parsers"

    val corpDir = parsers/corpus
    if (! corpDir.exists()) { mkdirs(corpDir)}
*/
    val inflDir = mkdirs(repo/"parsers"/corpus/"inflection")



    val srcCorpus = sourceDir/corpus


    val nounsSrc = srcCorpus/"rules-tables/nouns"
    val nounsFst = inflDir/"nouninfl.fst"
    NounRulesInstaller( nounsSrc.toJava,nounsFst.toJava )

    //IndeclRulesInstaller(srcCorpus / "rules-tables/indeclinables", inflDir / "indeclinfl.fst")


/*
    val verbsSrc = mkdirs(srcCorpus/"rules-tables/verbs")
    val verbsFst = inflDir/"verbinfl.fst"
    VerbRulesInstaller(verbsSrc.toJava, verbsFst.toJava )
*/
    //IrregVerbRulesInstaller(srcCorpus / "rules-tables/verbs", inflDir / "verbinfl.fst")

    val inflFst = (repo/"fst/inflection").createIfNotExists()
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
      println("File to copy: " + fst)
      cp(fst, fstTarget)
    }
  }

}
