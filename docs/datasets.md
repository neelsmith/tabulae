---
title: Organization of data sets
layout: page
---

For a given corpus, the parsing system expects a specific directory organization that is illustrated in the `datatemplate`  directory.

Tabular files defining *inflectional rules* are in subdirectories of of the `rules-tables` directory.  Tabular files defining *lexical items* ("stems") are in subdirectories of the `stems-tables` directory.  In addition to these tables (which may be unique for each corpus to analyze), the `orthography` directory must include a file named `alphabet.fst`.  (Typically, many corpora might use an identical alphabet.)  This is a very simple file in the syntax of the Stuttgart Finite State Tooklkit (SFST).

Each corpus-specific dataset is in a named subdirectory of a root directory that can configured in `config.properties`. In `sbt`, you can use the `corpusTemplate` task to set up a new named copy of the `datatemplate` files in the configured datasets directory.

The`tabulae` build system compiles parsers into named subdirectories of the `parsers` directory, using the name for of the dataset directory as the corpus directory. (E.g., building a parser from data in `datasets/testcorpus` will put the resulting parser in `parsers/testcorpus`.)
