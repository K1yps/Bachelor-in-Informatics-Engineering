-- | Este módulo define funções comuns da Tarefa 1 do trabalho prático.
module Tarefa1_2018li1g145 where

import LI11819
import Data.List
import Tarefa0_2018li1g145

-- * Testes

-- | Testes unitários da Tarefa 1.
--
-- Cada teste é uma sequência de 'Instrucoes'.
testesT1 :: [Instrucoes]
testesT1 = [ [Move C, Move D, MudaTetromino, MudaTetromino, MudaParede, Desenha, MudaTetromino, Roda, Roda, Move B, Desenha, MudaParede, Move E, Move E, Move E, Desenha], 
             [MudaTetromino, Move B, Move B, Roda, Roda, Roda, Desenha, Move E, Desenha, Move C, MudaParede, Desenha, Move E, Move E, Move C, MudaTetromino, MudaTetromino, MudaTetromino, Desenha],
             [Move E, Move E, Move C, Desenha, MudaTetromino, MudaTetromino, MudaTetromino, MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino,MudaParede, Desenha, Move D, Move D, Desenha],
             [MudaTetromino, Move B, Desenha, Move B, Desenha, Roda, Move D,Move D, Move D, Desenha, MudaParede, Move C, MudaTetromino,MudaTetromino,Desenha],
             [Roda,MudaParede,Move C,Move D,Desenha],
             [MudaParede,Move E, Move B,Desenha],
             [MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino,Desenha,Move B, Move B,Desenha],
             [],
             [Move B,MudaParede,MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino,Desenha],
             [Move D,Desenha,MudaTetromino,MudaParede,Move C,Move C,Move C,Move B,Desenha],
             [Move E,Move E,Move E,Move B,Move B,Move B,Move C,Move D, MudaTetromino,MudaTetromino,MudaTetromino,Desenha] ]

-- * Funções principais da Tarefa 1.

-- | Aplica uma 'Instrucao' num 'Editor'.
instrucao :: Instrucao -- ^ A 'Instrucao' a aplicar.
          -> Editor    -- ^ O 'Editor' anterior.
          -> Editor    -- ^ O 'Editor' resultante após aplicar a 'Instrucao'.

--
--    * 'MudaParede' - muda o tipo de 'Parede'.
instrucao (MudaParede) (Editor pos d t p m) = (Editor pos d t (next p [Destrutivel,Indestrutivel,Destrutivel]) m)

--
--    * 'Desenha' - altera o 'Mapa' para incluir o 'Tetromino' atual, sem alterar os outros parâmetros.
instrucao (Desenha) (Editor pos d t p m) = (Editor pos d t p (desenhatetr (Editor pos d t p m)))

--
--    * 'Move' - move numa dada 'Direcao'.
instrucao (Move x) (Editor pos d t p m) = (Editor (somaVetores pos (direcaoParaVetor x)) d t p m) 

--
--    * 'Roda' - Muda a 'Direcao' do Tetromino
instrucao (Roda) (Editor pos d t p m) = (Editor pos (next d [C,D,B,E,C]) t p m)

--    * 'MudaTetromino' - seleciona a 'Peca' seguinte (usar a ordem léxica na estrutura de dados),
--       sem alterar os outros parâmetros.
instrucao (MudaTetromino) (Editor pos d t p m) = (Editor pos d (next t [I,J,L,O,S,T,Z,I]) p m) 


-- * Funções auxiliares da função instrucao

-- | Recebe um elemento de uma lista, e devolve o elemento seguinte da mesma lista
next :: Eq a => a -> [a] -> a
next x (l:ls) | x==l = head ls
              | otherwise = next x ls


-- | Tem como função desenhar o 'Editor' no 'Mapa'
desenhatetr :: Editor -> Mapa            
desenhatetr (Editor pos d t p m) = (insertTetr p (posAalterar pos pos b (a,b)) (concat (makeTetrMatriz d t)) m)
                                    where
                                      (a,b)=(dimensaoMatriz (makeTetrMatriz d t))

-- | Insere o Tetromino no mapa 
insertTetr ::  Parede -> [Posicao]-> [Bool] -> Mapa -> Mapa
insertTetr _ _ [] m = m
insertTetr _ [] _ m = m
insertTetr p (l:ls) (x:xs) m | x == True  = atualizaPosicaoMatriz l (Bloco p) (insertTetr p ls xs m)
                             | otherwise  = insertTetr p ls xs m

-- | Roda um 'Tetromino' e converte-o numa 'Matriz Bool'
makeTetrMatriz :: Direcao -> Tetromino -> Matriz Bool
makeTetrMatriz d t | d == C = tetrominoParaMatriz t 
                   | d == D = rodaMatriz (tetrominoParaMatriz t) 
                   | d == B = rodaMatriz (rodaMatriz (tetrominoParaMatriz t)) 
                   | d == E = rodaMatriz (rodaMatriz (rodaMatriz (tetrominoParaMatriz t)))

-- | Cria uma lista de todas as posiçoes a alterar no mapa ao usar a instrucao 'Desenha'           
posAalterar :: Posicao -> Posicao -> Int -> Dimensao -> [Posicao]
posAalterar (a,b) (x,y) ac (t,v) | ac>0 && t/=0 = (x+1,y+1):posAalterar (a,b) (x,y+1) (ac-1) (t,v)   
                                 | ac==0 && t/=0 = posAalterar (a,b) (x+1,b) v ((t-1),v)
                                 | ac==0 && t==0 = []


-- * Funções principais (cont.)

-- | Aplica uma sequência de 'Instrucoes' num 'Editor'.
-- __NB:__ Deve chamar a função 'instrucao'.
instrucoes :: Instrucoes -- ^ As 'Instrucoes' a aplicar.
           -> Editor     -- ^ O 'Editor' anterior.
           -> Editor     -- ^ O 'Editor' resultante após aplicar as 'Instrucoes'.
instrucoes [] (Editor pos d t p m) = (Editor pos d t p m)
instrucoes (l:ls) (Editor pos d t p m) = instrucoes ls (instrucao l (Editor pos d t p m))

-- | Cria um 'Mapa' inicial com 'Parede's nas bordas e o resto vazio.
mapaInicial :: Dimensao -- ^ A 'Dimensao' do 'Mapa' a criar.
            -> Mapa     -- ^ O 'Mapa' resultante com a 'Dimensao' dada.
mapaInicial (_,0) = [] 
mapaInicial (0,_) = []
mapaInicial (a,b)  | a==1  = [replicate b (Bloco Indestrutivel)]
                   | otherwise = [replicate b (Bloco Indestrutivel)] ++ replicate (a-2) ((Bloco Indestrutivel):(replicate (b-2) (Vazia)) ++ [Bloco Indestrutivel]) ++ [replicate b (Bloco Indestrutivel)] 


-- | Cria um 'Editor' inicial.

-- __NB:__ Deve chamar as funções 'mapaInicial', 'dimensaoInicial', e 'posicaoInicial'.
editorInicial :: Instrucoes  -- ^ Uma sequência de 'Instrucoes' de forma a poder calcular a  'dimensaoInicial' e a 'posicaoInicial'.
              -> Editor      -- ^ O 'Editor' inicial, usando a 'Peca' 'I' 'Indestrutivel' voltada para 'C'.
editorInicial []     = (Editor pos (C) (I) (Indestrutivel) m)
                       where
                         pos = posicaoInicial []
                         m = mapaInicial (dimensaoInicial [])
editorInicial (l:ls) = (Editor pos (C) (I) (Indestrutivel) m)
                       where
                         pos = posicaoInicial (l:ls)
                         m = mapaInicial (dimensaoInicial (l:ls))

-- | Constrói um 'Mapa' dada uma sequência de 'Instrucoes'.

-- __NB:__ Deve chamar as funções 'Instrucoes' e 'editorInicial'.
constroi :: Instrucoes -- ^ Uma sequência de 'Instrucoes' dadas a um 'Editor' para construir um 'Mapa'.
         -> Mapa       -- ^ O 'Mapa' resultante.
constroi []     = selectMap (editorInicial [])
                  where
                   selectMap (Editor pos d t p m) = m
constroi (l:ls) = selectMap (instrucoes (l:ls) (editorInicial (l:ls)))
                  where
                   selectMap (Editor pos d t p m) = m


