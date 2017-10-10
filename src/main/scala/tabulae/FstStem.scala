package edu.holycross.shot.tabulae

 /** Multicharacter symbols that can appear in stem strings.  */
//  static ArrayList editorialTags = ["<#>", "<lo>", "<sh>", "<ro>", "<sm>", "<isub>"]


trait FstStem

/** String values from FST representation of a noun stem.
*
* @param stemId Abbreviated URN for stem.
* @param lexId Abbreviated URN for lexical entity.
* @param stem Stem string, in FST symbols.
* @param inflClass Inflectional class.
* @param gender String value for gender.
* @param accent Persisten accent pattern.
*/
case class NounStem(stemId: String, lexentId: String, stem: String, gender: String, inflClass: String,  accent: String) extends FstStem

object NounStem {

  /** Create full [[NounStem]] object from noun-specific FST.
  *
  * @param stemId Abbreviated URN for stem.
  * @param lexId Abbreviated URN for lexical entity.
  * @param remainder Noun-specific FST to parse.
  */
  def apply(stemId: String, lexId: String, remainder: String): NounStem = {
    val parts = remainder.split("<noun>")
    val nounRE = "<([^>]+)><([^>]+)><([^>]+)>".r
    val nounRE(gender, inflectionClass, accent) =  parts(1)
    NounStem(stemId, lexId, parts(0), gender, inflectionClass, accent)
  }
}



object FstStem {


  val typeTags: Vector[String] =  Vector(
    "<noun>", "<verb>"
  )

  /** Create an [[FstStem]] object from the FST
  * representation of a stem.
  *
  * @param fst The "stem" half of an FST reply.
  */
  def apply(fst: String):  FstStem = {
    val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
    val idsRE(stemId, lexEntity, remainder) = fst
    val stemClass =   stemType(remainder)
    stemClass match {
      case Noun => {
        val parts = remainder.split("<noun>")
        NounStem(stemId, lexEntity, remainder)

      }
      case _ => throw new Exception("Typde not yet implementded: ")
    }

  }


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
    }
  }
}
