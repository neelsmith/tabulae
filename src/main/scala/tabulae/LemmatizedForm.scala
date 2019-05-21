package edu.holycross.shot.tabulae


/** A valid grammatical form identification.*/
sealed trait LemmatizedForm {
  def lemmaId: String
  def stemId: String
  def ruleId: String
  def posLabel: String = {
    this match {
      case v: VerbForm => "verb"
      /*
      case n: NounForm => "noun"
      case adj: AdjectiveForm => "adjective"
      case adv: AdverbForm => "adverb"
      case gnd: GerundForm => "gerund"
      case indecl: IndeclinableForm => "indeclinable"
      case inf: InfinitiveForm => "infinitive"
      case ptcpl: ParticipleForm => "participle"*/
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
      case vr: VerbRule => {
        VerbForm(stemEntry.lexEntity, stemEntry.stemId, inflection.ruleId, vr.person, vr.grammaticalNumber, vr.tense, vr.mood, vr.voice)
      }
      /*
      case nr: NounRule => {
        NounForm(stemEntry.lexEntity,stemEntry.lexId, nr.gender, nr.grammaticalCase, nr.grammaticalNumber)
      }

      case adjr: AdjectiveRule => {
        AdjectiveForm(stemEntry.lexEntity, stemEntry.lexadjr.gender, adjr.grammaticalCase, adjr.grammaticalNumber, adjr.degree)
      }

      case ir: IndeclRule => {
        IndeclinableForm(stemEntry.lexEntity, ir.pos)
      }


      case gr: GerundRule => {
        GerundForm(stemEntry.lexEntity, gr.grammaticalCase)
      }

      case pr: ParticipleRule => {
        ParticipleForm(stemEntry.lexEntity, pr.gender, pr.grammaticalCase, pr.grammaticalNumber, pr.tense, pr.voice)
      }*/
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
case class VerbForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, person: Person, grammaticalNumber: GrammaticalNumber, tense: Tense, mood: Mood, voice: Voice) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn
}

/** Factory object to build a [[VerbForm]] from string vaues.
*/
object VerbForm {
  /** Create a [[VerbForm]] from five FST symbols.
  */
  def apply(lemmaId:  String, stemId:  String, ruleId:  String, p: String, n: String, t: String, m: String, v: String): VerbForm = {
    VerbForm(lemmaId, stemId, ruleId, personForFstSymbol(p), numberForFstSymbol(n), tenseForFstSymbol(t), moodForFstSymbol(m), voiceForFstSymbol(v))
  }
}
