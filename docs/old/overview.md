---
layout: page
title: Overview
---


`tabulae` is a system for building Latin morphological parsers.

`tabulae` makes it possible to build corpus-specific Latin morphological parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific analyzers that identify citable lexical items and citable inflectional rules for each analysis.


The `tabulae` github repository at <https://github.com/neelsmith/tabulae> includes the basic logic for parsing Latin morphology, written in the language of the Stuttgart Finite State Transducer toolbox (SFST). You supply data for a specific corpus in simple tables. The tabulae library can read your data set, rewrite it in the SFST notation, and combine this with the basic parsing logic to compile a parser.
