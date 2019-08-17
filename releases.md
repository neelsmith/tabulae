# Release notes for `tabulae`


**4.0.0**:  Major breaking change. Tabulae now permits merging multiple corpus sources if they have compatible orthographies (where one character set is a true subset of the other, and their usage does not conflict.)

**3.4.0**: Added support for parsing FST output for irregular adverbs.

**3.3.0**: Added support for parsing FST output for irregular nouns.

**3.2.0**: Set up framework for instantiating SFST strings for irregular analyses as `LemmatizedForm`s.


**3.1.0**:  Support CEX output from `AnalyzedToken` instances, and for Vectors of `AnalyzedToken` from the `AnalyzedToken` object.

**3.0.0**:  API-breaking addition of all analytical URNs to `AnalyzedToken` class.

**2.4.3**:  Updates library dependencies.

**2.4.2**:  Updates library dependencies.

**2.4.1**:  Fixes a bug in PoS functions of `AnalyzedToken` when no analyses exist.

**2.4.0**:  Expand `AnalyzedToken` class with functions for manipulating tokens morphologically.

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
