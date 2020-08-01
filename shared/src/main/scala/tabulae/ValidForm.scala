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
object ValidAdjectiveForm {
  def apply(formUrn: Cite2Urn) : ValidAdjectiveForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))
    val d = digits(ValidForm.columnNames("degree"))

    ValidAdjectiveForm(
      formUrn,
      ValidForm.genderCodes(g),
      ValidForm.caseCodes(c),
      ValidForm.numberCodes(n),
      ValidForm.degreeCodes(d)
    )
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
object ValidAdverbForm {
  def apply(formUrn: Cite2Urn) : ValidAdverbForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val d = digits(ValidForm.columnNames("degree"))

    ValidAdverbForm(
      formUrn,
      ValidForm.degreeCodes(d)
    )
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
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    false
  }
}
object ValidFiniteVerbForm {
  def apply(formUrn: Cite2Urn) : ValidFiniteVerbForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val p = digits(ValidForm.columnNames("person"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))
    val t = digits(ValidForm.columnNames("tense"))
    val m = digits(ValidForm.columnNames("mood"))
    val v = digits(ValidForm.columnNames("voice"))

    ValidFiniteVerbForm(
      formUrn,
      ValidForm.personCodes(p),
      ValidForm.numberCodes(n),
      ValidForm.tenseCodes(t),
      ValidForm.moodCodes(m),
      ValidForm.voiceCodes(v)
    )

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
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctTenseVoiceCombo = {
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
    val correctZeros = ValidForm.correctZeroes(digits, Vector(0,3,7))

    correctZeros && correctTenseVoiceCombo
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
object ValidGerundiveForm {
  def apply(formUrn: Cite2Urn) : ValidGerundiveForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))

    ValidGerundiveForm(
      formUrn,
      ValidForm.genderCodes(g),
      ValidForm.caseCodes(c),
      ValidForm.numberCodes(n)
    )
  }
}



case class ValidGerundForm(formUrn: Cite2Urn, grammaticalCase: GrammaticalCase) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeroes = ValidForm.correctZeroes(digits, Vector(0,1,2,3,4,5,7))
    val correctCaseValue = ValidForm.validValue(digits(6), Vector("1", "2", "3", "4"))
    correctZeroes  && correctCaseValue
  }
}
object ValidGerundForm {
  def apply(formUrn: Cite2Urn) : ValidGerundForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val c = digits(ValidForm.columnNames("grammaticalCase"))

    ValidGerundForm(
      formUrn,
      ValidForm.caseCodes(c)
    )
  }
}


case class ValidSupineForm(formUrn: Cite2Urn, grammaticalCase: GrammaticalCase) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    val digits = formUrn.objectComponent.split("").toVector
    val correctZeroes = ValidForm.correctZeroes(digits, Vector(0,1,2,3,4,5,7))
    val correctCaseValue = ValidForm.validValue(digits(6), Vector("3", "4"))
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
        val msg = "URN " + formUrn + " has invalid valid for supine case"
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
    val correctPosValue = ValidForm.validValue(digits(8), Vector("A", "B", "C", "D"))
    correctZeroes && correctPosValue
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
