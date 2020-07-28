---
title: Tabulae
layout: page
---


# Tabulae

There are three main phases to building a corpus-specific parser and working with its output.

1. you write (or modify existing) plain-text tables defining rules and vocabulary for your parser.
2. `tabulae`'s build packages read your tables, then write and compile SFST code.
3. `tabulae`'s analysis packages can read the output of the SFST parser, and give you structured objects.

This documentation accordingly falls into three parts;

1. [Building a corpus-specific dataset](./tables/)
2. [Compiling and using a parser](./compiling/)
3. [Analyzing the output of the parser](analysis/)
