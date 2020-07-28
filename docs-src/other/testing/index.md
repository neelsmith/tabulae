---
title: sbt tasks for testing
layout: page
nav_order: 1
parent: Other topics
---


**Version @VERSION@**

# `sbt` tasks for testing

From an `sbt` console:


-   `testPoS/posTests X` Suite of tests validating construction of a parser, organized by "part-of-speech".  Validates:
    -   installing core logic written in FST to build area
    -   reading stem files and translating to  FST in build area
    -   reading rules files and translating to  FST in build area
    -   composing FST acceptor transducers for each type of stem+rule combination present in the data set
    -   composing makefiles to compile the parser
-   `testBuild/unitTests a-g` Test basic parser construction from Allen-Greenough data set.  **Not up to date**.
-   `testBuild/integrationTests a-g` **Not  currently functioning?**
