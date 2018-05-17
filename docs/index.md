---
layout: page
title: Tabulae
---


## Overview

`tabulae` is a system for building Latin morphological parsers.

`tabulae` makes it possible to build corpus-specific Latin morphological parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific analyzers that identify citable lexical items and citable inflectional rules for each analysis.



## Prerequisites


- A POSIX-like environment with `sh`, `echo` and `make`
- [Scala](https://www.scala-lang.org/) and [sbt](https://github.com/sbt/sbt)
- [Stuttgart FST toolbox](http://www.cis.uni-muenchen.de/~schmid/tools/SFST/)


## Overview

The build file for this repository includes tasks for compiling a FST parser using rules and vocabulary read from a specified set of tabular files.  The resulting parser can be used interactively or can batch process a list of words.

The accompanying Scala code library, `edu.holycross.shot.tabulae`, can read the output of this parser, and construct structured morphological objects.

## Installing and using `tabulae`

-   [Installation and configuration](configuration)
-   Managing [your data sets](datasets)
-   [Building and using a FST parser](parsing)
-   [Using code libraries to work with parsed output](code-library)

## Current status

-   [status](status)


## Under the hood

More information about [how `tabulae` works](how-works)
