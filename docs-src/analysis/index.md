---
title: Analyzing the output of a parser
layout: page
nav_order: 3
has_children: true
---

**Version @VERSION@**

# Analyzing the output of a parser

The SFST parser maps your tokens to long descriptive strings identifying the morphology of the token, as well as identifying the lexeme and the specific rules `tabulae` applied to find that parse.

The tabulae library includes code that interprets this output, and turns it into structured objects.

This section of the `tabulae` documentation shows you how to use this code library in Scala to analyze your morphological data.

Because the possible questions you could ask of an analyzed corpus is open ended, this section is the most complex part of the `tabulae` documentation.

- [Overview](parsedOutput/)
-  A short, complete example
