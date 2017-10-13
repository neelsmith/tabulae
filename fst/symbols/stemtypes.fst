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
#adj1and2# = <0_a_um><er_era_erum>

#adjectiveclass# = #adj1and2#


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Adverb stem types
#indecladv# = <indeclpos><indeclcomp><indeclsup>

#adverbclass# = #indecladv#

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Verb stem types
#regular# = <are_vb>

#verbclass#  = #regular#


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Indeclinable type
#indeclclass# = <conjunct><exclam><prepos>


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Irregular type
#irregclass# = <irregadj><irregnoun><irregadv><irregcverb><irreginfin><irregptcpl><irregvadj>

% Union of all stemtypes
#stemtype# = #nounclass# #adjectiveclass# #adverbclass# #verbclass# #indeclclass# #irregclass#
