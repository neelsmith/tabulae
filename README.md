# `tabulae` #

`tabulae` is a system for building Latin morphological parsers.

`tabulae` makes it possible to build corpus-specific Latin morphological parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific morphological analyzers, identifying citable lexical items and citable inflectional rules for each analysis.



## Information and documentation

- Current [API documentation](http://neelsmith.info/code/auto/tabulae/edu/holycross/shot/tabulae/).
-  I presented `tabulae` at [DH2019](https://dh2019.adho.org) in a short paper, "A Corpus-linguistic Approach to the Analysis of Latin Morphology"
-   read more about its rationale and approach to parsing a historical language in "Morphological Analysis of Historical Languages" (*Bulletin of the Institute for Classical Studies* 59-2, 2016, 89-102)

## Current version: 6.0.1

Status:  **active development**. [Release notes](releases.md)

Tabulae composes and compiles parsers using the Stuttgart Finite State System Toolkit, [SFST](https://www.cis.uni-muenchen.de/~schmid/tools/SFST/). These parts of the tabulae code run under the JVM.

The  **5.x** release series added classes for reading and analyzing output from an SFST parser.  These parts of the `tabulae` library are cross-compiled for ScalaJS as well as the JVM.


## Acknowledgments

This project is especially indebted to Harry Schmidt's [parsley](https://github.com/goldibex/parsley-core), and to the openly licensed data sets of the [Perseus project](http://www.perseus.tufts.edu).
