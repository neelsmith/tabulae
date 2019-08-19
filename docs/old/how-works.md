---
title: How tabulae works
layout: page
---



The `project` directory defines classes for building a build system.  These classes are directly used by the build tasks of `build.sbt`.  The high-level `sbt` task `buildFst` composes FST versions of tabular data, assembles these files and predefined FST files in a source tree, composes `make` files for the resulting source tree, and then uses the SFST compiler to compile a binary parser.  The `buildFst` task invokes

-   the `fstCompile` build task.  It in turn uses tasks defined in these classes of the `project` directory:
    1.  the `RulesInstaller` class, which uses
        -   `NounRulesInstaller` class
        -   `VerbRulesInstaller` class
    2.  the `DataInstaller` class, which uses
        -   `NounDataInstaller` class
        -   `VerbDataInstaller` class
    3.  the `BuildComposer` class,which uses
        -   `InflectionComposer` class
        -   `AcceptorComposer` class
        -   `ParserComposer` class
        -   `MakefileComposer` class

Generic parsing logic in SFST syntax is in the `fst` library.  These files are automatically added to the source tree when compiling a binary parser.
