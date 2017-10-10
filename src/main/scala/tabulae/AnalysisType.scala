package edu.holycross.shot.tabulae



sealed trait AnalysisType

/** Noun analysis type.*/
object Noun extends AnalysisType
/** Adjective analysis type.*/
object Adjective extends AnalysisType
/** Adverb analysis type.*/
object Adverb extends AnalysisType
/** Analysis type for indeclinable form.*/
object Indeclinable extends AnalysisType
/** Analysis type for conjugated verb form.*/
object Verb extends AnalysisType
/** Analysis type for infinitive verb form.*/
object Infinitive extends AnalysisType
/** Analysis type for participial verb form.*/
object Participle extends AnalysisType


// supine, gerund...
