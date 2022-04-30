-- | Este módulo define funções comuns da Tarefa 4 do trabalho prático.
module Tarefa4_2018li1g145 where

import LI11819
import Data.List
import Tarefa0_2018li1g145
import Tarefa1_2018li1g145
import Tarefa2_2018li1g145

-- * Testes
-- | Testes unitários da Tarefa 4.
--
-- Cada teste é um 'Estado'.
-- Cada teste é um 'Estado'.
testesT4 :: [Estado]
testesT4 = [Estado (mapaInicial (9,9))   [Jogador (1,1) B 5 5 5] [DisparoCanhao 0 (3,3) E, DisparoCanhao 0 (3,3) D, DisparoCanhao 0 (4,4) C, DisparoCanhao 0 (4,4) B],
            Estado (mapaInicial (9,9))   [Jogador (1,1) B 5 5 5] [DisparoCanhao 0 (3,3) E, DisparoCanhao 0 (3,4) D, DisparoCanhao 0 (4,4) C, DisparoCanhao 0 (5,4) B],
            Estado (mapaInicial (9,9))   [Jogador (1,1) B 5 5 5] [DisparoCanhao 0 (3,3) E, DisparoCanhao 0 (3,3) C, DisparoCanhao 0 (3,4) C, DisparoCanhao 0 (3,4) D, DisparoCanhao 0 (4,3) B, DisparoCanhao 0 (4,3) E, DisparoCanhao 0 (4,4) B, DisparoCanhao 0 (4,4) D], 
            Estado (mapaInicial (9,9))   [Jogador (1,1) B 5 5 5] [DisparoCanhao 0 (3,3) E, DisparoCanhao 0 (3,3) D, DisparoCanhao 0 (3,3) C, DisparoCanhao 0 (3,4) E, DisparoCanhao 0 (3,4) D, DisparoCanhao 0 (3,4) B, DisparoCanhao 0 (4,3) C, DisparoCanhao 0 (4,3) B, DisparoCanhao 0 (4,3) E, DisparoCanhao 0 (4,4) C, DisparoCanhao 0 (4,4) B, DisparoCanhao 0 (4,4) D, DisparoCanhao 0 (5,5) E, DisparoCanhao 0 (5,5) D, DisparoCanhao 0 (5,5) C, DisparoCanhao 0 (5,5) B],
            Estado (mapaInicial (10,6))  [Jogador (1,1) B 5 5 5] [DisparoLaser  0 (2,1) B, DisparoCanhao 0 (3,1) E, DisparoCanhao 0 (4,1) D, DisparoCanhao 0 (5,1) E, DisparoCanhao 0 (6,1) C, DisparoCanhao 0 (7,1) B],
            Estado (mapaInicial (6,10))  [Jogador (1,1) B 5 5 5] [DisparoLaser  0 (1,2) D, DisparoCanhao 0 (1,3) E, DisparoCanhao 0 (1,4) D, DisparoCanhao 0 (1,5) E, DisparoCanhao 0 (1,6) C, DisparoCanhao 0 (1,7) B],
            Estado (mapaInicial (6,6))   [Jogador (1,1) B 5 5 5, Jogador (1,3) B 5 5 5] [DisparoChoque 0 1, DisparoChoque 1 0],
            Estado (mapaInicial (6,6))   [Jogador (1,1) B 5 5 5] [DisparoChoque 0 5],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Destrutivel,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Destrutivel,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]] 
            [Jogador (6,1) C 5 5 5] [DisparoCanhao 0 (1,1) C, DisparoCanhao 0 (2,1) B, DisparoCanhao 0 (1,3) C, DisparoCanhao 0 (2,3) B, DisparoCanhao 0 (5,5) C, DisparoCanhao 0 (6,5) B],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]] 
            [Jogador (6,1) C 5 5 5] [DisparoCanhao 0 (1,1) C, DisparoCanhao 0 (2,1) B, DisparoCanhao 0 (1,3) C, DisparoCanhao 0 (2,3) B, DisparoCanhao 0 (5,5) C, DisparoCanhao 0 (6,5) B],
            
            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]] 
            [Jogador (6,1) C 5 5 5] [DisparoCanhao 0 (1,1) C, DisparoCanhao 0 (2,1) B, DisparoCanhao 0 (1,3) C, DisparoCanhao 0 (2,3) B],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
            [Jogador (1,1) D 5 5 5] [DisparoCanhao 0 (1,3) D, DisparoCanhao 0 (3,3) D, DisparoCanhao 0 (1,6) E, DisparoCanhao 0 (3,6) E, DisparoCanhao 0 (6,3) D, DisparoCanhao 0 (6,6) E],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
            [Jogador (1,1) D 5 5 5] [DisparoCanhao 0 (1,3) D, DisparoCanhao 0 (3,3) D, DisparoCanhao 0 (1,6) E, DisparoCanhao 0 (3,6) E, DisparoCanhao 0 (6,3) D, DisparoCanhao 0 (6,6) E],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
            [Jogador (1,1) D 5 5 5] [DisparoCanhao 0 (1,3) D, DisparoCanhao 0 (3,3) D, DisparoCanhao 0 (1,6) E, DisparoCanhao 0 (3,6) E, DisparoCanhao 0 (6,3) D, DisparoCanhao 0 (6,6) E],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
            [Jogador (1,1) D 5 5 5] [DisparoCanhao 0 (4,3) D, DisparoCanhao 0 (3,4) B, DisparoCanhao 0 (3,5) B, DisparoCanhao 0 (4,6) E, DisparoCanhao 0 (6,5) C, DisparoCanhao 0 (5,6) E, DisparoCanhao 0 (6,4) C, DisparoCanhao 0 (5,3) D],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Destrutivel,Vazia,Bloco Destrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
            [Jogador (1,1) D 5 5 5] [DisparoLaser 0 (1,2) D],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
            [Jogador (1,1) D 5 5 5] [DisparoLaser 0 (1,2) D, DisparoLaser 0 (3,2) D],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
            [Jogador (1,1) B 5 5 5] [DisparoLaser 0 (2,1) B],

            Estado [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],
            [Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
            [Jogador (1,1) B 5 5 5] [DisparoLaser 0 (2,1) B, DisparoLaser 0 (2,3) B],

            Estado (mapaInicial (9,9)) [Jogador (1,1) B 2 2 2, Jogador (6,1) D 2 2 9, Jogador (6,4) C 1 0 1] [DisparoCanhao 0 (5,1) B, DisparoCanhao 1 (6,3) D],
            Estado (mapaInicial (9,9)) [Jogador (4,4) E 5 2 2] [DisparoCanhao 0 (3,3) D, DisparoCanhao 0 (3,5) B, DisparoCanhao 0 (5,5) E, DisparoCanhao 0 (5,3) C],
            Estado (mapaInicial (9,9)) [Jogador (4,4) E 5 2 2] [DisparoCanhao 0 (3,3) B, DisparoCanhao 0 (3,5) E, DisparoCanhao 0 (5,5) C, DisparoCanhao 0 (5,3) D],
            Estado (mapaInicial (9,9)) [Jogador (1,1) B 2 2 2, Jogador (6,1) D 2 2 9, Jogador (6,4) C 1 0 1] [DisparoLaser 0 (2,1) B, DisparoLaser 1 (6,2) D],
            Estado (mapaInicial (9,9)) [Jogador (1,1) D 9 9 9, Jogador (4,4) E 5 2 2] [DisparoLaser 0 (3,3) D, DisparoLaser 0 (3,5) B, DisparoLaser 0 (5,5) E, DisparoLaser 0 (5,3) C],
            Estado (mapaInicial (9,9)) [Jogador (1,1) D 9 9 9, Jogador (4,4) E 5 2 2] [DisparoLaser 0 (3,3) B, DisparoLaser 0 (3,5) E, DisparoLaser 0 (5,5) C, DisparoLaser 0 (5,3) D],
            Estado (mapaInicial (9,9)) [Jogador (4,4) E 1 2 2, Jogador (1,1) D 5 5 5] [DisparoCanhao 1 (4,3) D, DisparoCanhao 1 (4,5) E],
            Estado (mapaInicial (9,9)) [Jogador (4,4) E 1 2 2, Jogador (1,1) D 5 5 5] [DisparoLaser 1 (4,3) D, DisparoLaser 1 (4,5) E]
            
            ]
-- * Funções principais da Tarefa 4.

-- | Avança o 'Estado' do jogo um 'Tick' de tempo.
--
-- __NB:__ Apenas os 'Disparo's afetam o 'Estado' do jogo com o passar do tempo.
--
-- __NB:__ Deve chamar as funções 'tickChoques', 'tickCanhoes' e 'tickLasers' pela ordem definida.
tick :: Estado -- ^ O 'Estado' anterior.
     -> Estado -- ^ O 'Estado' após um 'Tick'.
tick = tickChoques . tickCanhoes . tickLasers

-- | Avança o 'Estado' do jogo um 'Tick' de tempo, considerando apenas os efeitos dos tiros de 'Laser' disparados.
tickLasers :: Estado -> Estado
tickLasers (Estado m pl dp) = lasers dplas (Estado m pl (dpcan++dpcho))
                             where (dplas,dpcan,dpcho) = (separadp dp)

-- | Dado uma lista de Disparo Lasers aplica-os a um estado
lasers :: [Disparo] -> Estado -> Estado
lasers [] a = a
lasers (x:xs) a = lasers xs (laser x a)

-- | Dado um DisparoLaser aplica-o a um estado
laser :: Disparo -> Estado -> Estado
laser (DisparoLaser a pos d) (Estado m pl dp) |blocoIndestest m pos d = Estado (destroimapa m pos d) (menosvidalas pos d pl) (destroidplas pos dp)
                                              |otherwise = laser (DisparoLaser a (avancagrelha pos d) d) (Estado (destroimapa m pos d) (menosvidalas pos d pl) (destroidplas pos dp))

-- | Dadas as coordenadas e direcao de um Disparo Laser e uma lista de jogadores, retira as vidas dos mesmos (se as tiverem) 
menosvidalas :: PosicaoGrelha -> Direcao -> [Jogador] -> [Jogador]
menosvidalas _ _ [] = []
menosvidalas (x,y) d ((Jogador pos d2 a b c):xs) |(d==B || d==C) && (pos==(x,y) || pos==(x,y-1) || pos==(x,y+1)) && (a>0) = (Jogador pos d2 (a-1) b c):menosvidalas (x,y) d xs
                                                 |(d==E || d==D) && (pos==(x,y) || pos==(x-1,y) || pos==(x+1,y)) && (a>0) = (Jogador pos d2 (a-1) b c):menosvidalas (x,y) d xs
                                                 |otherwise = (Jogador pos d2 a b c):menosvidalas (x,y) d xs

-- | Destroi os disparos a frente de um laser
destroidplas :: PosicaoGrelha -> [Disparo] -> [Disparo]
destroidplas _ [] = []
destroidplas pos (x:xs) = case x of 
                          (DisparoCanhao a pos2 d) -> if pos==pos2 then destroidplas pos xs else x:(destroidplas pos xs)
                          (DisparoLaser {}) -> x:(destroidplas pos xs)
                          (DisparoChoque {}) -> x:(destroidplas pos xs) 

-- | Avança o 'Estado' do jogo um 'Tick' de tempo, considerando apenas os efeitos das balas de 'Canhao' disparadas.
tickCanhoes :: Estado -> Estado
tickCanhoes (Estado m pl dp) = Estado (mapdestroimapa record m) (atualizaplcan pl record) ((avancadp ((canhoescaso2 (canhoescaso1 dpcan)) \\ (record)) ++dplas++dpcho ) )
                              where
                                (dplas,dpcan,dpcho) = separadp dp
                                record = registodp dpcan m pl


-- | Destroi disparos canhoes que se "atravessaram" 
canhoescaso1 :: [Disparo] -> [Disparo]
canhoescaso1 [] = []
canhoescaso1 (x:xs) = canhoescaso1aux x (canhoescaso1 xs)

canhoescaso1aux :: Disparo -> [Disparo] -> [Disparo]
canhoescaso1aux a [] = [a]
canhoescaso1aux a (x:xs) = if dpatras x a then xs else x:(canhoescaso1aux a xs)

-- | Verifica se um disparo tem outro disparo atras dele
dpatras :: Disparo -> Disparo -> Bool
dpatras (DisparoCanhao _ (x,y) d1) (DisparoCanhao _ (t,v) d2) | (d1==D) && (d2==E) = ((x,y-1) == (t,v))
                                                              | (d1==E) && (d2==D) = ((x,y+1) == (t,v))
                                                              | (d1==C) && (d2==B) = ((x+1,y) == (t,v))
                                                              | (d1==B) && (d2==C) = ((x-1,y) == (t,v))
                                                              | otherwise = False


-- | Destroi o mapa nas suas varias posicoes
mapdestroimapa :: [Disparo] -> Mapa -> Mapa
mapdestroimapa [] a = a
mapdestroimapa _ [] = []
mapdestroimapa ((DisparoCanhao _ pos d):xs) m = destroimapa (mapdestroimapa xs m) pos d


-- | Atualiza as vidas dos jogadores conforme uma lista de Disparoscanhao
atualizaplcan :: [Jogador] -> [Disparo] -> [Jogador]
atualizaplcan [] _ = []
atualizaplcan a [] = a
atualizaplcan (x:xs) l = (menosvidacanhao x l):atualizaplcan xs l 

-- | Retira uma vida a um jogador se ele for atingido por algum dos disparos recebidos
menosvidacanhao :: Jogador -> [Disparo] -> Jogador
menosvidacanhao a [] = a
menosvidacanhao (Jogador pos d 0 b c) _ = (Jogador pos d 0 b c)
menosvidacanhao (Jogador pos d1 a b c) ((DisparoCanhao _ (x,y) d):xs)| d==B && (pos==(x+1,y) || pos==(x+1,y-1) || pos==(x+1,y+1)) && (a>0) =  rip
                                                                     | d==E && (pos==(x,y-1) || pos==(x-1,y-1) || pos==(x+1,y-1)) && (a>0) =  rip
                                                                     | d==C && (pos==(x-1,y) || pos==(x-1,y-1) || pos==(x-1,y+1)) && (a>0) =  rip
                                                                     | d==D && (pos==(x,y+1) || pos==(x-1,y+1) || pos==(x+1,y+1)) && (a>0) =  rip
                                                                     | otherwise = menosvidacanhao (Jogador pos d1 a b c) xs
                                                                     where rip = (menosvidacanhao (Jogador pos d1 (a-1) b c) xs)

-- | Dado uma lista de disparos que nao sofrem colisoes avança os disparos para a posicao seguinte
avancadp :: [Disparo] -> [Disparo]
avancadp [] = []
avancadp ((DisparoCanhao a b c):xs) = (DisparoCanhao a (avancagrelha b c) c):avancadp xs 

-- | Elimina disparos que se encontram na mesma posicao do mapa
canhoescaso2 :: [Disparo] -> [Disparo]
canhoescaso2 [] = []
canhoescaso2 (d:ds) | elem (posdp [d]) [posdp ds] = canhoescaso2 (delpos ds (posdp [d]))
                    | otherwise                   = d: canhoescaso2 ds

-- | Apaga de uma lista os disparos em determinada posiçao
delpos :: [Disparo] -> [PosicaoGrelha] -> [Disparo]
delpos [] _ = []
delpos (d:ds) [x] | (pos d)==x  = delpos ds [x]
                  | otherwise   = d: delpos ds [x]
                   where
                    pos (DisparoCanhao _ p _) = p

-- | Dado uma lista de disparos retorna as suas posicoes
posdp :: [Disparo] -> [PosicaoGrelha]
posdp [] = []
posdp ((DisparoCanhao _ p _):ds) = p:posdp ds


-- | Cria um registo dos disparos que chocam ou com um jogador ou com o mapa
registodp :: [Disparo]-> Mapa-> [Jogador] -> [Disparo] 
registodp [] _ _ = []
registodp ((DisparoCanhao a pos d):xs) m pl |(frontblocks m pos d)||(frontpl pl pos d) = (DisparoCanhao a pos d):(registodp xs m pl)
                                            |otherwise = (registodp xs m pl)
                                            
-- | Verifica se existe um jogador a frente da bala canhao
frontpl :: [Jogador] -> PosicaoGrelha -> Direcao -> Bool
frontpl [] _ _ = False
frontpl ((Jogador pos _ a _ _):xs) (x,y) d | d==B && (pos==(x+1,y) || pos==(x+1,y-1) || pos==(x+1,y+1)) && (a>0) = True 
                                           | d==E && (pos==(x,y-1) || pos==(x-1,y-1) || pos==(x+1,y-1)) && (a>0) = True 
                                           | d==C && (pos==(x-1,y) || pos==(x-1,y-1) || pos==(x-1,y+1)) && (a>0) = True 
                                           | d==D && (pos==(x,y+1) || pos==(x-1,y+1) || pos==(x+1,y+1)) && (a>0) = True 
                                           | otherwise = frontpl xs (x,y) d

-- | Verica se existem blocos no mapa numa determinada direcao de uma posicao grellha
frontblocks ::  Mapa -> PosicaoGrelha -> Direcao -> Bool
frontblocks m pos d =  a/=(Vazia) || b/= (Vazia)
                     where (a,b) = tipobloco m pos d 
      


-- | Avança o 'Estado' do jogo um 'Tick' de tempo, considerando apenas os efeitos dos campos de 'Choque' disparados.
tickChoques :: Estado -> Estado
tickChoques (Estado m pl dp) = (Estado m pl (dplas++dpcan++choques(dpcho)) )
                              where (dplas,dpcan,dpcho) = (separadp dp)

-- | Diminui ou remove um disparo dependendo do seu tempo restante 
choques :: [Disparo] -> [Disparo]
choques [] = []
choques ((DisparoChoque a b):t) |b==0 = choques t
                                |otherwise = (DisparoChoque a (b-1)):(choques t)

-- * Funçoes auxiliares gerais aos tres tipos de disparos 

-- ** Algumas destas funçoes parecem redundates e desnesseçarias mas nao são

-- | Testa se ambos os blocos a frente de uma posicao grelha sao indestrutiveis
blocoIndestest :: Mapa -> PosicaoGrelha -> Direcao -> Bool
blocoIndestest m pos d = a && b
                       where (a,b) = blocoIndestestseparado m pos d

-- | Testa se os blocos a frente de uma posicao grelha sao indestrutiveis separadamente
blocoIndestestseparado :: Mapa -> PosicaoGrelha -> Direcao -> (Bool,Bool)
blocoIndestestseparado m pos d = (a==(Bloco Indestrutivel) , b== (Bloco Indestrutivel))
                          where (a,b) = tipobloco m pos d 


-- | Dado um Mapa , uma posiçao na grelha e uma direcao devolve uma par das peças que se encontram a frente da direcao dada 
tipobloco :: Mapa -> PosicaoGrelha -> Direcao -> (Peca,Peca)
tipobloco m pos dir = ( ((m!!a)!!b) , ((m!!c)!!d) )
                    where ((a,b),(c,d))= direcaobloco pos dir 

-- | Dada uma posiçao na grelha, retorna as posiçoes do mapa numa dada direcao
direcaobloco ::  PosicaoGrelha -> Direcao -> (Posicao, Posicao) 
direcaobloco (x,y) D =((x+1,y+1),(x,y+1))
direcaobloco (x,y) B =((x+1,y),(x+1,y+1))
direcaobloco (x,y) E =((x,y),(x+1,y))
direcaobloco (x,y) C =((x,y),(x,y+1))


-- | Dada uma direçao avaça uma posicao na grellha
avancagrelha :: PosicaoGrelha -> Direcao -> PosicaoGrelha
avancagrelha (x,y) D = (x,y+1)
avancagrelha (x,y) C = (x-1,y)
avancagrelha (x,y) E = (x,y-1)
avancagrelha (x,y) B = (x+1,y)


-- | Destroi o mapa numaa direcao de uma determinada posicao da grelha
destroimapa :: Mapa -> PosicaoGrelha -> Direcao -> Mapa
destroimapa m pos d  |teste1 && teste2  = m
                     |not(teste1) && teste2 = atualizaPosicaoMapa x Vazia m
                     |teste1 && not(teste2) = atualizaPosicaoMapa y Vazia m
                     |not (teste1 && teste2) = atualizaPosicaoMapa x Vazia (atualizaPosicaoMapa y Vazia m)
                        where 
                            (teste1,teste2) = blocoIndestestseparado m pos d 
                            (x,y) = direcaobloco pos d


-- | Separa uma lista de disparo em um triplo de listas de disparo dos tres tipos existentes
separadp :: [Disparo] -> ([Disparo],[Disparo],[Disparo])
separadp [] = ([],[],[])
separadp (x:xs) = case x of
                   (DisparoLaser {})  -> (x:t,w,v)
                   (DisparoCanhao {}) -> (t,x:w,v)
                   (DisparoChoque {})   -> (t,w,x:v)
                  where
                     (t,w,v)= separadp xs

-- * Funcoes repetidas da tarefa 0 que tinham erros  -> corrigidas

atualizaPosicaoMapa :: Posicao -> a -> Matriz a -> Matriz a
atualizaPosicaoMapa (a,b) w (x:xs) | (a<=length (x:xs) && b<=length x) = atualizaIndiceLista a aux1 (x:xs)
                                   | otherwise = (x:xs)
                                     where
                                      aux1 = atualizaIndiceLista b w ((x:xs) !! a)

