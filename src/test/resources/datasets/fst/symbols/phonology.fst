% phonology.fst
%
% Definitions of all legal symbols in stem files (lexica) except for
% stem types (defined in stemtypes.fst)

%
% Basic alphabet specific to this orthographic system:
#include "@workdir@symbols/alphabet.fst"


#diaeresis# = \+
#diacritic# = #diaeresis#


#character# = #letter# #diacritic#
$character$ = [#character#]

% Additional editorial symbols used in stem files:
#vowelquant# = <lo><sh>
#morpheme# = <#>

#editorial# = #morpheme#  #vowelquant#


% All valid chars used in stem file:
#stemchars# = #character# #editorial#
#inmorpheme# = #character# #vowelquant#
