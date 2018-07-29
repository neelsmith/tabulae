% morphsymbols.fst
%
% Definitions of all symbols used to express a morphological analysis
%
% "Parts of speech": not traditional definition, but distinguished by
% analytical pattern.  E.g., <infin> has distinct pattern that is neither
% noun nor verb.
#pos# = <noun><adj><verb><vadj><infin><gerundive><gerund><supine><ptcpl><adv><pron><irregcverb><irregnoun><irregadj><irregadv><irreginfin><irregptcpl><irregvadj><irregpron>

#verbparts# = <verb><infin><ptcpl><gerundive><gerund><supine>
%
% 1. Noun morphology:
%
#gender# = <masc><fem><neut>
#case# = <nom><acc><gen><dat><abl><voc>
#number# = <sg><pl>
%
% 2. Adjective morphology:
%
#degree# = <pos><comp><superl>
%
% 3. Verb morphology:
%
#person# = <1st><2nd><3rd>
% number already defined in noun morphology
#tense# = <pres><impft><fut><futpft><pft><plupft>
#mood# = <indic><subj><imptv>
#voice# = <act><pass>

% Need to add gerunds, supine....

#finiteform# = <infin><ptcpl>

#morphtag# = #pos# #gender# #case# #number# #person# #tense# #mood# #voice# #degree# #finiteform#

%
% 4. Indeclinable forms:
%
#indecl# = <indeclprep><indeclconj><indeclinterj>


% Consider whether those belong here or elsewhere...?

% Transducers for the above variables:
$gender$ = [#gender#]
$case$ = [#case#]
$number$ = [#number#]

$person$ = [#person#]
$tense$ = [#tense#]
$mood$ = [#mood#]
$voice$ = [#voice#]

$degree$ = [#degree#]


% Identity variables for the transducers:
$=gender$ = [#gender#]
$=case$ = [#case#]
$=number$ = [#number#]
