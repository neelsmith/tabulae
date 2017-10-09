# tabulae #

A build system for Latin morphological parsers.


## Organization


-   `datasets`: suites of inflectional endings and lexical stems
-   `project` classes:  Configuraiton file;  others...?





## Acknowledgments

This project is especially indebted to Harry Schmidt's [parsley](https://github.com/goldibex/parsley-core).




## Sequence

`buildFst` invokes

-    `fstCompile` task.  It calls:
    -    `RulesInstaller` class
        -   `NounRulesInstaller` class
    -    `DataInstaller` class
        -   `NounDataInstaller` class
