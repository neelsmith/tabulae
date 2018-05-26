---
title:  How Tabulae builds parsers
layout: page
---


## Overview

`Tabulae` has a core logic for parsing Latin, written in SFST notation.

`Tabulae` reads a set of delimited text files defining inflectional rules and vocabulary items.  These data tables encode stems and endings in an explicitly specified alphabet. `Tabulae` composes SFST expressions for the data in the tables of endings and stems, and then  compiles this together with the core logic into an executable binary parser.

The chain of transducers that `tabulae` creates follow a classic design pattern.  The transducers for stems and endings are joined so that every stem is paired with every possible ending (the Cartesian cross product of the two lists).  This is filtered by a series of transducers that accept only valid combinations of stem and endings.  Verb stems will only be permitted with verb endings, for example, and more specifically, only verb stems of a specific type will be permitted with specific verb endings (e.g., first-conjugation verb stems will only be allowed with first-conjugation endings).  A final transducer mediates between surface forms and full analysis by removing all analytical annotation, leaving only the literal stem string and literal ending string joined together.  This final transducer therefore maps every possible valid combination of stem and ending in a surface form to one or more full analyses of that form.

## Step by step

The main buildfile includes a task (`fstCompile`) that sequentially:

1.  Finds a `Configuration` object.  This identifies the location of binary executables for compiling the parser in the local file system, and gives the root directory of the data set to use.
2.  Installs vocabulary (stem) data from  the configured data source by converting tables to FST and installing in configured build area.  The resulting SFST is written in SFST's brief "lexicon" notational form. The general installer invokes specific installers for each form of table, namely:
    -  installer for stems of indeclinable forms
    -  installer for stems of conjugated verb forms
3.  Installs inflectional rules from  the configured data source by converting tables to FST and installing in configured build. The resulting SFST is expressed as a series of named transducers.  The general installer invokes specific installers for each form of table, namely:
    -  installer for inflectional rules for specific classes of indeclinable forms
    -  installer for inflectional rules for conjugated verb forms
4.  Composes the files expressing the FST parsing logic by copying in generic transducers from the `fst` directory, and rewriting them based on inspection of the given lexica and rule sets.  This is a little tricky because successive transducers in the FST pipeline may or may not be present.  Steps are:
     - Creates the `symbols` directory defining basic set of symbols for this parser.
    -  Adds the symbols defined for the specific orthographic system (alphabet) of this parser.
    -  Creates a transducer accepting all inflectional patterns. (This step is unnecessary for the stem data in SFST's lexicon format.)
    -  Creates the chain of acceptors that filter the cross product of rules and stems so that only valid forms are permitted.  Since a given corpus may or may not include instances of any given type of vocuablary item, the sequence of chained transducers can be complex.  The final step is a single "acceptor" transducer.
    -  Composes the final parser: a handful of lines that cross stems and endings and feeds them to the final acceptor transducer.
5.  Composes `makefile`s for building the binary parser from FST source. SFST uses traditional `make` to compile its parsers.  Like the chain of acceptors, the make files have to be dynamically composed so that they function correctly whether or not particular kinds of vocabulary are present.
6.  Compiles the binary.
