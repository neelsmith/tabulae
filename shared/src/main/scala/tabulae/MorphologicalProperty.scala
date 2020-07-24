package edu.holycross.shot.tabulae


/** A valid property used in morphological identification
* following a particular [[AnalysisType]].
*/
sealed trait MorphologicalProperty {
    def decimalCode: Int
}

/** Case property used in identifying substantives ([[Noun]], [[Adjective]]),
*  and [[Participle]].
*/
sealed trait GrammaticalCase extends MorphologicalProperty

/** Nominative case.*/
case object Nominative extends GrammaticalCase {
  def decimalCode: Int = 1
}
/** Genitive case.*/
case object Genitive extends GrammaticalCase {
  def decimalCode: Int = 2
}
/** Dative case.*/
case object Dative extends GrammaticalCase {
  def decimalCode: Int = 3
}
/** Accusative case.*/
case object Accusative extends GrammaticalCase {
  def decimalCode: Int = 4
}
/** Ablative case.*/
case object Ablative extends GrammaticalCase {
  def decimalCode: Int = 5
}
/** Vocative case.*/
case object Vocative extends GrammaticalCase {
  def decimalCode: Int = 6
}


/** Gender property used in identifying substantives ([[Noun]], [[Adjective]]),
*  and [[Participle]].
*/
sealed trait Gender extends MorphologicalProperty
/** Masculine gender.*/
case object Masculine extends Gender {
  def decimalCode: Int = 1
}
/** Feminine gender.*/
case object Feminine extends Gender {
  def decimalCode: Int = 2
}
/** Neuter gender.*/
case object Neuter extends Gender {
  def decimalCode: Int = 3
}


/** Number property used in identifying conjugated verbs ([[Verb]]),
* participles ([[Participle]]), and substantives ([[Noun]], [[Adjective]]).
*/
sealed trait GrammaticalNumber extends MorphologicalProperty

/** Singular number.*/
case object Singular extends GrammaticalNumber {
  def decimalCode: Int = 1
}
/** Plural number.*/
case object Plural extends GrammaticalNumber  {
  def decimalCode: Int = 2
}


/** Degree property used in identifying [[Adverb]] and [[Adjective]].
*/
sealed trait Degree extends MorphologicalProperty

/** Positive degree.*/
case object Positive extends Degree  {
  def decimalCode: Int = 1
}
/** Comparative degree.*/
case object Comparative extends Degree  {
  def decimalCode: Int = 2
}
/** Superlative degree.*/
case object Superlative extends Degree {
  def decimalCode: Int = 3
}


/** Person property used in identifying [[Verb]].
*/
sealed trait Person extends MorphologicalProperty

/** First person.*/
case object First extends Person {
  def decimalCode: Int = 1
}
/** Second person.*/
case object Second extends Person {
  def decimalCode: Int = 2
}
/** Third person.*/
case object Third extends Person {
  def decimalCode: Int = 3
}


/** Tense property used in all verb forms ([[Verb]], [[Participle]], [[Infinitive]]).
*/
sealed trait Tense extends MorphologicalProperty

/** Present tense. */
case object Present extends Tense {
  def decimalCode: Int = 1
}
/** Imperfect tense. */
case object Imperfect extends Tense {
  def decimalCode: Int = 2
}
/** Future tense. */
case object Future extends Tense {
  def decimalCode: Int = 3
}
/** Perfect tense. */
case object Perfect extends Tense {
  def decimalCode: Int = 4
}
/** Pluperfect tense. */
case object Pluperfect extends Tense {
  def decimalCode: Int = 5
}
/** FuturePerfect tense. */
case object FuturePerfect extends Tense {
  def decimalCode: Int = 6
}

/** Mood property used in identifying [[Verb]].
*/
sealed trait Mood extends MorphologicalProperty

/** Indicative mood.*/
case object Indicative extends Mood {
  def decimalCode: Int = 1
}
/** Subjunctive mood.*/
case object Subjunctive extends Mood {
  def decimalCode: Int = 2
}
/** Imperative mood.*/
case object Imperative extends Mood {
  def decimalCode: Int = 3
}

/** Voice property used in identifying [[Verb]].
*/
sealed trait Voice extends MorphologicalProperty
/** Active voice.*/
case object Active extends Voice {
  def decimalCode: Int = 1
}
/** Passive voice.*/
case object Passive extends Voice {
  def decimalCode: Int = 2
}


/** Part-of-speech property used in identifying [[Indeclinable]] forms.
*/
sealed trait IndeclinablePoS extends MorphologicalProperty
/** Indeclinable conjunction.*/
case object Conjunction extends IndeclinablePoS {
  def decimalCode: Int = 1
}
/** Indeclinable preposition.*/
case object Preposition extends IndeclinablePoS {
  def decimalCode: Int = 2
}
/** Indeclinable exclamation.*/
case object Exclamation extends IndeclinablePoS {
  def decimalCode: Int = 3
}
/** Indeclinable numeral.*/
case object Numeral extends IndeclinablePoS {
  def decimalCode: Int = 4
}
