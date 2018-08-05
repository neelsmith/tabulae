---
layout: page
title: Technical overview
---


The build file for this repository includes tasks for compiling a FST parser using rules and vocabulary read from a specified set of tabular files.  The resulting parser can be used interactively or can batch process a list of words.

The accompanying Scala code library, `edu.holycross.shot.tabulae`, can read the output of this parser, and construct structured morphological objects.
