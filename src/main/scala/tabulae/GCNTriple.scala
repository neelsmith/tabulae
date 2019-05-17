package edu.holycross.shot.tabulae


/** Class simplifying working with shared morpholgoical properties of all
* substantives  (nouns, adjectives, participles), namely, the triple
* gender, case and number.
*
* @param gender Gender (masculine, feminine or neuter).
* @param gcase Grammatical case (only nominative, genitive, dative, accusative,
* or ablative:  no vocative in numismatic Latin).
* @param gnumber Grammatical number (singular or plural).
*/
case class GCNTriple(gender: Gender, gcase: GrammaticalCase, gnumber: GrammaticalNumber)
