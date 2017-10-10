# `tabulae` #

A system for building Latin morphological parsers.

`tabulae` makes it possible to build corpus-specific Latin morphological parsers from tabular data for inflectional rules and for vocabulary ("stems").  It's a Latin sibling of [`kanones`](https://github.com/neelsmith/kanones):  like `kanones`, its aim is to build corpus-specific morphological analyzers, identifying citable lexical items and citable inflectional rules for each analysis.



## Prerequisites


- A POSIX-like environment with `sh`, `echo` and `make`
- [Scala](https://www.scala-lang.org/) and [sbt](https://github.com/sbt/sbt)
- [Stuttgart FST toolbox](http://www.cis.uni-muenchen.de/~schmid/tools/SFST/)

## Organization of files



### System configuration

In `config.properties`, you specify the location in your file system of required binaries for `fst` and `make`. In addition, you can configure custom location for your datasets with an absolute file path or a path relative to the repository root.  (Default: `datasets`.)

### User-managed data sets

For a given corpus, the parsing system expects a specific directory organization, as illustrated in the `datatemplate`  directory.  Tabular files defining inflectional rules are in subdirectories of of the `rules-tables` directory.  Tabular files defining lexical items ("stems") are in subdirectories of the `stems-tables` directory.  In addition to these tables (which may be unique for each corpus to analyze), the `orthography` directory must include a file named `alphabet.fst`.  (Typically, many corpora might use an identical alphabet.)  This is a very simple file in the syntax of the Stuttgart Finite State Tooklkit (SFST).

Each corpus-specific dataset is in a named subdirectory of a root directory that can configured in `config.properties`. In `sbt`, you can use the `corpusTemplate` task to set up a new named copy of the `datatemplate` files in the configured datasets directory.

The`tabulae` build system compiles parsers into named subdirectories of the `parsers` directory, using the name for of the dataset directory as the corpus directory. (E.g., building a parser from data in `datasets/testcorpus` will put the resulting parser in `parsers/testcorpus`.)

### Other files

The `project` directory defines classes for building a build system.  These classes are directly used by the build tasks of `build.sbt`.  The high-level `sbt` task `buildFst` composes FST versions of tabular data, assembles these files and predefined FST files in a source tree, composes `make` files for the resulting source tree, and then uses the SFST compiler to compile a binary parser.  The `buildFst` task invokes

-   the `fstCompile` build task.  It in turn uses tasks defined in these classes of the `project` directory:
    1.  the `RulesInstaller` class, which uses
        -   `NounRulesInstaller` class
    2.  the `DataInstaller` class, which uses
        -   `NounDataInstaller` class
    3.  the `BuildComposer` class,which uses
        -   `InflectionComposer` class
        -   `AcceptorComposer` class
        -   `ParserComposer` class
        -   `MakefileComposer` class

Generic parsing logic in SFST syntax is in the `fst` library.  These files are automatically added to the source tree when compiling a binary parser.



## Acknowledgments

This project is especially indebted to Harry Schmidt's [parsley](https://github.com/goldibex/parsley-core), and to the openly licensed data sets of the [Perseus project](http://www.perseus.tufts.edu).
