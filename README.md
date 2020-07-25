# `tabulae` #

`tabulae` is a system for building corpus-specific Latin morphological parsers.

`tabulae` makes it possible to build Latin parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific morphological analyzers, identifying citable lexical items and citable inflectional rules for each analysis.

Tabulae composes source code for and compiles parsers using the Stuttgart Finite State System Toolkit, [SFST](https://www.cis.uni-muenchen.de/~schmid/tools/SFST/). These parts of the tabulae code run under the JVM.

The  **5.x** release series added classes for reading and analyzing output from an SFST parser.  These parts of the `tabulae` library are cross-compiled for ScalaJS as well as the JVM.

## Why do we need *tabulae*?

There are already several excellent openly available Latin morphological parsers. (In addition to the list at [wiki.digitalclassicist.org](https://wiki.digitalclassicist.org/Morphological_parsing_or_lemmatising_Greek_and_Latin), note especially [LatMor](http://www.cis.uni-muenchen.de/~schmid/tools/LatMor/) and [Parsley](https://github.com/goldibex/parsley-core).) The parsers you build with `tabulae` differ from other parsers in two ways.

### 1. Corpus-specific parsing

Tabulae automates the building of parsers targeted at the language and orthography of specific corpora.  A form *anime* can be recognized as vocative singular of *animus* in Plautus, but as genitive singular, dative singular, nominative plural or vocative plural of *anima* in a diplomatic edition of the Latin psalms, where *e* might represent the represent the orthographic equivalent of classical *ae*, and the noun *animus* is not part of the lexicon.


### 2. URNs for cross-corpus identifiers

`tabulae` identifies all components of a parser's output with canonically citable CITE2 URNs. (The CITE2 URN scheme is currently in expert review by the IETF's URN working group.)  This allows us to unite analyses across corpora despite their differences in language and orthography.

When we parse *animae* in Plautus with a classical Latin parser and *anime* in a diplomatic edition of the Psalms with a parser specific to that corpus, both parsers will recognize identify the lexeme as `urn:cite2:hmt:ls:n2612`.  Our two parsers can thus recognize that *anime* in the Psalms and *animae* in Plautus are equivalent forms of the same lexical entity, but the token identical token *anime* represents different lexical entities in the two corpora.




## Information and documentation


-  Tabulae was presented in a short paper at DH2019:  Neel Smith, "[A Corpus-linguistic Approach to the Analysis of Latin Morphology](https://dev.clariah.nl/files/dh2019/boa/0434.html)."
-  Read more about its rationale and approach to parsing a historical language in Neel Smith, "Morphological Analysis of Historical Languages," *Bulletin of the Institute for Classical Studies* 59-2, 2016, 89-102.



## Current version: 6.2.0

- Status:  **active development**. [Release notes](releases.md)
- Current [API documentation](http://neelsmith.info/code/auto/tabulae/edu/holycross/shot/tabulae/).


## Acknowledgments

This project is especially indebted to Harry Schmidt's [parsley](https://github.com/goldibex/parsley-core), and to the openly licensed data sets of the [Perseus project](http://www.perseus.tufts.edu).
