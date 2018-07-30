import better.files._
import better.files.File._
import better.files.Dsl._


case class CompoundEntry(
  ruleId: String, compoundLexEnt: String, prefix: String, simplexLexEnt: String
)

object CompoundVerbDataInstaller {

  /** Write FST rules for all verb stem data in a directory
  * of tabular files.
  *
  * @param srcDir Directory with inflectional rules.
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

    installRegularCompounds(compoundEntries, targetDir/"lexicon-compoundverbs.fst",
    regularVerbMap)
  }

/*
    val irregDir= corpus/"irregular-stems/verbs"
    val irregFormMap = irregMap(irregDir)
    installIrregularCompounds(
        compoundDir,
        targetDir/"lexicon-irregcompoundverbs.fst",
        irregFormMap
        )
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

  def installRegularCompounds(compounds: Vector[CompoundEntry], targetFile: File, simplexMap: Map[String, String]) = {

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

  def installIrregularCompounds(compoundDir: File, targetFile: File, simplexMap: Map[String, String]) = { }
/*
    val compoundInfo = cexRules(compoundDir)
    println("COMPOUND INFO:\n" + compoundInfo)

    val compoundDataLines = for (c <- compoundInfo) yield {
      val cols = c.split("#")
      println("\n\nIRREGULAR:\n" + cols.toVector)

      if (cols.size < 4) {
        throw new Exception("Installing compound verb data: too few columns in " + c)
      } else {
        val ruleId = cols(0)
        val lexent = cols(1)
        val prefix = cols(2)
        val simplexId = cols(3)
        if (simplexMap.keySet.contains(simplexId)) {
          println("MATCH ON " + simplexMap(simplexId))
        } else {

          throw new Exception("Installing irregular compounds: lexical map did not contain key for " + simplexId + " in " + simplexMap.keySet)
        }
      }
    val verbFst = VerbDataInstaller.verbLinesToFst(compoundDataLines)
    (targetFile).overwrite(verbFst)

  }*/

  /** Map lexical URNs to data for the simplex verb.
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
