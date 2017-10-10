package edu.holycross.shot.tabulae


/** The inflectional pattern ("rule") component of a full FST parse.
* Implementations of this trait parse FST strings into appropriate
* substrings for each analytical type ("part of speech").
*/
trait FstRule


/** Rule entry for an indeclinable form.
*
* @param ruleId Abbreviated URN string for rule.
* @param pos Part of speech.
*/
case class IndeclRule(ruleId: String, pos: String ) extends FstRule

/** Factory to create full [[IndeclRule]] object from FST.
*
*/
object IndeclRule {

  /** Create [[IndeclRule]] from part of speech and tagged
  * identifier by stripping off tag markers used in FST string.
  *
  * @param pos String identifying part of speech.
  * @param urn FST string marking rule identifier within <u> tag.
  */
  def fromStrings(pos: String, urn: String): IndeclRule = {
    val dataRE  = "<u>(.+)<\\/u>".r
    val dataRE(ruleId) = urn
    IndeclRule( ruleId,pos)
  }
}



/** Rule entry for a noun form.
*
* @param ruleId Abbreviated URN string for rule.
* @param gender String value for gender.
* @param grammaticalCase String value for case.
* @param grammaticalNumber String value for number.
* @param declClass String value for declension class.
* @param ending String value for ending to apply to stem.
*/
case class NounRule(ruleId: String,gender: String, grammaticalCase: String,
grammaticalNumber:String, declClass: String, ending: String ) extends FstRule




/** Factory to create full [[NounRule]] object from FST.
*
*/
object NounRule {


  /** Create full [[NounRule]] object from noun-specific FST.
  *
  * @param declClass String value for declension class.
  * @param nounData Noun-specific FST to parse.
  */
  def apply(declClass: String, nounData: String): NounRule = {
    val dataRE  = "([^<]+)<([^<]+)><([^<]+)><([^<]+)><u>(.+)<\\/u>".r
    val dataRE(ending, gender, grammCase, grammNumber,ruleId) = nounData
    NounRule(ruleId, gender, grammCase, grammNumber, declClass, ending)
  }
}


case class VerbRule(ruleId: String, person: String,
grammaticalNumber:String, tense: String, mood: String,voice: String, inflClass: String, ending: String ) extends FstRule

object VerbRule {
  /** Create full [[VerbRule]] object from verb-specific FST.
  *
  */
  def apply(inflClass: String, verbData: String): VerbRule = {
    val dataRE  = "([^<]+)<([^>]+)><([^>]+)><([^>]+)><([^>]+)><([^>]+)><u>(.+)<\\/u>".r
    val dataRE(ending,person,grammNumber,tense,mood,voice,ruleId) =  verbData
    VerbRule(ruleId, person, grammNumber, tense, mood, voice, inflClass, ending)
  }
}


object FstRule {

  /** Create an [[FstRule]] object from the FST
  * representation of a rule.
  *
  * @param fst The "rule" half of an FST reply.
  */
  def apply(fst: String): FstRule = {
    val idsRE = "<([^<]+)><([^<]+)>(.+)".r
    val idsRE(inflClass, stemType, remainder) = fst
    /*println("FST RULE:\n")
    println(s"\tclass ${inflClass}")
    println(s"\tremainder ${remainder}") */
    stemType match {
      case "noun" => NounRule(inflClass,  remainder)
      case "indecl" => IndeclRule.fromStrings(inflClass, remainder)
      case "verb" =>  VerbRule(inflClass, remainder)

      case s: String => throw new Exception(s"Type ${s} not implemented")
    }
  }

}
