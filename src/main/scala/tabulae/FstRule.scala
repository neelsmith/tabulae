package edu.holycross.shot.tabulae

trait FstRule


// <u>vienna.n3_0</u><u>lexent.n3</u>kai/<conjunct>
// \:\:
// <conjunct><indecl><u>indeclinfl.2</u>
case class IndeclRule(ruleId: String, pos: String ) extends FstRule

/** Create full [[IndeclRule]] object from noun-specific FST.
*
*/
object IndeclRule {
  def fromStrings(pos: String, urn: String): IndeclRule = {
    val dataRE  = "<u>(.+)<\\/u>".r
    val dataRE(ruleId) = urn
    IndeclRule( ruleId,pos)
  }
}


/**
*/
case class NounRule(ruleId: String,gender: String, grammaticalCase: String,
grammaticalNumber:String, declClass: String, ending: String ) extends FstRule

object NounRule {
  /** Create full [[NounRule]] object from noun-specific FST.
  *
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
