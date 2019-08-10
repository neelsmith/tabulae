---
layout: page
title: Tabulae
---

## Summary

-   [Overview](overview)
-   Current version: live code in these documents tested against version **3.4.0**. See [release notes](https://github.com/neelsmith/tabulae/blob/master/releases.md)
-   API docs (Obscenely out of date.  On-line API for version 1.0.0 [here](api/edu/holycross/shot/tabulae/index.html).)



## Technical prerequisites


-   A POSIX-like environment with `sh`, `echo` and `make`
-   [sbt](https://github.com/sbt/sbt)
-   [Stuttgart FST tools](http://www.cis.uni-muenchen.de/~schmid/tools/SFST/)


## Installing and using `tabulae`

-   [Building and using a FST parser](parsing)
-   Managing [your data sets](datasets)
-   [Using code libraries to work with parsed output](code-library)




## A brief tour of `tabulae`

You build a parser from tabular data defining regular stems, rules for applying regular endings, and tables of irregular forms.  Each stem belongs to a specific stem class;  each rule applies to a specific stem class.  When the parser is built, rules and stems belonging to the same stem class are matched up to form valid possible forms.

All the data in your tables must belong to a specified alphabet, and be identified by URNs.  In your data tables, the URNs appear in an abbreviated form:  the URN manager matches abbreviations with full URNs for each collection of data.


-   an [alphabet for your parser](alphabet)
-   the [URN manager](urnmanager)
-   the [lexicon of stems](lexicon)
-   the [morphological rules tables](rules)
-   [irregular forms](irregulars)



## Under the hood

More information about [how `tabulae` works](notes/narrative/)
