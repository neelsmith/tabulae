
import better.files.{File => ScalaFile, _}
import better.files.Dsl._

/**
*/
object SymbolsComposer {

  // Works with a repository directory and a corpus name
  // to compose files of FST symbols.
  //
  /** Create all FST files defining symbols of a
  * parser's FST alphabet.
  *
  * @param repo Root of tabulae repository.  Source data
  * will be drawn from repo/fst/symbols.
  * @param corpus Name of corpus.  Output will be written
  * in the pasers/CORPUS build space.
  */
  def apply(repo: ScalaFile, corpus: String) : Unit = {
    val fstDir = repo/"parsers"/corpus/"fst"
    if (! fstDir.exists) { mkdirs(fstDir)}
    val corpusDir = repo/"parsers"/corpus
    println("Corpus dir is " + corpusDir)
    println("Exits? " + corpusDir.exists())
    composeMainFile(corpusDir)
    val symbolDir = repo/"parsers"/corpus/"symbols"
    if (! symbolDir.exists) {mkdirs(symbolDir)}
    val symbolSrc = repo/"fst/symbols"

    copyFst(repo/"fst/symbols", symbolDir )

    rewritePhonologyFile(repo/"parsers"/corpus/"symbols/phonology.fst", repo/"parsers"/corpus)
  }

  // This only works if you've already installed the source
  // files (eg, by invoking copySecondaryFiles)
  def rewritePhonologyFile(f: ScalaFile, workDir: ScalaFile): Unit = {
    val lines = f.lines.toVector
    val rewritten = lines.map(_.replaceAll("@workdir@", workDir.toString + "/")).mkString("\n")
    f.overwrite(rewritten)
  }


  /** Install symbols from src into project in dest.
  *
  * @param src Directory with symbols files (e.g., "fst/symbols"
  * in this repo).
  * @param dest Directory where files should be written. This
  * will be parsers/CORPUS/symbols.
  */
  def copyFst(src: ScalaFile, dest: ScalaFile) : Unit = {
    if (! dest.exists()) {mkdirs(dest)}
     val fstFiles = src.glob("*.fst").toVector
     for (f <- fstFiles) {
       f.copyToDirectory(dest)
     }
  }


  /** Write file symbols.fst in project directory.
  *
  * @param projectDir Directory for parser.
  */
  def composeMainFile(projectDir: ScalaFile): Unit = {
    if (! projectDir.exists()) { mkdirs(projectDir)}
    val fst = StringBuilder.newBuilder
    fst.append("% symbols.fst\n% A single include file for all symbols used in this FST.\n\n")

    fst.append("% 1. morphological tags\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/morphsymbols.fst\"\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/stemtypes.fst\"\n\n")

    fst.append("% 2. ASCII representation of polytonic Greek\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/phonology.fst\"\n\n")

    fst.append("% 3. Editorial symbols\n")
    fst.append("#include \"" + projectDir.toString + "/symbols/markup.fst\"\n\n")

    val symbolsFile = (projectDir/"symbols.fst").createIfNotExists()

    symbolsFile.overwrite(fst.toString)
  }

}
