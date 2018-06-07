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
    if (! lexica.exists){mkdirs(lexica)}
    //NounDataInstaller(dataSource, repo, corpus)
    //IndeclDataInstaller(dataSource,repo, corpus)

    val verbsTarget = lexica/"lexicon-verbs.fst"

    VerbDataInstaller(dataSource/corpusName/"stems-tables/verbs-simplex", verbsTarget)
    //IrregVerbDataInstaller(dataSource, repo, corpus)
  }

}
