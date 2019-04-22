import edu.holycross.shot.tabulae._

val f = "src/test/resources/coins-no-indecl-no-irreg.txt"

val analyzedTokens = FstFileReader.formsFromFile(f)

def noAnalyses(v: Vector[AnalyzedToken]) = {
  v.filter(_.analyses.isEmpty)
}
def singleLemma(t: AnalyzedToken): Boolean = {
  val lemmata = t.analyses.map(_.lemma).distinct
  (lemmata.size == 1)
}



def singlePos(t: AnalyzedToken): Boolean = {
  val posLabels = t.analyses.map(_.posLabel).distinct
  (posLabels.size == 1)
}


def profileAnalyses(v: Vector[AnalyzedToken]) = {
  println("Number of tokens: " + v.size)
  val analyzed = v.filter(_.analyses.nonEmpty)
  println("Number of tokens analyzed: " +  analyzed.size)

  println("Tokens with unique/multiple analyses: " + analyzed.filter(_.analyses.size == 1).size + "/" + analyzed.filter(_.analyses.size > 1).size)
    println("Tokens with unique/multiple lemmata: " + analyzed.filter(singleLemma(_)).size + "/" + analyzed.filterNot(singleLemma(_)).size)
}


def mapValueForCoin(tkn: AnalyzedToken) = {
  println(tkn.token)

  val remapped = for (analysis <- tkn.analyses) yield {
    analysis match {
      case nf:  NounForm => nf.posLabel + nf.gender + "-" + nf.grammaticalCase + "-" + nf.grammaticalNumber
      case af:  AdjectiveForm => af.posLabel + af.gender + "-" + af.grammaticalCase + "-" + af.grammaticalNumber
      case lf: LemmatizedForm => {"Not a noun or adj.: " + f}
    }
  }
  remapped
}

def mapValuesForCoins(tkns: Vector[AnalyzedToken]) = {
  tkns.map(mapValueForCoin(_))
}
