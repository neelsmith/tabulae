% stemtypes.fst
%
% Definitions of morphological stem types used to unite stem entries and
% inflectional patterns
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Noun stem types
#noun1# = <a_ae><a_ae_gk><e_es><es_ae><as_ae>
#noun2# = <us_i><ius_i><0_i><er_ri><os_i>
#noun3# = <is_is><x_ctis><tas_tatis><0_is><o_onis><o_inis><er_ris><en_inis><us_eris><us_oris><s_is><es_itis><s_dis><x_cis><x_gis><0_dis><0_is><0_tis><eps_ipis><ut_itis><s_tis><ex_icis><s_ris><nx_gis>

#noun3istem# =  <i_is_is><i_er_ris><i_0_is><i_s_is><i_es_is><i_s_tis>
#noun4# = <us_us>
#noun5# = <es_ei>





%
#nounclass# = #noun1# #noun2# #noun3# #noun3istem# #noun4# #noun5#



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Adjective stem types
#adj1and2# = <us_a_um><0_a_um><er_ra_rum><us_a_um_pos><us_a_um_superl>

#adj3# = <is_e><er_ris_re><or_oris_comp>

#adj3single# = <ns_ntis><x_cis><us_eris><0_is><0_is_i><es_itis><s_dis><s_itis>



#adjectiveclass# = #adj1and2# #adj3# #adj3single#



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Verb stem types
#regular# = <conj1><conj2><conj3><conj3io><conj4><c1pres><c2pres><c3pres><c4pres><c3iopres><pftact><pftpass>

#verbclass#  = #regular#

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Indeclinable type
#indeclclass# = <indeclconj><indeclinterj><indeclprep><indeclnum>


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Irregular type
#irregclass# = <irregcverb><irregnoun><irregadj><irregadv><irreginfin><irregptcpl><irreggrnd><irreggrndv><irregsupn><irregpron>


% Union of all stemtypes
#stemtype# = #nounclass# #adjectiveclass# #verbclass# #indeclclass# #irregclass#
