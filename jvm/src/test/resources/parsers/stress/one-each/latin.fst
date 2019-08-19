
%% latin.fst : a Finite State Transducer for ancient latin morphology
%
% All symbols used in the FST:
#include "/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/symbols.fst"

% Dynamically loaded lexica of stems:
$stems$ = "/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-adjectives.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-compoundverbs.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-indeclinables.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-irregadjectives.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-irregadverbs.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-irregcompoundinfinitives.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-irregcompoundverbs.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-irreginfinitives.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-irregnouns.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-irregpronouns.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-irregverbs.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-nouns.fst" |\
"/Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/lexica/lexicon-verbs.fst"

% Dynamically loaded inflectional rules:
$ends$ = "</Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/inflection.a>"

% Morphology data is the crossing of stems and endings:
$morph$ = $stems$ <div> $ends$

% Acceptor (1) filters for content satisfying requirements for
% morphological analysis and  (2) maps from underlying to surface form
% by suppressing analytical symbols, and allowing only surface strings.
$acceptor$ = "</Users/nsmith/Desktop/tabulae/jvm/src/test/resources/parsers/stress/one-each/acceptor.a>"

% Final transducer:
$morph$ || $acceptor$

