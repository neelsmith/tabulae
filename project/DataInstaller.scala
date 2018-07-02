import better.files._
import better.files.File._
import better.files.Dsl._

/** Install stems data for a corpus-specific parser.
*
* @param dataSource Root directory for corpus-specific data sets.
* @param repo Root of tabulae repository.  Data will be installed to
* repo/parsers/CORPUS
* @param corpusName Name of corpus.  This is the name of the subdirectory of
* dataSource with stem tables, and the name of the destination directory for FST
* in repo/parsers.
*/
object DataInstaller {

  def apply(dataSource: File, repo: File, corpusName: String): Unit = {
    //println(s"Convert morphological lexicon tables in ${dataSource} to FST")
    val lexica = mkdirs(repo/"parsers"/corpusName/"lexica")

    val indeclTarget = lexica/"lexicon-indeclinables.fst"
    IndeclDataInstaller(dataSource/corpusName/"stems-tables/indeclinables",indeclTarget)

    val verbsTarget = lexica/"lexicon-verbs.fst"
    VerbDataInstaller(dataSource/corpusName/"stems-tables/verbs-simplex", verbsTarget)

    val nounsTarget = lexica/"lexicon-nouns.fst"
    NounDataInstaller(dataSource/corpusName/"stems-tables/nouns", nounsTarget)

    val adjsTarget = lexica/"lexicon-adjectives.fst"
    AdjectiveDataInstaller(dataSource/corpusName/"stems-tables/adjectives", adjsTarget)


    val irregVerbsTarget = lexica/"lexicon-irregverbs.fst"
    IrregVerbDataInstaller(dataSource/corpusName/"irregular-stems/verbs", irregVerbsTarget)

    val irregNounsTarget = lexica/"lexicon-irregnouns.fst"
    IrregNounDataInstaller(dataSource/corpusName/"irregular-stems/nouns", irregNounsTarget)

    val irregAdjectivesTarget = lexica/"lexicon-irregadjectives.fst"
    IrregAdjectiveDataInstaller(dataSource/corpusName/"irregular-stems/adjectives", irregAdjectivesTarget)

    val irregAdverbsTarget = lexica/"lexicon-irregadverbs.fst"
    IrregAdverbDataInstaller(dataSource/corpusName/"irregular-stems/adverbs", irregAdverbsTarget)

    val irregPronounsTarget = lexica/"lexicon-irregpronouns.fst"
    IrregPronounDataInstaller(dataSource/corpusName/"irregular-stems/pronouns", irregPronounsTarget)


    val irregInfinsTarget = lexica/"lexicon-irreginfinitives.fst"
    IrregInfinitiveDataInstaller(dataSource/corpusName/"irregular-stems/infinitives", irregInfinsTarget)
  }

}
