use_module(library(csv)).

:- dynamic carregarPredefinicoes/0.
:- dynamic nodo/5.
:- dynamic arco/3.

carregarPredefinicoes():-
    carregaNodos("datasetProcessado/nodos.csv"),
    carregaArcos("datasetProcessado/arestas.csv").

carregaArcos(Nome_CSV):-
    csv_read_file(Nome_CSV, Data, [functor(arco), separator(0';),arity(3),match_arity(false)]),
    assertListaArcos(Data).


assertListaArcos([arco(Ponta1,Ponta2,Distancia)|T]):-
    not(existe(Ponta1,Ponta2)),
    assert(arco(Ponta1,Ponta2,Distancia)),
    assertListaArcos(T).

assertListaArcos([_|T]):- assertListaArcos(T).

assertListaArcos([]).


carregaNodos(Nome_CSV):-
    csv_read_file(Nome_CSV, Data, [functor(prenodo), separator(0';),arity(5),match_arity(false)]),
    assertListaNodos(Data).


assertListaNodos([prenodo(Id,Nome,Latitude,Longitude,Residuos)|T]):-
    split_string(Residuos, "|", '', ListaResiduos), 
    paraPares(ListaResiduos,Res),
    assert(nodo(Id,Nome,Latitude,Longitude,Res)),
    assertListaNodos(T).

    
assertListaNodos([_|T]):-assertListaNodos(T).

assertListaNodos([]).
        
obtem_melhor_g([Caminho], Caminho) :- !.

obtem_melhor_g([Caminho1/Custo1/Est1,_/_/Est2|Caminhos], MelhorCaminho) :-
	Est1 =< Est2, !,
	obtem_melhor_g([Caminho1/Custo1/Est1|Caminhos], MelhorCaminho).
	
obtem_melhor_g([_|Caminhos], MelhorCaminho) :- 
	obtem_melhor_g(Caminhos, MelhorCaminho).


        

paraPares([String|T],[S|R]):-
    split_string(String, "=", '', [A,B|_]),
    atom_number(B, X),
    S = (A,X),
    paraPares(T,R).

paraPares([],[]).

existe(X,Y):-
    arco(X,Y,_).

existe(X,Y):-
    arco(Y,X,_).