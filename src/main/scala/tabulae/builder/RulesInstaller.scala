package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._

object RulesInstaller {

  /** Install stems data for a corpus-specific parser.
  *
  * @param dataSets Root directory for corpus-specific data sets.
  * @param corpusList "Corpus names" are names subdirectories of the
  * dataSets directory.  Each corpus can have stem tables to install.
  * @param parsers Writable directory for work compiling parser. The parser
  * source will be assembled in a sub-directory named from the list of names
  * in corpusList.
  * @param fst Root directory with pre-composed fst not derived from
  * user-managed tables.
  */
  def apply(dataSets: File, corpusList: Vector[String], parsers: File, fst: File ): Unit = {

    val inflDir = parsers / corpusList.mkString("-") / "inflection"
    if (! inflDir.exists) {mkdirs(inflDir)}
    assert(inflDir.exists, "RulesInstaller: Unable to create inflection directory " + inflDir)

    val verbsFst = inflDir / "verbinfl.fst"
    VerbRulesInstaller(dataSets, corpusList, verbsFst)

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

    val inflFst = fst / "inflection"
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
