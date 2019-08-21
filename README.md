# `tabulae` #

`tabulae` is a system for building Latin morphological parsers.

`tabulae` makes it possible to build corpus-specific Latin morphological parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific morphological analyzers, identifying citable lexical items and citable inflectional rules for each analysis.


## Current version: 5.2.0

Status:  **active development**. [Release notes](releases.md)

The  **5.x** release series provides complete analysis of all possible SFST output from a parser built with `tabulae`.  Beginning with version **5.1.0**, the analytical parts of the  library are cross-compiled for ScalaJS as well as the JVM.

## Information and documentation

- The project web site at <https://neelsmith.github.io/tabulae>) is currently being updated to agree with the 5.x release series.
- Complete [API docs for version 5.0.0](https://neelsmith.github.io/tabulae/api/edu/holycross/shot/tabulae/index.html) are now online.
-  I presented `tabulae` at [DH2019](https://dh2019.adho.org) in a short paper, "A Corpus-linguistic Approach to the Analysis of Latin Morphology"
-   read more about its rationale and approach to parsing a historical language in "Morphological Analysis of Historical Languages" (*Bulletin of the Institute for Classical Studies* 59-2, 2016, 89-102)

## Acknowledgments

This project is especially indebted to Harry Schmidt's [parsley](https://github.com/goldibex/parsley-core), and to the openly licensed data sets of the [Perseus project](http://www.perseus.tufts.edu).
