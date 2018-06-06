import better.files._
import better.files.File._
import better.files.Dsl._


object DataInstaller {

  def apply(dataSource: File, repo: File, corpusName: String): Unit = {
    //println(s"Convert morphological lexicon tables in ${dataSource} to FST")
    val lexica = mkdirs(repo/"parsers"/corpusName/"lexica")

    //NounDataInstaller(dataSource, repo, corpus)
    //IndeclDataInstaller(dataSource,repo, corpus)

    val verbsTarget = lexica/"lexicon-verbs.fst"

    VerbDataInstaller(dataSource/"stems-tables/verbs-simplex", verbsTarget)
    //IrregVerbDataInstaller(dataSource, repo, corpus)
  }

}
