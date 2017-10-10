package edu.holycross.shot.tabulae



sealed trait Form

object Form {

  /** From a raw FST string, identify a morphological form.
  *
  * @param s String value of a single FST analysis.
  */
  def apply(s: String): Form = {
    val halves = s.split("::")
    require(halves.size == 2, "Could not find :: delimited parts of FST string " + s)

    val inflection = FstRule(halves(1))

    inflection match {
      case nr: NounRule => {
        NounForm(nr.gender, nr.grammaticalCase, nr.grammaticalNumber)
      }
      case ir: IndeclRule => {
        IndeclinableForm(ir.pos)
      }
      case vr: VerbRule => {
        VerbForm(vr.person, vr.grammaticalNumber, vr.tense, vr.mood, vr.voice)
      }
      case _ => throw new Exception(s"Form ${inflection} not yet implemented.")
    }
  }
}

case class VerbForm(person: Person, grammaticalNumber: GrammaticalNumber, tense: Tense, mood: Mood, voice: Voice) extends Form {}

object VerbForm {
  /** Create a [[VerbForm]] from five FST symbols.
  */
  def apply(p: String, n: String, t: String, m: String, v: String): VerbForm = {
    VerbForm(personForFstSymbol(p), numberForFstSymbol(n), tenseForFstSymbol(t), moodForFstSymbol(m), voiceForFstSymbol(v))
  }
}



/** Indeclinable forms are identified only by their part of speech.
*
* @param pos Part of speech.
*/
case class IndeclinableForm(pos: IndeclinablePoS) extends Form {}
object IndeclinableForm {

  def apply(s: String): IndeclinableForm ={
    IndeclinableForm(indeclinablePoSForFst(s))
  }
}


case class NounForm(gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends Form {}

object NounForm {
  /** Create a [[NounForm]] from three FST symbols.
  */
  def apply(g: String, c: String, n: String): NounForm = {
    NounForm(genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n))
  }
}


case class AdjectiveForm(gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber, degree: Degree) extends Form {}
