package edu.holycross.shot.tabulae



sealed trait AnalysisType

object Noun extends AnalysisType
object Adjective extends AnalysisType
object Adverb extends AnalysisType
object Indeclinable extends AnalysisType
object Verb extends AnalysisType
object Infinitive extends AnalysisType
object Participle extends AnalysisType


// supine, gerund...
