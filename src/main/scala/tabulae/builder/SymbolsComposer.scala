package edu.holycross.shot.tabulae.builder

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
  * @param corpusList Name of corpus.  Output will be written
  * in the parsers/CORPUS build space.
  */
  // apply(repo: ScalaFile, corpusList: Vector[String]) : Unit = {
  def apply(corpusDir: ScalaFile, fstSource:  ScalaFile) : Unit = {
    // we need:
    // corpus directory
    // symbols source directory
    // that's all?

    //val fstDir = repo / "parsers" / corpusList.mkString("-") / "fst"
    if (! fstSource.exists) { mkdirs(fstSource)}
    assert(fstSource.exists,"SymbolsComposer: failed to make directory " + fstSource)

    if (! corpusDir.exists) { mkdirs(corpusDir)}
    assert(corpusDir.exists,"SymbolsComposer: failed to make directory " + corpusDir)

    val symbolDir = corpusDir / "symbols"
    if (! symbolDir.exists) {mkdirs(symbolDir)}
    assert(symbolDir.exists,"SymbolsComposer: failed to make directory " + symbolDir)

    composeMainFile(corpusDir)

    copyFst(fstSource, symbolDir )

    rewritePhonologyFile(symbolDir / "phonology.fst", corpusDir)
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
    assert(dest.exists, "SymbolsComposer: failed to make directory " + dest)
     val fstFiles = src.glob("*.fst").toVector
     for (f <- fstFiles) {
      val targetFile = dest / f.name
      targetFile.overwrite(f.lines.mkString("\n"))
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

    val symbolsFile = (projectDir / "symbols.fst").createIfNotExists()
    assert(symbolsFile.exists,"SymbolsComposer: unable to create " + symbolsFile)
    symbolsFile.overwrite(fst.toString)
  }

}
