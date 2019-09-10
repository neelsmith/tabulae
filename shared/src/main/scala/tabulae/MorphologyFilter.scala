package edu.holycross.shot.tabulae


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** Identify any set of possible [[MorphologicalProperty]]s.
*/
case class MorphologyFilter(
  pos: Option[String] = None,
  person: Option[Person] = None,
  grammaticalNumber: Option[GrammaticalNumber] = None,
  tense: Option[Tense] = None,
  mood: Option[Mood] = None,
  voice: Option[Voice] = None,
  gender: Option[Gender] = None,
  grammaticalCase: Option[GrammaticalCase] = None,
  degree: Option[Degree] = None,
  indeclinablePoS : Option[IndeclinablePoS] = None
) extends LogSupport {
  Logger.setDefaultLogLevel(LogLevel.DEBUG)


  /** True if a pos is specified and agrees with the pos of
  * the given form.
  *
  * @param lemmaizedForm Form to check.
  */
  def posAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    pos match {
      case None => true
      case posString: Option[String] => (posString.get == lemmatizedForm.posLabel)
    }
  }
  /** True if a person is specified and agrees with the value of
  * the given form.
  *
  * @param lemmatizedForm Form to check.
  */
  def personAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    person match {
      case None => true
      case personOpt: Option[Person] => (personOpt == lemmatizedForm.verbPerson)
    }
  }
  def numberAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    grammaticalNumber match {
      case None => true
      case numberOpt: Option[GrammaticalNumber] => ((numberOpt == lemmatizedForm.verbNumber) || (numberOpt == lemmatizedForm.substantiveNumber) )
    }
  }
  def tenseAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    tense match {
      case None => true
      case tenseOpt: Option[Tense] => ((tenseOpt == lemmatizedForm.verbTense) || (tenseOpt == lemmatizedForm.infinitiveTense) || (tenseOpt == lemmatizedForm.participleTense) )
    }
  }
  def moodAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    mood match {
      case None => true
      case moodOpt: Option[Mood] => (moodOpt == lemmatizedForm.verbMood)
    }
  }
  def voiceAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    voice match {
      case None => true
      case voiceOpt: Option[Voice] => ((voiceOpt == lemmatizedForm.verbVoice) || (voiceOpt == lemmatizedForm.infinitiveVoice) || (voiceOpt == lemmatizedForm.participleVoice))
    }
  }
  def genderAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    gender match {
      case None => true
      case genderOpt: Option[Gender] => (genderOpt == lemmatizedForm.substantiveGender)
    }
  }
  def caseAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    grammaticalCase match {
      case None => true
      case caseOpt: Option[GrammaticalCase] => (caseOpt == lemmatizedForm.substantiveCase)
    }
  }
  def degreeAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    degree match {
      case None => true
      case degreeOpt: Option[Degree] => (degreeOpt == lemmatizedForm.adverbDegree)
    }
  }
  def indeclinableAgrees(lemmatizedForm: LemmatizedForm) : Boolean = {
    indeclinablePoS match {
      case None => true
      case indeclOpt: Option[IndeclinablePoS] => (indeclOpt == lemmatizedForm.indeclinablePartOfSpeech)
    }
  }


  /** If if a give [[LemmatizedForm]] matches all specifications of
  * the [[MorphologyFilter]].
  *
  * @param form Form to check.
  */
  def agrees(form: LemmatizedForm) : Boolean = {

    posAgrees(form) && personAgrees(form) && numberAgrees(form) && tenseAgrees(form) && moodAgrees(form) && voiceAgrees(form) && genderAgrees(form) && caseAgrees(form) && degreeAgrees(form) && indeclinableAgrees(form)
  }
}
