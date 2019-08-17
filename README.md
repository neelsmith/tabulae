# `tabulae` #

`tabulae` is a system for building Latin morphological parsers.

`tabulae` makes it possible to build corpus-specific Latin morphological parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific morphological analyzers, identifying citable lexical items and citable inflectional rules for each analysis.


## Current version: 4.0.0

Status:  **active development**. [Release notes](releases.md)

Release **4.0.0** permits integrating multiple data sources to compile a single parser.  This should greatly facilitate the development of parsers for related corpora with shared content (whether shared lexicon or shared inflectional rules). This will not necessitate any modifications to existing `tabulae` data sets: from an end-user's point of view, nothing changes, and scripts working with parsed output  be unaffected.

With version **4.0.0**, all code for generating fst source code and compiling a binary parser from tabular data sources has been  rewritten and thoroughly tested.

Current development is now focusing on code for working with the output of `tabulae` parsers.


## Information and documentation

The project web site (<https://neelsmith.github.io/tabulae>) is not up to date with version 4.0.0.

-  I presented `tabulae` at [DH2019](https://dh2019.adho.org) in a short paper, "A Corpus-linguistic Approach to the Analysis of Latin Morphology"
-   read more about its rationale and approach to parsing a historical language in "Morphological Analysis of Historical Languages" (*Bulletin of the Institute for Classical Studies* 59-2, 2016, 89-102)

## Acknowledgments

This project is especially indebted to Harry Schmidt's [parsley](https://github.com/goldibex/parsley-core), and to the openly licensed data sets of the [Perseus project](http://www.perseus.tufts.edu).
