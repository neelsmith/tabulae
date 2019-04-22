
package edu.holycross.shot

/**  Classes for working with Latin morphological concepts,
*  and for parsing string output of tabulae's FST parsing into
*  objects.
*/
package object tabulae {

  /** Convert ASCII string to FST symbols.
  *
  * @param ascii Latin string.
  */
  def asciiToFst(ascii: String): String = {
    ascii.replaceAll( "_","<lo>").
          replaceAll( "\\^", "<sh>").
          replaceAll( "-", "<#>")
  }

  /** Convert string in FST symbols to ASCII.
  *
  * @param ascii Latin string in FST symbols.
  */
  def fstToAscii(fst: String): String = {
    fst.replaceAll("<lo>", "_").
      replaceAll("<sh>", "^").
      replaceAll("<#>","-")
  }

  /** Map FST symbol name to [[Gender]].  */
  val genderForFstSymbol: Map[String,Gender] = Map(
    "fem" -> Feminine,
    "masc" -> Masculine,
    "neut" -> Neuter
  )


  /** Map FST symbol name to [[GrammaticalCase]].  */
  val caseForFstSymbol: Map[String, GrammaticalCase] = Map(
    "nom" -> Nominative,
    "gen" -> Genitive,
    "dat" -> Dative,
    "acc" -> Accusative,
    "abl" -> Ablative,
    "voc" -> Vocative
  )

  /** Map FST symbol name to [[GrammaticalNumber]].  */
  val numberForFstSymbol: Map[String, GrammaticalNumber] = Map(
    "sg" -> Singular,
    "pl" -> Plural
  )

  /** Map FST symbol name to [[Degree]].  */
  val degreeForFstSymbol: Map[String, Degree] = Map(
    "pos" -> Positive,
    "comp" -> Comparative,
    "superl" -> Superlative
  )

  /** Map string used in test data tables to [[Degree]].  */
  val degreeFForTestLabel: Map[String,Degree] = Map(
    "positive" -> Positive,
    "comparative" -> Comparative,
    "superlative" -> Superlative
  )

  /** Map string used in test data tables to [[Gender]].  */
  val genderForTestLabel: Map[String,Gender] = Map(
    "feminine" -> Feminine,
    "masculine" -> Masculine,
    "neuter" -> Neuter
  )

  /** Map string used in test data tables to [[GrammaticalCase]].  */
  val caseForTestLabel: Map[String, GrammaticalCase] = Map(
    "nominative" -> Nominative,
    "genitive" -> Genitive,
    "dative" -> Dative,
    "accusative" -> Accusative,
    "ablative" -> Ablative,
    "vocative" -> Vocative
  )

  /** Map string used in test data tables to [[GrammaticalNumber]].  */
  val numberForTestLabel: Map[String, GrammaticalNumber] = Map(
    "singular" -> Singular,
    "plural" -> Plural
  )

  /** Map string used in test data tables to [[IndeclinablePoS]].  */
  val indeclinablePoSForTestLabel: Map[String, IndeclinablePoS] = Map(
    "conjunction" -> Conjunction,
    "preposition" -> Preposition,
    "exclamation" -> Exclamation
  )



  /** Map string used in fst data to [[IndeclinablePoS]]. */
  val indeclinablePoSForFst: Map[String, IndeclinablePoS] = Map(
    "conjunct" -> Conjunction,
    "preposition" -> Preposition,
    "exclamation" -> Exclamation
  )


  /** Map string used in test data tables to [[Person]].  */
  val personForFstSymbol: Map[String, Person] = Map(
    "1st" -> First,
    "2nd" -> Second,
    "3rd" -> Third
  )

  /** Map string used in test data tables to [[Person]].  */
  val personForTestLabel: Map[String, Person] = Map(
    "first" -> First,
    "second" -> Second,
    "third" -> Third
  )

  /** Map string used in test data tables to [[Tense]].  */
  val tenseForFstSymbol: Map[String, Tense] = Map(
    "pres" -> Present,
    "impft" -> Imperfect,
    "fut" -> Future,
    "pft" -> Perfect,
    "plpft" -> Pluperfect,
    "futpft" -> FuturePerfect
  )

  /** Map string used in test data tables to [[Tense]].  */
  val tenseForTestLabel: Map[String, Tense] = Map(
    "present" -> Present,
    "imperfect" -> Imperfect,
    "future" -> Future,
    "perfect" -> Perfect,
    "pluperfect" -> Pluperfect,
    "future-perfect" -> FuturePerfect,
  )

  /** Map string used in test data tables to [[Mood]].  */
  val moodForFstSymbol: Map[String, Mood] = Map(
    "indic" -> Indicative,
    "subj" -> Subjunctive,
    "imptv" -> Imperative
  )

  /** Map string used in test data tables to [[Mood]].  */
  val moodForTestLabel: Map[String, Mood] = Map(
    "indicative" -> Indicative,
    "subjunctive" -> Subjunctive,
    "imperative" -> Imperative
  )

  /** Map string used in test data tables to [[Voice]].  */
  val voiceForFstSymbol: Map[String, Voice] = Map(
    "act" -> Active,
    "pass" -> Passive
  )

  /** Map string used in test data tables to [[Voice]].  */
  val voiceForTestLabel: Map[String, Voice] = Map(
    "active" -> Active,
    "passive" -> Passive
  )

}
