
%% latin.fst : a Finite State Transducer for ancient latin morphology
%
% All symbols used in the FST:
#include "/data/repos/latin/tabulae/test_build/parsers/minimum/symbols.fst"

% Dynamically loaded lexica of stems:
$stems$ = "/data/repos/latin/tabulae/test_build/parsers/minimum/lexica/lexicon-indeclinables.fst"

% Dynamically loaded inflectional rules:
$ends$ = "</data/repos/latin/tabulae/test_build/parsers/minimum/inflection.a>"

% Morphology data is the crossing of stems and endings:
$morph$ = $stems$ $separator$ $separator$ $ends$

% Acceptor filters for content satisfying requirements for% morphological analysis and maps from underlying to surface form.
$acceptor$ = "</data/repos/latin/tabulae/test_build/parsers/minimum/acceptor.a>"

% Final transducer:
$morph$ || $acceptor$

