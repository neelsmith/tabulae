
import better.files.{File => ScalaFile, _}
import better.files.Dsl._

/** Object for composing all files that are generated from source,
* rather than built from templates or data sets.  These include
* the necessary makefiles, created by [[MakefileComposer]], and
* the top-level FST files acceptor.fst (created by [[AcceptorComposer]]),
* inflection.fst (created by [[InflectionComposer]]), and greek.fst
* created by [[ParserComposer]].
*/
object BuildComposer {


  def installAlphabet(dataSrc: ScalaFile, repo: ScalaFile, corpus: String): Unit = {
    val symbolsDir = repo/"parsers"/corpus/"symbols"
    mkdirs(symbolsDir)
    (dataSrc/corpus/"orthography/alphabet.fst").copyTo(symbolsDir/"alphabet.fst")
  }


  /**  Assemble a tabulae build.
  *
  * @param dataSource Root directory for corpus-specific datasets.
  * @param repo Root directory of tabulae repository.  Build space will
  * be created in repo/parsers/CORPUS.
  * @param corpus Name of corpus. This is the name of an extant subdirectory
  * of dataSource, and will the name of the subdirectory in repo/parsers
  * where the build is assembled.
  * @param fstcompiler:  Explicit path to SFST compiler binary.
  */
  def apply(dataSource: ScalaFile, repo: ScalaFile, corpus: String, fstcompiler: String) : Unit = {
    println("Composing a lot of build things.")
    println("Data source is " + dataSource)
    println("Repo is " + repo)

    //val corpusDir = repo/"parsers"/corpus
    //val projectDir = repo / corpusDir

    //SymbolsComposer(repo, corpus)
    installAlphabet(dataSource, repo, corpus)
    //InflectionComposer(projectDir.toScala)
    //AcceptorComposer(repo, corpus)
    //ParserComposer(projectDir)
    //MakefileComposer(projectDir, fstcompiler)

    //GeneratorComposer(repo, corpus)
  }

}
