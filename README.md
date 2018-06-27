# `tabulae` #

A system for building Latin morphological parsers.

`tabulae` makes it possible to build corpus-specific Latin morphological parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific morphological analyzers, identifying citable lexical items and citable inflectional rules for each analysis.

## Information

See the project web site: <https://neelsmith.github.io/tabulae>.


## Notes on directory organization

The directory `fst` contains the core logic of the parser written in the notation of the Stuttgart FST toolset.  These files are independent of particular data sets, and are compiled into every parser that `tabulae` builds.  Copies of this directory are included in the subprojects `test_build`,  `test_pos` and `test_wordlists` to simplify automated testing.  These copies can be rebuilt with rsync, e.g. from the root of the repository, `rsync -avz fst/ test_pos/fst`

## Status

See a [scorecard for the current version](https://github.com/neelsmith/tabulae/wiki/Scorecard) (0.0.1)





## Acknowledgments

This project is especially indebted to Harry Schmidt's [parsley](https://github.com/goldibex/parsley-core), and to the openly licensed data sets of the [Perseus project](http://www.perseus.tufts.edu).
