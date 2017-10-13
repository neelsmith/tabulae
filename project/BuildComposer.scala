import sbt._
import java.io.PrintWriter


/** Object for composing all files that are generated from source,
* rather than built from templates or data sets.  These include
* the necessary makefiles, created by [[MakefileComposer]], and
* the top-level FST files acceptor.fst (created by [[AcceptorComposer]]),
* inflection.fst (created by [[InflectionComposer]]), and greek.fst
* created by [[ParserComposer]].
*/
object BuildComposer {

  def apply(dataSource: File, repo: File, corpus: String, fstcompiler: String) : Unit = {
    println("Composing a lot of build things.")

    val corpusDir = "parsers/" + corpus
    val projectDir = repo / corpusDir
    SymbolsComposer(repo, corpus)
    val alphabet = dataSource / s"${corpus}/orthography/alphabet.fst"
    println("COPY ALPHABET from " + alphabet)
    IO.copyFile(alphabet,  repo / s"parsers/${corpus}/symbols/alphabet.fst")
    InflectionComposer(projectDir)
    AcceptorComposer(repo, corpus)
    ParserComposer(projectDir)
    MakefileComposer(projectDir, fstcompiler)

    //GeneratorComposer(repo, corpus)
  }

}
