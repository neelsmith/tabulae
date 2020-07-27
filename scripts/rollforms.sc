
// Person#Number#Tense#Mood#Voice#Gender#Case#Degree
val gender = Vector("masculine", "feminine", "neuter")
val gcase = Vector("nominative", "genitive", "dative", "accusative", "ablative", "vocative")
val gnumber = Vector("singular", "plural")
val degree = Vector("positive", "comparative", "superlative")
val tense = Vector("present", "imperfect", "future", "perfect", "pluperfect", "future perfect")
val mood = Vector("indicative", "subjunctive", "imperative")
val voice = Vector("active", "passive")
val person = Vector("first", "second", "third")


// infl groups:
val noun =  0
val pronoun = 1
val adjective =  2
val adverb = 3
val finiteverb = 4
val participle = 5
val infinitive =  6
val gerundive = 7
val gerund = 8
val supine = 9
val indeclconj =  0xA
val indeclprep = 0xB
val indecexclam = 0xC
val indeclnum = 0xD


val posLabels = Map(
  noun -> "noun",
  pronoun -> "pronoun",
  gerund -> "gerund",
  supine -> "supine",
  indeclconj -> "uninflected conjunction",
  indeclprep -> "uninflected preposition",
  indecexclam -> "uninflected exclamation",
  indeclnum -> "uninflected number"


)
// 0 for noun, 1 for pronoun
def rollSubst(nounOrPronoun: Int): Vector[String] = {

    val threedeep = for ( (g, gidx) <- gender.zipWithIndex ) yield {
    val embedded = for ( (gc, gcidx) <- gcase.zipWithIndex) yield {
      val records = for ((gnum, gnumidx) <- gnumber.zipWithIndex) yield {
        val s = s"0${gnumidx + 1}000${gidx + 1}${gcidx + 1}0${nounOrPronoun}#${posLabels(nounOrPronoun)}: ${g} ${gc} ${gnum}"
        s
      }
      records
    }
    embedded
  }
  threedeep.flatten.flatten
}



def rollAdjectives : Vector[String]= {
    val adjs = for ( (g, gidx) <- gender.zipWithIndex ) yield {
      val embedded = for ( (gc, gcidx) <- gcase.zipWithIndex) yield {
        val substs = for ((gnum, gnumidx) <- gnumber.zipWithIndex) yield {
          val records = for ((d, didx) <- degree.zipWithIndex) yield {
            val s = s"0${gnumidx + 1}000${gidx + 1}${gcidx + 1}${didx + 1}${adjective}#adjective: ${g} ${gc} ${gnum} ${d}"
            s
          }
          records
        }
        substs
      }
    embedded
  }
  adjs.flatten.flatten.flatten
}
def rollAdvs : Vector[String] = {
  val records = for ((d, didx) <- degree.zipWithIndex) yield {
    val s = s"0000000${didx + 1}${adverb}#adverb: ${d} degree"
    s
  }
  records
}
def rollParticiples = {
  val ptcpls = for ( (v, vidx) <- voice.zipWithIndex) yield {
    val tensed = for ( (t, tidx) <- tense.zipWithIndex) yield {
      val adjs = for ( (g, gidx) <- gender.zipWithIndex ) yield {
        val embedded = for ( (gc, gcidx) <- gcase.zipWithIndex) yield {
          val substs = for ((gnum, gnumidx) <- gnumber.zipWithIndex) yield {
            val records = for ((d, didx) <- degree.zipWithIndex) yield {
              val s = s"0${gnumidx + 1}${tidx + 1}0${vidx + 1}${gidx + 1}${gcidx + 1}0${participle}#participle: ${t} ${v} ${g} ${gc} ${gnum}"
              s
            }
            records
          }
          substs
        }
        embedded
      }
      adjs
    }
    tensed
  }
  ptcpls.flatten.flatten.flatten.flatten.flatten
}
def rollInfinitives = {
  val infins = for ( (t, tidx) <- tense.zipWithIndex)  yield {
    val records = for ( (v, vidx) <- voice.zipWithIndex) yield {
      val s = s"00${tidx + 1}0${vidx + 1}000${infinitive}#infinitive: ${t} ${v}"
      s
    }
    records
  }
  infins.flatten
}

// 8 for gerund, 9 for supine
def rollVerbalNouns(vnoun: Int) = {
  val records = for ( (gc, gcidx) <- gcase.zipWithIndex) yield {
    val s = s"000000${gcidx + 1}0${vnoun}#${posLabels(vnoun)}: ${gc}"
    s
  }
  records
}


def rollFiniteVerbs = {
  val verbs = for ( (p, pidx) <- person.zipWithIndex) yield {
    val nums = for ((gnum, gnumidx) <- gnumber.zipWithIndex) yield {
      val tenses = for ((t, tidx) <- tense.zipWithIndex) yield {
        val moods = for ((m, midx) <- mood.zipWithIndex) yield {
          val records = for ((v, vidx) <- voice.zipWithIndex) yield {
            val s = s"${pidx + 1}${gnumidx + 1}${tidx + 1}${midx + 1}${vidx + 1}000${finiteverb}#finite verb: ${p} ${gnum} ${t} ${m} ${v}"
            s
          }
          records
        }
        moods
      }
      tenses
    }
    nums
  }
  verbs.flatten.flatten.flatten.flatten
}


def rollUninfl = {
  Vector(0xA, 0xB, 0xC, 0xD).map (i=> "00000000" + i.toHexString.toUpperCase + "#" + posLabels(i))
}
def cex: String = {
  val group = "urn:cite2:tabulae:morphforms.v1:"
  val header = "urn#label\n"
  header + (rollSubst(0) ++ rollSubst(1)  ++ rollAdjectives ++ rollAdvs ++ rollParticiples ++ rollFiniteVerbs ++ rollInfinitives ++ rollVerbalNouns(gerund) ++ rollVerbalNouns(supine )  ++ rollUninfl).map(group + _ ).mkString("\n")
}

import java.io.PrintWriter
def writeCex(fname: String = "forms.cex") : Unit = {
  new PrintWriter(fname){write(cex); close;}
  println("\nPermutation of all form URN values written to " + fname + ".")
}
