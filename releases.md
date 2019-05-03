# Release notes for `tabulae`

**2.3.0**:  Support parsing SFST output on incomplete/failed analyses.

**2.2.0**: Adds stem class for indeclinable numbers.

**2.1.2**: Further fixes a bug in `AcceptorComposer` in constructing FST strings for indeclinable forms.


**2.1.1**: Fixes a bug in `AcceptorComposer` in constructing FST strings for indeclinable forms.


**2.1.0**: Adds utility function in `LemmatizedForm` trait for labelling "parts of speech".


**2.0.1**:  Fixes a typo in converting string representation of ablative case object.

**2.0.0**: API-breaking change replaces `Form` trait with `LemmatizedForm` trait.

**1.2.0**:  Adds support for parsing participle and gerund forms from `FstFileReader`.


**1.1.0**:  Adds `FstFileReader` object.  Can read FST output from a parser built with `tabulae`, and convert to object model.  Version 1.1.0 recognizes analyses of nouns, adjectives, indeclinable forms, and conjugated verb forms.

**1.0.0**: initial release.  From tabular source data, can build binary parsers with complete coverage of all analytical types.
