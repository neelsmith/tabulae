

## Contents

This directory is for corpus-specific datasets (morphological lexica, inflectional rules, and definition of orthographic system).

Each corpus should have its dataset in a named directory with subdirectories laid out following the example in this repository's `datatemplate` directory.

You can put a copy of the accompanying `datatemplate` directory in `datasets`, or build templates for your datasets from an `sbt` console:

    corpus CORPUSNAME

## Included sample datasets

These sample datasets include further documentation (see the accompanying `README` files), and a wordlist you can use to test the dataset.

-   `analytical-types`.  A dataset with one example of each rule type, one example of each stem type, and one example of each irregular type.
-   `stem-classes`.  *In progress*.  A dataset with one example of each stem class.
-   `ag-rules`.  *In progress*.  A dataset with rules covering all paradigm forms in Allen and Greenough's *Latin Grammar*.
