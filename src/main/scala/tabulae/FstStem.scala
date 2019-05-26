package edu.holycross.shot.tabulae

//<u>dev1.n2</u><u>lexent.n2</u>agricol<noun><fem><a_ae>::<a_ae><noun>as<fem><acc><pl><u>lnouninfl.a_ae10</u>


/** The lexical entity ("stem") component of a full FST parse.
* Implementations of this trait parse FST strings into appropriate
* substrings for each analytical type ("part of speech").
*/
trait FstStem {
  def stem: String
  def stemId: String
  def lexEntity: String
}


/** Lexicon entry for a verb.
*
* @param stemId Abbreviated URN string for stem.
* @param lexEntity Abbreviated URN string for lexical entity.
* @param stem Stem string, in FST symbol alphabet.
* @param inflClass String value for inflectional class.
*/
case class VerbStem(stemId: String, lexEntity: String, stem: String, inflClass: String) extends FstStem
object VerbStem {

  /** Create full [[VerbStem]] object from verb-specific FST.
  *
  * @param stemId Abbreviated URN for stem.
  * @param lexId Abbreviated URN for lexical entity.
  * @param remainder Verb-specific FST to parse.
  */
  def apply(stemId: String, lexId: String, remainder: String): VerbStem = {
    val parts = remainder.split("<verb>")
    VerbStem(stemId, lexId, parts(0), parts(1).replaceFirst("<","").replaceFirst(">",""))
  }
}


/** Lexicon entry for an indeclinable form.
*
* @param stemId Abbreviated URN string for stem.
* @param lexId Abbreviated URN string for lexical entity.
* @param stem Stem string, in FST symbol alphabet.
* @param pos String value for part of speech.
*/
case class IndeclStem(stemId: String, lexEntity: String, stem: String, pos: String ) extends FstStem

object IndeclStem {

  /** Create full [[IndeclStem]] object from noun-specific FST.
  *
  * @param stemId Abbreviated URN for stem.
  * @param lexId Abbreviated URN for lexical entity.
  * @param remainder Noun-specific FST to parse.
  */
  def apply(stemId: String, lexId: String, remainder: String): IndeclStem = {
    //println("INDECL: stem/lexId/remainder = " + Vector(stemId, lexId, remainder).mkString(", "))
    val parts = remainder.split("<indecl>")
    IndeclStem(stemId, lexId, parts(0),parts(1).replaceFirst("<","").replaceFirst(">",""))
  }
}


case class IrregularAdverbStem(stemId: String, lexEntity: String, stem: String, degree: String ) extends FstStem

object IrregularAdverbStem {
    def apply(stemId: String, lexId: String, remainder: String): IrregularAdverbStem = {
      // example: adhuc<pos><irregadv>
      val dataParts = remainder.replaceFirst("<irregadv>","").split("<")
      val stem = dataParts(0)
      val degree = dataParts(1).replaceFirst(">","")
      IrregularAdverbStem(stemId, lexId, stem, degree)
    }
}



//case class IrregularNounStem(stemId: String, lexEntity: String, stem: String, gender: String, grammCase: String, grammNumber: String ) extends FstStem

object IrregularNounStem {
    def apply(stemId: String, lexId: String, remainder: String) : NounStem = {
      val dataParts = remainder.replaceFirst("<irregnoun>","")
      println("D PARTS " + dataParts + s" (from ${remainder})")
      //ivppiter<masc><nom><sg>
      /// (from ivppiter<masc><nom><sg><irregnoun>)
      //val stem = "S1" //dataParts(0)
      val dataRE  = "([^<]*)<([^<]+)><([^<]+)><([^<]+)><irregnoun>".r
      val dataRE(stem, gender, grammCase, grammNumber) =  remainder

      println(s"${stem}-${gender}-${grammCase}-${grammNumber}")
/*
  val dataRE  = "([^<]*)<([^<]+)><([^<]+)><([^<]+)><irregnoun>".r
  //<masc><nom><sg><u>ocremorph.0_is1</u>
  val dataRE(ending, gender, grammCase, grammNumber,ruleId) = nounData
  NounRule(ruleId, gender, grammCase, grammNumber, declClass, ending)
  */
      // "<u>ocremorph.n25359mns</u><u>lexent.n25359</u>ivppiter<masc><nom><sg><irregnoun>"
      //val irregStem = "R2"//dataParts(1)//.replaceFirst(">","")
      //println("WORK ON IRREG STEM AND RULE " + irregStem + " //  " +  stem)

//stemId: String, lexEntity: String, stem: String, gender: String, inflClass: String
      val ns = NounStem(stemId, lexId, stem, gender, "irregnoun")
      println("CREATED REGULAR NounStem from irreg data: " + ns)
      ns
    }
}

/** Lexicon entry for an adjective.
*
* @param stemId Abbreviated URN string for stem.
* @param lexId Abbreviated URN string for lexical entity.
* @param stem Stem string, in FST symbol alphabet.
* @param inflClass String value for inflectional class.
*/
case class AdjectiveStem(stemId: String, lexEntity: String, stem: String, inflClass: String ) extends FstStem

/** Factory object to build [[AdjectiveStem]] from a noun-specific
* string with undifferentiated analytical parts.
*/
object AdjectiveStem {

  /** Create full [[AdjectiveStem]] object from noun-specific FST.
  *
  * @param stemId Abbreviated URN for stem.
  * @param lexId Abbreviated URN for lexical entity.
  * @param remainder Noun-specific FST to parse.
  */
  def apply(stemId: String, lexId: String, remainder: String): AdjectiveStem = {
    val parts = remainder.split("<adj>")
    val inflectionClass =  parts(1).replaceAll("[<>]", "")
    AdjectiveStem(stemId, lexId, parts(0), inflectionClass)
  }
}


/** Lexicon entry for a noun.
*
* @param stemId Abbreviated URN string for stem.
* @param lexId Abbreviated URN string for lexical entity.
* @param fstStem Stem string, in FST symbol alphabet.
* @param inflClass String value for inflectional class.
* @param gender String value for gender.
*/
case class NounStem(stemId: String, lexEntity: String, stem: String, gender: String, inflClass: String ) extends FstStem  {
  //def stem : String = fstStem
}

/** Factory object to build [[NounStem]] from a noun-specific
* string with undifferentiated analytical parts.
*/
object NounStem {

  /** Create full [[NounStem]] object from noun-specific FST.
  *
  * @param stemId Abbreviated URN for stem.
  * @param lexId Abbreviated URN for lexical entity.
  * @param remainder Noun-specific FST to parse.
  */
  def apply(stemId: String, lexId: String, remainder: String): NounStem = {
    val parts = remainder.split("<noun>")
    val nounRE = "<([^>]+)><([^>]+)>".r
    val nounRE(gender, inflectionClass) =  parts(1)
    NounStem(stemId, lexId, parts(0), gender, inflectionClass)
  }
}


/** Factory object for creating [[FstStem]] objects
* from the "stem" half of a FST reply.
*/
object FstStem {

  /** Create an [[FstStem]] object from the FST representation of a stem.
  *
  * @param fst The "stem" half of an FST reply.
  */
  def apply(fst: String):  FstStem = {
    // isolate URNs for stem and for lexical entity:
    val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
    val idsRE(stemId, lexEntity, remainder) = fst

    //println(s"GOT from idsRE: ${stemId}, ${lexEntity}, ${remainder}")
    val stemClass =  stemType(remainder)
    //println("So stem class is " + stemClass)
    stemClass match {
      case Noun => NounStem(stemId, lexEntity, remainder)
      case Verb => VerbStem(stemId, lexEntity, remainder)
      case Indeclinable =>  IndeclStem(stemId, lexEntity, remainder)
      case Adjective =>  AdjectiveStem(stemId, lexEntity, remainder)

      // Irregular forms:
      case IrregularAdverb =>  IrregularAdverbStem(stemId, lexEntity, remainder)
      case IrregularNoun =>  IrregularNounStem(stemId, lexEntity, remainder)

      case _ => throw new Exception("FstStem: type not yet implemented: " + stemClass)
    }

  }


  /** Determine the analyticaltype of an FST reply.
  *
  * @param stemFst The "stem" half of an FST reply.
  */
  def stemType(stemFst: String) : AnalysisType = {

    // FST symbols identifying inflectional type ("part of speech").
    val posTags: Vector[String] =  Vector(
      "<noun>", "<verb>","<indecl>", "<adj>", "<pron>",
      "<irregcverb>", "<irregnoun>", "<irregadj>", "<irregadv>", "<irreginfin>","<irregptcpl>","<irreggrnd>","<irreggrndv>",
      "<irregsupn>", "<irregpron>"
    )
    // Define true/false for match with each allowed pos tag:
    val typeMatches = posTags.map( t => {
      stemFst.contains(t)
    })
    // zip T/F and tags together, filter on true:
    val pairs = posTags.zip(typeMatches).filter(_._2)
    require(pairs.size == 1, s"For stem fst ${stemFst}, did not match a unique type : " + pairs)
    val pair = pairs(0)
    pair._1 match {
      case "<noun>" => Noun
      case "<verb>" => Verb
      case "<indecl>" => Indeclinable
      case "<adj>" => Adjective
      case "<irregadv>" => IrregularAdverb
      case "<irregnoun>" => IrregularNoun

      case _ => { println("Could not figure out stem type " + pair._1); throw new Exception("FstStem: did not recognize type " + pair._1)}

    }
  }
}
