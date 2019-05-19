package edu.holycross.shot.tabulae




/** Association of [[LemmatizedForm]]s with a surface string (token).
*
* @param token A morphologically analyzed surface form.
* @param analyses [[LemmatizedForm]]s associated with this token.
*/
case class AnalyzedToken(token: String, analyses: Vector[LemmatizedForm]) {



  /** True if tkn is a preposition.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def prepToken : Boolean = {
    if (analyses.isEmpty) {
      false
    } else {
      analyses(0) match {
        case indecl: IndeclinableForm => {

          ((indeclPos.nonEmpty) && (indeclPos.contains(Preposition)))
        }
        case _ => false
      }
    }
  }

  /** True if tkn is a conjugated verb form.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def verbToken : Boolean = {
    if (analyses.isEmpty) {
      false
    } else {
      analyses(0) match {
        case v: VerbForm => true
        case _ => false
      }
    }
  }

  /** True if tkn is a noun.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def nounToken : Boolean = {
    if (analyses.isEmpty) {
      false
    } else {
      analyses(0) match {
        case n: NounForm => true
        case _ => false
      }
    }
  }

  /** True if tkn is an adjective.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def adjToken : Boolean = {
    if (analyses.isEmpty) {
      false
    } else {
      analyses(0) match {
        case n: AdjectiveForm => true
        case _ => false
      }
    }
  }

  /** True if tkn is a particple.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def ptcplToken : Boolean = {
    if (analyses.isEmpty) {
      false
    } else {
      analyses(0) match {
        case n: ParticipleForm => true
        case _ => false
      }
    }
  }


  /** True if tkn is an indeclinable form.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def indeclToken : Boolean = {
    if (analyses.isEmpty) {
      false
    } else {
      analyses(0) match {
        case indecl: IndeclinableForm => true
        case _ => false
      }
    }
  }

  //
  // Common to all substantive forms (noun, adj, ptcpl):  GCN
  //
  /** List of possible values for gender.  For a
  * substantive (noun, adj, ptcpl), this should be a non-empty
  * Vector of Gender values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def substGender: Vector[Gender] = {
    if (analyses.isEmpty) {
      Vector.empty[Gender]
    } else {
      val genderList = for (lysis <- analyses) yield {
        lysis match {
            case n : NounForm => Some(n.gender)
            case adj : AdjectiveForm => Some(adj.gender)
            case ptcpl : ParticipleForm => Some(ptcpl.gender)
            case _ => None
        }
      }
      genderList.flatten.toVector.distinct
    }
  }

  /** List of possible values for grammatical case.  For a
  * substantive (noun, adj, ptcpl), this should be a non-empty
  * Vector of GrammaticalCase values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def substCase : Vector[GrammaticalCase] = {
    if (analyses.isEmpty) {
      Vector.empty[GrammaticalCase]
    } else {
      val caseList = for (lysis <- analyses) yield {
        lysis match {
            case n : NounForm => Some(n.grammaticalCase)
            case adj : AdjectiveForm => Some(adj.grammaticalCase)
            case ptcpl : ParticipleForm => Some(ptcpl.grammaticalCase)
            case _ => None
        }
      }
      caseList.flatten.toVector.distinct.filterNot(_ == Vocative)
    }
  }

  /** List of possible values for gender.  For a
  * substantive (noun, adj, ptcpl) or a conjugaged verb form,
  * this should be a non-empty
  * Vector of Gender values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def grammNumber: Vector[GrammaticalNumber] = {
    if (analyses.isEmpty) {
      Vector.empty[GrammaticalNumber]
    } else {
      val numberList = for (lysis <- analyses) yield {
        lysis match {
            case n : NounForm => Some(n.grammaticalNumber)
            case adj : AdjectiveForm => Some(adj.grammaticalNumber)
            case ptcpl : ParticipleForm => Some(ptcpl.grammaticalNumber)
            case v : VerbForm => Some(v.grammaticalNumber)
            case _ => None
        }
      }
      numberList.flatten.toVector.distinct
    }
  }

  /** List of Gender/Case/Number triples.  For a
  * substantive (noun, adj, ptcpl), this should be a non-empty
  * Vector of GCNTriples.
  * For other "parts of speech," this will be an empty Vector.
  */
  def gcn: Vector[GCNTriple] = {
    if (analyses.isEmpty) {
      Vector.empty[GCNTriple]
    } else {
      val tripleList = for (lysis <- analyses) yield {
        lysis match {
            case n : NounForm => Some(GCNTriple(n.gender, n.grammaticalCase, n.grammaticalNumber))
            case adj : AdjectiveForm => Some(GCNTriple(adj.gender, adj.grammaticalCase, adj.grammaticalNumber))
            case ptcpl : ParticipleForm => Some(GCNTriple(ptcpl.gender, ptcpl.grammaticalCase, ptcpl.grammaticalNumber))
            //case cverb:
            // case participle:
            case _ => None
        }
      }
      tripleList.flatten.toVector.distinct
    }
  }



  //
  // Common to all VERBAL forms (conjugated, inf, ptcpl):  tense, voice
  //

  /** List of possible values for tense.  For a
  * verbal form (conjugated form, infinitive, ptcpl), this should be a non-empty
  * Vector of Tense values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def tense: Vector[Tense] = {
    if (analyses.isEmpty) {
      Vector.empty[Tense]
    } else {
      val tenseList = for (lysis <- analyses) yield {
        lysis match {
            case v : VerbForm => Some(v.tense)
            // infinitive
            case ptcpl : ParticipleForm => Some(ptcpl.tense)
            case _ => None
        }
      }
      tenseList.flatten.toVector.distinct
    }
  }

  /** List of possible values for voice.  For a
  * verbal form (conjugated form, infinitive, ptcpl), this should be a non-empty
  * Vector of Voice values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def voice: Vector[Voice] = {
    if (analyses.isEmpty) {
      Vector.empty[Voice]
    } else {
      val voiceList = for (lysis <- analyses) yield {
        lysis match {
            case v : VerbForm => Some(v.voice)
            // infinitive
            case ptcpl : ParticipleForm => Some(ptcpl.voice)
            case _ => None
        }
      }
      voiceList.flatten.toVector.distinct
    }
  }

  // Specific to conjugated forms:  Person, Mood.

  /** List of possible values for person.  For a
  * conjugated verb form, this should be a non-empty
  * Vector of Person values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def person: Vector[Person] = {
    if (analyses.isEmpty) {
      Vector.empty[Person]
    } else {
      val personList = for (lysis <- analyses) yield {
        lysis match {
            case v : VerbForm => Some(v.person)
            case _ => None
        }
      }
      personList.flatten.toVector.distinct
    }
  }


  /** List of possible values for mood.  For a
  * conjugated verb form, this should be a non-empty
  * Vector of Mood values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def mood: Vector[Mood] = {
    if (analyses.isEmpty) {
      Vector.empty[Mood]
    } else {
      val moodList = for (lysis <- analyses) yield {
        lysis match {
            case v : VerbForm => Some(v.mood)
            case _ => None
        }
      }
      moodList.flatten.toVector.distinct
    }
  }



  //
  // Specific to indeclinable forms:  part of speech label
  //
  def indeclPos: Vector[IndeclinablePoS] = {
    if (analyses.isEmpty) {
      Vector.empty[IndeclinablePoS]

    } else {
      val posList = for (lysis <- analyses) yield {
        lysis match {
            case indecl : IndeclinableForm => Some(indecl.pos)
            case _ => None
        }
      }
      posList.flatten.toVector.distinct
    }
  }


}
