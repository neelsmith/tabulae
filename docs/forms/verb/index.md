---
title: Verb forms
layout: page
---



## Creating a `VerbFrom` from SFST output

The SFST output for analysis of a conjugated verb form looks like this (where each entry in the Vector is one line of output):

```scala
val fstLines = Vector(
  "> fecit",
  "<u>ocremorph.n17516b</u><u>ls.n17516</u><#>fec<verb><pftact><div><pftact><verb>it<3rd><sg><pft><indic><act><u>livymorph.pftact_pft3</u>"
)
```

The `FstReader` object can parse a Vector of SFST output lines, and create a (possibly empty) Vector of `AnalyzedToken`s.  We should get only one token from the FST above:


```scala
import edu.holycross.shot.tabulae._
val analyzedTokens = FstReader.parseFstLines(fstLines)
assert(analyzedTokens.size == 1)

```


Each `AnalyzedToken` pairs the token String with a Vector of things extending the  `LemmatizedForm` trait.

```scala
scala> val analysis = analyzedTokens(0)
analysis: edu.holycross.shot.tabulae.AnalyzedToken = AnalyzedToken(fecit,Vector(VerbForm(ls.n17516,ocremorph.n17516b,livymorph.pftact_pft3,Third,Singular,Perfect,Indicative,Active)))

scala> analysis.literalToken
res1: String = fecit

scala> analysis.analyses
res2: Vector[edu.holycross.shot.tabulae.LemmatizedForm] = Vector(VerbForm(ls.n17516,ocremorph.n17516b,livymorph.pftact_pft3,Third,Singular,Perfect,Indicative,Active))
```

Use Scala pattern matching to get a specific type of `LemmatizedForm`:

```scala
val verbForm: VerbForm = analysis.analyses(0) match {
  case vb: VerbForm => vb
  case _ => throw new Exception("Nope, not a verb")
}
```

You can then work with the specific functions appropriate to that type of form.

```scala
assert(analysis.literalToken == "fecit")
assert (verbForm.person == Third)
assert (verbForm.grammaticalNumber == Singular)
assert (verbForm.tense == Perfect)
assert (verbForm.mood == Indicative)
assert (verbForm.voice == Active)
```

Note that this example is for *conjugated* verb forms. Participles, gerunds, gerundives and infinitives have their own analytical patterns.
