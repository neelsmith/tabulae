package edu.holycross.shot.tabulae



sealed trait MorphologicalProperty

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


sealed trait Gender extends MorphologicalProperty
/** Masculine gender.*/
case object Masculine extends Gender
/** Feminine gender.*/
case object Feminine extends Gender
/** Neuter gender.*/
case object Neuter extends Gender



sealed trait GrammaticalNumber extends MorphologicalProperty

/** Singular number.*/
case object Singular extends GrammaticalNumber
/** Plural number.*/
case object Plural extends GrammaticalNumber


sealed trait Degree extends MorphologicalProperty

/** Positive degree.*/
case object Positive extends Degree
/** Comparative degree.*/
case object Comparative extends Degree
/** Superlative degree.*/
case object Superlative extends Degree

sealed trait Person extends MorphologicalProperty

/** First person.*/
case object First extends Person
/** Second person.*/
case object Second extends Person
/** Third person.*/
case object Third extends Person

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


sealed trait Mood extends MorphologicalProperty

/** Indicative mood.*/
case object Indicative extends Mood
/** Subjunctive mood.*/
case object Subjunctive extends Mood
/** Imperative mood.*/
case object Imperative extends Mood


sealed trait Voice extends MorphologicalProperty
/** Active voice.*/
case object Active extends Voice
/** Passive voice.*/
case object Passive extends Voice


sealed trait IndeclinablePoS extends MorphologicalProperty
/** Indeclinable conjunction.*/
case object Conjunction extends IndeclinablePoS
/** Indeclinable preposition.*/
case object Preposition extends IndeclinablePoS
/** Indeclinable exclamation.*/
case object Exclamation extends IndeclinablePoS
