---
title: Parsing text output
layout: page
---

Some incomplete notes:

## Create a `Form` from a string


```scala
import edu.holycross.shot.tabulae._
val nounForm = Form( "<u>dev1.n1</u><u>lexent.n1</u>femin<noun><fem><a_ae><div><a_ae><noun>as<fem><acc><pl><u>lnouninfl.a_ae10</u>")

val verbForm = Form("<u>dev1.v1</u><u>lexent.v1</u><#>am<verb><conj1><div><conj1><verb>i<1st><sg><pft><indic><act><u>lverbinfl.are_pftind1</u>")
```


## Group `Form`s together in a list


```scala
scala> val forms = Vector.empty[Form] :+ nounForm :+ verbForm
forms: scala.collection.immutable.Vector[edu.holycross.shot.tabulae.Form] = Vector(NounForm(Feminine,Accusative,Plural), VerbForm(First,Singular,Perfect,Indicative,Active))
```
