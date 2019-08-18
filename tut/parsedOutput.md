---
title: Parsed output
layout: page
---


The `FstReader` object converts multi-line strings of SFST output to some kind of analytical structure.  It recognizes the SFST patterns for a token and for a sequence analyses, and from pairings of tokens and analyses creates `AnalyticalForm`s.

An `AnalyticalForm` associates a literal token String with a (possibly empty) Vector of `LemmatizedForm`s.

The `apply` function of the `LemmatizedForm` object considers a single line of SFST output and makes an `Option[LemmatizedForm]` (`None` if it cannot parse the string).  The `LemmatizedForm` interface requires ID strings for:

- the lexeme
- the applied rule
- the applied stem
- a "part of speech" label

The `LemmatizedForm` interface is implemented by classes for possible analytical pattern, namely:

- `VerbForm` (conjugated verb form): PNTMV.  See [an example](../forms/verb/).
- `ParticipleForm`: GCNTV
- `GerundiveForm`:  GCN
- `GerundForm`:  C
- `AdverbForm`:  D
- `NounForm`: GCN
- `AdjectiveForm`: GCND
- `IndeclinableForm`: Pos
