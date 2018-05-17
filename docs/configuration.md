---
title: Installation and configuration
layout: page
---

In the root directory of this repository, the file `config.properties-template` illustrates how to configure a build.  By default, the `sbt` tasks look for a file  named `config.properties`;  alternatively, you can supply the name of a configuration file as a second parameter to the `sbt fst` task.  (The configuration file must be relative to the base directory of this repository.)  Clone `config.properties-template` and edit it to specify the location in your file system of the required binaries for `fst` and `make`.

You can configure a custom location for your datasets with either an absolute file path or a path relative to the repository root.  `tabulae` will look in this directory for your datasets.  The default value is the relative directory `datasets`.


## Example

You create a subdirectory named `configs`, and clone `config.properties-template` to a file named `configs/morphology.properties`.  You edit its contents to identify an external directory where your morphology dataset is located, as follows:


    # Directory for datasets: relative or absolute path
    datadir = /data/morphology

    # Location of necessary utilities in local system
    FSTCOMPILER = /usr/local/bin/fst-compiler
    FSTINFL = /usr/local/bin/fst-infl
    MAKE = /usr/bin/make


With your `/data/morphology` directory, you have created a subdirectory namedk `livy` where you have morphological data for a corpus of Livy.

From an sbt console, you can compile a parser for this corpus as follows:

    fst livy configs/morphology.properties
    
