import better.files._
import better.files.File._
import better.files.Dsl._



case class CompoundEntry(
  ruleId: String, compoundLexEnt: String, prefix: String, simplexLexEnt: String
)

object CompoundVerbDataInstaller {

  /** Write FST rules for all compound verb stem data
  * in a corpus.
  *
  * @param corpus Dataset for a corpus.
  * @param targetFile File to write FST statements to.
  */
  def apply(corpus: File, targetDir: File) = {
    val simplexDir = corpus/"stems-tables/verbs-simplex"
    val regularVerbMap = rulesMap(simplexDir)
    val irregDir = corpus/"irregular-stems/verbs"
    val irregVerbMap = irregMap(irregDir)
    val verbKeys = regularVerbMap.keySet ++ irregVerbMap.keySet


    val compoundDir= corpus/"stems-tables/verbs-compound"
    val compoundEntries= compoundInfo(compoundDir)
    require(reffOK(compoundEntries, verbKeys))

    installRegularCompounds(compoundEntries,                targetDir/"lexicon-compoundverbs.fst",
      regularVerbMap)

    installIrregularCompounds(compoundEntries,
          targetDir/"lexicon-irregcompoundverbs.fst",
          irregVerbMap)
  }



  /** True if all lexical IDs for simplex verbs in
  * a list of [[CompoundEntry]]s are present in the
  * keys of the simplex or irregular data set.
  *
  * @param compounds List of [[CompoundEntry]]s to check.
  * @param keys Strings with lexical entity IDs for all
  * regular and irregular stems in a data set.
  */
  def reffOK (compounds: Vector[CompoundEntry], keys: Set[String]) : Boolean = {
    for (c <- compounds) {
      if (keys.contains(c.simplexLexEnt)) {
        //ok
      } else {
        throw new Exception("CompoundVerbDataInstaller:  bad reference to simplex verb " + c.simplexLexEnt + " in compound verb object " + c)
      }
    }
    true
  }

  /** Create a Vector of [[CompoundEntry]]s from the CEX
  * files in a speified directory.
  *
  * @param compoundDir Directory containing CEX files with
  * information about compound verb stems.
  */
  def compoundInfo(compoundDir: File):Vector[CompoundEntry] = {
    val cexData = cexRules(compoundDir)
    val compoundEntries = for (cexRow <- cexData.filter(_.nonEmpty))  yield {
      val cols = cexRow.split("#")
      if (cols.size < 4 ){
        throw new Exception("CompoundVerbDataInstaller:  two few columns in "+ cexRow)
      } else {
        CompoundEntry(cols(0),cols(1),cols(2),cols(3))
      }
    }
    compoundEntries
  }


  /** For a given list of [[CompoundEntry]]s, expand
  * any entries corresponding to simplex stems, and
  * write the results to a file.
  *
  * @param compounds List of [[CompoundEntry]]s to check.
  * @param targetFile File where results should be written.
  * @param simplexMap Map of lexical entity IDs to simplex
  * stem data in CEX format.
  */
  def installRegularCompounds(compounds: Vector[CompoundEntry], targetFile: File, simplexMap: Map[String, String]) : Unit = {

    val compoundDataLines = for (c <- compounds) yield {
      if (simplexMap.keySet.contains(c.simplexLexEnt)) {
        val simplexLine =  simplexMap(c.simplexLexEnt)
        val cols = simplexLine.split("#").toVector
        //Rule#LexicalEntity#Stem#Class
        if (cols.size < 3)  {
          throw new Exception("CompoundVerbDataInstaller: two few columns in data source " + cols)
        } else {
          val simplexRule = cols(0)
          val stem = cols(1)
          val stemClass = cols(2)
          val ruleParts = simplexRule.split("\\.")
          s"${c.ruleId}_${ruleParts(1)}#${c.compoundLexEnt}#${c.prefix}${stem}#${stemClass}"
        }
      } else {
        ""
      }
    }

    val verbFst = VerbDataInstaller.verbLinesToFst(compoundDataLines.filter(_.nonEmpty))
    (targetFile).overwrite(verbFst)
  }

  def installIrregularCompounds(compounds: Vector[CompoundEntry], targetFile: File, compoundMap: Map[String, String]) : Unit = {

    val compoundDataLines = for (c <- compounds) yield {
      if (compoundMap.keySet.contains(c.simplexLexEnt)) {
        val compoundLine =  compoundMap(c.simplexLexEnt)
        val cols = compoundLine.split("#").toVector

        if (cols.size < 9)  {
          throw new Exception("CompoundVerbDataInstaller: two few columns in data source " + cols)
        } else {
          val ruleId = cols(0)
          val lexent = cols(1)
          val stem = cols(2)
          val  person =cols(3)
          val num = cols(4)
          val  tense = cols(5)
          val mood = cols(6)
          val voice = cols(7)
          val stemClass = cols(8)
          val ruleParts = ruleId.split("\\.")
          s"${c.ruleId}_${ruleParts(1)}#${c.compoundLexEnt}#${c.prefix}${stem}#${person}#${num}#${tense}#${mood}#${voice}"
        }
      } else {
        ""
      }
    }
    val verbFst = IrregVerbDataInstaller.verbLinesToFst(compoundDataLines.filter(_.nonEmpty))
    (targetFile).overwrite(verbFst)
  }

  /** Map lexical URNs to data for simplex verb stems.
  *
  * @param simplexDir Directory containing .cex files
  * with verb stem data.
  */
  def rulesMap(simplexDir: File): Map[String, String] = {
    val raw = cexRules(simplexDir)
    //ag.v1#lexent.n2280#am#conj1
    raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 4) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line " + s)
      } else {
        val ruleId = cols(0)
        val lexent = cols(1)
        val stem = cols(2)
        val stemClass = cols(3)
        val data = ruleId + "#" + stem + "#" + stemClass
        (lexent-> data)
      }
    }).toMap
  }

  /** Map lexical URNs to data for irregular verb forms.
  *
  * @param irregDir Directory containing .cex files
  * with verb stem data.
  */
  def irregMap(irregDir: File) = {
    val raw = cexRules(irregDir)
    raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 4) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line for irregular form " + s)
      } else {
        //ag.irrv1#lexent.n46529#sum#1st#sg#pres#indic#act
        val ruleId = cols(0)
        val lexent = cols(1)
        val stem = cols(2)
        val person = cols(3)
        val num = cols(4)
        val tense = cols(5)
        val mood = cols(6)
        val voice = cols(7)
        val stemClass = "irregcverb"
        val data = List(ruleId, lexent, stem, person, num, tense,mood,voice,stemClass).mkString("#")
        (lexent-> data)
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
