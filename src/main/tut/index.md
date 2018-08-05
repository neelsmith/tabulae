---
layout: page
title: Tabulae
---

## Summary

-   [Overview](overview)
-   Current version: **1.0.0**. See [release notes](releases)
-   [API docs](api) for version 1.0.0




## Technical prerequisites


-   A POSIX-like environment with `sh`, `echo` and `make`
-   [sbt](https://github.com/sbt/sbt)
-   [Stuttgart FST tools](http://www.cis.uni-muenchen.de/~schmid/tools/SFST/)


## Installing and using `tabulae`

-   [Installation and configuration](configuration)
-   Managing [your data sets](datasets)
-   [Building and using a FST parser](parsing)
-   [Using code libraries to work with parsed output](code-library)




## A brief tour of `tabulae`


The `tabulae` github repository at <https://github.com/neelsmith/tabulae> includes the basic logic for parsing Latin morphology, written in the language of the Stuttgart Finite State Transducer toolbox (SFST). You supply data for a specific corpus in simple tables (as described below). The tabulae library can read your data set, rewrite it in the SFST notation, and combine this with the basic parsing logic to compile a parser.



You build a parser from tabular data defining regular stems, rules for applying regular endings, and tables of irregular forms.  Each stem belongs to a specific stem class;  each rule applies to a specific stem class.  When the parser is built, rules and stems belonging to the same stem class are matched up to form valid possible forms.

-   defining the orthographic system of a corpus
-   the URN manager (TBA)
-   the [lexicon of stems](lexicon)
-   the [morphological rules tables](rules)
-   [irregular forms](irregulars)

All the data in your tables must belong to a specified alphabet, and be identified by URNs.  In your data tables, the URNs appear in an abbreviated form:  the URN manager matches abbreviations with full URNs for each collection of data.

-  An [alphabet for your parser](alphabet)
-  The [URN manager](urnmanager)



## Current status

-   [status](status)


## Under the hood

More information about [how `tabulae` works](notes/narrative/)
