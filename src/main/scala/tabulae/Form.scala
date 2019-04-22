package edu.holycross.shot.tabulae


/** A valid grammatical form identification.*/
sealed trait Form

/** Factory object to create full [[Form]] from a string of FST.
*/
object Form {

  /** From a raw FST string, identify a morphological form.
  *
  * @param s String value of a single FST analysis.
  */
  def apply(s: String): Form = {
    val halves = s.split("<div>")
    require(halves.size == 2, "Could not find <div>-delimited parts of FST string " + s)

    val inflection = FstRule(halves(1))

    inflection match {
      case nr: NounRule => {
        NounForm(nr.gender, nr.grammaticalCase, nr.grammaticalNumber)
      }
      case adjr: AdjectiveRule => {
        AdjectiveForm(adjr.gender, adjr.grammaticalCase, adjr.grammaticalNumber, adjr.degree)
      }

      case ir: IndeclRule => {
        IndeclinableForm(ir.pos)
      }
      case vr: VerbRule => {
        VerbForm(vr.person, vr.grammaticalNumber, vr.tense, vr.mood, vr.voice)
      }

      case gr: GerundRule => {
        GerundForm(gr.grammaticalCase)
      }

      case pr: ParticipleRule => {
        ParticipleForm(pr.gender, pr.grammaticalCase, pr.grammaticalNumber, pr.tense, pr.voice)
      }
      case _ => throw new Exception(s"Form ${inflection} not yet implemented.")
    }
  }
}


/** Conjugated verb form, identified by person, number, tense, mood and voice.
*
* @param person Property for person.
* @param grammaticalNumber Property for number.
* @param tense Property for tense.
* @param mood Property for mood.
* @param voice Property for voice.
*/
case class VerbForm(person: Person, grammaticalNumber: GrammaticalNumber, tense: Tense, mood: Mood, voice: Voice) extends Form {}

/** Factory object to build a [[VerbForm]] from string vaues.
*/
object VerbForm {
  /** Create a [[VerbForm]] from five FST symbols.
  */
  def apply(p: String, n: String, t: String, m: String, v: String): VerbForm = {
    VerbForm(personForFstSymbol(p), numberForFstSymbol(n), tenseForFstSymbol(t), moodForFstSymbol(m), voiceForFstSymbol(v))
  }
}

/** Indeclinable form, identified only by their part of speech.
*
* @param pos Part of speech.
*/
case class IndeclinableForm(pos: IndeclinablePoS) extends Form {}
object IndeclinableForm {

  def apply(s: String): IndeclinableForm ={
    IndeclinableForm(indeclinablePoSForFst(s))
  }
}

/** Noun form, identified by gender, case and number.
*
* @param gender Property for number.
* @param grammaticalCase Property for case.
* @param grammaticalNumber Property for number.
*/
case class NounForm(gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends Form {}

/** Factory object to build a [[NounForm]] from string vaues.
*/
object NounForm {
  /** Create a [[NounForm]] from three FST symbols.
  */
  def apply(g: String, c: String, n: String): NounForm = {
    NounForm(genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n))
  }
}


/** Noun form, identified by gender, case and number.
*
* @param grammaticalCase Property for case.
*/
case class GerundForm(grammaticalCase: GrammaticalCase) extends Form {}
/** Factory object to build a [[NounForm]] from string vaues.
*/
object GerundForm {
  /** Create a [[GerundForm]] from one FST symbols.
  */
  def apply( c: String): GerundForm = {
    GerundForm(caseForFstSymbol(c))
  }
}


/** Adjective form, identified by gender, case, number and degree.
*
* @param gender Property for number.
* @param grammaticalCase Property for case.
* @param grammaticalNumber Property for number.
*/
case class AdjectiveForm(gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber, degree: Degree) extends Form {}


object AdjectiveForm {
  /** Create an [[AdjectiveForm]] from four FST symbols.
  */
  def apply(g: String, c: String, n: String, d: String): AdjectiveForm = {
    AdjectiveForm(genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n), degreeForFstSymbol(d))
  }
}

case class AdverbForm(degree: Degree) extends Form {}


case class ParticipleForm(gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber, tense: Tense, voice:  Voice) extends Form {}

object ParticipleForm {
  /** Create a [[ParticipleForm]] from four FST symbols.
  */
  def apply(g: String, c: String, n: String, t: String, v: String): ParticipleForm = {
    ParticipleForm(genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n), tenseForFstSymbol(t),voiceForFstSymbol(v))
  }
}


case class InfinitiveForm(tense: Tense, voice:  Voice) extends Form {}
