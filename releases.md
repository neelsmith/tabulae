# Release notes for `tabulae`

**7.0.1**: Turn off debugging output.

**7.0.0**: Revised API to `LematizedForm` with constructor working directly from URN value for morphological form.

**6.6.1**: Handles bad value in Form URNs.

**6.6.0**: Adds `fromFormUrn` method to the `LemmatizedForm` object.

**6.5.0**: Adds labelling of `ValidForm` implementations.

**6.4.0**: Adds the `ValidForm` trait and 10 implementations.

**6.3.0**: Adds the `urn` method to the `UrnManager` class.

**6.2.0**: Adds the `UrnManager` class, and `formUrn` method to `LemmatizedForm`.

**6.1.0**: Adds method for `formId` to `LemmatizedForm`.

**6.0.1**: Correctly republish binaries for 6.0.0.

**6.0.0**: Adds `LewisShort` object, and `formLabel` function on `LemmatizedForm` trait.

**5.5.0**: Adds new `MorphologyFilter` class for matching arbitrary sets of grammatical properties against a `LemmatizedForm`.

**5.4.1**:  Fixes a bug in  classification of first-declension nouns doubling stem consonant when forming superlative.

**5.4.0**:  Adds support for indeclinable numerals.

**5.3.1**:  Fixes a bug recognizing parses of conjugated verbs in pluperfect tense.

**5.3.0**:  Overrides `toString` for all morphological form classes.


**5.2.0**:  Adds full suite of functions to `LemmatizedForm` trait for morphological querying.

**5.1.0**:  Adds crosscompilation for ScalaJS as well as JVM for analytical parts of the library.

**5.0.0**:   Breaking change in internal APIs.  Provides complete analysis of all possible SFST output from a parser built with `tabulae`.


**4.0.0**:  Major breaking change. Tabulae now permits merging multiple corpus sources if they have compatible orthographies (where one character set is a true subset of the other, and their usage does not conflict.)  This should greatly facilitate the development of parsers for related corpora with shared content (whether shared lexicon or shared inflectional rules).

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
