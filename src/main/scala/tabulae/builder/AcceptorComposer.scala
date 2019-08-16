package edu.holycross.shot.tabulae.builder


import better.files.{File => ScalaFile, _}
import better.files.Dsl._

/** Factory object for composing and writing to a file the top-level
* acceptor transducer, acceptor.fst, in the root of the project FST build.
* It checks for every possible lexicon file, and includes acceptors for
* those kinds that are present in this given build.
*
* The acceptor transducer is the final stage in the morphological pipeline.
* See documentation on the tabulae github repository for details.
*/
object AcceptorComposer {

  /** Compose acceptor.fst and the intermediate fst files
  * it depends on for a named corpus.  Note that this will only
  * work if some lexica files have already been installed.
  *
  * @param parsers Root of working area for writing parsers.
  * @param corpusList  List of "corpora" names.
  */
  def apply(parsers: ScalaFile, corpusList: Vector[String]): Unit = {
    val projectDir = parsers / corpusList.mkString("-")
    composeMainAcceptor(projectDir)
  }

  /** Write acceptor.fst, the final transducer in the
  * the FST chain.  Should only include acceptors for
  * components that actually appear in the project's data.
  *
  * @param projectDir The directory for the corpus-specific
  * parser where acceptor.fst should be written.
  */
  def composeMainAcceptor(projectDir: ScalaFile): Unit = {
    //println("MAKEING MAIN ACCEPTOR FOR " + projectDir)
    val fst = StringBuilder.newBuilder
    // automatically included
    fst.append("#include \"" + projectDir.toString + "/symbols.fst\"\n")

    // All the individual acceptor transducers:
    val acceptors : Vector[String] = Vector(
      verbAcceptor(projectDir) ,
      indeclAcceptor(projectDir),
      infinitiveAcceptor(projectDir),
      participleAcceptor(projectDir),
      gerundiveAcceptor(projectDir),
      gerundAcceptor(projectDir),
      supineAcceptor(projectDir),

      nounAcceptor(projectDir),
      adjectiveAcceptor(projectDir),
      adverbAcceptor(projectDir),

      irregVerbAcceptor(projectDir),
      irregInfinitiveAcceptor(projectDir),
      irregParticipleAcceptor(projectDir),
      irregGerundAcceptor(projectDir),
      irregGerundiveAcceptor(projectDir),
      irregSupineAcceptor(projectDir),

      irregNounAcceptor(projectDir),
      irregAdverbAcceptor(projectDir),
      irregPronounAcceptor(projectDir),
      irregAdjectiveAcceptor(projectDir)
    )
    for (acceptor <- acceptors) { fst.append(acceptor + "\n") }
    fst.append("\n\n" + topLevelAcceptor(projectDir) + "\n")

    val acceptorFile = projectDir / "acceptor.fst"
    acceptorFile.overwrite(fst.toString)
  }

  /** Rewrite a single file by replacing all occurrences of
  * the variable name `@workdir@` with the string value for the
  * work directory.
  *
  * @param f File to rewrite.
  * @param workDir Actual directory where corpus-specific
  * parser is to be built.
  */
  def rewriteFile(f: ScalaFile, workDir: ScalaFile): Unit = {
    val lines = f.lines.toVector
    val rewritten = lines.map(_.replaceAll("@workdir@", workDir.toString + "/")).mkString("\n")
    f.overwrite(rewritten)
  }

  // BOOLEAN FUNCTIONS DETERMINING IF PARTICULAR ANALYTICAL
  // TYPES SHOULD BE INCLUDED IN THE ACCEPTOR:
  def includeInfinitives(dir: ScalaFile): Boolean = {
    includeVerbs(dir)
  }
  def includeParticiples(dir: ScalaFile): Boolean = {
    includeVerbs(dir)
  }
  def includeGerundives(dir: ScalaFile): Boolean = {
    includeVerbs(dir)
  }
  def includeGerunds(dir: ScalaFile): Boolean = {
    includeVerbs(dir)
  }
  def includeSupines(dir: ScalaFile): Boolean = {
    includeVerbs(dir)
  }



  def includeIrregVerbs(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irregverbs.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregVerbAcceptor(dir : ScalaFile): String = {
    if (includeIrregVerbs(dir) ) {
      """
% Irregular verb acceptor
$squashirregverburn$ =  <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+[#person#] [#number#] [#tense#] [#mood#] [#voice#]<irregcverb><div><irregcverb><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
    } else {""}
  }



  def includeIrregNouns(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irregverbs.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregNounAcceptor(dir : ScalaFile): String = {
    if (includeIrregNouns(dir) ) {
      """
% Irregular noun acceptor
$squashirregnounurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ $gender$ $case$ $number$ <irregnoun> <div> <irregnoun> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
    } else {""}
  }

  def includeIrregAdjectives(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irregverbs.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregAdjectiveAcceptor(dir : ScalaFile): String = {
    if (includeIrregNouns(dir) ) {
      """
% Irregular adjective acceptor
$squashirregadjurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ $gender$ $case$ $number$ $degree$ <irregadj> <div> <irregadj> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
    } else {""}
  }


  def includeIrregPronouns(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irregpronouns.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregPronounAcceptor(dir : ScalaFile): String = {
    if (includeIrregPronouns(dir) ) {
      """
% Irregular pronoun acceptor
$squashirregpronurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ $gender$ $case$ $number$ <irregpron> <div> <irregpron> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
    } else {""}
  }

  def includeIrregAdverbs(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irregadverbs.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregAdverbAcceptor(dir : ScalaFile): String = {
    if (includeIrregAdverbs(dir) ) {
      """
% Irregular adverb acceptor
$squashirregadvurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ $degree$ <irregadv> <div> <irregadv> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
    } else {""}
  }

  def includeIrregSupines(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irregsupines.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregSupineAcceptor(dir : ScalaFile): String = {
  if (includeIrregSupines(dir) ) {
"""
% Irregular supine acceptor
$squashirregsupineurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+  $case$ <irregsupn> <div> <irregsupn> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>

"""
} else {""}
}

  def includeIrregGerunds(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irreggerunds.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregGerundAcceptor(dir : ScalaFile): String = {
    if (includeIrregGerunds(dir) ) {
  """
% Irregular gerund acceptor
$squashirreggerundurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+  $case$ <irreggrnd> <div> <irreggrnd> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>

  """
} else { ""}
  }

  def includeIrregGerundives(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irreggerundives.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregGerundiveAcceptor(dir : ScalaFile): String = {
    if (includeIrregGerundives(dir) ) {
  """
% Irregular gerundive acceptor
$squashirreggerundiveurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ $gender$ $case$ $number$  <irreggrndv> <div> <irreggrndv> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
  """
} else { "" }
  }


  def includeIrregParticiples(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irregparticiples.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregParticipleAcceptor(dir : ScalaFile): String = {
    if (includeIrregParticiples(dir) ) {
  """
% Irregular participle acceptor
$squashirregptcplurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ $gender$ $case$ $number$ $tense$ $voice$ <irregptcpl> <div> <irregptcpl> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
} else {""}
  }


  def includeIrregInfinitives(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-irreginfinitives.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  def irregInfinitiveAcceptor(dir : ScalaFile): String = {
    if (includeIrregInfinitives(dir) ) {
"""
% Irregular infinitive acceptor
$squashirreginfinnurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ $tense$ $voice$ <irreginfin> <div> <irreginfin> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
    } else {""}
  }


  /** True if parser lexica include entries for verbs.
  *
  * @param dir Root directory of work space (repo/parsers/CORPUS).
  *
  */
  def includeVerbs(dir: ScalaFile): Boolean = {
    val lexica = dir / "lexica"
    val verbsSource = lexica / "lexicon-verbs.fst"
    verbsSource.exists && verbsSource.lines.nonEmpty
  }

  /** String defining verb acceptor. */
  def verbAcceptor(dir : ScalaFile): String = {
    if (includeVerbs(dir) ) {
"""
% Conjugated verb form acceptor
$=verbclass$ = [#verbclass#]
$squashverburn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$  $separator$+$=verbclass$ <verb>[#stemchars#]* [#person#] [#number#] [#tense#] [#mood#] [#voice#]<u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""

} else { "" }
}


  def includeNouns(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-nouns.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }
  /** String defining final noun acceptor transducer.*/
  def nounAcceptor(dir : ScalaFile): String = {
  if (includeNouns(dir) ) {  """
% Noun acceptor:
$=nounclass$ = [#nounclass#]
$squashnounurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<noun>$=gender$ $=nounclass$   <div> $=nounclass$  <noun> [#stemchars#]* $=gender$ $case$ $number$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

def includeAdverbs(dir: ScalaFile): Boolean = {
  includeAdjectives(dir)
}
def includeAdjectives(dir: ScalaFile): Boolean = {
  val indeclSource = dir / "lexica/lexicon-adjectives.fst"
  indeclSource.exists && indeclSource.lines.nonEmpty
}
/** String defining final noun acceptor transducer.*/
def adjectiveAcceptor(dir : ScalaFile): String = {
if (includeAdjectives(dir) ) {  """
% Adjective acceptor:
$=adjectiveclass$ = [#adjectiveclass#]
$squashadjurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<adj>$=adjectiveclass$   <div> $=adjectiveclass$  <adj> [#stemchars#]* $=gender$ $case$ $number$ $degree$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}


/** String defining final noun acceptor transducer.*/
def infinitiveAcceptor(dir : ScalaFile): String = {
  if (includeVerbs(dir) ) {  """
% Infinitive acceptor
$=verbclass$ = [#verbclass#]
$squashinfurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$  <div> $=verbclass$  <infin>  [#stemchars#]* $tense$ $voice$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
  } else { "" }
}


def participleAcceptor(dir : ScalaFile): String = {

if (includeVerbs(dir) ) {  """
% Participle acceptor
$=verbclass$ = [#verbclass#]
$squashptcplurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$  <div> $=verbclass$  <ptcpl>  [#stemchars#]* $gender$ $case$ $number$ $tense$ $voice$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

def gerundiveAcceptor(dir : ScalaFile): String = {

if (includeVerbs(dir) ) {  """
% Gerundive acceptor
$=verbclass$ = [#verbclass#]
$squashgerundiveurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$ <div> $=verbclass$ <gerundive>[#stemchars#]* [#gender#] [#case#][#number#] <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

def gerundAcceptor(dir : ScalaFile): String = {

if (includeVerbs(dir) ) {  """
% Gerund acceptor
$=verbclass$ = [#verbclass#]
$squashgerundurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$ <div> $=verbclass$ <gerund>[#stemchars#]* [#case#] <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

def supineAcceptor(dir : ScalaFile): String = {

if (includeVerbs(dir) ) {  """
% Supine acceptor
$=verbclass$ = [#verbclass#]
$squashsupineurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<verb>$=verbclass$ <div> $=verbclass$ <supine>[#stemchars#]* [#case#] <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

/** String defining final noun acceptor transducer.*/
def adverbAcceptor(dir : ScalaFile): String = {
  // use adjective stems for regular formation of advs:
if (includeAdjectives(dir) ) {  """
$=adjectiveclass$ = [#adjectiveclass#]
$squashadvurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<adj>$=adjectiveclass$   <div> $=adjectiveclass$  <adv> [#stemchars#]* $degree$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

//<u>ocremorph.indecl2</u>
//<u>ls.n16278</u>et
//<indeclconj><div><indeclconj><u>indeclinfl.2</u>

/** String defining final acceptor transducer for indeclinable forms.*/
def indeclAcceptor (dir : ScalaFile): String = {
  if (includeIndecls(dir) ) {  """
% Indeclinable form acceptor:
$=indeclclass$ = [#indecl#]
$squashindeclurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ <indecl> $=indeclclass$ <div>   $=indeclclass$ <indecl><u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
""" } else { "" }
}

/** String defining final adjective acceptor transducer.  */
def adjAcceptor(dir : ScalaFile): String = {
  """
% adjective acceptor:
$=adjectiveclass$ = [#adjectiveclass#]
$squashadjurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>[#stemchars#]+<adj> $=adjectiveclass$   $separator$+ $=adjectiveclass$  <adj> [#stemchars#]* $=gender$ $case$ $number$ $degree$ <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>
"""
}

  /** True if parser lexica include data for indeclinables.
  *
  * @param dir Directory for corpus data set.
  */
  def includeIndecls(dir: ScalaFile): Boolean = {
    val indeclSource = dir / "lexica/lexicon-indeclinables.fst"
    indeclSource.exists && indeclSource.lines.nonEmpty
  }






  /** Compose FST for union of transducers squashing URNs.
  *
  * @param dir Directory for corpus data set.
  */
  def unionOfSquashers(dir: ScalaFile) : String = {
    val fst = StringBuilder.newBuilder
    fst.append("% Union of all URN squashers.\n\n$acceptor$ = ")

    def typesList = List(
      (includeVerbs(_),"$squashverburn$" ),
      (includeInfinitives(_),"$squashinfurn$" ),
      (includeParticiples(_),"$squashptcplurn$" ),

      (includeGerundives(_),"$squashgerundiveurn$" ),
      (includeGerunds(_),"$squashgerundurn$" ),
      (includeSupines(_),"$squashsupineurn$" ),

      (includeNouns(_),"$squashnounurn$" ),
      (includeAdjectives(_),"$squashadjurn$" ),
      (includeAdverbs(_),"$squashadvurn$" ),
      (includeIndecls(_),"$squashindeclurn$" ),

      (includeIrregVerbs(_), "$squashirregverburn$"),
      (includeIrregInfinitives(_), "$squashirreginfinnurn$"),
      (includeIrregParticiples(_), "$squashirregptcplurn$"),
      (includeIrregGerunds(_), "$squashirreggerundurn$"),
      (includeIrregGerundives(_), "$squashirreggerundiveurn$"),
      (includeIrregSupines(_), "$squashirregsupineurn$"),

      (includeIrregNouns(_), "$squashirregnounurn$"),
      (includeIrregAdjectives(_), "$squashirregadjurn$"),
      (includeIrregAdverbs(_), "$squashirregadvurn$"),
      (includeIrregPronouns(_), "$squashirregpronurn$")
    )
    val xducerList = for (xducer <- typesList) yield {
      if (xducer._1(dir)) { xducer._2} else {""}
    }
    val online = xducerList.filter(_.nonEmpty)
    if (online.isEmpty) {
      throw new Exception("AcceptorComposer:  no lexica found.")

    } else {
      fst.append(online.mkString(" | "))
      fst.append("\n")
      fst.toString
    }
  }

  /** String defining union of acceptors for each distinct
  * analytical pattern, followed by a transducer removing
  * all analysis-level symbols.
  *
  * @param dir Directory for corpus data set.
  */
  def topLevelAcceptor(dir : ScalaFile): String = {
    val constructed  = unionOfSquashers(dir)
  val trail = """
%% Put all symbols in 2 categories:  pass
%% surface symbols through, suppress analytical symbols.
#analysissymbol# = #editorial# #urntag# #indecl# #pos# #morphtag# #stemtype#  #separator#
#surfacesymbol# = #letter# #diacritic#
ALPHABET = [#surfacesymbol#] [#analysissymbol#]:<>
$stripsym$ = .+

%% The canonical pipeline: (morph data) -> acceptor -> parser/stripper
$acceptor$ || $stripsym$
"""
    constructed + trail
  }

}
