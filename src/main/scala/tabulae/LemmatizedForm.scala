package edu.holycross.shot.tabulae

/*

Analytical patterns we need to implement:

<noun><adj><verb><vadj><infin><gerundive><gerund><supine><ptcpl><adv><pron><irregcverb><irregnoun><irregadj><irregadv><irreginfin><irregptcpl><irreggrnd><irreggrndv><irregsupn><irregpron><indecl>
*/


/** A valid grammatical form identification.*/
sealed trait LemmatizedForm {
  def lemmaId: String
  def stemId: String
  def ruleId: String
  def posLabel: String = {
    this match {
      case v: VerbForm => "verb"
      case n: NounForm => "noun"
      case adj: AdjectiveForm => "adjective"
      case ptcpl: ParticipleForm => "participle"
      case gnd: GerundForm => "gerund"
      case gndv: GerundiveForm => "gerundive"
      case adv: AdverbForm => "adverb"
      case indecl: IndeclinableForm => "indeclinable"
      /*
      case inf: InfinitiveForm => "infinitive"
      */
    }
  }
}

/** Factory object to create full [[LemmatizedForm]] from a string of FST.
*/
object LemmatizedForm {


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
      case _ => {
        val err = "Irreg class "+ irregClass + " not recognized."
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


      inflection match {
        case vr: VerbRule => {
          Some(VerbForm(stemEntry.lexEntity, stemEntry.stemId, inflection.ruleId, vr.person, vr.grammaticalNumber, vr.tense, vr.mood, vr.voice))
        }

        case nr: NounRule => {
          Some(NounForm(stemEntry.lexEntity,stemEntry.stemId,inflection.ruleId, nr.gender, nr.grammaticalCase, nr.grammaticalNumber))
        }
        case adjr: AdjectiveRule => {
          Some(AdjectiveForm(stemEntry.lexEntity,stemEntry.stemId,inflection.ruleId, adjr.gender, adjr.grammaticalCase, adjr.grammaticalNumber, adjr.degree))
        }

        case pr: ParticipleRule => {
          Some(ParticipleForm(stemEntry.lexEntity,stemEntry.stemId,inflection.ruleId, pr.gender, pr.grammaticalCase, pr.grammaticalNumber, pr.tense, pr.voice))
        }
        case gr: GerundiveRule => {
          Some(GerundiveForm(stemEntry.lexEntity,stemEntry.stemId,inflection.ruleId, gr.gender, gr.grammaticalCase, gr.grammaticalNumber))
        }

        case gr: GerundRule => {
          Some(GerundForm(stemEntry.lexEntity,stemEntry.stemId,inflection.ruleId, gr.grammaticalCase))
        }

        case ir: IndeclRule => {
          Some(IndeclinableForm(stemEntry.lexEntity,stemEntry.stemId,inflection.ruleId, ir.pos))
        }

        case _ => {
          println(s"Form.scala: form ${inflection} not yet implemented.")
          None
        }
        //throw new Exception(s"Form.scala: form ${inflection} not yet implemented.")
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
}

/** Factory object to build a [[NounForm]] from string vaues.
*/
object GerundiveForm {
  /** Create a [[GerundForm]] from one FST symbols.
  */
  def apply( lemmaUrn: String, stemUrn: String, ruleUrn: String, g: String, c: String, n: String): GerundiveForm = {
    GerundiveForm(lemmaUrn, stemUrn, ruleUrn, genderForFstSymbol(g), caseForFstSymbol(c), numberForFstSymbol(n) )
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

case class AdverbForm(lemmaUrn: String, stemUrn: String, ruleUrn: String, degree: Degree) extends LemmatizedForm {
    def lemmaId = lemmaUrn
    def stemId = stemUrn
    def ruleId = ruleUrn
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
}
object IndeclinableForm {

  def apply(lemmaUrn: String, stemUrn: String, ruleUrn: String, s: String): IndeclinableForm ={
    //println("INDCL FORM lemma/s "  + lemma + ", " + s)
    IndeclinableForm(lemmaUrn, stemUrn, ruleUrn, indeclinablePoSForFst(s))
  }
}
