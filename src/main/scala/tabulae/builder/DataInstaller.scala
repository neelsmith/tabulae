package edu.holycross.shot.tabulae.builder

import better.files._
import better.files.File._
import better.files.Dsl._

/** Install stems data for a corpus-specific parser.
*
* @param dataSource Root directory for corpus-specific data sets.
* @param repo Root of tabulae repository.  Data will be installed to
* repo/parsers/CORPUS
* @param corpusList(0) Name of corpus.  This is the name of the subdirectory of
* dataSource with stem tables, and the name of the destination directory for FST
* in repo/parsers.
*/
object DataInstaller {


  def apply(dataSource: File, repo: File, corpusList: Vector[String]): Unit = {
    //println(s"Convert morphological lexicon tables in ${dataSource} to FST")
    val lexica = mkdirs(repo / "parsers" / corpusList.mkString("-") / "lexica")
    if (! lexica.exists) {
      mkdirs(lexica)
    }
        
    val verbsTarget = lexica / "lexicon-verbs.fst"
    VerbDataInstaller(dataSource, corpusList, verbsTarget)


/*
    val nounsTarget = lexica / "lexicon-nouns.fst"
    NounDataInstaller(dataSource /corpusList(0) / "stems-tables/nouns", nounsTarget)

    val adjsTarget = lexica / "lexicon-adjectives.fst"
    AdjectiveDataInstaller(dataSource / corpusList(0) / "stems-tables/adjectives", adjsTarget)

    val indeclTarget = lexica / "lexicon-indeclinables.fst"
    IndeclDataInstaller(dataSource / corpusList(0) / "stems-tables/indeclinables", indeclTarget)

    val compoundVerbsTarget = lexica / "lexicon-compoundverbs.fst"
    CompoundVerbDataInstaller(dataSource / corpusList(0), lexica)




    val irregVerbsTarget = lexica / "lexicon-irregverbs.fst"
    IrregVerbDataInstaller(dataSource / corpusList(0) / "irregular-stems/verbs", irregVerbsTarget)

    val irregNounsTarget = lexica / "lexicon-irregnouns.fst"
    IrregNounDataInstaller(dataSource / corpusList(0)/ "irregular-stems/nouns", irregNounsTarget)

    val irregAdjectivesTarget = lexica / "lexicon-irregadjectives.fst"
    IrregAdjectiveDataInstaller(dataSource / corpusList(0) / "irregular-stems/adjectives", irregAdjectivesTarget)

    val irregAdverbsTarget = lexica / "lexicon-irregadverbs.fst"
    IrregAdverbDataInstaller(dataSource/corpusList(0) / "irregular-stems/adverbs", irregAdverbsTarget)

    val irregPronounsTarget = lexica / "lexicon-irregpronouns.fst"
    IrregPronounDataInstaller(dataSource/corpusList(0) / "irregular-stems/pronouns", irregPronounsTarget)


    val irregInfinsTarget = lexica / "lexicon-irreginfinitives.fst"
    IrregInfinitiveDataInstaller(dataSource/corpusList(0) / "irregular-stems/infinitives", irregInfinsTarget)

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
