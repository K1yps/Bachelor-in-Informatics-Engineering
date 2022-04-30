-- | Este módulo define funções comuns da Tarefa 2 do trabalho prático.
module Tarefa2_2018li1g145 where

import LI11819
import Data.List
import Tarefa0_2018li1g145
import Tarefa1_2018li1g145

-- * Testes

-- | Testes unitários da Tarefa 2.
--
-- Cada teste é um triplo (/identificador do 'Jogador'/,/'Jogada' a efetuar/,/'Estado' anterior/).
testesT2 :: [(Int,Jogada,Estado)]
testesT2 = [(0, Movimenta D, Estado (mapaInicial (6,6)) [(Jogador (1,1) D 3 2 2)] []), 
            (0, Movimenta E, Estado (mapaInicial (6,6)) [(Jogador (1,1) D 3 2 2)] []),
            (1, Movimenta E, Estado (mapaInicial (6,6)) [(Jogador (2,2) E 3 0 0),(Jogador (2,4) E 3 0 0)] []),
            (1, Dispara Canhao, Estado [[Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia],
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel],
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Bloco Destrutivel,Vazia]]
                          [(Jogador (1,1) C 1 1 1),(Jogador (2,3) E 2 0 3)] 
                          [(DisparoCanhao 2 (2,2) E),(DisparoChoque 1 3)]),
            (1, Dispara Laser, Estado (mapaInicial (7,7)) [(Jogador (1,1) C 1 1 1),(Jogador (2,3) E 2 0 3)] [(DisparoCanhao 2 (2,2) E),(DisparoChoque 1 3)]),
            (1, Movimenta B, Estado (mapaInicial (7,7)) [(Jogador (1,1) C 1 1 0),(Jogador (2,3) E 2 0 3)] [(DisparoCanhao 2 (2,2) E),(DisparoChoque 1 3)]),
            (2, Dispara Choque, Estado (mapaInicial (8,9)) [(Jogador (1,1) C 1 1 1),(Jogador (2,3) E 2 0 3),(Jogador (6,6) B 4 5 3)] []) ]

-- * Funções principais da Tarefa 2.

-- | Efetua uma jogada.
jogada :: Int -- ^ O identificador do 'Jogador' que efetua a jogada.
       -> Jogada -- ^ A 'Jogada' a efetuar.
       -> Estado -- ^ O 'Estado' anterior.
       -> Estado -- ^ O 'Estado' resultante após o jogador efetuar a jogada.
jogada j (Movimenta d) (Estado m js ds) = (Estado m (transformapl m d js js j ds) ds)
jogada j (Dispara arma) (Estado m js ds) = (Estado m js1 (d++ds))
                                            where
                                               (js1,d) = selecionapl j arma js j


-- * Funções Auxiliares relevantes para ambos os tipos de jogada
-- | Devolve o numero de vidas que um 'Jogador' tem.
vidas :: Jogador -> Int
vidas (Jogador pos d vid las choq) = vid

-- * Funções Auxiliares do tipo de jogada 'Movimenta'

-- | Transforma o 'Jogador' conforme a jogada que pretende realizar 
transformapl :: Mapa -> Direcao -> [Jogador] -> [Jogador] -> Int -> [Disparo] -> [Jogador]
transformapl _ _ [] _ _ _ = []
transformapl m d (x:xs) l a ds | a==0 && vidas x > 0 && (espacoLivre m d x ds (posicoespls (delete x l)) (posicoespls l)) = (movepl d x):xs
                               | a==0 && vidas x > 0 = (rodapl d x):xs
                               | otherwise = x:(transformapl m d xs l (a-1) ds)

-- | Verifica se o espaço para o que o player se pretende mover está livre, ou seja, é 'Vazia', não se encontra ocupado por outro Jogador e não está sob choque
--   #g1 sao as posiçoes na grelha de todos os outros players (não inclui o proprio, nem inclui os players mortos)  
--   #g2 sao as posiçoes de todos os players (inclui o proprio) 
espacoLivre :: Mapa -> Direcao-> Jogador -> [Disparo] -> [PosicaoGrelha] -> [PosicaoGrelha] -> Bool
espacoLivre m C (Jogador (x,y) d a b c) ds g1 g2 |    encontraPosicaoMatriz (x,y+1) m == Vazia  -- utiliza posiçoes
                                                   && encontraPosicaoMatriz (x,y+2) m == Vazia
                                                   && anyBelongs [(x-2,y),(x-2,y-1),(x-2,y+1)] g1 == False  --utiliza posiçoes grelha
                                                   && espacoSobChoque (x,y) ds g2 == False = True
                                                 | otherwise = False 
espacoLivre m D (Jogador (x,y) d a b c) ds g1 g2 |    encontraPosicaoMatriz (x+1,y+3) m == Vazia 
                                                   && encontraPosicaoMatriz (x+2,y+3) m == Vazia 
                                                   && anyBelongs [(x,y+2),(x-1,y+2),(x+1,y+2)] g1 == False  
                                                   && espacoSobChoque (x,y) ds g2 == False = True
                                                 | otherwise = False                                        
espacoLivre m B (Jogador (x,y) d a b c) ds g1 g2 |    encontraPosicaoMatriz (x+3,y+1) m == Vazia 
                                                   && encontraPosicaoMatriz (x+3,y+2) m == Vazia 
                                                   && anyBelongs [(x+2,y),(x+2,y-1),(x+2,y+1)] g1 == False 
                                                   && espacoSobChoque (x,y) ds g2 == False = True
                                                 | otherwise = False
espacoLivre m E (Jogador (x,y) d a b c) ds g1 g2 |    encontraPosicaoMatriz (x+1,y) m == Vazia 
                                                   && encontraPosicaoMatriz (x+2,y) m == Vazia 
                                                   && anyBelongs [(x,y-2),(x-1,y-2),(x+1,y-2)] g1 == False  
                                                   && espacoSobChoque (x,y) ds g2 == False = True
                                                 | otherwise = False

-- | Verifica se o espaço à volta de determinada posiçao grelha está sob choque (por parte de outro jogador)
espacoSobChoque :: PosicaoGrelha -> [Disparo] -> [PosicaoGrelha] -> Bool
espacoSobChoque (x,y) ds g2 = anyBelongs [(x-1,y),(x-1,y+1),(x,y+2),(x+1,y+2),(x+2,y),(x+2,y+1),(x,y-1),(x+1,y-1)] (concat ( delete (area6x6 (x,y)) (areaChoque ds g2) ))

 -- | Recebe a lista de disparos e as posiçoes de todos os players e devolve uma lista com todas as posiçoes grelha sob choque, em listas correspondentes a cada player
areaChoque :: [Disparo] -> [PosicaoGrelha] -> [[PosicaoGrelha]]
areaChoque [] _ = [[]]
areaChoque _ [] = [[]]
areaChoque ((DisparoCanhao j pos d):xs) l  = areaChoque xs l
areaChoque ((DisparoLaser j pos d):xs) l   = areaChoque xs l               
areaChoque ((DisparoChoque j t):xs) l      = [area6x6 (l !! j)] ++ areaChoque xs l

-- | Devolve a área da grelha 6x6 relativa à posiçao grelha
area6x6 :: PosicaoGrelha -> [PosicaoGrelha]
area6x6 (x,y) = [(x-2,y-2),(x-1,y-2),(x,y-2),(x+1,y-2),(x+2,y-2),(x+3,y-2),
                 (x-2,y-1),(x-1,y-1),(x,y-1),(x+1,y-1),(x+2,y-1),(x+3,y-1), 
                 (x-2,y),(x-1,y),(x,y),(x+1,y),(x+2,y),(x+3,y),
                 (x-2,y+1),(x-1,y+1),(x,y+1),(x+1,y+1),(x+2,y+1),(x+3,y+1),
                 (x-2,y+2),(x-1,y+2),(x,y+2),(x+1,y+2),(x+2,y+2),(x+3,y+2),
                 (x-2,y+3),(x-1,y+3),(x,y+3),(x+1,y+3),(x+2,y+3),(x+3,y+3)]


-- | Verifica se algum dos elementos da primeira lista dada pertence à segunda
anyBelongs :: [PosicaoGrelha] -> [PosicaoGrelha] -> Bool
anyBelongs [] _ = False
anyBelongs _ [] = False
anyBelongs (x:xs) l | elem x l  = True 
                    | otherwise = anyBelongs xs l

-- | Cria uma lista com todas as posicoes dos Jogadores vivos (com mais de 0 vidas)
posicoespls :: [Jogador] -> [PosicaoGrelha]
posicoespls [] = []
posicoespls ((Jogador (x,y) d a b c):ls) | a > 0 = (x,y):posicoespls ls
                                         | otherwise = posicoespls ls

-- | Move o Jogador ou roda-o, dependendo se esta virado para a direcao que deu
movepl :: Direcao -> Jogador -> Jogador
movepl x (Jogador pos d a b c) | x==d = (Jogador (somaVetores pos (direcaoParaVetor x)) d a b c)
                               | otherwise = (Jogador pos x a b c)

-- | Roda o Jogador para a direcao dada
rodapl :: Direcao -> Jogador -> Jogador    
rodapl x (Jogador pos d a b c) = (Jogador pos x a b c)



-- * Funções Auxiliares do tipo de jogada 'Dispara'

-- | Seleciona o Jogador e dispara, no caso de este ter muniçoes e vidas
selecionapl :: Int -> Arma -> [Jogador] -> Int -> ([Jogador],[Disparo])
selecionapl _ _ [] _ = ([],[])
selecionapl ac arma (x:xs) a | a==0 && vidas x > 0 = ( t:xs , v )
                             | otherwise = ( x:f , fs )
                              where 
                                  (t,v)= disparapl ac arma x
                                  (f,fs)= selecionapl ac arma xs (a-1)


-- | Cria o disparo, se este puder ser disparado pelo 'Jogador' em causa
disparapl :: Int-> Arma -> Jogador -> (Jogador,[Disparo])
disparapl ac Canhao (Jogador pos d a b c) = ((Jogador pos d a b c),[DisparoCanhao ac (posdisparo d pos) d])  
disparapl ac Laser  (Jogador pos d a b c) |b>0 = ((Jogador pos d a (b-1) c),[DisparoLaser ac (posdisparo d pos) d])
                                          |otherwise = ((Jogador pos d a b c),[]) 
disparapl ac Choque (Jogador pos d a b c) |c>0 = ((Jogador pos d a b (c-1)),[DisparoChoque ac 5])
                                          |otherwise = ((Jogador pos d a b c),[])

-- | Devolve a posicao onde o disparo se vai encontrar ao ser disparado
posdisparo :: Direcao -> PosicaoGrelha -> PosicaoGrelha
posdisparo x pos = somaVetores pos (direcaoParaVetor x)

