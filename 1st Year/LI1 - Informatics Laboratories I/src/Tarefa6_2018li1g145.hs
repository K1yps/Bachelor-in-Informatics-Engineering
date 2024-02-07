-- * Relatorio Tarefa 6

-- ** Introdução

{- Na Tarefa 6 o objetivo é criar um bot, ou seja, criar um progama capaz de jogar o tanks por si mesmo sem precisar de qualquer interação humana.
   Desta forma a função 'bot', dado um estado de um jogo e um indice de um dos jogadores, faz uma jogada para esse mesmo jogador.   
-}

-- ** Descrição

{- Desta forma, como inicio do trabalho, começamos por separar o Estado nos seus componentes (mapa , lista de jogadores e lista de disparos) e consecutivamente 
separamos o jogador correspondente ao bot, que assumimos como sendo a variavel 'owo'. Com isto podemos assim ter acesso a várias informações essencias contidas 
no jogador representado pelo bot tornam-se mais facil a gestão do mesmo.
-}

{- Posteriormente na função 'actbotp1', a partir da informação anterior será testado a presença de uma série de padrões (cada um representado por várias funções diferentes)
no estado atual do jogo e, dependendo dos padrões existentes, o bot realizará uma jogada.
-}

{- O primeiro padrão existente ocorre quando não existe nenhum jogador para além do bot. Caso isto aconteça o bot não realiza qualquer jogada.
-}

{- O segundo padrão faz recurso a três funções, as funções 'pldir', 'distblocind' e 'lasdesativo'. A função 'distblocind' dada o Mapa do Estado, a Direçao e Posicão do bot,
retorna um inteiro que corresponde á distancia entre o bot e o bloco indestrutivel mais proximo na direção em que esta virado. Com esta informação a função 'pldir' verifica se existe um jogador à frente do jogador e atrás destes blocos indestrutiveis.
Caso isto aconteça, se o bot tiver munição e se este nao tenha acabado de disparar um laser (padrao que é verificado pela funcao 'lasdesativo') , este Dispara um Laser.
-}

{- O terceiro padrão, através das funções 'plnaregiao' e 'choqdesativo', verifica respetivamente se existe um jogador numa area de 25 blocos a partir do bot e se o bot não tem nenhum choque ativo.
Caso ambos se verifiquem e se o bot tiver munição, este dispara um Choque.
-}

{- O terceiro padrão verifica, através da função 'frontblocdest', se o jogador tem blocos destrutiveis nas quatro posicões a sua frente. Caso isto aconteça,
 o bot dispara o seu canhão para abrir caminho no mapa para poder se movimentar. De outo modo, o quarto padrão verifica se tem um jogador presence em uma zona em formato de cone á sua frente através da função 'plproximo'. Ora esta função recebe informações do
 jogador correspondente ao bot, recebe informações do estado atual do jogo e recebe a altura do cone, que é calculada pela função 'distblocmulti' que, de uma maneira semelhante à função 'distblocind', calcula a distancia entre o bot e o próximo bloco.
 Com estas informações, a função verifica se algum destes players se situa neste cone, cujas dimensoes são calculadas pela função auxiliar 'auxplprox'. Com base nisto, se algum player estiver nesta zona e se nenhuma dos padrões anteriores se verificar, o bot dispara o seu canhão.
-}

{- Ora se nenhum destes padrões de verificar, através da função 'foward', o bot move na direção em que está virado. Ora se esta direção tiver o caminho obstruido, este vira de direção e tentar outra vez seguir em frente. Assim, o bot tem um meio de locumoção eficaz e simples. Porém também 
faz os seus movimentos bastante previsiveis. Com isto em mente, criamos a função ('padraodecisao') que altera este padrão de movimentos conforme a proximidade dos players ao bot para minimizar este fator.
-}

-- ** Discussão e Conclusão

{- Assim, ao verificarmos a existência destes padrões, criamos um progama capaz de tomar decisões e consecutivamente criamos um bot capaz de jogar o jogo Tanks. Ora isto é bastante útil visto que como
este jogo implica vários jogadores, deixa de ser necessário existir pelo menos quatro seres humanos para o jogo poder ser jogado e assim, permite também que jogadores novos aprendam as mecânicas do jogo
antes de jogarem com jogadores experientes.
-}


-- | Este módulo define funções comuns da Tarefa 6 do trabalho prático.
module Tarefa6_2018li1g145 where

import Data.List
import Tarefa4_2018li1g145
import Tarefa1_2018li1g145
import LI11819

-- * Funções principais da Tarefa 6.

-- | Define um ro'bot' capaz de jogar autonomamente o jogo.
bot :: Int          -- ^ O identificador do 'Jogador' associado ao ro'bot'.
    -> Estado       -- ^ O 'Estado' para o qual o ro'bot' deve tomar uma decisão.
    -> Maybe Jogada -- ^ Uma possível 'Jogada' a efetuar pelo ro'bot'.
bot n (Estado m pl dp) = actbotp1 n owo (Estado m (delete owo pl) dp)
                        where owo=(pl!!n)

-- | Funcao que Retorna uma jogada conforme uma serie de padroes estabelecidos pela suas funçao auxiliares
actbotp1 :: Int -> Jogador -> Estado -> Maybe Jogada
actbotp1 _ _ (Estado m [] dp) = Nothing
actbotp1 n (Jogador pos d v las chq) (Estado m pl dp) |(las > 0) && (pldir h d pl pos) && (lasdesativo n dp)   = Just (Dispara Laser )
                                                      |(chq > 0) && (choqdesativo n dp) && (plnaregiao pos pl) = Just (Dispara Choque)
                                                      |(plproximo j d pl pos) || (frontblocodest m pos d)      = Just (Dispara Canhao)
                                                      |otherwise = Just (forward m d pos pl)
                                                       where
                                                        h = distblocind m pos d
                                                        j = distblocmulti m pos d



-- * Funcoes para disparar um laser
-- | Verifica se existe um jogador exatamente a frente da posicao dada
pldir :: Int -> Direcao -> [Jogador] -> Posicao -> Bool 
pldir _ _ [] _ = False
pldir dist d ((Jogador (a,b) _ v _ _):xs) (x,y) |d==C = (a<x && (x-a<=dist) && v>0 && ((b==y) || (b-1==y) || (b+1==y))) || (pldir dist d xs (x,y))
                                                |d==B = (a>x && (a-x<=dist) && v>0 && ((b==y) || (b-1==y) || (b+1==y))) || (pldir dist d xs (x,y))
                                                |d==E = (b<y && (y-b<=dist) && v>0 && ((a==x) || (a-1==x) || (a+1==x))) || (pldir dist d xs (x,y))
                                                |d==D = (b>y && (b-y<=dist) && v>0 && ((a==x) || (a-1==x) || (a+1==x))) || (pldir dist d xs (x,y))


-- | Verifica se o jogador nao disparou um laser na jogada anterior
lasdesativo :: Int -> [Disparo] -> Bool
lasdesativo _ [] = True
lasdesativo n (x:xs) = case x of
                   (DisparoLaser a b c) -> (a/=n) && lasdesativo n xs
                   (DisparoCanhao {})   -> lasdesativo n xs
                   (DisparoChoque {})   -> lasdesativo n xs


-- * Funcoes para disparar um choquee
-- | Dado uma posiçao verifica se existe um jogador proximo dessa coordenada
plnaregiao :: Posicao -> [Jogador] -> Bool
plnaregiao _ [] = False
plnaregiao (x,y) ((Jogador (a,b) _ v _ _):xs) = (v>0 && ((x+5)>a) && ((x-5)<a) && ((y+5)>b) && ((y-5)<b) ) || (plnaregiao (x,y) xs) 

-- | Dado um indice de um jogador verifica se nao tem choques ativos
choqdesativo :: Int -> [Disparo] -> Bool
choqdesativo _ [] = True
choqdesativo n (x:xs) = case x of
                   (DisparoLaser {})     -> choqdesativo n xs
                   (DisparoCanhao {})    -> choqdesativo n xs
                   (DisparoChoque a t)   -> (a/=n) && choqdesativo n xs


-- * Funcoes para Disparar um Canhao

-- | Verifica se exite um Jogador num cone em frente ao bot
plproximo :: Int -> Direcao -> [Jogador] -> Posicao -> Bool 
plproximo _ _ [] _ = False
plproximo dist d ((Jogador (a,b) _ v _ _):xs) (x,y) |d==C = (a<x && (x-a<=dist) && (b-t<=y) && (b+t>=y) && v>0) || plproximo dist d xs (x,y)
                                                    |d==B = (a>x && (a-x<=dist) && (b-t<=y) && (b+t>=y) && v>0) || plproximo dist d xs (x,y)
                                                    |d==E = (b<y && (y-b<=dist) && (a-u<=x) && (a+u>=x) && v>0) || plproximo dist d xs (x,y)
                                                    |d==D = (b>y && (b-y<=dist) && (a-u<=x) && (a+u>=x) && v>0) || plproximo dist d xs (x,y)
                                                    where
                                                       t = auxplprox a x
                                                       u = auxplprox b y
-- | Funcao auxiliar da duncao plproximo que define o formato do cone em que é verificado a existencia de um jogador
auxplprox :: Int -> Int -> Int
auxplprox a b = if m<1 then 0 else if m<=3 then 1 else if m<6 then 2 else 3
               where m = (abs (b-a)) 
 

-- | Verifica se existem blocos destrutiveis a frente ou 1 bloco a frente do bot
frontblocodest :: Mapa -> Posicao -> Direcao -> Bool
frontblocodest m pos d = if (t == Bloco Destrutivel) || (u == Bloco Destrutivel) then True else (t /= Bloco Indestrutivel) && (u /= Bloco Indestrutivel) && ((v == Bloco Destrutivel) || (w == Bloco Destrutivel))
                          where
                             (t,u) = tipobloco m (avancagrelha pos d) d
                             (v,w) = tipobloco m (avancagrelha (avancagrelha pos d) d) d

-- * Funcoes para Mover o bot
-- | Move o player para a sua proxima posicao
forward :: Mapa -> Direcao -> Posicao -> [Jogador] -> Jogada
forward m d pos pl | (a == Vazia) && (b == Vazia) = Movimenta d
                   | otherwise =  Movimenta (padraodecisao d pos pl)
                   where
                      (a,b) = tipobloco m (avancagrelha pos d) d

padraodecisao :: Direcao -> Posicao -> [Jogador] -> Direcao
padraodecisao d pos pl = if (plnaregiao pos pl) then (next d [C,E,B,D,C]) else (next d [C,D,B,E,C])


-- * Funcoes Gerais

-- | Retorna a menor distancia em que o proximo Bloco Indestrutivel se encontra
distblocind :: Mapa -> Posicao -> Direcao -> Int
distblocind m pos d = if t==(Bloco Indestrutivel) || u==(Bloco Indestrutivel) then 0 else 1+(distblocind m (avancagrelha pos d) d)
                    where (t,u) = tipobloco m pos d


-- | Retorna a menor distancia em que ao proximo Bloco se encontra
distblocmulti :: Mapa -> Posicao -> Direcao -> Int
distblocmulti m pos d = if t==(Bloco Indestrutivel) || u==(Bloco Indestrutivel) || t==(Bloco Destrutivel) || u==(Bloco Destrutivel) 
                         then 0 else 1+(distblocmulti m (avancagrelha pos d) d)
                         where (t,u) = tipobloco m pos d
