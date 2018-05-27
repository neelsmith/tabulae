import sbt._

object Utils {


  /** Delete all subdirectories of a given directory.
  * Return names of deleted diretories.
  *
  * @param dir Directory to empty out.
  */
  def deleteSubdirs(dir: File, verbose: Boolean = true): Vector[String] = {
    val filesVector = dir.listFiles.toVector
    val deleted = for (f <- filesVector) yield {
      if (f.exists && f.isDirectory) {
        if (verbose) { println("\tdeleting " + f) } else {}
        IO.delete(f)
        f.toString
      } else {
        ""
      }
    }
    deleted.filter(_.nonEmpty)
  }

  /** Make sure directory exists.
  *
  * @param d Directory to check.
  */
  def dir(d: File) : File = {
    if (! d.exists) {d.mkdir} else {}
    require(d.isDirectory, s"File ${d} is not a directory")
    d
  }

  /** Builds happen in corpus-specific subdirectory of
  * the "parserse directory".
  */
  def buildDirectory(repoRoot: File , corpus: String) = {
    repoRoot / s"parsers/${corpus}"
  }
}
