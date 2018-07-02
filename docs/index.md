---
layout: page
title: Tabulae
---


## Overview

`tabulae` is a system for building Latin morphological parsers.

`tabulae` makes it possible to build corpus-specific Latin morphological parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific analyzers that identify citable lexical items and citable inflectional rules for each analysis.



## Technical prerequisites


- A POSIX-like environment with `sh`, `echo` and `make`
- [Scala](https://www.scala-lang.org/) and [sbt](https://github.com/sbt/sbt)
- [Stuttgart FST toolbox](http://www.cis.uni-muenchen.de/~schmid/tools/SFST/)


## Technical overview

The build file for this repository includes tasks for compiling a FST parser using rules and vocabulary read from a specified set of tabular files.  The resulting parser can be used interactively or can batch process a list of words.

The accompanying Scala code library, `edu.holycross.shot.tabulae`, can read the output of this parser, and construct structured morphological objects.


## A brief tour of `tabulae`

You build a parser from tabular data defining regular stems, rules for applying regular endings, and tables of irregular forms.  Each stem belongs to a specific stem class;  each rule applies to a specific stem class.  When the parser is built, rules and stems belonging to the same stem class are matched up to form valid possible forms.


-  The [lexicon of stems](lexicon)
-  The [morphological rules tables](rules)
-  [Irregular forms](irregulars)

All the data in your tables must belong to a specified alphabet, and be identified by URNs.  In your data tables, the URNs appear in an abbreviated form:  the URN manager matches abbreviations with full URNs for each collection of data.

-  An [alphabet for your parser](alphabet)
-  The [URN manager](urnmanager)

## Installing and using `tabulae`

-   [Installation and configuration](configuration)
-   Managing [your data sets](datasets)
-   [Building and using a FST parser](parsing)
-   [Using code libraries to work with parsed output](code-library)

## Current status

-   [status](status)

## Testing

-  Summary of [sbt tasks for testing](testing).

A note on directory organization: the directory `fst` contains the core logic of the parser written in the notation of the Stuttgart FST toolset. These files are independent of particular data sets, and are compiled into every parser that tabulae builds. Copies of this directory are included in the subprojects `test_build`, `test_pos` and `test_wordlists` to simplify automated testing. These copies can be rebuilt with rsync, e.g. from the root of the repository, `rsync -avz fst/ test_pos/fst`

## Under the hood

More information about [how `tabulae` works](notes/narrative/)
