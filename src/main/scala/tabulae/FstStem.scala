package edu.holycross.shot.tabulae

//<u>dev1.n2</u><u>lexent.n2</u>agricol<noun><fem><a_ae>::<a_ae><noun>as<fem><acc><pl><u>lnouninfl.a_ae10</u>


/** The lexical entity ("stem") component of a full FST parse.
* Implementations of this trait parse FST strings into appropriate
* substrings for each analytical type ("part of speech").
*/
trait FstStem


/** Lexicon entry for a verb.
*
* @param stemId Abbreviated URN string for stem.
* @param lexId Abbreviated URN string for lexical entity.
* @param stem Stem string, in FST symbol alphabet.
* @param inflClass String value for inflectional class.
*/
case class VerbStem(stemId: String, lexentId: String, stem: String, inflClass: String) extends FstStem
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


/** Lexicon entry for a noun.
*
* @param stemId Abbreviated URN string for stem.
* @param lexId Abbreviated URN string for lexical entity.
* @param stem Stem string, in FST symbol alphabet.
* @param inflClass String value for inflectional class.
* @param gender String value for gender.
*/
case class NounStem(stemId: String, lexentId: String, stem: String, gender: String, inflClass: String ) extends FstStem


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


  /** FST symbols identifying inflectional type ("part of speech").
  */
  val typeTags: Vector[String] =  Vector(
    "<noun>", "<verb>"
  )

  /** Create an [[FstStem]] object from the FST representation of a stem.
  *
  * @param fst The "stem" half of an FST reply.
  */
  def apply(fst: String):  FstStem = {
    // isolate URNs for stem and for lexical entity:
    val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
    val idsRE(stemId, lexEntity, remainder) = fst

    println(s"GOT from idsRE: ${stemId}, ${lexEntity}, ${remainder}")
    val stemClass =  stemType(remainder)
    println("So stem class is " + stemClass)
    stemClass match {
      case Noun => {
        val parts = remainder.split("<noun>")
        NounStem(stemId, lexEntity, remainder)
      }
      case Verb => {
        val parts = remainder.split("<verb>")
        VerbStem(stemId, lexEntity, remainder)
      }
      case _ => throw new Exception("Type not yet implemented: " + stemClass)
    }

  }


  /** Determine the analyticaltype of an FST reply.
  *
  * @param stemFst The "stem" half of an FST reply.
  */
  def stemType(stemFst: String) : AnalysisType = {
    val typeMatches = typeTags.map( t => {
      val parts = stemFst.split(t).toVector
      parts.size == 2
    })

    val pairs = typeTags.zip(typeMatches).filter(_._2)

    require(pairs.size == 1, "Matched multiple types : " + pairs)
    val pair = pairs(0)
    pair._1 match {
      case "<noun>" => Noun
      case "<verb>" => Verb
    }
  }
}
