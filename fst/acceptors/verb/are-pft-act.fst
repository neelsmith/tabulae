%  are.fst
%
% Generate perfect stem for regular -are verbs.
%
#include "@workdir@symbols.fst"


%% Extend stems for regular verbs:


% perfect stem>
#pftsys# = <pft><plupft><futpft>


ALPHABET = [#editorial# #urntag# #urnchar# <verb> #morphtag# #stemtype#  #separator# #letter# #diacritic# \. #stemchars# ]


#=ltr# = #consonant# #vowel# <lo> <sh>
$are_pft$ =  {[#=ltr#]}:{[#=ltr#]av} ^->  (<u>[#urnchar#]+[#period#][#urnchar#]+</u><u>[#urnchar#]+[#period#][#urnchar#]+</u>[#stemchars#]+  __ <verb><are_vb>\:\:<are_vb><verb> [#stemchars#]+[#person#][#number#][#pftsys#][#mood#][#voice#])


$are_pft$
