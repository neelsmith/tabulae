package edu.holycross.shot.tabulae


/** A valid grammatical form identification.*/
sealed trait LemmatizedForm {
  def lemma: String
  def posLabel: String = {
    this match {
      case v: VerbForm => "verb"
      case n: NounForm => "noun"
      case adj: AdjectiveForm => "adjective"
      case adv: AdverbForm => "adverb"
      case gnd: GerundForm => "gerund"
      case indecl: IndeclinableForm => "indeclinable"
      case inf: InfinitiveForm => "infinitive"
      case ptcpl: ParticipleForm => "participle"


    }
  }
}

/** Factory object to create full [[LemmatizedForm]] from a string of FST.
*/
object LemmatizedForm {

  /** From a raw FST string, identify a morphological form.
  *
  * @param s String value of a single FST analysis.
  */
  def apply(s: String): LemmatizedForm = {
    val halves = s.split("<div>")
    require(halves.size == 2, "LemmatizedFrom: could not find <div>-delimited parts of FST string in " + s)

    val stemEntry = FstStem(halves(0))
    val inflection = FstRule(halves(1))

    inflection match {
      case nr: NounRule => {
        NounForm(stemEntry.lexEntity, nr.gender, nr.grammaticalCase, nr.grammaticalNumber)
      }

      case adjr: AdjectiveRule => {
        AdjectiveForm(stemEntry.lexEntity, adjr.gender, adjr.grammaticalCase, adjr.grammaticalNumber, adjr.degree)
      }

      case ir: IndeclRule => {
        IndeclinableForm(stemEntry.lexEntity, ir.pos)
      }
      case vr: VerbRule => {
        VerbForm(stemEntry.lexEntity, vr.person, vr.grammaticalNumber, vr.tense, vr.mood, vr.voice)
      }

      case gr: GerundRule => {
        GerundForm(stemEntry.lexEntity, gr.grammaticalCase)
      }

      case pr: ParticipleRule => {
        ParticipleForm(stemEntry.lexEntity, pr.gender, pr.grammaticalCase, pr.grammaticalNumber, pr.tense, pr.voice)
      }
      case _ => throw new Exception(s"Form.scala: form ${inflection} not yet implemented.")
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
case class VerbForm(lemma: String, person: Person, grammaticalNumber: GrammaticalNumber, tense: Tense, mood: Mood, voice: Voice) extends LemmatizedForm {}

/** Factory object to build a [[VerbForm]] from string vaues.
*/
object VerbForm {
  /** Create a [[VerbForm]] from five FST symbols.
  */
  def apply(lemma: String, p: String, n: String, t: String, m: String, v: String): VerbForm = {
    VerbForm(lemma, personForFstSymbol(p), numberForFstSymbol(n), tenseForFstSymbol(t), moodForFstSymbol(m), voiceForFstSymbol(v))
  }
}

/** Indeclinable form, identified only by their part of speech.
*
* @param pos Part of speech.
*/
case class IndeclinableForm(lemma: String, pos: IndeclinablePoS) extends LemmatizedForm {}
object IndeclinableForm {

  def apply(lemma: String, s: String): IndeclinableForm ={
    //println("INDCL FORM lemma/s "  + lemma + ", " + s)
    IndeclinableForm(lemma, indeclinablePoSForFst(s))
  }
}

/** Noun form, identified by gender, case and number.
*
* @param gender Property for number.
* @param grammaticalCase Property for case.
* @param grammaticalNumber Property for number.
*/
case class NounForm(lemma: String, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends LemmatizedForm {}

/** Factory object to build a [[NounForm]] from string vaues.
*/
object NounForm {
  /** Create a [[NounForm]] from three FST symbols.
  */
  def apply(lemma: String, g: String, c: String, n: String): NounForm = {
    NounForm(lemma, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n))
  }
}


/** Noun form, identified by gender, case and number.
*
* @param grammaticalCase Property for case.
*/
case class GerundForm(lemma: String, grammaticalCase: GrammaticalCase) extends LemmatizedForm {}
/** Factory object to build a [[NounForm]] from string vaues.
*/
object GerundForm {
  /** Create a [[GerundForm]] from one FST symbols.
  */
  def apply( lemma: String, c: String): GerundForm = {
    GerundForm(lemma, caseForFstSymbol(c))
  }
}


/** Adjective form, identified by gender, case, number and degree.
*
* @param gender Property for number.
* @param grammaticalCase Property for case.
* @param grammaticalNumber Property for number.
*/
case class AdjectiveForm(lemma: String, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber, degree: Degree) extends LemmatizedForm {}


object AdjectiveForm {
  /** Create an [[AdjectiveForm]] from four FST symbols.
  */
  def apply(lemma: String, g: String, c: String, n: String, d: String): AdjectiveForm = {
    AdjectiveForm(lemma, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n), degreeForFstSymbol(d))
  }
}

case class AdverbForm(lemma: String, degree: Degree) extends LemmatizedForm {}


case class ParticipleForm(lemma: String, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber, tense: Tense, voice:  Voice) extends LemmatizedForm {}

object ParticipleForm {
  /** Create a [[ParticipleForm]] from four FST symbols.
  */
  def apply(lemma: String, g: String, c: String, n: String, t: String, v: String): ParticipleForm = {
    ParticipleForm(lemma, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n), tenseForFstSymbol(t),voiceForFstSymbol(v))
  }
}


case class InfinitiveForm(lemma: String, tense: Tense, voice:  Voice) extends LemmatizedForm {}
