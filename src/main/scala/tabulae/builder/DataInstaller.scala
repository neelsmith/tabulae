package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._

/** Install stems data for a corpus-specific parser.
*
* @param dataSets Root directory for corpus-specific data sets.
* @param corpusList "Corpus names" are names subdirectories of the
* dataSets directory.  Each corpus can have stem tables to install.
* @param parsers Writable directory for work compiling parser. The parser
* source will be assembled in a sub-directory named from the list of names
* in corpusList.
*/
object DataInstaller {

  def apply(dataSets: File, corpusList: Vector[String], parsers: File): Unit = {
    val lexica = parsers / corpusList.mkString("-") / "lexica"
    if (! lexica.exists) {
      mkdirs(lexica)
    }

    // All varieties of verb data:
    val verbsTarget = lexica / "lexicon-verbs.fst"
    VerbDataInstaller(dataSets, corpusList, verbsTarget)

    val irregVerbsTarget = lexica / "lexicon-irregverbs.fst"
    IrregVerbDataInstaller(dataSets, corpusList, irregVerbsTarget)

    val irregInfinsTarget = lexica / "lexicon-irreginfinitives.fst"
    IrregInfinitiveDataInstaller(dataSets, corpusList, irregInfinsTarget)

    CompoundVerbDataInstaller(dataSets, corpusList, lexica)
    /// end verb data

    val indeclTarget = lexica / "lexicon-indeclinables.fst"
    IndeclDataInstaller(dataSets, corpusList, indeclTarget)

    val nounsTarget = lexica / "lexicon-nouns.fst"
    NounDataInstaller(dataSets, corpusList, nounsTarget)

    val irregPronounsTarget = lexica / "lexicon-irregpronouns.fst"
    IrregPronounDataInstaller(dataSets, corpusList, irregPronounsTarget)

    val adjsTarget = lexica / "lexicon-adjectives.fst"
    AdjectiveDataInstaller(dataSets, corpusList, adjsTarget)
/*

    val irregNounsTarget = lexica / "lexicon-irregnouns.fst"
    IrregNounDataInstaller(dataSource / corpusList(0)/ "irregular-stems/nouns", irregNounsTarget)

    val irregAdjectivesTarget = lexica / "lexicon-irregadjectives.fst"
    IrregAdjectiveDataInstaller(dataSource / corpusList(0) / "irregular-stems/adjectives", irregAdjectivesTarget)

    val irregAdverbsTarget = lexica / "lexicon-irregadverbs.fst"
    IrregAdverbDataInstaller(dataSource/corpusList(0) / "irregular-stems/adverbs", irregAdverbsTarget)
*/


// THESE MAY ALL BE BOGUS:
/*
    val irregPtcplsTarget = lexica / "lexicon-irregparticiples.fst"
    IrregParticipleDataInstaller(dataSource/corpusList(0) / "irregular-stems/participles", irregPtcplsTarget)

    val irregGerundsTarget = lexica / "lexicon-irreggerunds.fst"
    IrregGerundDataInstaller(dataSource/corpusList(0) / "irregular-stems/gerunds", irregGerundsTarget)

    val irregGerundivesTarget = lexica / "lexicon-irreggerundives.fst"
    IrregGerundiveDataInstaller(dataSource/corpusList(0) / "irregular-stems/gerundives", irregGerundivesTarget)

    val irregSupinesTarget = lexica / "lexicon-irregsupines.fst"
    IrregSupineDataInstaller(dataSource/corpusList(0) / "irregular-stems/supines", irregSupinesTarget)
*/

  }

}
