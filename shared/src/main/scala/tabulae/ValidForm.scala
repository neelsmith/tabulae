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
}

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
