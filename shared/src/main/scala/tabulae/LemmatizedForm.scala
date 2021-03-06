package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
/*

Analytical patterns we need to implement:

<noun><adj><verb><vadj><infin><gerundive><gerund><supine><ptcpl><adv><pron><irregcverb><irregnoun><irregadj><irregadv><irreginfin><irregptcpl><irreggrnd><irreggrndv><irregsupn><irregpron><indecl>
*/


/** A valid grammatical form identification.*/
sealed trait LemmatizedForm {
  def formLabel: String
  def lemmaId: String
  def stemId: String
  def ruleId: String
  def formId : String
  def posLabel: String = {
    this match {
      case v: VerbForm => "verb"
      case n: NounForm => "noun"
      case pron: PronounForm => "pronoun"
      case adj: AdjectiveForm => "adjective"
      case ptcpl: ParticipleForm => "participle"
      case gnd: GerundForm => "gerund"
      case gndv: GerundiveForm => "gerundive"
      case adv: AdverbForm => "adverb"
      case indecl: IndeclinableForm => "indeclinable"
      case inf: InfinitiveForm => "infinitive"
      case sup: SupineForm => "supine"
    }
  }
 def posCode : Int = {
   this match {
     case n: NounForm => 0
     case pron: PronounForm => 1
     case adj: AdjectiveForm => 2
     case adv: AdverbForm => 3
     case v: VerbForm => 4
     case ptcpl: ParticipleForm => 5
     case inf: InfinitiveForm => 6
     case gndv: GerundiveForm => 7
     case gnd: GerundForm => 8
     case sup: SupineForm => 9
     case indecl: IndeclinableForm =>
       indecl.pos.toString match  {
         case "Conjunction" => 0xA
         case "Preposition" => 0xB
         case "Exclamation" => 0xC
         case "Numeral" => 0xD
       }
   }
 }



  def formUrn : Cite2Urn = {
    Cite2Urn("urn:cite2:tabulae:morphforms.v1:" + formId)
  }

  def substantiveGender: Option[Gender] = {
    this match {
      case n: NounForm => Some(n.gender)
      case pron: PronounForm => Some(pron.gender)
      case adj: AdjectiveForm => Some(adj.gender)
      case ptcpl: ParticipleForm => Some(ptcpl.gender)
      case gdv: GerundiveForm => Some(gdv.gender)
      case _ => None
    }
  }

  def substantiveCase: Option[GrammaticalCase] = {
    this match {
      case n: NounForm => Some(n.grammaticalCase)
      case pron: PronounForm => Some(pron.grammaticalCase)
      case adj: AdjectiveForm => Some(adj.grammaticalCase)
      case ptcpl: ParticipleForm => Some(ptcpl.grammaticalCase)
      case gdv: GerundiveForm => Some(gdv.grammaticalCase)
      case grnd: GerundForm => Some(grnd.grammaticalCase)
      case supine: SupineForm => Some(supine.grammaticalCase)
      case _ => None
    }
  }

  def substantiveNumber: Option[GrammaticalNumber] = {
    this match {
      case n: NounForm => Some(n.grammaticalNumber)
      case pron: PronounForm => Some(pron.grammaticalNumber)
      case adj: AdjectiveForm => Some(adj.grammaticalNumber)
      case ptcpl: ParticipleForm => Some(ptcpl.grammaticalNumber)
      case gdv: GerundiveForm => Some(gdv.grammaticalNumber)
      case _ => None
    }
  }

  def verbPerson: Option[Person] = {
    this match {
      case v: VerbForm => Some(v.person)
      case _ => None
    }
  }

  def verbNumber: Option[GrammaticalNumber] = {
    this match {
      case v: VerbForm => Some(v.grammaticalNumber)
      case _ => None
    }
  }

  def verbTense: Option[Tense] = {
    this match {
      case v: VerbForm => Some(v.tense)
      case _ => None
    }
  }
  def verbMood: Option[Mood] = {
    this match {
      case v: VerbForm => Some(v.mood)
      case _ => None
    }
  }
  def verbVoice: Option[Voice] = {
    this match {
      case v: VerbForm => Some(v.voice)
      case _ => None
    }
  }

  def infinitiveTense: Option[Tense] = {
    this match {
      case inf: InfinitiveForm => Some(inf.tense)
      case _ => None
    }
  }

  def infinitiveVoice: Option[Voice] = {
    this match {
      case inf: InfinitiveForm => Some(inf.voice)
      case _ => None
    }
  }

  def participleTense: Option[Tense] = {
    this match {
      case ptcpl: ParticipleForm => Some(ptcpl.tense)
      case _ => None
    }
  }

  def participleVoice: Option[Voice] = {
    this match {
      case ptcpl: ParticipleForm => Some(ptcpl.voice)
      case _ => None
    }
  }


  def adverbDegree: Option[Degree] = {
    this match {
      case adv: AdverbForm => Some(adv.degree)
      case _ => None
    }
  }
  def adjectiveDegree: Option[Degree] = {
    this match {
      case adj: AdjectiveForm => Some(adj.degree)
      case _ => None
    }
  }

  def indeclinablePartOfSpeech: Option[IndeclinablePoS] = {
    this match {
      case indecl: IndeclinableForm => Some(indecl.pos)
      case _ => None
    }
  }
}

/** Factory object to create full [[LemmatizedForm]] from a string of FST.
*/
object LemmatizedForm {

  /** Alternative constructor for [[LemmatizedForm]] when
  * form is expressed as a URN value.
  */
  def apply(lemmaId: String, stemId: String, ruleId: String, form: Cite2Urn) : Option[LemmatizedForm] = {
    if (form.objectComponent == "null") {
      None

    } else {
      val digits = form.objectComponent.split("").toVector
      val index = ValidForm.columnNames("inflectionType")
      if (digits.size <= index) {
        throw new Exception("Too few digits. Could not make lemmatized form from " + form )
      }
      val partOfSpeech = digits(index)
      val lemmatizedForm = partOfSpeech match {

        case "0" => {
          val noun = ValidNounForm(form)
          NounForm(lemmaId, stemId, ruleId, noun.gender, noun.grammaticalCase, noun.grammaticalNumber)
        }

        case "1" => {
          val pronoun = ValidPronounForm(form)
          PronounForm(lemmaId, stemId, ruleId, pronoun.gender, pronoun.grammaticalCase, pronoun.grammaticalNumber)
        }

        case "2" => {
          val adj = ValidAdjectiveForm(form)
          AdjectiveForm(lemmaId, stemId, ruleId, adj.gender, adj.grammaticalCase, adj.grammaticalNumber, adj.degree)
        }

        case "3" => {
          val adv = ValidAdverbForm(form)
          AdverbForm(lemmaId, stemId, ruleId, adv.degree)
        }

        case "4" => {
          val verb = ValidFiniteVerbForm(form)
          VerbForm(lemmaId, stemId, ruleId, verb.person, verb.grammaticalNumber, verb.tense,
          verb.mood, verb.voice)

        }
        case "5" => {
          val ptcp = ValidParticipleForm(form)
          ParticipleForm(lemmaId, stemId, ruleId, ptcp.gender, ptcp.grammaticalCase, ptcp.grammaticalNumber, ptcp.tense, ptcp.voice )
        }
        case "6" => {
          val infin = ValidInfinitiveForm(form)
          InfinitiveForm(lemmaId, stemId, ruleId, infin.tense, infin.voice)
        }
        case "7" => {
          val gerundive = ValidGerundiveForm(form)
          GerundiveForm(lemmaId, stemId, ruleId, gerundive.gender, gerundive.grammaticalCase, gerundive.grammaticalNumber)
        }
        case "8" => {
          val gerund = ValidGerundForm(form)
          GerundForm(lemmaId, stemId, ruleId, gerund.grammaticalCase)
        }

        case "9" => {
          val supine = ValidSupineForm(form)
          SupineForm(lemmaId, stemId, ruleId, supine.grammaticalCase)
        }


        case  "A" => {
          val conjunction = ValidUninflectedForm(form)
          IndeclinableForm(lemmaId, stemId, ruleId, conjunction.indeclinablePoS)
        }


        case  "B" => {
          val preposition = ValidUninflectedForm(form)
          IndeclinableForm(lemmaId, stemId, ruleId, preposition.indeclinablePoS)
        }
        case  "C" => {
          val exclamation = ValidUninflectedForm(form)
          IndeclinableForm(lemmaId, stemId, ruleId, exclamation.indeclinablePoS)
        }
        case  "D" => {
          val numeral = ValidUninflectedForm(form)
          IndeclinableForm(lemmaId, stemId, ruleId, numeral.indeclinablePoS)
        }

        case _ => throw new Exception("Can not parse PoS value " + partOfSpeech)
      }
      Some(lemmatizedForm)
    }

  }

  def posCodeLabels: Map[String, String] = Map(
    "0" -> "noun",
    "1" -> "pronoun",
    "2" -> "adjective",
    "3" -> "adverb",
    "4" -> "finiteverb",
    "5" -> "participle",
    "6" -> "infinitive",
    "7" -> "gerundive",
    "8" -> "gerund",
    "9" -> "gerundive",
    "A" -> "conjunction",
    "B" -> "preposition",
    "C" -> "exclamation",
    "D" -> "numeral"
  )



  def irregularForm(fst: String) : Option[LemmatizedForm] = {
    //println("Received irreg. form " + fst)
    val parts = fst.split("<div>")
    // require 2 parts...

    val rule = parts(1)
    //println("Rule part is "+ rule)
    val ruleRE = "<([^>]+)><u>([^<]+)</u>".r
    val ruleRE(irregClass, ruleId) = rule
    //println("\tClass " + irregClass)
    //println("\tRule " + ruleId)
    val resultForm = irregClass match {
      case "irregnoun" => {
        val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
        val idsRE(stemId, lexEntity, remainder) = parts(0)

        val dataRE  = "([^<]*)<([^<]+)><([^<]+)><([^<]+)><irregnoun>".r
        val dataRE(stem, gender, grammCase, grammNumber) =  remainder

        Some(NounForm(lexEntity, stemId, ruleId, gender, grammCase, grammNumber))
      }


    case "irregcverb" => {
      val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
      val idsRE(stemId, lexEntity, remainder) = parts(0)

      val dataRE  = "([^<]*)<([^>]+)><([^>]+)><([^>]+)><([^>]+)><([^>]+)><irregcverb>".r
      val dataRE(stem, person, grammNumber, tense, mood, voice) =  remainder.replaceFirst("<#>", "")

      Some(VerbForm(lexEntity, stemId, ruleId, person, grammNumber, tense, mood, voice))
    }


    case "irregpron" => {
      val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
      val idsRE(stemId, lexEntity, remainder) = parts(0)

      val dataRE  = "([^<]*)<([^<]+)><([^<]+)><([^<]+)><irregpron>".r
      val dataRE(stem, gender, grammCase, grammNumber) =  remainder

      Some(PronounForm(lexEntity, stemId, ruleId, gender, grammCase, grammNumber))
    }

      case "irregadv" => {
        //<u>ocremorph.n25115</u><u>ls.n25115</u>
      //itervm<pos><irregadv>
      //<div>
      //<irregadv><u>irreginfl.2</u>
        val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
        val idsRE(stemId, lexEntity, remainder) = parts(0)
        //itervm<pos><irregadv>
        val dataRE  = "([^<]*)<([^<]+)><irregadv>".r
        val dataRE(stem, degr) =  remainder

        Some(AdverbForm(lexEntity, stemId, ruleId, degr))
      }

      case "irregadj" => {

        val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
        val idsRE(stemId, lexEntity, remainder) = parts(0)
        //<u>proof.irradj1</u><u>lexent.n48627</u>totius<masc><gen><sg><pos>
        //<irregadj>
        //<div>
        //<irregadj><u>irreginfl.1</u>
            val dataRE  = "([^<]*)<([^>]+)><([^>]+)><([^>]+)><([^>]+)><irregadj>".r
        val dataRE(stem, gender, grammCase, grammNumber, degr) =  remainder

        Some(AdjectiveForm(lexEntity, stemId, ruleId, gender, grammCase, grammNumber, degr))
      }

      case "irreginfin" => {

        //<u>proof.irrinf1</u><u>lexent.n15868</u>isse<pft><act><irreginfin>
        //<div>
        //<irreginfin><u>irreginfl.4</u>
        val idsRE = "<u>([^<]+)<\\/u><u>([^<]+)<\\/u>(.+)".r
        val idsRE(stemId, lexEntity, remainder) = parts(0)
        //isse<pft><act><irreginfin>
        val dataRE  = "([^<]*)<([^>]+)><([^>]+)><irreginfin>".r
        val dataRE(stem, tense, voice) =  remainder

        Some(InfinitiveForm(lexEntity, stemId, ruleId, tense, voice))

      }

      case _ => {
        val err = "Lemmatizedform: irreg class "+ irregClass + " not recognized."
        //throw new Exception(err)
        println(err)
        None
      }
    }

    resultForm

  }

  /** From a raw FST string, identify a morphological form.
  *
  * @param s String value of a single FST analysis.
  */
  def apply(s: String): Option[LemmatizedForm] = {
    val halves = s.split("<div>")
    require(halves.size == 2, "LemmatizedFrom: could not find <div>-delimited parts of FST string in " + s)


    // CHECK FOR IRREG HERE.
    // This is an enormous kludge, but for now, we're using it.
    // A better option might be parsing out the inflclass and checking against
    // a list of irregular classes.  But it comes down to a similar convention:
    // some class names (here,mere strings) are irregular, some are regular.
    if (s.contains("<irreg")) {
      irregularForm(s)

    } else {
      // This is for handling regular forms:
      val stemEntry = FstStem(halves(0))
      val inflection = FstRule(halves(1))
      if (inflection == None) {
        None

      } else {
        inflection.get match {
          case vr: VerbRule => {
            Some(VerbForm(stemEntry.lexEntity, stemEntry.stemId, vr.ruleId, vr.person, vr.grammaticalNumber, vr.tense, vr.mood, vr.voice))
          }

          case nr: NounRule => {
            Some(NounForm(stemEntry.lexEntity,stemEntry.stemId,nr.ruleId, nr.gender, nr.grammaticalCase, nr.grammaticalNumber))
          }
          case adjr: AdjectiveRule => {
            Some(AdjectiveForm(stemEntry.lexEntity,stemEntry.stemId,adjr.ruleId, adjr.gender, adjr.grammaticalCase, adjr.grammaticalNumber, adjr.degree))
          }



          case advr: AdverbRule => {
            Some(AdverbForm(stemEntry.lexEntity,stemEntry.stemId,advr.ruleId, advr.degree))
          }

          case pr: ParticipleRule => {
            Some(ParticipleForm(stemEntry.lexEntity,stemEntry.stemId,pr.ruleId, pr.gender, pr.grammaticalCase, pr.grammaticalNumber, pr.tense, pr.voice))
          }
          case gr: GerundiveRule => {
            Some(GerundiveForm(stemEntry.lexEntity,stemEntry.stemId,gr.ruleId, gr.gender, gr.grammaticalCase, gr.grammaticalNumber))
          }

          case gr: GerundRule => {
            Some(GerundForm(stemEntry.lexEntity,stemEntry.stemId,gr.ruleId, gr.grammaticalCase))
          }

          case ir: IndeclRule => {
            Some(IndeclinableForm(stemEntry.lexEntity,stemEntry.stemId,ir.ruleId, ir.pos))
          }

          case infr: InfinitiveRule => {
            Some(InfinitiveForm(stemEntry.lexEntity,stemEntry.stemId,infr.ruleId,infr.tense, infr.voice ))
          }

          case sup: SupineRule => {
            Some(SupineForm(stemEntry.lexEntity,stemEntry.stemId,sup.ruleId,sup.grammaticalCase))
          }

          case _ => {
            println(s"LemmatizedForm.scala: form ${inflection} not yet implemented.")
            None
          }
        }
      }
    }
  }
}


/** Conjugated verb form, identified by person, number, tense, mood and voice.
*
* @param person Property for person.
* @param grammaticalNumber Property for number.
* @param tense Property for tense.
* @param mood Property for mood.
* @param voice Property for voice.
*/
case class VerbForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, person: Person, grammaticalNumber: GrammaticalNumber, tense: Tense, mood: Mood, voice: Voice) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn

  override def toString = {
      val data  = Vector(person,grammaticalNumber, tense, mood, voice).mkString(", ").toLowerCase
      s"verb: " + data  + s" (${lemmaId})"
  }
  def formLabel = {
    val data  = Vector(person,grammaticalNumber, tense, mood, voice).mkString(", ").toLowerCase
    s"verb: " + data
  }
  def formId = {
    s"${person.decimalCode}${grammaticalNumber.decimalCode}${tense.decimalCode}${mood.decimalCode}${voice.decimalCode}000${posCode}"
  }
}

/** Factory object to build a [[VerbForm]] from string vaues.
*/
object VerbForm {
  /** Create a [[VerbForm]] from five FST symbols.
  */
  def apply(lemmaId:  String, stemId:  String, ruleId:  String, p: String, n: String, t: String, m: String, v: String): VerbForm = {
    VerbForm(lemmaId, stemId, ruleId, personForFstSymbol(p), numberForFstSymbol(n), tenseForFstSymbol(t), moodForFstSymbol(m), voiceForFstSymbol(v))
  }
}


/** Noun form, identified by gender, case and number.
*
* @param gender Property for number.
* @param grammaticalCase Property for case.
* @param grammaticalNumber Property for number.
*/
case class NounForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn

  def formLabel = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber).mkString(", ").toLowerCase
    s"noun: " + data
  }
  def formId = {
    s"0${grammaticalNumber.decimalCode}000${gender.decimalCode}${grammaticalCase.decimalCode}0${posCode}"
  }
  override def  toString = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber).mkString(", ").toLowerCase
    s"noun: " + data + s" (${lemmaId})"
  }
}

/** Factory object to build a [[NounForm]] from string vaues.
*/
object NounForm {
  /** Create a [[NounForm]] from three FST symbols.
  */
  def apply(lemmaUrn: String, stemUrn: String, ruleUrn: String, g: String, c: String, n: String): NounForm = {
    NounForm(lemmaUrn, stemUrn, ruleUrn, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n))
  }

}


/** Noun form, identified by gender, case and number.
*
* @param gender Property for number.
* @param grammaticalCase Property for case.
* @param grammaticalNumber Property for number.
*/
case class PronounForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn


  def formLabel = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber).mkString(", ").toLowerCase
    s"pronoun: " + data
  }
  def formId = {
    s"0${grammaticalNumber.decimalCode}000${gender.decimalCode}${grammaticalCase.decimalCode}0${posCode}"
  }


  override def  toString = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber).mkString(", ").toLowerCase
    s"pronoun: " + data  + s" (${lemmaId})"
  }
}

/** Factory object to build a [[PronounForm]] from string vaues.
*/
object PronounForm {
  /** Create a [[PronounForm]] from three FST symbols.
  */
  def apply(lemmaUrn: String, stemUrn: String, ruleUrn: String, g: String, c: String, n: String): PronounForm = {
    PronounForm(lemmaUrn, stemUrn, ruleUrn, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n))
  }
}

/** Adjective form, identified by gender, case, number and degree.
*
* @param gender Property for number.
* @param grammaticalCase Property for case.
* @param grammaticalNumber Property for number.
*/
case class AdjectiveForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber, degree: Degree) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn
  override def  toString = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber,  degree).mkString(", ").toLowerCase
    s"adjective: " + data + s" (${lemmaId})"
  }

  def  formLabel = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber,  degree).mkString(", ").toLowerCase
    s"adjective: " + data
  }
  def formId = {
    s"0${grammaticalNumber.decimalCode}000${gender.decimalCode}${grammaticalCase.decimalCode}${degree.decimalCode}${posCode}"
  }
}


object AdjectiveForm {

  def apply(lemmaUrn: String, stemUrn: String, ruleUrn: String, g: String, c: String, n: String, d: String): AdjectiveForm = {
    AdjectiveForm(lemmaUrn, stemUrn, ruleUrn, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n), degreeForFstSymbol(d))
  }
}


case class ParticipleForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, gender: Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber, tense: Tense, voice:  Voice) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn

  def formLabel = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber, tense, voice).mkString(", ").toLowerCase
    s"participle: " + data
  }
  def  formId = {
    s"0${grammaticalNumber.decimalCode}${tense.decimalCode}0${voice.decimalCode}${gender.decimalCode}${grammaticalCase.decimalCode}0${posCode}"
  }
  override def toString = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber, tense, voice).mkString(", ").toLowerCase
    s"participle: " + data + s" (${lemmaId})"
  }
}

object ParticipleForm {

  def apply(lemmaUrn: String, stemUrn: String, ruleUrn: String, g: String, c: String, n: String, t: String, v: String): ParticipleForm = {
    ParticipleForm(lemmaUrn, stemUrn, ruleUrn, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n), tenseForFstSymbol(t),voiceForFstSymbol(v))
  }
}

/**  form, identified by gender, case and number.
*
* @param grammaticalCase Property for case.
*/
case class GerundiveForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, gender : Gender, grammaticalCase: GrammaticalCase, grammaticalNumber: GrammaticalNumber) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn

  def formLabel = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber).mkString(", ").toLowerCase
    s"gerundive: " + data
  }
  def  formId = {
    s"0${grammaticalNumber.decimalCode}000${gender.decimalCode}${grammaticalCase.decimalCode}0${posCode}"
  }
  override def toString = {
    val data  = Vector(gender, grammaticalCase, grammaticalNumber).mkString(", ").toLowerCase
    s"gerundive: " + data + s" (${lemmaId})"
  }
}

/** Factory object to build a [[NounForm]] from string vaues.
*/
object GerundiveForm {
  /** Create a [[GerundiveForm]] from one FST symbols.
  */
  def apply( lemmaUrn: String, stemUrn: String, ruleUrn: String, g: String, c: String, n: String): GerundiveForm = {
    GerundiveForm(lemmaUrn, stemUrn, ruleUrn, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n) )
  }
}



/**
*/
case class InfinitiveForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, tense: Tense, voice: Voice) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn
  def formLabel = {
    val data  = Vector(tense, voice).mkString(", ").toLowerCase
    s"infinitive: " + data
  }
  def formId = {
    s"00${tense.decimalCode}0${voice.decimalCode}000${posCode}"
  }
  override def toString = {
    val data  = Vector(tense, voice).mkString(", ").toLowerCase
    s"infinitive: " + data + s" (${lemmaId})"
  }
}

/** Factory object to build a [[GerundForm]] from string vaues.
*/
object InfinitiveForm {
  /** Create an  [[InfinitiveForm]] from one FST symbols.
  */
  def apply( lemmaUrn: String, stemUrn: String, ruleUrn: String, tns: String, vce :String): InfinitiveForm = {
    InfinitiveForm(lemmaUrn, stemUrn, ruleUrn, tenseForFstSymbol(tns), voiceForFstSymbol(vce))
  }
}



/**  form, identified by case.
*
* @param grammaticalCase Property for case.
*/
case class GerundForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, grammaticalCase: GrammaticalCase) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn

  def formLabel = {
    val data  = Vector(grammaticalCase).mkString(", ").toLowerCase
    s"gerund: " + data
  }
  def formId = {
      s"000000${grammaticalCase.decimalCode}0${posCode}"
    }

  override def toString = {
    val data  = Vector(grammaticalCase).mkString(", ").toLowerCase
    s"gerund: " + data + s" (${lemmaId})"
  }
}

/** Factory object to build a [[GerundForm]] from string vaues.
*/
object GerundForm {
  /** Create a [[GerundForm]] from one FST symbols.
  */
  def apply( lemmaUrn: String, stemUrn: String, ruleUrn: String, c: String): GerundForm = {
    GerundForm(lemmaUrn, stemUrn, ruleUrn, caseForFstSymbol(c))
  }
}

case class SupineForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, grammaticalCase: GrammaticalCase) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn

  def formLabel = {
    val data  = Vector(grammaticalCase).mkString(", ").toLowerCase
    s"supine: " + data
  }
  def formId = {
    s"000000${grammaticalCase.decimalCode}0${posCode}"
  }
  override def toString = {
    val data  = Vector(grammaticalCase).mkString(", ").toLowerCase
    s"supine: " + data + s" (${lemmaId})"
  }
}
object SupineForm {
  def apply(
    lemmaUrn: String,
    stemUrn: String,
    ruleUrn: String,
    grammCase: String): SupineForm = {
    SupineForm(lemmaUrn, stemUrn, ruleUrn, caseForFstSymbol(grammCase))
  }
}


case class AdverbForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, degree: Degree) extends LemmatizedForm {
    def lemmaId = lemmaUrn
    def stemId = stemUrn
    def ruleId = ruleUrn

  def formLabel = {
      s"adverb: ${degree} degree".toLowerCase
    }
    def formId = {
      s"0000000${degree.decimalCode}${posCode}"
    }
    override def  toString = {
      s"adverb: ${degree} degree".toLowerCase + s" (${lemmaId})"
    }

}
object AdverbForm {

  def apply(lemmaUrn: String, stemUrn: String, ruleUrn: String, deg: String): AdverbForm = {
    AdverbForm(lemmaUrn, stemUrn, ruleUrn, degreeForFstSymbol(deg))
  }
}


/** Indeclinable form, identified only by their part of speech.
*
* @param pos Part of speech.
*/
case class IndeclinableForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, pos: IndeclinablePoS) extends LemmatizedForm {
  def lemmaId = lemmaUrn
  def stemId = stemUrn
  def ruleId = ruleUrn

  def formLabel = {
    s"uninflected form: ${pos}".toLowerCase
  }
  def formId = {
    s"00000000${posCode.toHexString.toUpperCase}"
  }
  override def  toString = {
    s"uninflected form: ${pos}".toLowerCase + s" (${lemmaId})"
  }

}
object IndeclinableForm {

  def apply(lemmaUrn: String, stemUrn: String, ruleUrn: String, s: String): IndeclinableForm ={
    //println("INDCL FORM lemma/s "  + lemma + ", " + s)
    IndeclinableForm(lemmaUrn, stemUrn, ruleUrn, indeclinablePoSForFst(s))
  }
}
