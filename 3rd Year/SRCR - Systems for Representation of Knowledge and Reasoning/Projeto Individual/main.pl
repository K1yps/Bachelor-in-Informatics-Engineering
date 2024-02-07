:-include("baseDeConhecimento.pl"). % Contém os predicados para carregar a base de conhecimento
:-include("utilidades.pl"). % Contém os predicados para tramento de dados 

:- dynamic nodo/5.
:- dynamic arco/3.
:- dynamic garagem/1.
:- dynamic deposito/1.

%Para Testes
garagem(21949).
deposito(21966).

% ---- Geraradores de circuitos ------------------------------------------------------------------------------------------------------------


circuitoDFS(Garagem,Circuito,[Garagem|Percurso],Distancia,QuantidadeRecolhida):-
    garagem(Garagem),pontoMaisPerto(Garagem,Inicio),
    recolhaDFS(Inicio,PontoFim,EntreNodos,QuantidadeRecolhida,15000),
    deposito(Descarga), caminhoDFS(PontoFim,Descarga,[_|Transporte]),
    caminhoDFS(Descarga,Garagem,[_|Final]),
    append(EntreNodos,Transporte,P1),
    append(P1,Final,Percurso),
    calculaDistancia(Circuito,Distancia).


circuitoDFS(Garagem,Tipo,Circuito,[Garagem|Percurso],Distancia,QuantidadeRecolhida):-
    garagem(Garagem),pontoMaisPerto(Garagem,Inicio),
    recolhaDFS(Inicio,PontoFim,EntreNodos,QuantidadeRecolhida,Tipo,15000),
    deposito(Descarga), caminhoDFS(PontoFim,Descarga,[_|Transporte]),
    caminhoDFS(Descarga,Garagem,[_|Final]),
    append(EntreNodos,Transporte,P1),
    append(P1,Final,Percurso),
    calculaDistancia(Circuito,Distancia).

circuitoDFS(Garagem,Descarga,PontoInicio,PontoFim,Circuito,Distancia,QuantidadeRecolhida):-
    caminhoDFS(Garagem,PontoInicio,PercusoInicial),
    recolhaDFS(PontoInicio,PontoFim,[_|Percurso],QuantidadeRecolhida,15000),
    caminhoDFS(PontoFim,Descarga,[_|PercursoDescarga]),
    caminhoDFS(Descarga,Garagem,[_|PercuroRetorno]),
    concat([PercusoInicial,Percurso,PercursoDescarga,PercuroRetorno],Circuito),
    calculaDistancia(Circuito,Distancia).


circuitoDFS(Garagem,Tipo,Descarga,PontoInicio,PontoFim,Circuito,Distancia,QuantidadeRecolhida):-
    caminhoDFS(Garagem,PontoInicio,PercusoInicial),
    recolhaDFS(PontoInicio,PontoFim,[_|Percurso],QuantidadeRecolhida,Tipo,15000),
    caminhoDFS(PontoFim,Descarga,[_|PercursoDescarga]),
    caminhoDFS(Descarga,Garagem,[_|PercuroRetorno]),
    concat([PercusoInicial,Percurso,PercursoDescarga,PercuroRetorno],Circuito),
    calculaDistancia(Circuito,Distancia).

circuitoBFS(Garagem,[Garagem|Percurso],Distancia,QuantidadeRecolhida):-
    garagem(Garagem),pontoMaisPerto(Garagem,Inicio),
    recolhaBFS(Inicio,PontoFim,EntreNodos,_,QuantidadeRecolhida,15000),
    deposito(Descarga), caminhoBFS(PontoFim,Descarga,[_|Transporte]),
    caminhoDFS(Descarga,Garagem,[_|Final]),
    append(EntreNodos,Transporte,P1),
    append(P1,Final,Percurso),
    calculaDistancia([Garagem|Percurso],Distancia).

circuitoBFS(Garagem,Descarga,PontoInicio,PontoFim,Circuito,Distancia,QuantidadeRecolhida):-
    caminhoBFS(Garagem,PontoInicio,PercusoInicial),
    recolhaBFS(PontoInicio,PontoFim,[_|Percurso],_,QuantidadeRecolhida,15000),
    caminhoBFS(PontoFim,Descarga,[X|PercursoDescarga]),
    caminhoBFS(Descarga,Garagem,[_|PercuroRetorno]),
    concat([PercusoInicial,Percurso,PercursoDescarga,PercuroRetorno],Circuito),
    calculaDistancia(Circuito,Distancia),
    calculaQuantidadeTotal([X|PercursoDescarga],QuantidadeRecolhida).

% ---- DFS ---------------------------------------------------------------------------------------------------------------------------------
    
% Base

caminhoDFS(Orig,Dest,Cam):-
    depthfirst(Orig,Dest,[Orig],Cam).
    %condicao final: nodo actual = destinore

    depthfirst(Dest,Dest,LA,Cam):-
        reverse(LA,Cam).

    depthfirst(Act,Dest,LA,Cam):-
        adjacencia(Act,X,_),
        \+ member(X,LA),
        depthfirst(X,Dest,[X|LA],Cam).

% Recolhe todo o lixo até nao conseguir ir a mais nenhum nodo, ou estiver cheio
    
recolhaDFS(Orig,Dest,Cam,QuantRes,Max):-
    recolhaDepthfirst(Orig,Dest,[Orig],Cam,0,Max,QuantRes).
    %condicao final: nodo actual = destino

    recolhaDepthfirst(Act,Dest,LA,Cam,Quant,Max,QuantRes):-
        nodo(Act,_,_,_,Lixo),
        encher(Lixo,Quant,Max,NewQuant),
        continuarRecolhaDFS(Act,Dest,LA,Cam,NewQuant,Max,QuantRes).

    continuarRecolhaDFS(Act,Dest,LA,Cam,Quant,Max,QuantRes):-
        Quant < Max,
        adjacencia(Act,X,_),
        \+ member(X,LA),
        recolhaDepthfirst(X,Dest,[X|LA],Cam,Quant,Max,QuantRes).

    continuarRecolhaDFS(Dest,Dest,LA,Cam,Quant,_,Quant):- reverse(LA,Cam).

% Recolhe um tipo de lixo especifico até nao conseguir ir a mais nenhum nodo, ou estiver cheio

recolhaDFS(Orig,Dest,Cam,QuantRes,Tipo,Max):-
    recolhaDepthfirst(Orig,Dest,[Orig],Cam,0,Max,Tipo,QuantRes).
    %condicao final: nodo actual = destino
    
    recolhaDepthfirst(Act,Dest,LA,Cam,Quant,Max,Tipo,QuantRes):-
        nodo(Act,_,_,_,Lixo),
        encher(Tipo,Lixo,Quant,Max,NewQuant),
        continuarRecolhaDFS(Act,Dest,LA,Cam,NewQuant,Max,Tipo,QuantRes).
        
    continuarRecolhaDFS(Act,Dest,LA,Cam,Quant,Max,Tipo,QuantRes):-
        Quant < Max,
        adjacencia(Act,X,_),
        \+ member(X,LA),
        recolhaDepthfirst(X,Dest,[X|LA],Cam,Quant,Max,Tipo,QuantRes).
        
    continuarRecolhaDFS(Dest,Dest,LA,Cam,Quant,_,_,Quant):- reverse(LA,Cam). 

%Profundidade Limitada
 
caminhoDFSLim(Orig,Dest,Cam,ProfundidadeMax):-
    depthfirstLim(Orig,Dest,[Orig],Cam,0,ProfundidadeMax).
    %condicao final: nodo actual = destino

    depthfirstLim(Dest,Dest,LA,Cam,_,_):-
        reverse(LA,Cam).

    depthfirstLim(Act,Dest,LA,Cam,Profundidade,ProfundidadeMax):-
        Profundidade < ProfundidadeMax,
        adjacencia(Act,X,_),
        \+ member(X,LA),
        NP is Profundidade + 1,
        depthfirstLim(X,Dest,[X|LA],Cam,NP,ProfundidadeMax).

% Recolhe todo o lixo até nao conseguir ir a mais nenhum nodo, ou estiver cheio
    
recolhaDFSLim(Orig,Dest,Cam,QuantRes,Max,ProfundidadeMax):-
    recolhaDepthfirstLim(Orig,Dest,[Orig],Cam,0,Max,QuantRes,0,ProfundidadeMax).
    %condicao final: nodo actual = destino

    recolhaDepthfirstLim(Act,Dest,LA,Cam,Quant,Max,QuantRes,Profundidade,ProfundidadeMax):-
        Profundidade < ProfundidadeMax,
        nodo(Act,_,_,_,Lixo),
        encher(Lixo,Quant,Max,NewQuant),
        continuarRecolhaDFSLim(Act,Dest,LA,Cam,NewQuant,Max,QuantRes,Profundidade,ProfundidadeMax).

    continuarRecolhaDFSLim(Act,Dest,LA,Cam,Quant,Max,QuantRes,Profundidade,ProfundidadeMax):-
        Quant < Max,
        adjacencia(Act,X,_),
        \+ member(X,LA),
        NP is Profundidade +1,
        recolhaDepthfirstLim(X,Dest,[X|LA],Cam,Quant,Max,QuantRes,NP,ProfundidadeMax).

    continuarRecolhaDFSLim(Dest,Dest,LA,Cam,Quant,_,Quant,_,_):- reverse(LA,Cam).

% Recolhe um tipo de lixo especifico até nao conseguir ir a mais nenhum nodo, ou estiver cheio

recolhaDFSLim(Orig,Dest,Cam,QuantRes,Tipo,Max,ProfundidadeMax):-
    recolhaDepthfirstLim(Orig,Dest,[Orig],Cam,0,Max,Tipo,QuantRes,0,ProfundidadeMax).
    %condicao final: nodo actual = destino
    
    recolhaDepthfirstLim(Act,Dest,LA,Cam,Quant,Max,Tipo,QuantRes,Profundidade,ProfundidadeMax):-
        Profundidade < ProfundidadeMax,
        nodo(Act,_,_,_,Lixo),
        encher(Tipo,Lixo,Quant,Max,NewQuant),
        continuarRecolhaDFSLim(Act,Dest,LA,Cam,NewQuant,Max,Tipo,QuantRes,Profundidade,ProfundidadeMax).
        
    continuarRecolhaDFSLim(Act,Dest,LA,Cam,Quant,Max,Tipo,QuantRes,Profundidade,ProfundidadeMax):-
        Quant < Max,
        adjacencia(Act,X,_),
        \+ member(X,LA),
        NP is Profundidade +1,
        recolhaDepthfirstLim(X,Dest,[X|LA],Cam,Quant,Max,Tipo,QuantRes,NP,ProfundidadeMax).
        
    continuarRecolhaDFSLim(Dest,Dest,LA,Cam,Quant,_,_,Quant):- reverse(LA,Cam). 




% ---- BFS ---------------------------------------------------------------------------------------------------------------------------------

% Base

caminhoBFS(Orig,Dest,Cam):-breadthfirst(Dest,[[Orig]],Cam).

    breadthfirst(Dest,[[Dest|T]|_],Cam):-
        reverse([Dest|T],Cam).

    breadthfirst(Dest,[LA|Outros],Cam):-
        LA=[Act|_],
        findall([X|LA],
            (
                Dest\==Act,
                adjacencia(Act,X,_),
                \+ member(X,LA)
            ),
            Novos
        ),
        append(Outros,Novos,Todos),
        %chamada recursiva
        breadthfirst(Dest,Todos,Cam).


caminhoBFS(Orig,Dest,Cam,Distancia) :- 
    caminhoBFS(Orig,Dest,Cam),
    calculaDistancia(Cam,Distancia).

recolhaBFS(Orig,Dest,Cam,Distancia,QuantRes,Max):-
    caminhoBFS(Orig,Dest,Cam),
    calculaDistancia(Cam,Distancia),
    calculaQuantidadeTotal(Cam,QuantRes),
    QuantRes =< Max.

recolhaBFS(Orig,Dest,Cam,Distancia,QuantRes,Tipo,Max) :-
    caminhoBFS(Orig,Dest,Cam),
    calculaDistancia(Cam,Distancia),
    calculaQuantidadeTipo(Tipo,Cam,QuantRes),
    QuantRes =< Max.



caminhoBNB(Inicio,Destino,Caminho,Distancia):-
    branchAndBound(Destino,[(0,[Inicio])],Caminho,Distancia).

    branchAndBound(Destino,[(Distancia,[Destino|T])|_],Caminho,Distancia):-
    reverse([Destino|T],Caminho).

    branchAndBound(Destino,[(Da,LA)|Outros],Caminho,Distancia):-
    LA=[Atual|_],
    findall((DistX,[X|LA]),
    (Destino\==Atual,arco(Atual,X,DistanciaX),\+ member(X,LA),
    DistX is DistanciaX + Da),Novos),
    append(Outros,Novos,Todos),
    sort(Todos,TodosOrd),
    branchAndBound(Destino,TodosOrd,Caminho,Distancia).

recolhaBNB(Inicio,Destino,Caminho,Distancia,QuantRes,Max):-
    branchAndBound(Destino,[(0,0,[Inicio])],Caminho,Distancia,QuantRes,Max).

    branchAndBound(Destino,[(Da,QuantAtual,LA)|Outros],Caminho,Distancia,QuantRes,Max):-
    LA=[Atual|_],
    nodo(Atual,_,_,_,Lixo),
    encher(Lixo,QuantAtual,Max,NovaQuantidade),
    NovaQuantidade<Max,
    findall((DistX,NovaQuantidade,[X|LA]),
    (
        Destino\==Atual,
        arco(Atual,X,DistanciaX),
        \+ member(X,LA),
        DistX is DistanciaX + Da),
    Novos),
    append(Outros,Novos,Todos),
    sort(Todos,TodosOrd),
    branchAndBound(Destino,TodosOrd,Caminho,Distancia,QuantRes,Max).


branchAndBound(Destino,[(Distancia,QuantAtual,[Destino|T])|_],Caminho,Distancia,Max,Max):-
    nodo(Destino,_,_,_,Lixo),
    encher(Lixo,QuantAtual,Max,NovaQuantidade),
    NovaQuantidade=:=Max,
    reverse([Destino|T],Caminho).

branchAndBound(Destino,[(Distancia,QuantRes,[Destino|T])|_],Caminho,Distancia,QuantRes,Max):-
    QuantRes =< Max,
    reverse([Destino|T],Caminho).


recolhaBNB(Inicio,Destino,Caminho,Distancia,QuantRes,Tipo,Max):-
    branchAndBound(Destino,[(0,0,[Inicio])],Caminho,Distancia,QuantRes,Tipo,Max).

    branchAndBound(Destino,[(Da,QuantAtual,LA)|Outros],Caminho,Distancia,QuantRes,Tipo,Max):-
    LA=[Atual|_],
    nodo(Atual,_,_,_,Lixo),
    encher(Tipo,Lixo,QuantAtual,Max,NovaQuantidade),
    NovaQuantidade<Max,
    findall((DistX,NovaQuantidade,[X|LA]),
    (
        Destino\==Atual,
        arco(Atual,X,DistanciaX),
        \+ member(X,LA),
        DistX is DistanciaX + Da),
    Novos),
    append(Outros,Novos,Todos),
    sort(Todos,TodosOrd),
    branchAndBound(Destino,TodosOrd,Caminho,Distancia,QuantRes,Tipo,Max).

branchAndBound(Destino,[(Distancia,QuantAtual,[Destino|T])|_],Caminho,Distancia,Max,Tipo,Max):-
    nodo(Destino,_,_,_,Lixo),
    encher(Tipo,Lixo,QuantAtual,Max,NovaQuantidade),
    NovaQuantidade=:=Max,
    reverse([Destino|T],Caminho).

branchAndBound(Destino,[(Distancia,QuantRes,[Destino|T])|_],Caminho,Distancia,QuantRes,_,Max):-
    QuantRes =< Max,
    reverse([Destino|T],Caminho).

% ---- A* ----------------------------------------------------------------------------------------------------------------------------------

%Base
aEstrela(Orig,Dest,Cam,Distancia):-
    aEstrela2(Dest,[(_,0,[Orig])],Cam,Distancia).

    aEstrela2(Dest,[(_,Distancia,[Dest|T])|_],Cam,Distancia):-
        reverse([Dest|T],Cam).

    aEstrela2(Dest,[(_,Ca,LA)|Outros],Cam,Distancia):-
        LA=[Act|_],
        findall(
            (CEX,CaX,[X|LA]),
            (
                Dest\==Act,adjacencia(Act,X,DistanciaX),\+ member(X,LA),
                CaX is DistanciaX + Ca, estimativa(X,Dest,EstimativasX),
                CEX is CaX +EstimativasX
            ),
            Novos),
        append(Outros,Novos,Todos),
        sort(Todos,TodosOrd),
        aEstrela2(Dest,TodosOrd,Cam,Distancia).

aEstrelaDistanciaPorLixo(Orig,Dest,Cam,Distancia):-
    aEstrelaLPD2(Dest,[(_,0,[Orig])],Cam,Distancia).

    aEstrelaLPD2(Dest,[(_,Distancia,[Dest|T])|_],Cam,Distancia):-
        reverse([Dest|T],Cam).

    aEstrelaLPD2(Dest,[(_,Ca,LA)|Outros],Cam,Distancia):-
        LA=[Act|_],
        findall(
            (CEX,CaX,[X|LA]),
            (
                Dest\==Act,adjacencia(Act,X,DistanciaX),\+ member(X,LA),
                CaX is DistanciaX + Ca, estimativaResidPorDist(X,Dest,EstimativasX),
                CEX is CaX +EstimativasX
            ),
            Novos),
        append(Outros,Novos,Todos),
        sort(Todos,TodosOrd),
        aEstrelaLPD2(Dest,TodosOrd,Cam,Distancia).


% ---- Gulosa -----------------------------------------------------------------------------------------------------------------------------

gulosa(Partida, Destino,Caminho,Distancia) :-
	estimativa(Partida,Destino ,Estimativa),
	gulosa2([[Partida]/0/Estimativa],Destino,InvCaminho/Distancia/_),
    reverse(InvCaminho, Caminho).


gulosa2(Cam, Destino ,Caminho) :-
	melhorCaminhoGul(Cam, Caminho),
    Caminho = [Nodo|_]/_/_,
    Nodo == Destino.

gulosa2(Cam, Destino ,SolucaoCaminho) :-
	melhorCaminhoGul(Cam,MelhorCaminho),
	seleciona(MelhorCaminho, Cam, OutrosCam),
    findall(NovoCam, adjacenteGul(MelhorCaminho,NovoCam,Destino), ExpCam),
	append(OutrosCam, ExpCam, NovoCam),
    gulosa2(NovoCam,Destino ,SolucaoCaminho).	



adjacenteGul([Nodo|Caminho]/Distancia/_, [ProxNodo,Nodo|Caminho]/NovoDistancia/Est,Destino) :-
    adjacencia(Nodo, ProxNodo, PassoDistancia),\+ member(ProxNodo, Caminho),
    NovoDistancia is Distancia + PassoDistancia,
    estimativa(ProxNodo,Destino ,Est).



gulosaDistanciaPorLixo(Partida, Destino,Caminho,Distancia) :-
	estimativaResidPorDist(Partida,Destino ,Estimativa),
	gulosa2([[Partida]/0/Estimativa],Destino,InvCaminho/Distancia/_),
    reverse(InvCaminho, Caminho).

gulosaLPD2(Cam, Destino ,Caminho) :-
	melhorCaminhoGul(Cam, Caminho),
    Caminho = [Nodo|_]/_/_,
    Nodo == Destino.

gulosaLPD2(Cam, Destino ,SolucaoCaminho) :-
	melhorCaminhoGul(Cam,MelhorCaminho),
	seleciona(MelhorCaminho, Cam, OutrosCam),
    findall(NovoCam, adjacenteLPDGul(MelhorCaminho,NovoCam,Destino), ExpCam),
	append(OutrosCam, ExpCam, NovoCam),
    gulosaLPD2(NovoCam,Destino ,SolucaoCaminho).	

adjacenteLPDGul([Nodo|Caminho]/Distancia/_, [ProxNodo,Nodo|Caminho]/NovoDistancia/Est,Destino) :-
    adjacencia(Nodo, ProxNodo, PassoDistancia),\+ member(ProxNodo, Caminho),
    NovoDistancia is Distancia + PassoDistancia,
    estimativaDistPorResid(ProxNodo,Destino ,Est).


melhorCaminhoGul([Caminho], Caminho) :- !.

melhorCaminhoGul([Caminho1/Distancia1/Est1,_/_/Est2|Cam], MelhorCaminho) :- 
    Est1 =< Est2, !, melhorCaminhoGul([Caminho1/Distancia1/Est1|Cam], MelhorCaminho).
	
melhorCaminhoGul([_|Cam], MelhorCaminho) :- melhorCaminhoGul(Cam, MelhorCaminho).

seleciona(E, [E|Xs], Xs).
seleciona(E, [X|Xs], [X|Ys]) :- seleciona(E, Xs, Ys).



estatisticas(Questao):-
    statistics(runtime,[Start|_]),
    Questao,
    statistics(runtime,[Stop|_]),
    Runtime is Stop-Start,
    write("Tempo: "),write(Runtime).