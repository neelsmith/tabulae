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

    val participleMap = irregParticipleMap(corpus/"irregular-stems/participles")
    val infinitiveMap = irregInfinitiveMap(corpus/"irregular-stems/infinitives")
    val gerundMap = irregGerundMap(corpus/"irregular-stems/gerunds")
    val gerundiveMap = irregGerundiveMap(corpus/"irregular-stems/gerundives")
    val supineMap = irregSupineMap(corpus/"irregular-stems/supines")


    val allVerbKeys = regularVerbMap.keySet ++ irregVerbMap.keySet ++ participleMap.keySet ++ infinitiveMap.keySet ++ gerundMap.keySet ++ gerundiveMap.keySet ++ supineMap.keySet

    val compoundDir= corpus/"stems-tables/verbs-compound"
    val compoundEntries= compoundInfo(compoundDir)
    require(reffOK(compoundEntries, allVerbKeys))

    installRegularCompounds(compoundEntries,                targetDir/"lexicon-compoundverbs.fst",
      regularVerbMap)

    installIrregularCompounds(compoundEntries,
          targetDir/"lexicon-irregcompoundverbs.fst",
          irregVerbMap)


    installIrregularParticiples(compoundEntries,      targetDir/"lexicon-irregcompoundparticiples.fst",      participleMap)

    installIrregularInfinitives(compoundEntries,      targetDir/"lexicon-irregcompoundinfinitives.fst",      infinitiveMap)

    installIrregularGerundives(compoundEntries,      targetDir/"lexicon-irregcompoundgerundives.fst",      gerundiveMap)
  /*
    installIrregularGerunds(compoundEntries,      targetDir/"lexicon-irregcompoundgerunds.fst",  gerundMap)
  */
    installIrregularSupines(compoundEntries,      targetDir/"lexicon-irregcompoundsupines.fst",  supineMap)

  }




/*
    def installIrregularGerunds(compounds: Vector[CompoundEntry], targetFile: File, compoundMap: Map[String, Vector[String]]) : Unit = {
    //println("INSTALL PTCPLS IN " + targetFile)
    //println("PTCP MAP: " + compoundMap)
    //println("COMPOUND DATA: \n" + compounds.mkString("\n"))
    val compoundDataLines = for (c <- compounds) yield {

      if (compoundMap.keySet.contains(c.simplexLexEnt)) {
        val compoundLine =  compoundMap(c.simplexLexEnt)
        val cols = compoundLine.split("#").toVector

        if (cols.size < 4)  {
          throw new Exception("CompoundVerbDataInstaller: two few columns in data source " + cols)
        } else {
          val ruleId = cols(0)
          val lexent = cols(1)
          val stem = cols(2)
          val grammCase = cols(3)

          s"${c.ruleId}_${i}#${c.compoundLexEnt}#${c.prefix}${stem}#${grammCase}"
        }
      } else {
        ""
      }
    }
    val  gerundFst = IrregGerundDataInstaller.gerundLinesToFst(compoundDataLines.filter(_.nonEmpty))
    (targetFile).overwrite(gerundFst)
  }

  */
















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
        println("CompoundVerbDataInstaller:  bad reference to simplex verb " + c.simplexLexEnt + " in compound verb object " + c)
        println("Key set for simplex verbs was " + keys)
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
  def installRegularCompounds(compounds: Vector[CompoundEntry], targetFile: File, simplexMap: Map[String, Vector[String]]) = {//: Unit = {
    val compoundDataLines = for ((c,i) <- compounds.zipWithIndex) yield {
      if (simplexMap.keySet.contains(c.simplexLexEnt)) {
        val simplexLines =  simplexMap(c.simplexLexEnt)
        val compoundLines= for (ln <- simplexLines) yield {
          val cols = ln.split("#").toVector
          //Rule#LexicalEntity#Stem#Class
          if (cols.size < 3)  {
            throw new Exception("CompoundVerbDataInstaller: two few columns in data source " + cols)
          } else {
            val simplexRule = cols(0)
            val stem = cols(1)
            val stemClass = cols(2)

            s"${c.ruleId}_${i}#${c.compoundLexEnt}#${c.prefix}${stem}#${stemClass}"
          }
        }
        compoundLines
      } else {
        Vector.empty[String]
      }
    }

    val verbFst = VerbDataInstaller.verbLinesToFst(compoundDataLines.flatten.filter(_.nonEmpty))
    (targetFile).overwrite(verbFst)

  }

  def installIrregularCompounds(compounds: Vector[CompoundEntry], targetFile: File, compoundMap: Map[String, Vector[String]]) : Unit = {
    val compoundDataLines = for ((c,i) <- compounds.zipWithIndex) yield {
      if (compoundMap.keySet.contains(c.simplexLexEnt)) {
        val irregLines =  compoundMap(c.simplexLexEnt)
        val compoundLines = for (ln <- irregLines) yield {
          val cols = ln.split("#").toVector
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

            s"${c.ruleId}_${i}#${c.compoundLexEnt}#${c.prefix}${stem}#${person}#${num}#${tense}#${mood}#${voice}"
          }
        }
        compoundLines
      } else {
        Vector.empty[String]
      }
    }

    val verbFst = IrregVerbDataInstaller.verbLinesToFst(compoundDataLines.flatten.filter(_.nonEmpty))
    (targetFile).overwrite(verbFst)
  }


  def installIrregularParticiples(compounds: Vector[CompoundEntry], targetFile: File, compoundMap: Map[String, Vector[String]]) : Unit = {
    val compoundDataLines = for ((c,i) <- compounds.zipWithIndex) yield {
      if (compoundMap.keySet.contains(c.simplexLexEnt)) {
        val irregLines =  compoundMap(c.simplexLexEnt)
        val compoundLines = for (ln <- irregLines) yield {
          val cols = ln.split("#").toVector
          if (cols.size < 9)  {
            throw new Exception("CompoundVerbDataInstaller: two few columns in data source " + cols)
          } else {
            val ruleId = cols(0)
            val lexent = cols(1)
            val stem = cols(2)
            val  gender =cols(3)
            val grammCase = cols(4)
            val  num = cols(5)
            val tense = cols(6)
            val voice = cols(7)
            val stemClass = cols(8)
            s"${c.ruleId}_${i}#${c.compoundLexEnt}#${c.prefix}${stem}#${gender}#${grammCase}#${num}#${tense}#${voice}"
          }
        }
        compoundLines
      } else {
        Vector.empty[String]
      }
    }
    val ptcplFst = IrregParticipleDataInstaller.participleLinesToFst(compoundDataLines.flatten.filter(_.nonEmpty))
    (targetFile).overwrite(ptcplFst)
  }
  def installIrregularInfinitives(compounds: Vector[CompoundEntry], targetFile: File, compoundMap: Map[String, Vector[String]]) : Unit = {
    val compoundDataLines = for ((c,i) <- compounds.zipWithIndex) yield {
      if (compoundMap.keySet.contains(c.simplexLexEnt)) {
        val irregLines =  compoundMap(c.simplexLexEnt)
        val compoundLines = for (ln <- irregLines) yield {
          val cols = ln.split("#").toVector

          if (cols.size < 5)  {
            throw new Exception("CompoundVerbDataInstaller: two few columns in data source " + cols)
          } else {
            val ruleId = cols(0)
            val lexent = cols(1)
            val stem = cols(2)
            val  tense =cols(3)
            val voice = cols(4)
            s"${c.ruleId}_${i}#${c.compoundLexEnt}#${c.prefix}${stem}#${tense}#${voice}"
          }
        }
        compoundLines
      } else {
        Vector.empty[String]
      }
    }
    val infinFst = IrregInfinitiveDataInstaller.infinitiveLinesToFst(compoundDataLines.flatten.filter(_.nonEmpty))
    (targetFile).overwrite(infinFst)
  }

  def installIrregularGerundives(compounds: Vector[CompoundEntry], targetFile: File, compoundMap: Map[String, Vector[String]]) : Unit = {
    val compoundDataLines = for ((c,i) <- compounds.zipWithIndex) yield {
      if (compoundMap.keySet.contains(c.simplexLexEnt)) {
        val irregLines =  compoundMap(c.simplexLexEnt)
        val compoundLines = for (ln <- irregLines) yield {
          val cols = ln.split("#").toVector

          if (cols.size < 6)  {
            throw new Exception("CompoundVerbDataInstaller: two few columns in data source " + cols)
          } else {
            val ruleId = cols(0)
            val lexent = cols(1)
            val stem = cols(2)
            val gender = cols(3)
            val grammCase = cols(4)
            val num = cols(5)

            s"${c.ruleId}_${i}#${c.compoundLexEnt}#${c.prefix}${stem}#${gender}#${grammCase}#${num}"
          }
        }
        compoundLines
      } else {
        Vector.empty[String]
      }
    }
    val  gdvFst = IrregGerundiveDataInstaller.gerundiveLinesToFst(compoundDataLines.flatten.filter(_.nonEmpty))
    (targetFile).overwrite(gdvFst)
  }

  def installIrregularSupines(compounds: Vector[CompoundEntry], targetFile: File, compoundMap: Map[String, Vector[String]]) : Unit = {
    val compoundDataLines = for ((c,i) <- compounds.zipWithIndex) yield {
      if (compoundMap.keySet.contains(c.simplexLexEnt)) {
        val irregLines =  compoundMap(c.simplexLexEnt)
        val compoundLines = for (ln <- irregLines) yield {
          val cols = ln.split("#").toVector

          if (cols.size < 4)  {
            throw new Exception("CompoundVerbDataInstaller: two few columns in data source " + cols)
          } else {
            val ruleId = cols(0)
            val lexent = cols(1)
            val stem = cols(2)
            val  grammCase =cols(3)

            s"${c.ruleId}_${i}#${c.compoundLexEnt}#${c.prefix}${stem}#${grammCase}"
          }
        }
        compoundLines
      } else {
        Vector.empty[String]
      }
    }
    val  supineFst = IrregSupineDataInstaller.supineLinesToFst(compoundDataLines.flatten.filter(_.nonEmpty))
    (targetFile).overwrite(supineFst)
  }

  /** Map lexical URNs to data for simplex verb stems.
  *
  * @param simplexDir Directory containing .cex files
  * with verb stem data.
  */

  case class KVPair(k: String, v: String)

  def rulesMap(simplexDir: File): Map[String, Vector[String]] = {
    val raw = cexRules(simplexDir)
    //ag.v1#lexent.n2280#am#conj1
    val pairs = raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 4) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line " + s)
      } else {
        val ruleId = cols(0)
        val lexent = cols(1)
        val stem = cols(2)
        val stemClass = cols(3)
        val data = ruleId + "#" + stem + "#" + stemClass
        KVPair(lexent, data)
      }
    })//.toMap
    val grouped = pairs.groupBy(_.k)
    grouped.map{ case (k, v) => (k, v.map(_.v))}
    //HERE:  UNIFY ENTRIES INTO A SINGLE MAP OF VECTOR OF STRINGS
  }

  /** Map lexical URNs to data for irregular verb forms.
  *
  * @param irregDir Directory containing .cex files
  * with verb stem data.
  */
  def irregMap(irregDir: File) : Map[String, Vector[String]] = {
    val raw = cexRules(irregDir)
    val pairs = raw.map( s => {
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
        KVPair(lexent, data)
      }
    })
    val grouped = pairs.groupBy(_.k)
    grouped.map{ case (k, v) => (k, v.map(_.v))}
  }

  def irregParticipleMap(irregDir: File) : Map[String, Vector[String]] = {
    val raw = cexRules(irregDir)
    val pairs = raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 8) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line for irregular participle form " + s)
      } else {
        //Rule#LexicalEntity#Form#Gender#Case#Number#Tense#Voice
        val ruleId = cols(0)
        val lexent = cols(1)
        val stem = cols(2)
        val gender = cols(3)
        val grammCase = cols(4)
        val num = cols(5)
        val tense = cols(6)
        val voice = cols(7)
        val stemClass = "irregptcpl"
        val data = List(ruleId, lexent, stem, gender, grammCase, num,tense,voice,stemClass).mkString("#")
        KVPair(lexent,data)
      }
    })
    val grouped = pairs.groupBy(_.k)
    grouped.map{ case (k, v) => (k, v.map(_.v))}
  }
  def irregInfinitiveMap(irregDir: File) : Map[String, Vector[String]]= {
    val raw = cexRules(irregDir)
    val pairs =  raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 5) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line for irregular infinitive form " + s)
      } else {
        //proof.irrinf1#lexent.n15868#isse#pft#act
        val ruleId = cols(0)
        val lexent = cols(1)
        val stem = cols(2)
        val tense = cols(3)
        val voice = cols(4)
        val stemClass = "irreginfin"
        val data = List(ruleId, lexent, stem, tense,voice,stemClass).mkString("#")
        KVPair(lexent,data)
      }
    })
    val grouped = pairs.groupBy(_.k)
    grouped.map{ case (k, v) => (k, v.map(_.v))}
  }
  def irregGerundiveMap(irregDir: File) = {
    val raw = cexRules(irregDir)
    val pairs = raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 6) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line for irregular gerundive form " + s)
      } else {
        //Rule#LexicalEntity#Form#Gender#Case#Number
        val ruleId = cols(0)
        val lexent = cols(1)
        val stem = cols(2)
        val gender = cols(3)
        val grammCase = cols(4)
        val num = cols(5)
        val stemClass = "irreggrndv"
        val data = List(ruleId, lexent, stem, gender, grammCase, num,stemClass).mkString("#")
        KVPair(lexent,data)
      }
    })
    val grouped = pairs.groupBy(_.k)
    grouped.map{ case (k, v) => (k, v.map(_.v))}
  }
  def irregSupineMap(irregDir: File) = {
    val raw = cexRules(irregDir)
    val pairs = raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 4) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line for irregular supine form " + s)
      } else {
        //proof.irregsup1#lexent.n15868#itu#abl
        val ruleId = cols(0)
        val lexent = cols(1)
        val stem = cols(2)
        val grammCase = cols(3)
        val stemClass = "irregsupn"
        val data = List(ruleId, lexent, stem, grammCase, stemClass).mkString("#")
        KVPair(lexent,data)
      }
    })
    val grouped = pairs.groupBy(_.k)
    grouped.map{ case (k, v) => (k, v.map(_.v))}
  }


  def irregGerundMap(irregDir: File) = {
    val raw = cexRules(irregDir)
    raw.map( s => {
      val cols = s.split("#")
      if (cols.size < 4) {
        throw new Exception("CompoundVerbDataInstaller: too few columns in line for irregular gerund form " + s)
      } else {
        //proof.irrigrd1#lexent.n14599#dandi#gen
        val ruleId = cols(0)
        val lexent = cols(1)
        val stem = cols(2)
        val grammCase = cols(3)
        val stemClass = "irreggrnd"
        val data = List(ruleId, lexent, stem, grammCase,stemClass).mkString("#")
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
