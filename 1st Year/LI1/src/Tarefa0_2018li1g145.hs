
-- | Este módulo define funções genéricas sobre vetores e matrizes, que serão úteis na resolução do trabalho prático.
module Tarefa0_2018li1g145 where

import LI11819
import Data.List

-- * Funções não-recursivas.

-- | Um 'Vetor' é uma 'Posicao' em relação à origem.
type Vetor = Posicao
-- ^ <<http://oi64.tinypic.com/mhvk2x.jpg vetor>>

-- ** Funções sobre vetores

-- *** Funções gerais sobre 'Vetor'es.

-- | Soma dois 'Vetor'es.
somaVetores :: Vetor -> Vetor -> Vetor
somaVetores (x1,y1) (x2,y2) = (x1+x2,y1+y2)

-- | Subtrai dois 'Vetor'es.
subtraiVetores :: Vetor -> Vetor -> Vetor
subtraiVetores (x1,y1) (x2,y2) = (x1-x2,y1-y2)

-- | Multiplica um escalar por um 'Vetor'.
multiplicaVetor :: Int -> Vetor -> Vetor
multiplicaVetor a (x,y) = (a*x,a*y)
-- | Roda um 'Vetor' 90º no sentido dos ponteiros do relógio, alterando a sua direção sem alterar o seu comprimento (distância à origem).

rodaVetor :: Vetor -> Vetor
rodaVetor (x,y) = (y,-x)

-- | Espelha um 'Vetor' na horizontal (sendo o espelho o eixo vertical).

inverteVetorH :: Vetor -> Vetor
inverteVetorH (x,y) = (x,-y)

-- | Espelha um 'Vetor' na vertical (sendo o espelho o eixo horizontal).

inverteVetorV :: Vetor -> Vetor
inverteVetorV  (x,y) = (-x,y)

-- *** Funções do trabalho sobre 'Vetor'es.

-- | Devolve um 'Vetor' unitário (de comprimento 1) com a 'Direcao' dada.
direcaoParaVetor :: Direcao -> Vetor
direcaoParaVetor  a | a==C = (-1,0)
                    | a==D = (0,1)
                    | a==B = (1,0)
                    | a==E = (0,-1)

-- ** Funções sobre listas

-- *** Funções gerais sobre listas.

-- Funções não disponíveis no 'Prelude', mas com grande utilidade.

-- | Verifica se o indice pertence à lista.

eIndiceListaValido :: Int -> [a] -> Bool
eIndiceListaValido a l = a+1<=length l && a>=0

-- ** Funções sobre matrizes.

-- *** Funções gerais sobre matrizes.

-- | Uma matriz é um conjunto de elementos a duas dimensões.
--
-- Em notação matemática, é geralmente representada por:
--
-- <<https://upload.wikimedia.org/wikipedia/commons/d/d8/Matriz_organizacao.png matriz>>
type Matriz a = [[a]]

-- | Calcula a dimensão de uma matriz.
--
-- __NB:__ Note que não existem matrizes de dimensão /m * 0/ ou /0 * n/, e que qualquer matriz vazia deve ter dimensão /0 * 0/.

dimensaoMatriz :: Matriz a -> Dimensao
dimensaoMatriz [] = (0,0)
dimensaoMatriz (h:t) = if m>0 && n>0 then (m,n) else (0,0)
     where 
        m=length (h:t)
        n=length h

-- | Verifica se a posição pertence à matriz.
ePosicaoMatrizValida :: Posicao -> Matriz a -> Bool 
ePosicaoMatrizValida _ [] = False
ePosicaoMatrizValida (i,j) (h:t) = if i<=m && j<=n && i>0 && j>0 then True else False
                                   where 
                                     m=length (h:t)
                                     n=length h


-- | Verifica se a posição está numa borda da matriz.
eBordaMatriz :: Posicao -> Matriz a -> Bool
eBordaMatriz _ [] = False
eBordaMatriz (0,_) _ = False
eBordaMatriz (_,0) _ = False
eBordaMatriz (i,j) (h:t) = i==1 || i==length(h:t) || j==1 || j==length(h)


-- *** Funções do trabalho sobre matrizes.

-- | Converte um 'Tetromino' (orientado para cima) numa 'Matriz' de 'Bool'.
--
-- <<http://oi68.tinypic.com/m8elc9.jpg tetrominos>>
tetrominoParaMatriz :: Tetromino -> Matriz Bool
tetrominoParaMatriz a  
             | a == I = [[False,True,False,False],[False,True,False,False],[False,True,False,False],[False,True,False,False]]
             | a == J = [[False,True,False],[False,True,False],[True,True,False]]
             | a == L = [[False,True,False],[False,True,False],[False,True,True]]
             | a == O = [[True,True],[True,True]]
             | a == S = [[False,True,True],[True,True,False],[False,False,False]]
             | a == T = [[False,False,False],[True,True,True],[False,True,False]]
             | a == Z = [[True,True,False],[False,True,True],[False,False,False]]
-- * Funções recursivas.

-- ** Funções sobre listas.
--
-- Funções não disponíveis no 'Prelude', mas com grande utilidade.

-- | Devolve o elemento num dado índice de uma lista.
encontraIndiceLista :: Int -> [a] -> a
encontraIndiceLista a (x:xs) = if a==0 then x else encontraIndiceLista (a-1) xs  

-- | Modifica um elemento num dado índice.
--
-- __NB:__ Devolve a própria lista se o elemento não existir.

atualizaIndiceLista :: Int -> a -> [a] -> [a]
atualizaIndiceLista _ _ [] = []
atualizaIndiceLista a b (x:xs)  | a==0 = b:xs
                                | otherwise = x:atualizaIndiceLista (a-1) b xs 

-- ** Funções sobre matrizes.

-- | Roda uma 'Matriz' 90º no sentido dos ponteiros do relógio.
--
-- <<http://oi68.tinypic.com/21deluw.jpg rodaMatriz>>
rodaMatriz :: Matriz a -> Matriz a
rodaMatriz [] = []
rodaMatriz [l] = map (:[]) l
rodaMatriz (x:xs) = zipWith (++) (rodaMatriz xs) (map (:[]) x)

-- | Inverte uma 'Matriz' na horizontal.
--
-- <<http://oi64.tinypic.com/iwhm5u.jpg inverteMatrizH>>
inverteMatrizH :: Matriz a -> Matriz a
inverteMatrizH [] = []
inverteMatrizH (x:xs) = (reverse x):(inverteMatrizH xs)

-- | Inverte uma 'Matriz' na vertical.
--
-- <<http://oi64.tinypic.com/11l563p.jpg inverteMatrizV>>
inverteMatrizV :: Matriz a -> Matriz a
inverteMatrizV [] = []
inverteMatrizV (x:xs) = (inverteMatrizV xs )++[x]

-- | Cria uma nova 'Matriz' com o mesmo elemento.
criaMatriz :: Dimensao -> a -> Matriz a
criaMatriz (a,b) x  | a<=0 || b<=0 = []
                    | otherwise = [replicate b x] ++ criaMatriz (a-1,b) x

-- | Devolve o elemento numa dada 'Posicao' de uma 'Matriz'.
encontraPosicaoMatriz :: Posicao -> Matriz a -> a
encontraPosicaoMatriz (a,b) (x:xs)  |a==1 && b>0 = encontraIndiceLista (b-1) x
                                    |a/=1 && a>0 && b>0 = encontraIndiceLista (b-1) (encontraIndiceLista (a-1) (x:xs))

-- | Modifica um elemento numa dada 'Posicao'
--
-- __NB:__ Devolve a própria 'Matriz' se o elemento não existir.
atualizaPosicaoMatriz :: Posicao -> a -> Matriz a -> Matriz a
atualizaPosicaoMatriz (a,b) w (x:xs) | (a>0 && b>0 && a<=length (x:xs) && b<=length x) = atualizaIndiceLista (a-1) aux1 (x:xs)
                                     | otherwise = (x:xs)
                                                         where
                                                           aux1 = atualizaIndiceLista (b-1) w (encontraIndiceLista (a-1) (x:xs))
