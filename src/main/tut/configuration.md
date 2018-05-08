---
title: Installation and configuration
layout: page
---



In the root directory of this repository, make a copy of `config.properties-template` named `config.properties`.  Edit it to specify the location in your file system of the required binaries for `fst` and `make`.


Optionally, you can configure a custom location for your datasets with either an absolute file path or a path relative to the repository root.  `tabulae` will look in this directory for your datasets.  The default value is the relative directory `datasets`.
