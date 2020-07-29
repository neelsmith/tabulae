---
title: The lexicon of stems
layout: page
parent: Building a corpus-specific dataset
nav_order: 4
---



**Version @VERSION@**

# The lexicon of stems


In a `tabulae` dataset, the directory `stems-tables` contains the lexicon of stems for all words in your corpus that combine with regular endings.

Stem tables are tabular files using `#` as the column delimiter.  File names are not significant, but must end with the extension `.cex`.  Tables begin with a header line followed by one or more data lines.

All stem tables have four columns in common:

1.   a URN (in [abbreviated form](../urnmanager)) uniquely identifying this stem.
2.   an abbreviated URN identifying the lexical entity this stem belongs to.
3.   a string value for the stem.  This value must be expressed in the [alphabet defined for your corpus](../alphabet).
4.   a stem class.  This value must be one of the stemclasses defined for each analytical type.  See details in the links below for each analytical category.

In addition, stem tables for nouns have a fifth column for the gender of the lexical entity.

-   [indeclinables](indeclinables). For all indeclinable forms. (prepositions, conjunctions, exclamations)
-   `adjectives`.  Note that regularly formed adverbs are built from these stems, too.
-   `nouns`
-   `verbs-simplex`
-   `verbs-compound`
