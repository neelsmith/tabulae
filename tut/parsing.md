---
title: Building and using a parser
layout: page
---



You can build a parser with the included scala script `scripts/parse.sc`.  Start an sbt console:

    sbt console

and load the script

    :load scripts/parse.sc


Use the `compile` function to build a parser for a corpus directory in the `datasets` directory


    compile("CORPUS_NAME")

or specify an alternate location for your DATASETS

    compile("CORPUS_NAME", "DATASTS_DIRECTORY")


Either option compiles a SFST parser in `parsers/CORPUS_NAME/latin.a`.

You can use regular SFST tools such as `fst-mor` or `fst-infl` with this binary parser.  E.g., to parse Latin words you supply interactively, run

    fst-mor parsers/CORPUS_NAME/latin.a


There is also function task to apply your parser to a list of words read from a file with one word per line.  The syntax is:

    parse("WORDS_FILE", "CORPUS_NAME")
