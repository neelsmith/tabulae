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


}

case class ValidNounForm(formUrn: Cite2Urn, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends ValidForm {
  def urn = formUrn
  def validUrnValue: Boolean = false
}
object ValidNounForm {
  def apply(formUrn: Cite2Urn) : ValidNounForm = {
    val digits = formUrn.objectComponent.split("").toVector
    val g = digits(ValidForm.columnNames("gender")).toInt
    val c = digits(ValidForm.columnNames("grammaticalCase")).toInt
    val n = digits(ValidForm.columnNames("grammaticalNumber")).toInt
    val gender = g match {
      case 1 => Masculine
      case 2 => Feminine
      case 3 => Neuter
    }
    val gcase = c match {
      case 1 => Nominative
      case 2 => Genitive
      case 3 => Dative
      case 4 => Accusative
      case 5 => Ablative
      case 6 => Vocative
    }
    val gnumber = n match {
      case 1 => Singular
      case 2 => Plural
    }
    ValidNounForm(formUrn, gender, gcase, gnumber)
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
