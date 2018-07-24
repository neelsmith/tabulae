% stemtypes.fst
%
% Definitions of morphological stem types used to unite stem entries and
% inflectional patterns
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Noun stem types
#noun1# = <a_ae><as_ae><e_es><er_ri><es_ae>
#noun2# = <0_i><os_i><us_eris><us_i>
#noun3# = <0_is><en_inis><eps_ipis><er_ris><es_itis><ix_icis><ma_matis><o_onis><us_oris><x_cis><x_ctis><x_gis><is_idis><tas_tatis><ut_itis><o_inis><or_oris><is_is><s_is><s_tis>
#noun4# = <us_us>
#noun5# = <es_ei>

#nounclass# = #noun1# #noun2# #noun3# #noun4# #noun5#



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Adjective stem types
#adj1and2# = <us_a_um><0_a_um><er_ra_rum>

#adj3# = <is_e>

#adjectiveclass# = #adj1and2# #adj3#



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
