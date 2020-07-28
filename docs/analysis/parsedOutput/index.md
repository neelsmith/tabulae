---
title: Parsed output
layout: page
parent: Analyzing the output of a parser
nav_order: 0
---


# Parsed output

The `FstReader` object recognizes the SFST patterns for a token and for a sequence of analyses, and from pairings of tokens and analyses creates `AnalyticalToken`s.

An `AnalyticalToken` in turn associates a literal token String with a (possibly empty) Vector of `LemmatizedForm`s.  The `LemmatizedForm` is a trait associating the literal token with a single morphological analysis from a single lexeme.  The `LemmatizedForm` trait requires ID strings for:

- the lexeme
- the applied rule
- the applied stem
- a "part of speech" label

The `apply` function of the `LemmatizedForm` object considers a single line of SFST output and makes an `Option[LemmatizedForm]`. (Its value is `None` if the `LemmatizedForm` object cannot parse the string).

The `LemmatizedForm` trait is implemented by classes for possible analytical pattern, namely:

- `VerbForm` (conjugated verb form): analytical pattern PNTMV.  See [an example](../forms/verb/).
- `ParticipleForm`: analytical pattern GCNTV
- `InfinitiveForm`: analytical pattern TV
- `GerundiveForm`:  analytical pattern GCN
- `GerundForm`:  analytical pattern C
- `SupineForm`: analytical pattern C
- `AdverbForm`:  analytical pattern D
- `NounForm`: analytical pattern GCN
- `AdjectiveForm`: analytical pattern GCND
- `IndeclinableForm`: analytical pattern Pos
