package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import org.scalatest.FlatSpec

// This only works if you're online:  the LewisShort object loads
// data over the internet.
class LemmatizedFormCexSpec extends FlatSpec {

  def lemmaId(lemma: Cite2Urn): String = {
    lemma.collection + "." + lemma.objectComponent
  }
  "The LemmatizedForm object" should "build a LemmatizedForm for finite verb forms from CEX" in {
    //urn:cite2:linglat:tkns.v1:2020_08_02_1723#Record 2020_08_02_1723#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:4pr.3.25#dixit#urn:cite2:tabulae:ls.v1:n13804#urn:cite2:tabulae:morphforms.v1:314110004#LexicalToken#1723

    val lemma = Cite2Urn("urn:cite2:tabulae:ls.v1:n13804")
    val form = Cite2Urn("urn:cite2:tabulae:morphforms.v1:314110004")
    val lemmatizedForm = LemmatizedForm.fromFormUrn(lemmaId(lemma), "", "", form )
    assert(lemmatizedForm.verbMood.get == Indicative)
  }

  // urn:cite2:linglat:tkns.v1:2020_08_02_14#Record 2020_08_02_14#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:pr.1.7#Nox#urn:cite2:tabulae:ls.v1:n31309#urn:cite2:tabulae:morphforms.v1:010002100#LexicalToken#14
  it should  "build a LemmatizedForm for noun forms from CEX" in  {
    val lemma = Cite2Urn("urn:cite2:tabulae:ls.v1:n31309")
    val form = Cite2Urn("urn:cite2:tabulae:morphforms.v1:010002100")
    val lemmatizedForm = LemmatizedForm.fromFormUrn(lemmaId(lemma), "", "", form )
    assert(lemmatizedForm.substantiveGender.get == Feminine)
  }


  //urn:cite2:linglat:tkns.v1:2020_08_02_41#Record 2020_08_02_41#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:pr.1.23#id#urn:cite2:tabulae:ls.v1:n25029#urn:cite2:tabulae:morphforms.v1:010003101#LexicalToken#41
  it should  "build a LemmatizedForm for pronoun forms from CEX" in  {
    val lemma = Cite2Urn("urn:cite2:tabulae:ls.v1:n25029")
    val form = Cite2Urn("urn:cite2:tabulae:morphforms.v1:010003101")
    val lemmatizedForm = LemmatizedForm.fromFormUrn(lemmaId(lemma), "", "", form )
    assert(lemmatizedForm.substantiveGender.get == Neuter)
  }


  //urn:cite2:linglat:tkns.v1:2020_08_02_1150#Record 2020_08_02_1150#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:2pr.3.10#adulescentis#urn:cite2:tabulae:ls.v1:n1021#urn:cite2:tabulae:morphforms.v1:010001212#LexicalToken#1150
  it should  "build a LemmatizedForm for adjective forms from CEX" in  {
    val lemma = Cite2Urn("urn:cite2:tabulae:ls.v1:n1021")
    val form = Cite2Urn("urn:cite2:tabulae:morphforms.v1:010001212")
    val lemmatizedForm = LemmatizedForm.fromFormUrn(lemmaId(lemma), "", "", form )
    assert(lemmatizedForm.substantiveCase.get == Genitive)
  }


  //urn:cite2:linglat:tkns.v1:2020_08_02_1005#Record 2020_08_02_1005#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:2pr.1.31#ita#urn:cite2:tabulae:ls.v1:n25100#urn:cite2:tabulae:morphforms.v1:000000013#LexicalToken#1005
  it should  "build a LemmatizedForm for adverb forms from CEX" in  {
    val lemma = Cite2Urn("urn:cite2:tabulae:ls.v1:n25100")
    val form = Cite2Urn("urn:cite2:tabulae:morphforms.v1:000000013")
    val lemmatizedForm = LemmatizedForm.fromFormUrn(lemmaId(lemma), "", "", form )
    assert(lemmatizedForm.adverbDegree.get == Positive)
  }

  //urn:cite2:linglat:tkns.v1:2020_08_02_1163#Record 2020_08_02_1163#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:2pr.3.17#cognito#urn:cite2:tabulae:ls.v1:n8938#urn:cite2:tabulae:morphforms.v1:014021305#LexicalToken#1163
  it should  "build a LemmatizedForm for participle forms from CEX" in  {
    val lemma = Cite2Urn("urn:cite2:tabulae:ls.v1:n8938")
    val form = Cite2Urn("urn:cite2:tabulae:morphforms.v1:014021305")
    val lemmatizedForm = LemmatizedForm.fromFormUrn(lemmaId(lemma), "", "", form )
    assert(lemmatizedForm.participleTense.get == Perfect)
  }

  // urn:cite2:linglat:tkns.v1:2020_08_02_1258#Record 2020_08_02_1258#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:2pr.5.14#appellari#urn:cite2:tabulae:ls.v1:n3134#urn:cite2:tabulae:morphforms.v1:001020006#LexicalToken#1258
  it should  "build a LemmatizedForm for infinitive forms from CEX" in  {
    val lemma = Cite2Urn("urn:cite2:tabulae:ls.v1:n3134")
    val form = Cite2Urn("urn:cite2:tabulae:morphforms.v1:001020006")
    val lemmatizedForm = LemmatizedForm.fromFormUrn(lemmaId(lemma), "", "", form )
    assert(lemmatizedForm.infinitiveVoice.get == Passive)
  }

  //urn:cite2:linglat:tkns.v1:2020_08_02_2971#Record 2020_08_02_2971#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:12pr.2.13#faciendi#urn:cite2:tabulae:ls.v1:n17516#urn:cite2:tabulae:morphforms.v1:020001107#LexicalToken#2971
  it should  "build a LemmatizedForm for gerund forms from CEX" in  pending


  //urn:cite2:linglat:tkns.v1:2020_08_02_13782#Record 2020_08_02_13782#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:57pr.2.27#interficiendum#urn:cite2:tabulae:ls.v1:n24257#urn:cite2:tabulae:morphforms.v1:000000408#LexicalToken#13782
  it should  "build a LemmatizedForm for gerundive forms from CEX" in  pending

  // urn:cite2:linglat:tkns.v1:2020_08_02_30244#Record 2020_08_02_30244#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:126a.9.44#rogatu#urn:cite2:tabulae:ls.v1:n41838#urn:cite2:tabulae:morphforms.v1:000000509#LexicalToken#30244
  it should  "build a LemmatizedForm for supine forms from CEX" in  pending


  //urn:cite2:linglat:tkns.v1:2020_08_02_30256#Record 2020_08_02_30256#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:127a.1.2#et#urn:cite2:tabulae:ls.v1:n16278#urn:cite2:tabulae:morphforms.v1:00000000A#LexicalToken#30256
  //
  //urn:cite2:linglat:tkns.v1:2020_08_02_30260#Record 2020_08_02_30260#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:127a.1.6#a#urn:cite2:tabulae:ls.v1:n4#urn:cite2:tabulae:morphforms.v1:00000000B#LexicalToken#30260
  //
  // urn:cite2:linglat:tkns.v1:2020_08_02_31529#Record 2020_08_02_31529#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:134a.4.3#duodecim#urn:cite2:tabulae:ls.v1:n14920#urn:cite2:tabulae:morphforms.v1:00000000D#LexicalToken#31529
  it should  "build a LemmatizedForm for indeclinable forms from CEX" in  pending


  //val cexLine = "urn:cite2:linglat:tkns.v1:2020_08_02_12#Record 2020_08_02_12#urn:cts:latinLit:stoa1263.stoa001.hc_tkns:pr.1.5#et#urn:cite2:tabulae:ls.v1:n16278#urn:cite2:tabulae:morphforms.v1:00000000A#LexicalToken#12"
  //

}
