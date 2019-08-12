package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._

object RulesInstaller {

  /** Format compilable FST rules for a named corpus.
  *
  * @param datasets Base directory of data set.  Inflectional
  * rules are drawn from tables here.
  * @param repo Base directory of the Tabulae repository.  Fst with
  * inflectional rules in FST for invariant forms are drawn from here.
  * @param corpus Name of corpus.  All rules are installed in
  * a subdirectory of the repository's parsers directory named
  * for the corpus.
  */
  def apply(datasets: File, repo: File, corpusList: Vector[String]): Unit = {

    val inflDir = repo / "parsers" / corpusList.mkString("-") / "inflection"
    if (! inflDir.exists) {mkdirs(inflDir)}
    assert(inflDir.exists, "RulesInstaller: Unable to create inflection directory " + inflDir)

    println("INFLDIR IS " + inflDir)
    println("SOURCE DIR IS " + datasets)
    val srcCorpus = datasets / corpusList(0)
    println("Corpus source DIR IS " + srcCorpus)

    
    val verbsFst = inflDir / "verbinfl.fst"
    VerbRulesInstaller(datasets, corpusList, verbsFst )

/*
    val nounsSrc = srcCorpus / "rules-tables/nouns"
    val nounsFst = inflDir / "nouninfl.fst"
    NounRulesInstaller( nounsSrc,nounsFst )

    val adjsSrc = srcCorpus / "rules-tables/adjectives"
    val adjectivesFst = inflDir / "adjinfl.fst"
    AdjectiveRulesInstaller( adjsSrc,adjectivesFst )

    val advsSrc = srcCorpus / "rules-tables/adverbs"
    val advsFst = inflDir / "advinfl.fst"
    AdverbRulesInstaller( advsSrc,advsFst )



    val infinSrc = srcCorpus / "rules-tables/infinitives"
    val infinFst = inflDir / "infininfl.fst"
    InfinitiveRulesInstaller( infinSrc,infinFst )

    val ptcplSrc = srcCorpus / "rules-tables/participles"
    val ptcplFst = inflDir / "ptcplinfl.fst"
    ParticipleRulesInstaller( ptcplSrc,ptcplFst  )


    val gndvSrc = srcCorpus / "rules-tables/gerundives"
    val gndvFst = inflDir / "gerundiveinfl.fst"
    GerundiveRulesInstaller( gndvSrc,gndvFst  )

    val gndSrc = srcCorpus / "rules-tables/gerunds"
    val gndFst = inflDir / "gerundinfl.fst"
    GerundRulesInstaller( gndSrc,gndFst  )

    val supineSrc = srcCorpus / "rules-tables/supines"
    val supineFst = inflDir / "supineinfl.fst"
    SupineRulesInstaller( supineSrc,supineFst  )
*/

    val inflFst = repo / "fst/inflection"
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
      val targetFile = fstTarget / fst.name
      targetFile.overwrite(fst.lines.mkString("\n"))
    }
  }

}
