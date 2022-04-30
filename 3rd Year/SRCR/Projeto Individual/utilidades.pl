
% ----- Utilitades ----------------------------------------------------------------------------------------------

:- dynamic nodo/5.
:- dynamic arco/3.


adjacencia(X,Y,Dist) :-
    arco(X,Y,Dist).

adjacencia(X,Y,Dist) :-
    arco(Y,X,Dist).

% baseado na distancia percorrida
estimativa(Nodo1,Nodo2,Estimativa):-    
    nodo(Nodo1,_,X1,Y1,_),
    nodo(Nodo2,_,X2,Y2,_),
    Estimativa is sqrt((X1-X2)^2+(Y1-Y2)^2).

% baseado na distancia percorrida por volume de residuos recolhidos 
estimativaDistPorResid(Nodo1,Nodo2,Estimativa):-
    nodo(Nodo1,_,X1,Y1,_),
    nodo(Nodo2,_,X2,Y2,Residuos),
    sumResiduos(Residuos,Res),
    Estimativa is sqrt((X1-X2)^2+(Y1-Y2)^2)/Res.

sumResiduos([],0.0).

sumResiduos([(_,X)|T],Resultado):-
    sumResiduos(T,PreRes),
    Resultado is X + PreRes.

getResiduo(Tipo,[(Tipo,Res)|_],Res).

getResiduo(Tipo,[_|T],Res):- getResiduo(Tipo,T,Res).

encher([(_,Res)|T],Quant,Max,Fill):-
    Curr is Quant+Res,
    Max >= Curr,
    encher(T,Curr,Max,Fill).

encher([(_,Res)|_],Quant,Max,Max):-
    Curr is Quant+Res,
    Max < Curr.

encher([],Quant,_,Quant).


encher(Tipo,[(Tipo,Res)|T],Quant,Max,Fill):-
    Curr is Quant+Res,
    Max >= Curr,
    encher(T,Curr,Max,Fill).

encher(Tipo,[(Tipo,Res)|_],Quant,Max,Max):-
    Curr is Quant+Res,
    Max < Curr.

encher(Tipo,[_|T],Quant,Max,Fill):-
    encher(Tipo,T,Quant,Max,Fill).

encher(_,[],Quant,_,Quant).



calculaDistancia([P1, P2|T],Distancia):-
    adjacencia(P1, P2, N),
    calculaDistancia([P2|T], DistanciaAcumulada),
    Distancia is N + DistanciaAcumulada.
  
calculaDistancia([],0).

calculaQuantidadeTotal([Id|T],Quantidade):-
    nodo(Id, _, _,_,Lista),
    sumResiduos(Lista,Quant),
    calculaQuantidadeTotal(T, QuantAc),
    Quantidade is Quant + QuantAc.

calculaQuantidadeTotal([],0).

calculaQuantidadeTipo(Tipo,[Id|T],Quantidade):-
    nodo(Id, _, _,_,Lista),
    getResiduo(Tipo,Lista,Quant),
    calculaQuantidadeTipo(Tipo,T, QuantAc),
    Quantidade is Quant + QuantAc.

calculaQuantidadeTipo(Tipo,[_|T],Quantidade):- calculaQuantidadeTipo(Tipo,T, Quantidade).

calculaQuantidadeTipo(_,[],0).

merge([H|T],L,[H|Res]):- merge(T,L,Res).

merge([],L,L).

concat([A,B|T],Res) :- merge(A, B, Pr), concat([Pr|T],Res).

concat([X|[]],X).

concat([],[]).

pontoMaisPerto(A,X):-
    findall(F,arco(A,F,_),L),
    list_min(L,X).

list_min([L|Ls], Min) :-
    list_min(Ls, L, Min).

list_min([], Min, Min).
list_min([L|Ls], Min0, Min) :-
    Min1 is min(L, Min0),
    list_min(Ls, Min1, Min).

nao( Questao ) :-
    Questao, !, fail.
nao(_). 