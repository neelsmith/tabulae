
// Person#Number#Tense#Mood#Voice#Gender#Case#Degree
val gender = Vector("masculine", "feminine", "neuter")
val gcase = Vector("nominative", "genitive", "dative", "accusative", "ablative", "vocative")
val gnumber = Vector("singular", "plural")
val degree = Vector("positive", "comparative", "superlative")
val tense = Vector("present", "imperfect", "future", "perfect", "pluperfect", "future perfect")
val mood = Vector("indicative", "subjunctive", "imperative")
val voice = Vector("active", "passive")
val person = Vector("first", "second", "third")


def rollNouns: Vector[String] = {
    val threedeep = for ( (g, gidx) <- gender.zipWithIndex ) yield {
    val embedded = for ( (gc, gcidx) <- gcase.zipWithIndex) yield {
      val records = for ((gnum, gnumidx) <- gnumber.zipWithIndex) yield {
        val s = s"0${gnumidx + 1}000${gidx + 1}${gcidx + 1}0#${g} ${gc} ${gnum}"
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
            val s = s"0${gnumidx + 1}000${gidx + 1}${gcidx + 1}${didx + 1}#${g} ${gc} ${gnum} ${d}"
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
    val s = s"0000000${didx + 1}#${d} degree"
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
              val s = s"0${gnumidx + 1}${tidx + 1}0${vidx + 1}${gidx + 1}${gcidx + 1}0#${t} ${v} ${g} ${gc} ${gnum}"
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
      val s = s"00${tidx + 1}0${vidx + 1}000#${t} ${v}"
      s
    }
    records
  }
  infins.flatten
}
def rollVerbalNouns = {
  val records = for ( (gc, gcidx) <- gcase.zipWithIndex) yield {
    val s = s"000000${gcidx + 1}0#${gc}"
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
            val s = s"${pidx + 1}${gnumidx + 1}${tidx + 1}${midx + 1}${vidx + 1}000#${p} ${gnum} ${t} ${m} ${v}"
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

def cex: String = {
  val group = "urn:cite2:tabulae:morphforms.v1:"
  val header = "urn#label\nurn:cite2:tabulae:morphforms.v1:00000000#uninflected form\n"
  header + (rollNouns  ++ rollAdjectives ++ rollAdvs ++ rollParticiples ++ rollFiniteVerbs ++ rollInfinitives ++ rollVerbalNouns).map(group + _ ).mkString("\n")
}
