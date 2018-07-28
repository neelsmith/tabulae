import better.files._
import better.files.File._
import better.files.Dsl._


object CompoundVerbDataInstaller {

  /** Write FST rules for all verb stem data in a directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
  * @param targetFile File to write FST statements to.
  */
  def apply(stemsDir: File, targetDir: File) = {
    val simplexDir = stemsDir/"verbs-simplex"
    val simplexMap = rulesMap(simplexDir)

    val compoundDir= stemsDir/"verbs-compound"
    val compoundInfo = cexRules(compoundDir)
    val compoundDataLines = for (c <- compoundInfo) yield {
      val cols = c.split("#")
      if (cols.size < 4) {
        throw new Exception("Installing compound verb data: too few columns in " + c)
      } else {
        val ruleId = cols(0)
        val lexent = cols(1)
        val prefix = cols(2)
        val simplexId = cols(3)
        if (simplexMap.keySet.contains(simplexId)) {
          s"${ruleId}#${lexent}#${prefix}${simplexMap(simplexId)}"
        } else {
          throw new Exception("Lexical map did not contain key for " + simplexId)
        }
      }
    }
    val verbFst = VerbDataInstaller.verbLinesToFst(compoundDataLines)
    (targetDir/"lexicon-compoundverbs.fst").overwrite(verbFst)
  }

  /** Map lexical URNs to data for the simplex verb.
  *
  * @param simplexDir Directory containing .cex files
  * with verb stem data.
  */
  def rulesMap(simplexDir: File): Map[String, String] = {
    val raw = cexRules(simplexDir)
    raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 4) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line " + s)
      } else {
        val data = cols(2) + "#" + cols(3)
        (cols(1) -> data)
      }
    }).toMap
  }

  /** Create FST string for a verb tables in a given directory.
  *
  * @param dir Directory with tables of verb data.
  */
  def cexRules(dir: File) : Vector[String] = {
    val verbFiles = dir.glob("*.cex").toVector
    val fstLines = for (f <- verbFiles) yield {
      // omit empty lines and header
      f.lines.toVector.filter(_.nonEmpty).drop(1)
    }
    fstLines.flatten
  }

}
