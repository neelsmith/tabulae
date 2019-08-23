package edu.holycross.shot.tabulae


/** A valid property used in morphological identification
* following a particular [[AnalysisType]].
*/
sealed trait MorphologicalProperty

/** Case property used in identifying substantives ([[Noun]], [[Adjective]]),
*  and [[Participle]].
*/
sealed trait GrammaticalCase extends MorphologicalProperty

/** Nominative case.*/
case object Nominative extends GrammaticalCase
/** Genitive case.*/
case object Genitive extends GrammaticalCase
/** Dative case.*/
case object Dative extends GrammaticalCase
/** Accusative case.*/
case object Accusative extends GrammaticalCase
/** Ablative case.*/
case object Ablative extends GrammaticalCase
/** Vocative case.*/
case object Vocative extends GrammaticalCase


/** Gender property used in identifying substantives ([[Noun]], [[Adjective]]),
*  and [[Participle]].
*/
sealed trait Gender extends MorphologicalProperty
/** Masculine gender.*/
case object Masculine extends Gender
/** Feminine gender.*/
case object Feminine extends Gender
/** Neuter gender.*/
case object Neuter extends Gender


/** Number property used in identifying conjugated verbs ([[Verb]]),
* participles ([[Participle]]), and substantives ([[Noun]], [[Adjective]]).
*/
sealed trait GrammaticalNumber extends MorphologicalProperty

/** Singular number.*/
case object Singular extends GrammaticalNumber
/** Plural number.*/
case object Plural extends GrammaticalNumber


/** Degree property used in identifying [[Adverb]] and [[Adjective]].
*/
sealed trait Degree extends MorphologicalProperty

/** Positive degree.*/
case object Positive extends Degree
/** Comparative degree.*/
case object Comparative extends Degree
/** Superlative degree.*/
case object Superlative extends Degree


/** Person property used in identifying [[Verb]].
*/
sealed trait Person extends MorphologicalProperty

/** First person.*/
case object First extends Person
/** Second person.*/
case object Second extends Person
/** Third person.*/
case object Third extends Person


/** Tense property used in all verb forms ([[Verb]], [[Participle]], [[Infinitive]]).
*/
sealed trait Tense extends MorphologicalProperty

/** Present tense. */
case object Present extends Tense
/** Imperfect tense. */
case object Imperfect extends Tense
/** Future tense. */
case object Future extends Tense
/** Perfect tense. */
case object Perfect extends Tense
/** Pluperfect tense. */
case object Pluperfect extends Tense
/** FuturePerfect tense. */
case object FuturePerfect extends Tense

/** Mood property used in identifying [[Verb]].
*/
sealed trait Mood extends MorphologicalProperty

/** Indicative mood.*/
case object Indicative extends Mood
/** Subjunctive mood.*/
case object Subjunctive extends Mood
/** Imperative mood.*/
case object Imperative extends Mood

/** Voice property used in identifying [[Verb]].
*/
sealed trait Voice extends MorphologicalProperty
/** Active voice.*/
case object Active extends Voice
/** Passive voice.*/
case object Passive extends Voice


/** Part-of-speech property used in identifying [[Indeclinable]] forms.
*/
sealed trait IndeclinablePoS extends MorphologicalProperty
/** Indeclinable conjunction.*/
case object Conjunction extends IndeclinablePoS
/** Indeclinable preposition.*/
case object Preposition extends IndeclinablePoS
/** Indeclinable exclamation.*/
case object Exclamation extends IndeclinablePoS
/** Indeclinable numeral.*/
case object Numeral extends IndeclinablePoS
