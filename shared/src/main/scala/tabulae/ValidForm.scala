package edu.holycross.shot.tabulae


import edu.holycross.shot.cite._


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
      //case "5" => ValidParticipleForm(urn)
      //case "6" => ValidInfintiveForm(urn)
      //case "7" => ValidGerundiveForm(urn)
      //case "8" => ValidGerundForm(urn)
      //case "9" => ValidSupineForm(urn)
      //case  "A" => ValidUnflectedForm(urn)
      //case  "B" => ValidUnflectedForm(urn)
      //case  "C" => ValidUnflectedForm(urn)
      //case  "D" => ValidUnflectedForm(urn)

      case _ => throw new Exception("Can not parse PoS value " + partOfSpeech)
    }
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
    false
  }
}
object ValidNounForm {
  def apply(formUrn: Cite2Urn) : ValidNounForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))

    ValidNounForm(
      formUrn,
      ValidForm.genderCodes(g),
      ValidForm.caseCodes(c),
      ValidForm.numberCodes(n)
    )
  }
}


case class ValidPronounForm(formUrn: Cite2Urn, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    false
  }
}
object ValidPronounForm {
  def apply(formUrn: Cite2Urn) : ValidPronounForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender"))
    val c = digits(ValidForm.columnNames("grammaticalCase"))
    val n = digits(ValidForm.columnNames("grammaticalNumber"))

    ValidPronounForm(
      formUrn,
      ValidForm.genderCodes(g),
      ValidForm.caseCodes(c),
      ValidForm.numberCodes(n)
    )
  }
}

case class ValidAdjectiveForm(formUrn: Cite2Urn, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber,
degree: Degree) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = {
    // check all other columns are 0s
    false
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
    false
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
/*
  def fromCex(s: String): Option[LemmatizedForm] = {
    val urns = splitCex(s)
    val formOpt = posCodeLabels(digits(8).toHexString.toUpperCase) match {
      case "noun" => NounForm.fromCex(s)
      case _ => {
          println("NOT YET HANDLING " + posCodeLabels(digits(8).toHexString.toUpperCase))
          None
      }
    }
    println("URN: " + urn + " yields digits " + digits.size)
    formOpt
  }

*/

/*

def nounFromCex(digits: Vector[Int]): Option[LemmatizedForm] = {
  println(digits)

  val gender: Gender = {
    digits(4) match {
      case 1 => Masculine
      case 2 => Feminine
      case 3 => Neuter
    }
  }
  val grammaticalCase : GrammaticalCase = {
    digits(5) match {
      case 1 => Nominative
      case 2 => Genitive
      case 3 => Dative
      case 4 => Accusative
      case 5 => Ablative
      case 6 => Vocative
    }
  }
*/

///
// urn:cite2:linglat:tkns.v1:2020_07_25_4
//Record 2020_07_25_4
// urn:cts:latinLit:stoa1263.stoa001.hc_tkns:108a.1.1
// cum
// urn:cite2:tabulae:ls.v1:n11872
// urn:cite2:tabulae:morphforms.v1:00000000
