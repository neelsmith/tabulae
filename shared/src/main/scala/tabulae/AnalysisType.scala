package edu.holycross.shot.tabulae



/** A valid analytical pattern for a morphological analysis.*/
sealed trait AnalysisType {
  //def fromCex() :
}

/** Noun analysis type.*/
case object Noun extends AnalysisType {

  def fromStrings(g: String, c: String, n: String)  = {
    
  }

}
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


// irregulars...
/** Adverb analysis type.*/
//object IrregularAdverb extends AnalysisType

//object IrregularNoun extends AnalysisType
