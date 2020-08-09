package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** Classes extending [[ValidForm]] are required to
* report whether the values encoded in `urn`  are valid
* for that inflectional type ("part of speech").
*/
sealed trait ValidForm {
  def urn: Cite2Urn
  def validUrnValue: Boolean
  def label: String = ValidForm.labels(urn.toString)
}

object ValidForm {
  def apply(urn: Cite2Urn) : ValidForm = {
    val digits = urn.objectComponent.split("").toVector
    val partOfSpeech = digits(columnNames("inflectionType"))
    partOfSpeech match {
      case "0" => ValidNounForm(urn)
      case "1" => ValidPronounForm(urn)
      case "2" => ValidAdjectiveForm(urn)
      case "3" => ValidAdverbForm(urn)
      case "4" => ValidFiniteVerbForm(urn)
      case "5" => ValidParticipleForm(urn)
      case "6" => ValidInfinitiveForm(urn)
      case "7" => ValidGerundiveForm(urn)
      case "8" => ValidGerundForm(urn)
      case "9" => ValidSupineForm(urn)
      case  "A" => ValidUninflectedForm(urn)
      case  "B" => ValidUninflectedForm(urn)
      case  "C" => ValidUninflectedForm(urn)
      case  "D" => ValidUninflectedForm(urn)

      case _ => throw new Exception("Can not parse PoS value " + partOfSpeech)
    }
  }

  def validValue(digit: String, validValues: Vector[String]) : Boolean = {
    val validDigit = validValues.contains(digit)
    validDigit
  }

  def correctZeroes(digits: Vector[String], zeroColumns: Vector[Int]): Boolean = {
    val tf = zeroColumns.map(i => digits(i) == "0").distinct
    (tf.size == 1) && (tf.head)
  }

  val columnNames: Map[String, Int] = Map(
    "person" -> 0,
    "grammaticalNumber" -> 1,
    "tense" -> 2,
    "mood" -> 3,
    "voice" -> 4,
    "gender" -> 5,
    "grammaticalCase" -> 6,
    "degree" -> 7,
    "inflectionType" -> 8
  )

  val genderCodes: Map[String, Gender] = Map(
     "1" -> Masculine,
     "2" -> Feminine,
     "3" -> Neuter
  )

  val caseCodes : Map[String, GrammaticalCase] = Map (
     "1" -> Nominative,
     "2" -> Genitive,
     "3" -> Dative,
     "4" -> Accusative,
     "5" -> Ablative,
     "6" -> Vocative
  )

  val numberCodes : Map[String, GrammaticalNumber] = Map (
    "1" -> Singular,
    "2" -> Plural
  )
  val degreeCodes : Map[String, Degree] = Map (
    "1" -> Positive,
    "2" -> Comparative,
    "3" -> Superlative
  )
  val personCodes: Map[String, Person] = Map(
    "1" -> First,
    "2" -> Second,
    "3" -> Third
  )
  val tenseCodes: Map[String, Tense] = Map(
    "1" -> Present,
    "2" -> Imperfect,
    "3" -> Future,
    "4" -> Perfect,
    "5" -> Pluperfect,
    "6" -> FuturePerfect
  )
  val moodCodes: Map[String, Mood] = Map(
    "1" -> Indicative,
    "2" -> Subjunctive,
    "3" -> Imperative
  )
  val voiceCodes: Map[String, Voice] = Map(
    "1" -> Active,
    "2" -> Passive
  )
  val posCodes: Map[String, IndeclinablePoS] = Map(
    "A" -> Conjunction,
    "B" -> Preposition,
    "C" -> Exclamation,
    "D" -> Numeral
  )
  val labels : Map[String, String] = Map(
    "urn:cite2:tabulae:morphforms.v1:00000000A" -> "uninflected conjunction",
    "urn:cite2:tabulae:morphforms.v1:00000000B" -> "uninflected preposition",
    "urn:cite2:tabulae:morphforms.v1:00000000C" -> "uninflected exclamation",
    "urn:cite2:tabulae:morphforms.v1:00000000D" -> "uninflected number",
    "urn:cite2:tabulae:morphforms.v1:000000013" -> "adverb: positive degree",
    "urn:cite2:tabulae:morphforms.v1:000000023" -> "adverb: comparative degree",
    "urn:cite2:tabulae:morphforms.v1:000000033" -> "adverb: superlative degree",
    "urn:cite2:tabulae:morphforms.v1:000000108" -> "gerund: nominative",
    "urn:cite2:tabulae:morphforms.v1:000000208" -> "gerund: genitive",
    "urn:cite2:tabulae:morphforms.v1:000000308" -> "gerund: dative",
    "urn:cite2:tabulae:morphforms.v1:000000408" -> "gerund: accusative",
    "urn:cite2:tabulae:morphforms.v1:000000508" -> "gerund: ablative",
    "urn:cite2:tabulae:morphforms.v1:000000409" -> "supine: accusative",
    "urn:cite2:tabulae:morphforms.v1:000000509" -> "supine: ablative",
    "urn:cite2:tabulae:morphforms.v1:001010006" -> "infinitive: present active",
    "urn:cite2:tabulae:morphforms.v1:001020006" -> "infinitive: present passive",
    "urn:cite2:tabulae:morphforms.v1:003010006" -> "infinitive: future active",
    "urn:cite2:tabulae:morphforms.v1:003020006" -> "infinitive: future passive",
    "urn:cite2:tabulae:morphforms.v1:004010006" -> "infinitive: perfect active",
    "urn:cite2:tabulae:morphforms.v1:004020006" -> "infinitive: perfect passive",
    "urn:cite2:tabulae:morphforms.v1:010001100" -> "noun: masculine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010001101" -> "pronoun: masculine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010001107" -> "gerundive: masculine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010001112" -> "adjective: masculine nominative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010001122" -> "adjective: masculine nominative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010001132" -> "adjective: masculine nominative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010001200" -> "noun: masculine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010001201" -> "pronoun: masculine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010001207" -> "gerundive: masculine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010001212" -> "adjective: masculine genitive singular positive",
    "urn:cite2:tabulae:morphforms.v1:010001222" -> "adjective: masculine genitive singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010001232" -> "adjective: masculine genitive singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010001300" -> "noun: masculine dative singular",
    "urn:cite2:tabulae:morphforms.v1:010001301" -> "pronoun: masculine dative singular",
    "urn:cite2:tabulae:morphforms.v1:010001307" -> "gerundive: masculine dative singular",
    "urn:cite2:tabulae:morphforms.v1:010001312" -> "adjective: masculine dative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010001322" -> "adjective: masculine dative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010001332" -> "adjective: masculine dative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010001400" -> "noun: masculine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010001401" -> "pronoun: masculine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010001407" -> "gerundive: masculine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010001412" -> "adjective: masculine accusative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010001422" -> "adjective: masculine accusative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010001432" -> "adjective: masculine accusative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010001500" -> "noun: masculine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010001501" -> "pronoun: masculine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010001507" -> "gerundive: masculine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010001512" -> "adjective: masculine ablative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010001522" -> "adjective: masculine ablative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010001532" -> "adjective: masculine ablative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010001600" -> "noun: masculine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010001601" -> "pronoun: masculine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010001607" -> "gerundive: masculine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010001612" -> "adjective: masculine vocative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010001622" -> "adjective: masculine vocative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010001632" -> "adjective: masculine vocative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010002100" -> "noun: feminine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010002101" -> "pronoun: feminine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010002107" -> "gerundive: feminine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010002112" -> "adjective: feminine nominative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010002122" -> "adjective: feminine nominative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010002132" -> "adjective: feminine nominative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010002200" -> "noun: feminine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010002201" -> "pronoun: feminine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010002207" -> "gerundive: feminine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010002212" -> "adjective: feminine genitive singular positive",
    "urn:cite2:tabulae:morphforms.v1:010002222" -> "adjective: feminine genitive singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010002232" -> "adjective: feminine genitive singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010002300" -> "noun: feminine dative singular",
    "urn:cite2:tabulae:morphforms.v1:010002301" -> "pronoun: feminine dative singular",
    "urn:cite2:tabulae:morphforms.v1:010002307" -> "gerundive: feminine dative singular",
    "urn:cite2:tabulae:morphforms.v1:010002312" -> "adjective: feminine dative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010002322" -> "adjective: feminine dative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010002332" -> "adjective: feminine dative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010002400" -> "noun: feminine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010002401" -> "pronoun: feminine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010002407" -> "gerundive: feminine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010002412" -> "adjective: feminine accusative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010002422" -> "adjective: feminine accusative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010002432" -> "adjective: feminine accusative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010002500" -> "noun: feminine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010002501" -> "pronoun: feminine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010002507" -> "gerundive: feminine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010002512" -> "adjective: feminine ablative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010002522" -> "adjective: feminine ablative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010002532" -> "adjective: feminine ablative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010002600" -> "noun: feminine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010002601" -> "pronoun: feminine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010002607" -> "gerundive: feminine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010002612" -> "adjective: feminine vocative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010002622" -> "adjective: feminine vocative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010002632" -> "adjective: feminine vocative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010003100" -> "noun: neuter nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010003101" -> "pronoun: neuter nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010003107" -> "gerundive: neuter nominative singular",
    "urn:cite2:tabulae:morphforms.v1:010003112" -> "adjective: neuter nominative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010003122" -> "adjective: neuter nominative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010003132" -> "adjective: neuter nominative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010003200" -> "noun: neuter genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010003201" -> "pronoun: neuter genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010003207" -> "gerundive: neuter genitive singular",
    "urn:cite2:tabulae:morphforms.v1:010003212" -> "adjective: neuter genitive singular positive",
    "urn:cite2:tabulae:morphforms.v1:010003222" -> "adjective: neuter genitive singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010003232" -> "adjective: neuter genitive singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010003300" -> "noun: neuter dative singular",
    "urn:cite2:tabulae:morphforms.v1:010003301" -> "pronoun: neuter dative singular",
    "urn:cite2:tabulae:morphforms.v1:010003307" -> "gerundive: neuter dative singular",
    "urn:cite2:tabulae:morphforms.v1:010003312" -> "adjective: neuter dative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010003322" -> "adjective: neuter dative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010003332" -> "adjective: neuter dative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010003400" -> "noun: neuter accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010003401" -> "pronoun: neuter accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010003407" -> "gerundive: neuter accusative singular",
    "urn:cite2:tabulae:morphforms.v1:010003412" -> "adjective: neuter accusative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010003422" -> "adjective: neuter accusative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010003432" -> "adjective: neuter accusative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010003500" -> "noun: neuter ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010003501" -> "pronoun: neuter ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010003507" -> "gerundive: neuter ablative singular",
    "urn:cite2:tabulae:morphforms.v1:010003512" -> "adjective: neuter ablative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010003522" -> "adjective: neuter ablative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010003532" -> "adjective: neuter ablative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:010003600" -> "noun: neuter vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010003601" -> "pronoun: neuter vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010003607" -> "gerundive: neuter vocative singular",
    "urn:cite2:tabulae:morphforms.v1:010003612" -> "adjective: neuter vocative singular positive",
    "urn:cite2:tabulae:morphforms.v1:010003622" -> "adjective: neuter vocative singular comparative",
    "urn:cite2:tabulae:morphforms.v1:010003632" -> "adjective: neuter vocative singular superlative",
    "urn:cite2:tabulae:morphforms.v1:011011105" -> "participle: present active masculine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:011011205" -> "participle: present active masculine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:011011305" -> "participle: present active masculine dative singular",
    "urn:cite2:tabulae:morphforms.v1:011011405" -> "participle: present active masculine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:011011505" -> "participle: present active masculine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:011011605" -> "participle: present active masculine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:011012105" -> "participle: present active feminine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:011012205" -> "participle: present active feminine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:011012305" -> "participle: present active feminine dative singular",
    "urn:cite2:tabulae:morphforms.v1:011012405" -> "participle: present active feminine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:011012505" -> "participle: present active feminine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:011012605" -> "participle: present active feminine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:011013105" -> "participle: present active neuter nominative singular",
    "urn:cite2:tabulae:morphforms.v1:011013205" -> "participle: present active neuter genitive singular",
    "urn:cite2:tabulae:morphforms.v1:011013305" -> "participle: present active neuter dative singular",
    "urn:cite2:tabulae:morphforms.v1:011013405" -> "participle: present active neuter accusative singular",
    "urn:cite2:tabulae:morphforms.v1:011013505" -> "participle: present active neuter ablative singular",
    "urn:cite2:tabulae:morphforms.v1:011013605" -> "participle: present active neuter vocative singular",
    "urn:cite2:tabulae:morphforms.v1:013011105" -> "participle: future active masculine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:013011205" -> "participle: future active masculine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:013011305" -> "participle: future active masculine dative singular",
    "urn:cite2:tabulae:morphforms.v1:013011405" -> "participle: future active masculine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:013011505" -> "participle: future active masculine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:013011605" -> "participle: future active masculine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:013012105" -> "participle: future active feminine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:013012205" -> "participle: future active feminine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:013012305" -> "participle: future active feminine dative singular",
    "urn:cite2:tabulae:morphforms.v1:013012405" -> "participle: future active feminine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:013012505" -> "participle: future active feminine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:013012605" -> "participle: future active feminine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:013013105" -> "participle: future active neuter nominative singular",
    "urn:cite2:tabulae:morphforms.v1:013013205" -> "participle: future active neuter genitive singular",
    "urn:cite2:tabulae:morphforms.v1:013013305" -> "participle: future active neuter dative singular",
    "urn:cite2:tabulae:morphforms.v1:013013405" -> "participle: future active neuter accusative singular",
    "urn:cite2:tabulae:morphforms.v1:013013505" -> "participle: future active neuter ablative singular",
    "urn:cite2:tabulae:morphforms.v1:013013605" -> "participle: future active neuter vocative singular",
    "urn:cite2:tabulae:morphforms.v1:014021105" -> "participle: perfect passive masculine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:014021205" -> "participle: perfect passive masculine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:014021305" -> "participle: perfect passive masculine dative singular",
    "urn:cite2:tabulae:morphforms.v1:014021405" -> "participle: perfect passive masculine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:014021505" -> "participle: perfect passive masculine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:014021605" -> "participle: perfect passive masculine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:014022105" -> "participle: perfect passive feminine nominative singular",
    "urn:cite2:tabulae:morphforms.v1:014022205" -> "participle: perfect passive feminine genitive singular",
    "urn:cite2:tabulae:morphforms.v1:014022305" -> "participle: perfect passive feminine dative singular",
    "urn:cite2:tabulae:morphforms.v1:014022405" -> "participle: perfect passive feminine accusative singular",
    "urn:cite2:tabulae:morphforms.v1:014022505" -> "participle: perfect passive feminine ablative singular",
    "urn:cite2:tabulae:morphforms.v1:014022605" -> "participle: perfect passive feminine vocative singular",
    "urn:cite2:tabulae:morphforms.v1:014023105" -> "participle: perfect passive neuter nominative singular",
    "urn:cite2:tabulae:morphforms.v1:014023205" -> "participle: perfect passive neuter genitive singular",
    "urn:cite2:tabulae:morphforms.v1:014023305" -> "participle: perfect passive neuter dative singular",
    "urn:cite2:tabulae:morphforms.v1:014023405" -> "participle: perfect passive neuter accusative singular",
    "urn:cite2:tabulae:morphforms.v1:014023505" -> "participle: perfect passive neuter ablative singular",
    "urn:cite2:tabulae:morphforms.v1:014023605" -> "participle: perfect passive neuter vocative singular",
    "urn:cite2:tabulae:morphforms.v1:020001100" -> "noun: masculine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020001101" -> "pronoun: masculine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020001107" -> "gerundive: masculine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020001112" -> "adjective: masculine nominative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020001122" -> "adjective: masculine nominative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020001132" -> "adjective: masculine nominative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020001200" -> "noun: masculine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020001201" -> "pronoun: masculine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020001207" -> "gerundive: masculine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020001212" -> "adjective: masculine genitive plural positive",
    "urn:cite2:tabulae:morphforms.v1:020001222" -> "adjective: masculine genitive plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020001232" -> "adjective: masculine genitive plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020001300" -> "noun: masculine dative plural",
    "urn:cite2:tabulae:morphforms.v1:020001301" -> "pronoun: masculine dative plural",
    "urn:cite2:tabulae:morphforms.v1:020001307" -> "gerundive: masculine dative plural",
    "urn:cite2:tabulae:morphforms.v1:020001312" -> "adjective: masculine dative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020001322" -> "adjective: masculine dative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020001332" -> "adjective: masculine dative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020001400" -> "noun: masculine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020001401" -> "pronoun: masculine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020001407" -> "gerundive: masculine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020001412" -> "adjective: masculine accusative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020001422" -> "adjective: masculine accusative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020001432" -> "adjective: masculine accusative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020001500" -> "noun: masculine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020001501" -> "pronoun: masculine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020001507" -> "gerundive: masculine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020001512" -> "adjective: masculine ablative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020001522" -> "adjective: masculine ablative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020001532" -> "adjective: masculine ablative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020001600" -> "noun: masculine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020001601" -> "pronoun: masculine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020001607" -> "gerundive: masculine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020001612" -> "adjective: masculine vocative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020001622" -> "adjective: masculine vocative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020001632" -> "adjective: masculine vocative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020002100" -> "noun: feminine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020002101" -> "pronoun: feminine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020002107" -> "gerundive: feminine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020002112" -> "adjective: feminine nominative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020002122" -> "adjective: feminine nominative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020002132" -> "adjective: feminine nominative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020002200" -> "noun: feminine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020002201" -> "pronoun: feminine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020002207" -> "gerundive: feminine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020002212" -> "adjective: feminine genitive plural positive",
    "urn:cite2:tabulae:morphforms.v1:020002222" -> "adjective: feminine genitive plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020002232" -> "adjective: feminine genitive plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020002300" -> "noun: feminine dative plural",
    "urn:cite2:tabulae:morphforms.v1:020002301" -> "pronoun: feminine dative plural",
    "urn:cite2:tabulae:morphforms.v1:020002307" -> "gerundive: feminine dative plural",
    "urn:cite2:tabulae:morphforms.v1:020002312" -> "adjective: feminine dative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020002322" -> "adjective: feminine dative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020002332" -> "adjective: feminine dative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020002400" -> "noun: feminine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020002401" -> "pronoun: feminine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020002407" -> "gerundive: feminine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020002412" -> "adjective: feminine accusative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020002422" -> "adjective: feminine accusative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020002432" -> "adjective: feminine accusative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020002500" -> "noun: feminine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020002501" -> "pronoun: feminine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020002507" -> "gerundive: feminine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020002512" -> "adjective: feminine ablative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020002522" -> "adjective: feminine ablative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020002532" -> "adjective: feminine ablative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020002600" -> "noun: feminine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020002601" -> "pronoun: feminine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020002607" -> "gerundive: feminine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020002612" -> "adjective: feminine vocative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020002622" -> "adjective: feminine vocative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020002632" -> "adjective: feminine vocative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020003100" -> "noun: neuter nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020003101" -> "pronoun: neuter nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020003107" -> "gerundive: neuter nominative plural",
    "urn:cite2:tabulae:morphforms.v1:020003112" -> "adjective: neuter nominative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020003122" -> "adjective: neuter nominative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020003132" -> "adjective: neuter nominative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020003200" -> "noun: neuter genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020003201" -> "pronoun: neuter genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020003207" -> "gerundive: neuter genitive plural",
    "urn:cite2:tabulae:morphforms.v1:020003212" -> "adjective: neuter genitive plural positive",
    "urn:cite2:tabulae:morphforms.v1:020003222" -> "adjective: neuter genitive plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020003232" -> "adjective: neuter genitive plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020003300" -> "noun: neuter dative plural",
    "urn:cite2:tabulae:morphforms.v1:020003301" -> "pronoun: neuter dative plural",
    "urn:cite2:tabulae:morphforms.v1:020003307" -> "gerundive: neuter dative plural",
    "urn:cite2:tabulae:morphforms.v1:020003312" -> "adjective: neuter dative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020003322" -> "adjective: neuter dative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020003332" -> "adjective: neuter dative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020003400" -> "noun: neuter accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020003401" -> "pronoun: neuter accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020003407" -> "gerundive: neuter accusative plural",
    "urn:cite2:tabulae:morphforms.v1:020003412" -> "adjective: neuter accusative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020003422" -> "adjective: neuter accusative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020003432" -> "adjective: neuter accusative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020003500" -> "noun: neuter ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020003501" -> "pronoun: neuter ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020003507" -> "gerundive: neuter ablative plural",
    "urn:cite2:tabulae:morphforms.v1:020003512" -> "adjective: neuter ablative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020003522" -> "adjective: neuter ablative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020003532" -> "adjective: neuter ablative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:020003600" -> "noun: neuter vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020003601" -> "pronoun: neuter vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020003607" -> "gerundive: neuter vocative plural",
    "urn:cite2:tabulae:morphforms.v1:020003612" -> "adjective: neuter vocative plural positive",
    "urn:cite2:tabulae:morphforms.v1:020003622" -> "adjective: neuter vocative plural comparative",
    "urn:cite2:tabulae:morphforms.v1:020003632" -> "adjective: neuter vocative plural superlative",
    "urn:cite2:tabulae:morphforms.v1:021011105" -> "participle: present active masculine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:021011205" -> "participle: present active masculine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:021011305" -> "participle: present active masculine dative plural",
    "urn:cite2:tabulae:morphforms.v1:021011405" -> "participle: present active masculine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:021011505" -> "participle: present active masculine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:021011605" -> "participle: present active masculine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:021012105" -> "participle: present active feminine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:021012205" -> "participle: present active feminine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:021012305" -> "participle: present active feminine dative plural",
    "urn:cite2:tabulae:morphforms.v1:021012405" -> "participle: present active feminine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:021012505" -> "participle: present active feminine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:021012605" -> "participle: present active feminine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:021013105" -> "participle: present active neuter nominative plural",
    "urn:cite2:tabulae:morphforms.v1:021013205" -> "participle: present active neuter genitive plural",
    "urn:cite2:tabulae:morphforms.v1:021013305" -> "participle: present active neuter dative plural",
    "urn:cite2:tabulae:morphforms.v1:021013405" -> "participle: present active neuter accusative plural",
    "urn:cite2:tabulae:morphforms.v1:021013505" -> "participle: present active neuter ablative plural",
    "urn:cite2:tabulae:morphforms.v1:021013605" -> "participle: present active neuter vocative plural",
    "urn:cite2:tabulae:morphforms.v1:023011105" -> "participle: future active masculine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:023011205" -> "participle: future active masculine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:023011305" -> "participle: future active masculine dative plural",
    "urn:cite2:tabulae:morphforms.v1:023011405" -> "participle: future active masculine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:023011505" -> "participle: future active masculine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:023011605" -> "participle: future active masculine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:023012105" -> "participle: future active feminine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:023012205" -> "participle: future active feminine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:023012305" -> "participle: future active feminine dative plural",
    "urn:cite2:tabulae:morphforms.v1:023012405" -> "participle: future active feminine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:023012505" -> "participle: future active feminine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:023012605" -> "participle: future active feminine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:023013105" -> "participle: future active neuter nominative plural",
    "urn:cite2:tabulae:morphforms.v1:023013205" -> "participle: future active neuter genitive plural",
    "urn:cite2:tabulae:morphforms.v1:023013305" -> "participle: future active neuter dative plural",
    "urn:cite2:tabulae:morphforms.v1:023013405" -> "participle: future active neuter accusative plural",
    "urn:cite2:tabulae:morphforms.v1:023013505" -> "participle: future active neuter ablative plural",
    "urn:cite2:tabulae:morphforms.v1:023013605" -> "participle: future active neuter vocative plural",
    "urn:cite2:tabulae:morphforms.v1:024021105" -> "participle: perfect passive masculine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:024021205" -> "participle: perfect passive masculine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:024021305" -> "participle: perfect passive masculine dative plural",
    "urn:cite2:tabulae:morphforms.v1:024021405" -> "participle: perfect passive masculine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:024021505" -> "participle: perfect passive masculine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:024021605" -> "participle: perfect passive masculine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:024022105" -> "participle: perfect passive feminine nominative plural",
    "urn:cite2:tabulae:morphforms.v1:024022205" -> "participle: perfect passive feminine genitive plural",
    "urn:cite2:tabulae:morphforms.v1:024022305" -> "participle: perfect passive feminine dative plural",
    "urn:cite2:tabulae:morphforms.v1:024022405" -> "participle: perfect passive feminine accusative plural",
    "urn:cite2:tabulae:morphforms.v1:024022505" -> "participle: perfect passive feminine ablative plural",
    "urn:cite2:tabulae:morphforms.v1:024022605" -> "participle: perfect passive feminine vocative plural",
    "urn:cite2:tabulae:morphforms.v1:024023105" -> "participle: perfect passive neuter nominative plural",
    "urn:cite2:tabulae:morphforms.v1:024023205" -> "participle: perfect passive neuter genitive plural",
    "urn:cite2:tabulae:morphforms.v1:024023305" -> "participle: perfect passive neuter dative plural",
    "urn:cite2:tabulae:morphforms.v1:024023405" -> "participle: perfect passive neuter accusative plural",
    "urn:cite2:tabulae:morphforms.v1:024023505" -> "participle: perfect passive neuter ablative plural",
    "urn:cite2:tabulae:morphforms.v1:024023605" -> "participle: perfect passive neuter vocative plural",
    "urn:cite2:tabulae:morphforms.v1:111110004" -> "finite verb: first singular present indicative active",
    "urn:cite2:tabulae:morphforms.v1:111120004" -> "finite verb: first singular present indicative passive",
    "urn:cite2:tabulae:morphforms.v1:111210004" -> "finite verb: first singular present subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:111220004" -> "finite verb: first singular present subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:112110004" -> "finite verb: first singular imperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:112120004" -> "finite verb: first singular imperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:112210004" -> "finite verb: first singular imperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:112220004" -> "finite verb: first singular imperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:113110004" -> "finite verb: first singular future indicative active",
    "urn:cite2:tabulae:morphforms.v1:113120004" -> "finite verb: first singular future indicative passive",
    "urn:cite2:tabulae:morphforms.v1:114110004" -> "finite verb: first singular perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:114120004" -> "finite verb: first singular perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:114210004" -> "finite verb: first singular perfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:114220004" -> "finite verb: first singular perfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:115110004" -> "finite verb: first singular pluperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:115120004" -> "finite verb: first singular pluperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:115210004" -> "finite verb: first singular pluperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:115220004" -> "finite verb: first singular pluperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:116110004" -> "finite verb: first singular future perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:116120004" -> "finite verb: first singular future perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:121110004" -> "finite verb: first plural present indicative active",
    "urn:cite2:tabulae:morphforms.v1:121120004" -> "finite verb: first plural present indicative passive",
    "urn:cite2:tabulae:morphforms.v1:121210004" -> "finite verb: first plural present subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:121220004" -> "finite verb: first plural present subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:122110004" -> "finite verb: first plural imperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:122120004" -> "finite verb: first plural imperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:122210004" -> "finite verb: first plural imperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:122220004" -> "finite verb: first plural imperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:123110004" -> "finite verb: first plural future indicative active",
    "urn:cite2:tabulae:morphforms.v1:123120004" -> "finite verb: first plural future indicative passive",
    "urn:cite2:tabulae:morphforms.v1:124110004" -> "finite verb: first plural perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:124120004" -> "finite verb: first plural perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:124210004" -> "finite verb: first plural perfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:124220004" -> "finite verb: first plural perfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:125110004" -> "finite verb: first plural pluperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:125120004" -> "finite verb: first plural pluperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:125210004" -> "finite verb: first plural pluperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:125220004" -> "finite verb: first plural pluperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:126110004" -> "finite verb: first plural future perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:126120004" -> "finite verb: first plural future perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:211110004" -> "finite verb: second singular present indicative active",
    "urn:cite2:tabulae:morphforms.v1:211120004" -> "finite verb: second singular present indicative passive",
    "urn:cite2:tabulae:morphforms.v1:211210004" -> "finite verb: second singular present subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:211220004" -> "finite verb: second singular present subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:211310004" -> "finite verb: second singular present imperative active",
    "urn:cite2:tabulae:morphforms.v1:211320004" -> "finite verb: second singular present imperative passive",
    "urn:cite2:tabulae:morphforms.v1:212110004" -> "finite verb: second singular imperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:212120004" -> "finite verb: second singular imperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:212210004" -> "finite verb: second singular imperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:212220004" -> "finite verb: second singular imperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:213110004" -> "finite verb: second singular future indicative active",
    "urn:cite2:tabulae:morphforms.v1:213120004" -> "finite verb: second singular future indicative passive",
    "urn:cite2:tabulae:morphforms.v1:213310004" -> "finite verb: second singular future imperative active",
    "urn:cite2:tabulae:morphforms.v1:213320004" -> "finite verb: second singular future imperative passive",
    "urn:cite2:tabulae:morphforms.v1:214110004" -> "finite verb: second singular perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:214120004" -> "finite verb: second singular perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:214210004" -> "finite verb: second singular perfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:214220004" -> "finite verb: second singular perfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:215110004" -> "finite verb: second singular pluperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:215120004" -> "finite verb: second singular pluperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:215210004" -> "finite verb: second singular pluperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:215220004" -> "finite verb: second singular pluperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:216110004" -> "finite verb: second singular future perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:216120004" -> "finite verb: second singular future perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:221110004" -> "finite verb: second plural present indicative active",
    "urn:cite2:tabulae:morphforms.v1:221120004" -> "finite verb: second plural present indicative passive",
    "urn:cite2:tabulae:morphforms.v1:221210004" -> "finite verb: second plural present subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:221220004" -> "finite verb: second plural present subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:221310004" -> "finite verb: second plural present imperative active",
    "urn:cite2:tabulae:morphforms.v1:221320004" -> "finite verb: second plural present imperative passive",
    "urn:cite2:tabulae:morphforms.v1:222110004" -> "finite verb: second plural imperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:222120004" -> "finite verb: second plural imperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:222210004" -> "finite verb: second plural imperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:222220004" -> "finite verb: second plural imperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:223110004" -> "finite verb: second plural future indicative active",
    "urn:cite2:tabulae:morphforms.v1:223120004" -> "finite verb: second plural future indicative passive",
    "urn:cite2:tabulae:morphforms.v1:223310004" -> "finite verb: second plural future imperative active",
    "urn:cite2:tabulae:morphforms.v1:223320004" -> "finite verb: second plural future imperative passive",
    "urn:cite2:tabulae:morphforms.v1:224110004" -> "finite verb: second plural perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:224120004" -> "finite verb: second plural perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:224210004" -> "finite verb: second plural perfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:224220004" -> "finite verb: second plural perfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:225110004" -> "finite verb: second plural pluperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:225120004" -> "finite verb: second plural pluperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:225210004" -> "finite verb: second plural pluperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:225220004" -> "finite verb: second plural pluperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:226110004" -> "finite verb: second plural future perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:226120004" -> "finite verb: second plural future perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:311110004" -> "finite verb: third singular present indicative active",
    "urn:cite2:tabulae:morphforms.v1:311120004" -> "finite verb: third singular present indicative passive",
    "urn:cite2:tabulae:morphforms.v1:311210004" -> "finite verb: third singular present subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:311220004" -> "finite verb: third singular present subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:311310004" -> "finite verb: third singular present imperative active",
    "urn:cite2:tabulae:morphforms.v1:311320004" -> "finite verb: third singular present imperative passive",
    "urn:cite2:tabulae:morphforms.v1:312110004" -> "finite verb: third singular imperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:312120004" -> "finite verb: third singular imperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:312210004" -> "finite verb: third singular imperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:312220004" -> "finite verb: third singular imperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:313110004" -> "finite verb: third singular future indicative active",
    "urn:cite2:tabulae:morphforms.v1:313120004" -> "finite verb: third singular future indicative passive",
    "urn:cite2:tabulae:morphforms.v1:313310004" -> "finite verb: third singular future imperative active",
    "urn:cite2:tabulae:morphforms.v1:313320004" -> "finite verb: third singular future imperative passive",
    "urn:cite2:tabulae:morphforms.v1:314110004" -> "finite verb: third singular perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:314120004" -> "finite verb: third singular perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:314210004" -> "finite verb: third singular perfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:314220004" -> "finite verb: third singular perfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:315110004" -> "finite verb: third singular pluperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:315120004" -> "finite verb: third singular pluperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:315210004" -> "finite verb: third singular pluperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:315220004" -> "finite verb: third singular pluperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:316110004" -> "finite verb: third singular future perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:316120004" -> "finite verb: third singular future perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:321110004" -> "finite verb: third plural present indicative active",
    "urn:cite2:tabulae:morphforms.v1:321120004" -> "finite verb: third plural present indicative passive",
    "urn:cite2:tabulae:morphforms.v1:321210004" -> "finite verb: third plural present subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:321220004" -> "finite verb: third plural present subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:321310004" -> "finite verb: third plural present imperative active",
    "urn:cite2:tabulae:morphforms.v1:321320004" -> "finite verb: third plural present imperative passive",
    "urn:cite2:tabulae:morphforms.v1:322110004" -> "finite verb: third plural imperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:322120004" -> "finite verb: third plural imperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:322210004" -> "finite verb: third plural imperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:322220004" -> "finite verb: third plural imperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:323110004" -> "finite verb: third plural future indicative active",
    "urn:cite2:tabulae:morphforms.v1:323120004" -> "finite verb: third plural future indicative passive",
    "urn:cite2:tabulae:morphforms.v1:323310004" -> "finite verb: third plural future imperative active",
    "urn:cite2:tabulae:morphforms.v1:323320004" -> "finite verb: third plural future imperative passive",
    "urn:cite2:tabulae:morphforms.v1:324110004" -> "finite verb: third plural perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:324120004" -> "finite verb: third plural perfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:324210004" -> "finite verb: third plural perfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:324220004" -> "finite verb: third plural perfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:325110004" -> "finite verb: third plural pluperfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:325120004" -> "finite verb: third plural pluperfect indicative passive",
    "urn:cite2:tabulae:morphforms.v1:325210004" -> "finite verb: third plural pluperfect subjunctive active",
    "urn:cite2:tabulae:morphforms.v1:325220004" -> "finite verb: third plural pluperfect subjunctive passive",
    "urn:cite2:tabulae:morphforms.v1:326110004" -> "finite verb: third plural future perfect indicative active",
    "urn:cite2:tabulae:morphforms.v1:326120004" -> "finite verb: third plural future perfect indicative passive"
  )

}

/** Noun form identified by gender, case and number, with its
* corresponding form URN.
*
* @param formUrn URN for this form.
* @param gender
* @param grammaticalCase
* @param grammaticalNumber
*/
case class ValidNounForm(formUrn: Cite2Urn, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    ValidForm.correctZeroes(digits, Vector(0,2,3,4,7))
  }
}
object ValidNounForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidNounForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))

    try {
      ValidNounForm(
        formUrn,
        ValidForm.genderCodes(g),
        ValidForm.caseCodes(c),
        ValidForm.numberCodes(n)
      )
    } catch {
      case t: Throwable => {
        val msg = "URN " + formUrn + " has invalid values for noun GCN"
        warn(msg)
        throw new Exception(msg)
      }
    }
  }
}


case class ValidPronounForm(formUrn: Cite2Urn, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    ValidForm.correctZeroes(digits, Vector(0,2,3,4,7))
  }
}
object ValidPronounForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidPronounForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))

    try {
      ValidPronounForm(
        formUrn,
        ValidForm.genderCodes(g),
        ValidForm.caseCodes(c),
        ValidForm.numberCodes(n)
      )
    } catch {
      case e: Exception => {
        val msg = "URN " + formUrn + " has invalid values for pronoun GCN"
        warn (msg)
        throw new Exception(msg)
      }
    }
  }
}

case class ValidAdjectiveForm(formUrn: Cite2Urn, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber,
degree: Degree) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    ValidForm.correctZeroes(digits, Vector(0,2,3,4))
  }
}
object ValidAdjectiveForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidAdjectiveForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))
    val d = digits(ValidForm.columnNames("degree"))

    try {
      ValidAdjectiveForm(
        formUrn,
        ValidForm.genderCodes(g),
        ValidForm.caseCodes(c),
        ValidForm.numberCodes(n),
        ValidForm.degreeCodes(d)
      )
    } catch {
      case e: Exception => {
        val msg = "URN " + formUrn + " contains invalid values for adjective GCND"
        throw new Exception(msg)
      }
    }
  }
}

case class ValidAdverbForm(formUrn: Cite2Urn, degree: Degree) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    ValidForm.correctZeroes(digits, Vector(0,1,2,3,4,5,6))
  }
}
object ValidAdverbForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidAdverbForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val d = digits(ValidForm.columnNames("degree"))

    try {
      ValidAdverbForm(
        formUrn,
        ValidForm.degreeCodes(d)
      )
    } catch {
      case e: Exception => {
        val msg = "URN " + formUrn + " has invalid value for adverb degree"
        throw new Exception(msg)
      }
    }
  }
}


case class ValidFiniteVerbForm(formUrn: Cite2Urn,
  person: Person,
  grammaticalNumber: GrammaticalNumber,
  tense: Tense,
  mood: Mood,
  voice: Voice
) extends ValidForm {
  def urn = formUrn

  def validImperative: Boolean = {
    if (mood == Imperative) {
      tense match {
        case Present => {
          person match {
            case First => false
            case Second => true
            case Third => true
          }
        }
        case Future => {
          person match {
            case First => false
            case Second => true
            case Third => true
          }
        }
        case _ => false
      }
    } else {
      true
    }
  }


  def validSubjunctive: Boolean = {
    tense match {
      case Present => true
      case Imperfect => true
      case Perfect => true
      case Pluperfect => true
      case _ => false
    }
  }
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeroes = ValidForm.correctZeroes(digits, Vector(5,6,7))
    val validForMood = mood match {
      case Indicative => true
      case Imperative => validImperative
      case Subjunctive => validSubjunctive
    }
    correctZeroes && validForMood
  }
}
object ValidFiniteVerbForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidFiniteVerbForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val p = digits(ValidForm.columnNames("person"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))
    val t = digits(ValidForm.columnNames("tense"))
    val m = digits(ValidForm.columnNames("mood"))
    val v = digits(ValidForm.columnNames("voice"))

    try {
      ValidFiniteVerbForm(
        formUrn,
        ValidForm.personCodes(p),
        ValidForm.numberCodes(n),
        ValidForm.tenseCodes(t),
        ValidForm.moodCodes(m),
        ValidForm.voiceCodes(v)
      )
    } catch {
      case e: Exception => {
        val msg = "URN " + formUrn + " contains invalid values for finitive verb PNTMV"
        throw new Exception(msg)
      }
    }

  }
}

case class ValidParticipleForm(formUrn: Cite2Urn,
  tense: Tense,
  voice: Voice,
  gender: Gender,
  grammaticalCase: GrammaticalCase,
  grammaticalNumber: GrammaticalNumber
) extends ValidForm {
  def urn = formUrn

  def validTenseVoice : Boolean = {
    tense match {
      case Present => {
        voice match {
          case Active => true
          case Passive => false
        }
      }
      case Future => {
        voice match {
          case Active => true
          case Passive => false
        }
      }
      case Perfect => {
        voice match {
          case Active => false
          case Passive => true
        }
      }
      case _ => false
    }
  }

  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeros = ValidForm.correctZeroes(digits, Vector(0,3,7))

    correctZeros && validTenseVoice
  }
}
object ValidParticipleForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidParticipleForm = {
    val digits = formUrn.objectComponent.split("").toVector

    val t = digits(ValidForm.columnNames("tense"))
    val v = digits(ValidForm.columnNames("voice"))
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))

    try {
      ValidParticipleForm(
        formUrn,
        ValidForm.tenseCodes(t),
        ValidForm.voiceCodes(v),
        ValidForm.genderCodes(g),
        ValidForm.caseCodes(c),
        ValidForm.numberCodes(n),
      )
    } catch {
      case e: Exception => {
        val msg = "URN " + formUrn + " has invalid valudes for participle TVGCN"
        warn(msg)
        throw new Exception(msg)
      }
    }


  }
}


case class ValidInfinitiveForm(formUrn: Cite2Urn,
  tense: Tense,
  voice: Voice
) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeroes = ValidForm.correctZeroes(digits,
      Vector(0,1,3,5,6,7))
    val correctTenseValue = ValidForm.validValue(digits(2),
    Vector("1", "3", "4"))

    correctZeroes && correctTenseValue
  }
}
object ValidInfinitiveForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidInfinitiveForm = {
    val digits = formUrn.objectComponent.split("").toVector

    val t = digits(ValidForm.columnNames("tense"))
    val v = digits(ValidForm.columnNames("voice"))

    try {
      ValidInfinitiveForm(
        formUrn,
        ValidForm.tenseCodes(t),
        ValidForm.voiceCodes(v)
      )
    } catch {
      case t: Throwable => {
        val msg = "URN " + formUrn + " has invalid values for infinitive TV"
        warn(msg)
        throw new Exception(msg)
      }
    }


  }
}

case class ValidGerundiveForm(formUrn: Cite2Urn, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeroes = ValidForm.correctZeroes(digits, Vector(0,2,3,4,7))
    correctZeroes
  }
}
object ValidGerundiveForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidGerundiveForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))

    try {
      ValidGerundiveForm(
        formUrn,
        ValidForm.genderCodes(g),
        ValidForm.caseCodes(c),
        ValidForm.numberCodes(n)
      )
    } catch {
      case e: Exception => {
        val msg = "URN " + formUrn + " has invalid values for gerundive GCN"
        throw new Exception(msg)
      }
    }
  }
}


case class ValidGerundForm(formUrn: Cite2Urn, grammaticalCase: GrammaticalCase) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeroes = ValidForm.correctZeroes(digits, Vector(0,1,2,3,4,5,7))
    val correctCaseValue = ValidForm.validValue(digits(6), Vector("1", "2", "3", "4", "5"))
    correctZeroes  && correctCaseValue
  }
}
object ValidGerundForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidGerundForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    try {
      ValidGerundForm(
        formUrn,
        ValidForm.caseCodes(c)
      )
    } catch {
      case e: Exception => {
        val msg = "URN " + formUrn + " has invalid values for gerund case"
        throw new Exception(msg)
      }
    }

  }
}


case class ValidSupineForm(formUrn: Cite2Urn, grammaticalCase: GrammaticalCase) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeroes = ValidForm.correctZeroes(digits, Vector(0,1,2,3,4,5,7))
    val correctCaseValue = ValidForm.validValue(digits(6), Vector( "4", "5"))
    correctZeroes && correctCaseValue
  }
}
object ValidSupineForm extends LogSupport {
  def apply(formUrn: Cite2Urn) : ValidSupineForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val c = digits(ValidForm.columnNames("grammaticalCase"))

    try {
      ValidSupineForm(
        formUrn,
        ValidForm.caseCodes(c)
      )
    } catch {
      case e: Exception => {
        val msg = "URN " + formUrn + " has invalid value for supine case"
        warn(msg)
        throw new Exception(msg)
      }
    }

  }
}

case class ValidUninflectedForm(formUrn: Cite2Urn, indeclinablePoS: IndeclinablePoS) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeroes = ValidForm.correctZeroes(digits, Vector(0,1,2,3,4,5,6,7))
    correctZeroes
  }
}
object ValidUninflectedForm {
  def apply(formUrn: Cite2Urn) : ValidUninflectedForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val pos = digits(ValidForm.columnNames("inflectionType"))
    ValidUninflectedForm(
      formUrn,
      ValidForm.posCodes(pos)
    )
  }
}
