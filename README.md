# tabulae #

A build system for Latin morphological parsers.


## Organization


-   `datasets`: suites of inflectional endings and lexical stems
-   `project` classes:  Configuraiton file;  others...?


## Upgrade

Currently using `sbt.version=0.13.16` and `scalaVersion := "2.10.6"` in project folder settings, b/c apparentl system exec syntax changed with sbt 1.0?

Get on line and look this up.


## Acknowledgments

This project is especially indebted to Harry Schmidt's [parsley](https://github.com/goldibex/parsley-core).




## Sequence

`buildFst` invokes

-    `fstCompile` task.  It calls:
    -    `RulesInstaller` class
        -   `NounRulesInstaller` class
    -    `DataInstaller` class
        -   `NounDataInstaller` class
