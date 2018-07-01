% stemtypes.fst
%
% Definitions of morphological stem types used to unite stem entries and
% inflectional patterns
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Noun stem types
#noun1# = <a_ae>
#noun2# = <us_i><ius_i>
#noun3# = <e_is><x_gis><tas_tatis>


#nounclass# = #noun1# #noun2# #noun3#



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Adjective stem types
#adj1and2# = <us_a_um>

#adjectiveclass# = #adj1and2#



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Verb stem types
#regular# = <conj1><conj2><conj3><conj3io><conj4><c1pres><c2pres><c3pres><c4pres><c3iopres><pftact><pftpass>

#verbclass#  = #regular#

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Indeclinable type
#indeclclass# = <indeclconj><indeclinterj><indeclprep>


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Irregular type
#irregclass# = <irregadj><irregnoun><irregadv><irregcverb><irreginfin><irregptcpl><irregvadj>

% Union of all stemtypes
#stemtype# = #nounclass# #adjectiveclass# #verbclass# #indeclclass# #irregclass#
