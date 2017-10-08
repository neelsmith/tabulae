This directory is for corpus-specific datasets (morphological lexica, inflectional rules, and definition of orthographic system).

Each corpus should have its dataset in a named directory with subdirectories laid out following the example in this repository's `datatemplate` directory.

You build templates for your datasets with sbt:

    sbt corpus CORPUSNAME
