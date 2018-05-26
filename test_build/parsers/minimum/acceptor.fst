#include "/data/repos/latin/tabulae/test_build/parsers/minimum/symbols.fst"

% Indeclinable form acceptor:
$=indeclclass$ = [#indeclclass#]
$squashindeclurn$ = <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u> [#stemchars#]+ <indecl> $=indeclclass$  $separator$+  $=indeclclass$ <indecl> <u>[#urnchar#]:<>+\.:<>[#urnchar#]:<>+</u>



% Union of all URN squashers.

$acceptor$ = $squashindeclurn$

%% Put all symbols in 2 categories:  pass
%% surface symbols through, suppress analytical symbols.
#analysissymbol# = #editorial# #urntag# <noun><verb><indecl><ptcpl><infin><vadj><adj><adv> #morphtag# #stemtype#  #separator#
#surfacesymbol# = #letter# #diacritic#
ALPHABET = [#surfacesymbol#] [#analysissymbol#]:<>
$stripsym$ = .+

%% The canonical pipeline: (morph data) -> acceptor -> parser/stripper
$acceptor$ || $stripsym$

