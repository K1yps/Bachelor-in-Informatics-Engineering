
-- | Relatório da Tarefa 3
-- | Introdução
{-   Nesta tarefa, tivemos como objetivo criar um mecanismo de compressão/descompressão que permitisse traduzir um 'Estado' numa 'String' que fosse composta por um menor número de caratéres, poupando assim espaço em disco.
   A função 'comprime' seria responsável por transformar um Estado numa String, e a função 'descomprime' faria a operação inversa, garantindo que um Estado, após ser comprimido e descomprimido, fosse igual à sua forma original.
-}
-- | Objetivos
{-   Para abordar o desafio proposto, consideramos, em primeiro lugar, a divisão de um 'Estado' nos seus componentes: um 'Mapa', uma lista de jogadores, e uma lista de disparos.
     
     Do mapa, obtemos a sua dimensão (x,y) (atraveś da função dimensaoMatriz). Se o número de algarismos de x, somado com o número de algarismos de y, for um valor par, então estes resultarão num string tipo "xy". 
  (Por exemplo (12,33) seria o string "1233".) Se, por outro lado, a soma do número de algarismos for um número impar, será incluído o caratér '.' entre x e y, para permitir uma separação. (Assim, uma dimensão de valor 
  (23,3) resultaria num string "23.3".) A função responsável por este tipo de conversão, parTochar, será aplicada a todos os valores do tipo (x,y) que ocorram ao longo do estado.
     Após ser incluída a dimensão do mapa no string, é inserido o separador '*' e uma lista de valores entre 0 e 2. Estes valores correspondem ao tipo de peça existente no mapa quando este é convertido numa lista, 
  sendo 0 um espaço Vazio, 1 um Bloco Indestrutível, e 2 um Bloco Destrutível.
     
     Por seu lado, cada jogador teria os seus dados (à exceção da sua posição, a que seria aplicada a função parTochar) convertidos nos caratéres seus equivalentes, e seria precedido do separador 'J'. 
  O número de lasers seria também precedido pelo separador '/' e o número de choques por '#'. Assim, um jogador descrito como (Jogador (12,9) D 1 2 3) resultaria na string "J12.9D1/2#3".

     Por fim, cada disparo seria precedido por uma letra diferente consoante o seu tipo (A- DisparoCanhao, L-DisparoLaser, H-DisparoChoque), e, pela mesma lógica que a compressão dos jogadores, cada um dos seus dados será
 convertido no seu caratér equivalente (à exceção da sua posição, comprimida pela função parTochar), sendo que, após o número identificador do jogador que originou o disparo, é inserido o separador '-'.
 Deste modo, um disparo tipo (DisparoLaser 2 (4,4) C) resultaria na string "L2-44C".

     Após o Estado completo ter ser traduzido numa String, é aplicada uma função extra com o objetivo de reduzir ainda mais o número de caratéres utilizados. Esta função será a função rep, que encontra secções da string em que
 um mesmo caratér se repita entre 3 a 26 vezes seguidas e substitui essas secções por uma letra minúscula (visto que várias letras maiusculas já foram usadas como identificadores/separadores) seguida do caratér que substituiu. 
 Apesar de esta função ser aplicada a toda a String, esta terá maior probabilidade de vir a ser relevante na parte relativa ao Mapa. Por exemplo, um Mapa vazio de dimensões (4,4) resultaria na string "44*1111100110011111", mas,
 ao ser comprimida pela função rep, esta string pode ser encurtada para "44*e1001100e1".

    Para descomprimir a String obtida e reaver o Estado, será primeiro aplicada a função unrep, que encontra as letras minúsculas e identifica o caratér à sua frente, substituindo ambos pelo mesmo caratér repetido o número de vezes
 necessárias (sendo este número obtido através das funções chr e ord). Seguidamente, são identificados cada um dos separadores mencionados anteriormente através de funções como takeWhile e dropWhile, permitindo assim ler os caratéres
 entre eles, e traduzi-los corretamente como Ints, Direções, Blocos, etc. Se os caratéres a descomprimir corresponderem a uma posição ou dimensão, será utilizada a lógica inversa da função parTochar: confirma-se se o número de caratéres
 é par, e, em caso afirmativo, divide-se o string em dois ("3425" passa a (34,25)), em caso negativo, faz-se a divisão a partir do separador '.'.
-}
-- | Discussão e Conclusão
{-  Através da implementação das estratégias descritas, podemos obter, em muito menos caratéres, Strings correspondentes a Estados complexos. A compressão de Estados revelou-se particularmente eficaz em casos em que existam Estados com
  Mapas de grandes dimensões (e, consequentemente, maiores espaços vazios), devido à implementação das funções auxiliares rep/unrep, que poupam um grande número de caratéres. Por outro lado, ao recorrer a múltiplos separadores, evitou-se
  uma compressão tão completa de Estados mais simples, mas se estes separadores fossem removidos, seriamos obrigados a limitar o tipo de Estados que podem ser corretamente comprimidos e descomprimidos.
-}


-- | Este módulo define funções comuns da Tarefa 3 do trabalho prático.
module Tarefa3_2018li1g145 where

import LI11819
import Data.List
import Data.Char
import Tarefa0_2018li1g145
import Tarefa1_2018li1g145

-- * Testes

-- | Testes unitários da Tarefa 3.
--
-- Cada teste é um 'Estado'.
testesT3 :: [Estado]
testesT3 = [(Estado [[Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia],
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel],
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Bloco Destrutivel,Vazia]]
                          [(Jogador (1,1) C 1 1 1),(Jogador (2,3) E 2 0 3)] 
                          [(DisparoCanhao 2 (2,2) E),(DisparoChoque 1 3)] ),
            (Estado [[Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia],
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel],
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Bloco Indestrutivel, Vazia,Vazia,Vazia,Vazia,Vazia, Bloco Indestrutivel], 
                     [Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Bloco Destrutivel,Vazia]]
                          [] []),
            (Estado (mapaInicial (60,6)) [(Jogador (23,2) B 1 0 0), (Jogador (42,5) E 2 3 4), (Jogador (9,3) E 0 2 0)] []),
            (Estado (mapaInicial (109,42)) [(Jogador (96,12) E 1 2 2)] [(DisparoCanhao 11 (55,55) D),(DisparoLaser 100 (129,12) B)]),
            (Estado (mapaInicial (52,25)) [(Jogador (9,16) C 9 4 3)] [(DisparoChoque 2 5),(DisparoLaser 4 (22,9) C),(DisparoChoque 3 5)]),
            (Estado (mapaInicial (30,30)) [(Jogador (9,16) C 9 4 3)] [(DisparoCanhao 1 (12,6) C),(DisparoChoque 2 4),(DisparoLaser 3 (5,5) E)]),
            (Estado (mapaInicial (30,30)) [] [(DisparoCanhao 1 (12,6) C),(DisparoChoque 2 4),(DisparoLaser 3 (5,5) E)]),
            (Estado (mapaInicial (20,20)) [(Jogador (9,16) C 9 4 3)] 
                     [(DisparoCanhao 1 (12,6) C),(DisparoCanhao 4 (6,7) B),(DisparoChoque 2 44),(DisparoLaser 3 (5,5) E),(DisparoCanhao 6 (8,9) D),(DisparoChoque 3 5),(DisparoLaser 9 (11,1) B),(DisparoCanhao 8 (14,11) D)]),
            (Estado [] [(Jogador (1,1) C 1 1 1),(Jogador (2,3) E 2 0 3)] [(DisparoCanhao 2 (2,2) E),(DisparoChoque 1 113)] ),
            (Estado (mapaInicial (28,29)) [(Jogador (9,16) C 93 14 322)] [(DisparoChoque 2 15)] ) ]

-- * Funções principais da Tarefa 3.

-- | Comprime um 'Estado' para formato textual.
--
-- __NB:__ A função 'show' representa um 'Estado' num formato textual facilmente legível mas extenso.
--
-- __NB:__ Uma boa solução deve representar o 'Estado' dado no mínimo número de caracteres possível.
comprime :: Estado -> String
comprime (Estado m js ds) =  rep ( (parTochar (dimensaoMatriz m)) ++ '*':(comprimeMapa (concat m)) ++ (comprimepl js) ++ (comprimedp ds) )

-- | Transforma os elementos repetidos de uma String numa letra, que repesenta o nº de repetiçoes do caracter, e o caracter repetido
rep :: String -> String
rep [] = []
rep (x:xs) | length (takeWhile (==x) (x:xs)) <= 2 = x: rep xs
           | length (takeWhile (==x) (x:xs)) > 26 = 'z':x: rep (drop 26 (x:xs))
           | otherwise = chr (length (takeWhile (==x) (x:xs)) + 96) : x : rep (dropWhile (==x) (x:xs))

-- | Comprime um par a uma String
parTochar :: (Int,Int) -> String
parTochar (0,0) = []
parTochar (a,b) | odd (length ((show a) ++ (show b))) = (show a) ++ "." ++ (show b)
                | otherwise = (show a) ++ (show b)

-- | Comprime um mapa a uma String 
comprimeMapa :: [Peca] -> String
comprimeMapa [] = []
comprimeMapa (x:xs) | x==(Vazia)               = '0':(comprimeMapa xs)
                    | x==(Bloco Indestrutivel) = '1':(comprimeMapa xs)
                    | x==(Bloco Destrutivel)   = '2':(comprimeMapa xs)

-- | Comprime uma lista de Jogadores a uma String
comprimepl :: [Jogador] -> String
comprimepl [] = []
comprimepl ((Jogador (x,y) d a b c):xs) = "J" ++ parTochar (x,y) ++(show d)++(show a)++"/"++(show b)++"#"++(show c)++(comprimepl xs)

-- | Comprime uma lista de Disparos a uma String
comprimedp :: [Disparo] -> String
comprimedp [] = []
comprimedp ((DisparoCanhao j (x,y) d):xs) = "A" ++ (show j) ++ "-" ++ parTochar (x,y) ++ (show d) ++ (comprimedp xs)
comprimedp ((DisparoLaser j (x,y) d):xs)  = "L" ++ (show j) ++ "-" ++ parTochar (x,y) ++ (show d) ++ (comprimedp xs)
comprimedp ((DisparoChoque j t):xs) = "H" ++ (show j) ++ "-" ++ (show t) ++ (comprimedp xs)


-- | Descomprime um 'Estado' no formato textual utilizado pela função 'comprime'.
--
-- __NB:__ A função 'comprime' é válida de for possível recuperar o 'Estado' utilizando a função 'descomprime', i.e.:
--
-- prop> descomprime . comprime = id
--
-- __NB:__ Esta propriedade é particularmente válida para a solução pré-definida:
--
-- prop> read . show = id
descomprime :: String -> Estado
descomprime x | (dropWhile (testeAHL) (unrep x)) /= [] && elem 'J' x            = (Estado (descomprimeMapa (takeWhile (/='J') (unrep x))) (descomprimepl (dropWhile (/= 'J')(takeWhile (testeAHL) (unrep x)))) (descomprimedp (dropWhile (testeAHL) (unrep x))))
               -- ^ existem disparos e jogadores
              | (dropWhile (testeAHL) (unrep x)) == [] && elem 'J' x            = (Estado (descomprimeMapa (takeWhile (/='J') (unrep x))) (descomprimepl (dropWhile (/= 'J') (unrep x))) [])
               -- ^ não existem disparos mas existem jogadores
              | (dropWhile (testeAHL) (unrep x)) /= [] && (elem 'J' x == False) = (Estado (descomprimeMapa (takeWhile (testeAHL) (unrep x))) [] (descomprimedp (dropWhile (testeAHL) (unrep x))))
               -- ^ existem disparos mas não existem jogadores
              | otherwise                                                       = (Estado (descomprimeMapa (unrep x)) [] [])
               -- ^ não existem disparos nem jogadores

-- | Descomprime as representaçoes dos elementos repetidos para os elementos em si
unrep :: String -> String
unrep l | (dropWhile (testeRep) l ) /= [] = unrep ( (takeWhile (testeRep) l) ++ replicate ((ord (head (dropWhile  (testeRep) l)))-96) ((dropWhile  (testeRep) l) !! 1) ++ drop 2 (dropWhile  (testeRep) l) )
        | otherwise = l
-- | Testa se um caracter corresponde a uma letra do abecedario
testeRep :: Char -> Bool
testeRep x = (x/='a') && (x/='b') && (x/='c') && (x/='d') && (x/='e') && (x/='f') && (x/='g') && (x/='h') && (x/='i') && (x/='j') && (x/='k') && (x/='l') && (x/='m') && (x/='n') && (x/='o') && (x/='p') && (x/='q') && (x/='r') && (x/='s') && (x/='t') && (x/='u') && (x/='v') && (x/='w') && (x/='x') && (x/='y') && (x/='z') 


-- | Descomprime uma String válida no seu respetivo par ordenado
descomprimepar :: String -> (Int,Int)
descomprimepar l | elem '.' l = ( (read (takeWhile (/='.') l) :: Int), (read (tail (dropWhile (/='.') l)) :: Int) )
                 | otherwise = ( (read (half l)::Int), (read (otherhalf l)::Int) )

-- | Descomprime o primero elemento de um par 
half :: String -> String
half l = take (div (length l) 2) l
-- | Descomprime o segundo elemento de um par 
otherhalf :: String -> String
otherhalf l = drop (div (length l) 2) l


-- * Funçoes auxiliares para descomprimir mapas

-- | Descomprime uma String válida para um mapa
descomprimeMapa :: String -> Mapa
descomprimeMapa [] = [] 
descomprimeMapa l = interpretamapa (descomprimepar (takeWhile (/='*') l)) (tail (dropWhile (/='*') l))

-- | Intrepa as dimensoes do mapa para posteriormente o descomprimir
interpretamapa :: Dimensao -> String -> Mapa
interpretamapa _ [] = []
interpretamapa (a,b) x = (aux (take b x)) : (interpretamapa (a,b) (drop b x))
-- | Funcao auxiliar de 'intrepetamapa' que cria uma linha do mapa
aux :: String -> [Peca]
aux [] = []
aux (x:xs) | x=='0' = (Vazia):(aux xs)
           | x=='1' = (Bloco Indestrutivel):(aux xs)
           | x=='2' = (Bloco Destrutivel):(aux xs)


-- * Funçoes auxiliares para descomprimir jogadores

-- | Descomprime os jogadores
descomprimepl :: String -> [Jogador] 
descomprimepl []  = []
descomprimepl [x] = [] -- o primeriro elemento sera o carater 'J', que retiramos
descomprimepl (x:xs) = separapl xs

-- | Separa a String em partes que correspondem a cada um dos Jogadores
separapl :: String -> [Jogador]
separapl [] = []
separapl l | elem 'J' l = interpretapl (takeWhile (/='J') l) : separapl (tail (dropWhile (/='J') l))
           | otherwise = [interpretapl l]

-- | Descomprime uma String válida no seu respetivo Jogador 
interpretapl :: String -> Jogador
interpretapl l = (Jogador (descomprimepar (takeWhile (testeDirecoes) l)) (read [head (dropWhile (testeDirecoes) l)]::Direcao) (read (takeWhile (/='/') (tail (dropWhile (testeDirecoes) l)))::Int) (read (takeWhile (/='#')(tail (dropWhile (/='/') l)))::Int) (read (tail (dropWhile (/='#') l))::Int) )
-- | Testa se um caracter corresponde a uma 'Direcao'
testeDirecoes :: Char -> Bool 
testeDirecoes x = (x/='C') && (x/='D') && (x/='B') && (x/='E')

-- * Funçoes auxiliares para descomprimir os disparos

-- | Descomprime os disparos
descomprimedp :: String -> [Disparo]
descomprimedp [] = []
descomprimedp (x:xs) | x=='H' = interpretadpch (takeWhile (testeAHL) xs) : descomprimedp (dropWhile (testeAHL) xs)
                     | x=='A' || x=='L'  = interpretadp (x:(takeWhile (testeAHL) xs)) : descomprimedp (dropWhile (testeAHL) xs)                                     
-- | Testa se um caracter corresponde a um 'A','H' ou 'L'
testeAHL :: Char -> Bool
testeAHL x = (x/='A') && (x/='H') && (x/='L')

-- | Descomprime disparos do tipo Choque
interpretadpch :: String -> Disparo
interpretadpch l = (DisparoChoque (read (takeWhile (/='-') l) :: Int) (read (tail (dropWhile (/='-') l)) :: Int) )

-- | Descomprime disparos do tipo Canhao e do tipo Laser
interpretadp :: String -> Disparo
interpretadp (x:xs) | x=='A' = (DisparoCanhao (read (takeWhile (/='-') xs)::Int) (descomprimepar (init (tail (dropWhile (/='-') xs)))) (read [last xs]::Direcao) )
                    | x=='L' = (DisparoLaser  (read (takeWhile (/='-') xs)::Int) (descomprimepar (init (tail (dropWhile (/='-') xs)))) (read [last xs]::Direcao) )
