-- | Relatório da Tarefa 5
-- | Introdução
{-   Nesta tarefa, tivemos como objetivo implementar o jogo completo Tanks, com recurso ao Gloss e às tarefas previamente concebidas. Como tal, para além de permitir a
   visualização gráfica do jogo em si, esta tarefa irá permitir também a seleção de quatro mapas diferentes para jogar, a criação de mapas costumizados (recorrendo à tarefa 2)
   e a possibilidade de jogar contra bots (recurrendo à tarefa 6). Para além disso incluirá um ecrã inicial, um menu de fim de jogo, um menu de pausa, descrição dos dados dos 
   jogadores em jogo e um relógio funcional.
-}
-- | Objetivos
{-   A funçao principal da tarefa 5 será a função main, que recebe os sprites (em bitmap) relacionados com o jogo (os tanques dos players, os disparos, e o nome do jogo que aparece no ecrã
   inicial) e os carrega como Pictures, define a janela de jogo, o framerate (4) e a cor de fundo (preto) e, recorrendo a várias outras funções, permite jogar o jogo. As funções principais da 
   função main são, portanto: estadoInicial, desenhaEstado, reageEvento e reageTempo. Todas estas funções atuam sobre o type EstadoGloss, que inclui várias informações necessárias ao longo 
   do jogo, dependedo do menu ou ecrã em que estamos.

     O EstadoGloss será composto por vários elementos: 
   -- o MenuState - um Int que varia consoante as opções que selecionamos no jogo, sendo que uma sequência de MenuStates pode pertencer a um mesmo menu (por exemplo, os MenuStates 2,3,4 e 5
      pertencem todos ao menu de seleção de mapas, mas cada número corresponde à seleção de um mapa diferente - quando estamos em 2, o mapa 1 está realçado a amarelo, em 3 realçamos o mapa 2, 
      etc). Os MenuStates permitem saber como é que as funções reageTempo e reageEvento devem atuar, dependedo da fase do jogo.
   -- o Clock - ou seja, o relógio do jogo. A sua a informação só é visivel durante o jogo em si, pode ser posto em pausa e pode sofrer um reset quando se muda de menu ou de jogo.
   -- o Estado - o estado do jogo, como está definido em LI11819.
   -- a String - corresponde a uma versão comprimida do Estado inicial do jogo (através da função comprime da tarefa 3), que é necessária sempre que se pretende fazer um reset do jogo (mantendo
      o mesmo mapa, posição inicial dos jogadores, e número de bots).
   -- as Instrucoes - relevantes apenas quando estamos a construir um novo mapa, segundo as funções definidas na tarefa 1.
   -- os Bots - outro Int, este indica o número de bots que devem existir no jogo.
   -- [Picture] - a lista de pictures correspondentes aos bmps da função main, das quais o jogo vai necessitar.

     A função estadoInicial irá definir o EstadoGloss inicial do jogo (mal o iniciamos). Este será portanto composto pelo MenuState 1, um relógio a zeros, um Estado composto apenas por listas vazias,
   uma String e Instrucoes também vazias, 0 bots e uma lista de pictures que será correspondente aos bitmaps listados na função main - os 4 sprites dos jogadores, os 3 tipos de disparos, e o titulo
   do jogo. Daqui, poderemos alterar o EstadoGloss dados determinados eventos (reageEvento) ou com a passagem do tempo (reageTempo).

     A função reageTempo atuará de maneira diferente dependendo do MenuState do EstadoGloss que recebe. 
     Se o MS for 1 ou 0, a função alternará entre os dois (permitindo, em desenhaEstado, que o texto
   "[ x ] para jogar" mude de cor autonomamente). 
     Se o MS for 6, o que corresponde ao jogo em si, a função aplicará a função auxiliar reageEstado ao MS, permitindo que, caso exista um ou menos jogadores
   vivos em jogo, o MS mude automaticamente para 8 - correspondente ao menu de fim de jogo. Ao mesmo tempo, o relógio será avançado 0.25 segundos de cada vez (visto que reageTempo atua a cada frame que passa, 
   e o framerate definido é 4 - 4 frames por segundo), e atuarão duas funções sobre o Estado do jogo - tick (da tarefa 4, que afeta os disparos) e jogabot (que controlará o(s) bot(s), se existirem, por 
   recurso à função bot da tarefa 6). 
     Se o MS for 99 (que corresponde a um ecrã de transição, necessário para os resets), o MS passará a 6, o relógio volta a estar a zeros, e o Estado será substituido pela
   versão descomprimida da String guardada no EstadoGloss (utilizando a função descomprime da tarefa 3). 
     Qualquer outro MS será relativo a uma parte do jogo que não é afetada pela passagem do tempo, logo o
   EstadoGloss permanece inalterado.

     A função reageEvento aplica sempre a auxiliar reageEvMenu ao MenuState do EstadoGloss que recebe, função essa que permitirá, dependendo do MS que recebe e da tecla que é pressionada, avançar e retroceder 
   entre MenuStates - por exemplo, permite selecionar e/ou confirmar uma opção selecionada num menu, mudando assim de menu ou alterando o aspeto gráfico do jogo (dependendo isto também da função desenhaEstado).
   No entanto, dados certos MenuStates, poderão ser feitas mais alterações ao EG.
     Se o MS estiver entre 2 e 5, este corresponderá ao menu de seleção de mapas. Como este é o primeiro menu que o jogador irá encontrar, e como qualquer reversão a este menu força o apagamento dos dados do EstadoGloss 
   anterior, o Clock, as Intrucoes e os Bots passarão a ser os mesmos que aqueles definidos na função estadoInicial. O Estado do jogo será escolhido dependendo do MS, a partir da lista de Estados definida em estados.
     Se o MS estiver entre 16 e 20, este corresponderá ao menu de seleção de bots. O Clock e as Instrucoes vão passar a ter os mesmos valores que em estadoInicial, e o Estado será substituido pela versão descomprimida do 
   seu String (causando uma espécie de reset, necessário para quando se retrocede para o menu de seleção de bots sem passar pelo menu de seleção de mapas). O número de bots (Bots) será alterado de acordo com o MS.
     Se o MS for 15, este corresponderá ao ecrã de criação de mapas. As Instrucoes do EstadoGloss serão alteradas pela função auxiliar reageEvInstrucoes, que acresecentará intruções à lista dependendo das teclas que são
   pressionadas. O Estado será criado pela função constroi (da tarefa1) aplicada às Instrucoes do EG, e pela auxiliar criaEst, que cria um estado (a partir do mapa gerado pela função constroi) com um jogador em cada canto
   e sem disparos. O String será o mesmo Estado, comprimido pela função comprime (da tarefa3).
     Por último, se o MS for 6, este corresponderá ao jogo propriamente dito, logo o Estado será alterado pela auxiliar reageEv, que define quais dos jogadores serão controláveis (dependendo do número de bots) e, através da
   da função reageEvPl, permite que estes façam jogadas (tarefa 2) dependendo das teclas que são pressionadas.

     A função desenhaEstado será aquela que traduzirá cada EstadoGloss numa visualização gráfica, recorrendo portanto a várias funções externas que definem diferentes pictures, correspondentes quer a elementos estáticos do jogo
   (texto com instruções ou nomes de menus, grelhas, etc), semi-estáticos (o relógio, os dados dos jogadores) ou dinâmicos (a representação gráfica do Estado do jogo). Dependendo do MenuState do EG que a função recebe, esta irá
   representar elementos diferentes (recorrendo portanto a funções diferentes).
     À exceção das funções dependentes das imagens dos tanques dos jogadores, dos disparos, e do titulo inicial do jogo, todas as funções que geram pictures para desenhaEstado funcionam quase exclusivamente à base de funções 
   pré-definidas do Gloss. Isto é intencional, em parte para simplificar a conceção do código, mas também para dar um aspeto simples mas vibrante ao jogo.
-}
-- | Discussão e Conclusão
{-   Através da implementação destas funções conseguimos criar um jogo funcional com vários menus e opções que permitem a criação de jogos de Tanks complexos e variados. Incluimos instruções em todos os menus para facilitar o seu uso,
   e o framerate permite uma boa jogabilidade. Ao apostar num formato gráfico mais simples demos um aspeto mais 'retro' ao jogo, mas estéticamente agradável. O único defeito que pudemos apontar seria a limitação do tamanho dos mapas, 
   não pelas funções integradas não permitirem criar mapas maiores, mas por estes simplesmente não caberem no ecrã.
-}


-- | Este módulo define funções comuns da Tarefa 5 do trabalho prático.

-- module Tarefa5_2018li1g145 where

module Main where

import LI11819
import Data.List
import Data.Maybe
import Graphics.Gloss
import Graphics.Gloss.Interface.Pure.Game
import Tarefa0_2018li1g145
import Tarefa1_2018li1g145
import Tarefa2_2018li1g145
import Tarefa3_2018li1g145
import Tarefa4_2018li1g145
import Tarefa6_2018li1g145

--
-- __NB:__ Esta Tarefa é completamente livre. Deve utilizar a biblioteca <http://hackage.haskell.org/package/gloss gloss> para animar o jogo, e reutilizar __de forma completa__ as funções das tarefas anteriores.

--
-- * Funções necessárias para desenhar um Estado no ecrã de jogo
-- | Desenha um Estado completo
desenhaEst :: Estado -> [Picture] -> Picture
desenhaEst (Estado m pl dp) ps = pictures (desenhamp (blue) m ++ (desenhapl pl (take 4 ps) 0) ++ ( desenhadp dp pl m (drop 4 ps) ))

--
-- | Desenha o mapa
desenhamp :: Color -> Mapa -> [Picture] 
desenhamp c (xs:ys) = ( trx ((-200),200) $ (showPeca c xs) ) ++ try c ((-200),200) (xs:ys)

-- | Recebe uma lista de peças e transforma-as em pictures, dependendo do tipo de peça
showPeca :: Color -> [Peca] -> [Picture]
showPeca _ [] = []
showPeca c (x:xs) | x == Bloco Indestrutivel = pin : showPeca c xs
                  | x == Bloco Destrutivel   = pde : showPeca c xs
                  | otherwise                = (color c $ rectangleWire 30 30) : showPeca c xs
                  where
                   pde = pictures [color (greyN 0.80) $ rectangleSolid 30 30, rectangleWire 30 30]
                   pin = pictures [color blue $ rectangleSolid 30 30, rectangleWire 30 30]

-- | Cria uma linha do mapa
trx :: (Float,Float) -> [Picture] -> [Picture]
trx (x,y) [] = []
trx (x,y) (l:ls) = [ translate x y $ l ] ++ trx (x+30,y) ls

-- | Quando a linha do mapa está completa, passa a desenhar a próxima linha
try :: Color -> (Float,Float) -> Mapa -> [Picture]
try c (x,y) (xs:ys) | length (concat ys) >= length xs = ( trx (x,y-30) $ showPeca c (take (length xs) (concat ys)) ) ++ try c (x,y-30) (xs : tail ys)
                    | otherwise = []

--
-- | Desenha um editor (tarefa1) antes de ocorrer a instrucao 'Desenha' 
--  (Necessário apenas para o ecrã de construção de mapas.)
desenhaed :: Editor -> [Picture]
desenhaed (Editor (x,y) d tetr tipo m) = [translate (fromIntegral(y*30)) (fromIntegral(-x*30)) $ pictures $ picTetr tetr tipo d]

-- | Transforma um Tetromino na sua Picture correspondente
picTetr :: Tetromino -> Parede -> Direcao -> [Picture]
picTetr a p d = desenhamp (light $ azure) (rodar d (pecasm (tetrominoParaMatriz a) p) )
                 where
                   rodar m x | m==C = x
                             | m==D = rodaMatriz x
                             | m==B = rodaMatriz (rodar D x)
                             | m==E = rodaMatriz (rodar B x)

-- | Transforma uma Matriz Bool numa Matriz Peca
pecasm :: Matriz Bool -> Parede -> Matriz Peca
pecasm [] _ = []
pecasm (x:xs) p = (pecas x p) : pecasm xs p
                where
                  pecas [] _ = []
                  pecas (x:xs) p | x==True = (Bloco p):pecas xs p
                                 | otherwise = (Vazia):pecas xs p

--
-- | Desenha os jogadores no mapa
desenhapl :: [Jogador] -> [Picture] -> Int -> [Picture]
desenhapl [] _ _ = []
desenhapl (pl:pls) ps i = let (d, (x,y)):ls = estadoPlayers (pl:pls) in (vivo pl [translate x y $ rotate d $ (ps!!i)]) ++ desenhapl pls ps (i+1)
                           where
                               vivo pl p | vidas pl > 0 = p
                                         | otherwise    = []

-- | Recolhe os dados de uma lista de jogadores, devolvendo o angulo a que se deve rodar cada picture, e as coordenadas para as mover
estadoPlayers :: [Jogador] -> [(Float, (Float,Float))]
estadoPlayers [] = []
estadoPlayers ((Jogador (x,y) d a b c): js) = ( (dir d), ( (fromIntegral(-185+(y*30))), (fromIntegral(185-(x*30))) )  ): estadoPlayers js
                                              where
                                                dir C = 0
                                                dir D = 90
                                                dir B = 180
                                                dir E = 270  
--
-- | Desenha os disparos no mapa
desenhadp :: [Disparo] -> [Jogador] -> Mapa -> [Picture] -> [Picture]
desenhadp [] _ _ _ = []
desenhadp ((DisparoChoque j t):ds) js m ps    = desenhaChoque (areaChoq ((DisparoChoque j t):ds) js) m (ps!!2) ++ desenhadp ds js m ps
desenhadp ((DisparoLaser j pos d):ds) js m ps = desenhaLaser d (areaLaser m pos d) (ps!!1) ++ desenhadp ds js m ps
desenhadp ((DisparoCanhao j (x,y) d):ds) js m ps = (translate (fromIntegral(-185+(y*30))) (fromIntegral(185-(x*30))) $ (ps!!0)) : desenhadp ds js m ps

-- | Desenha um disparoLaser  
desenhaLaser :: Direcao -> [PosicaoGrelha] -> Picture -> [Picture]
desenhaLaser _ [] _ = []
desenhaLaser d ((x,y):hs) p = (translate (fromIntegral(-185+(fst (adjust d))+(y*30))) (fromIntegral(185+(snd (adjust d))-(x*30))) $ rotate (dir d) $ p) : desenhaLaser d hs p
                               where
                                  dir x | x==C || x==B = 90
                                        | otherwise    = 0
                                  adjust x | x==C = (0,-15)
                                           | x==D = (-15,0)
                                           | x==B = (0,15)
                                           | x==E = (15,0)

-- | Devolve as posicoesGrelha afetadas por um disparoLaser, dada a sua posicaoGrelha e direcao num mapa
areaLaser :: Mapa -> PosicaoGrelha -> Direcao -> [PosicaoGrelha]
areaLaser m p d | blocoIndestest m p d = []
                | otherwise = somaVetores p (direcaoParaVetor d):(areaLaser m (somaVetores p (direcaoParaVetor d)) d) 

-- | Desenha um disparoChoque
desenhaChoque :: [PosicaoGrelha] -> Mapa -> Picture -> [Picture]
desenhaChoque [] _ _ = []
desenhaChoque ((x,y):hs) m p | x<=(a-1) && x>=0 && y<=(b-1) && y>=0  = (translate (fromIntegral(-200+(y*30))) (fromIntegral(200-(x*30))) $ p) : desenhaChoque hs m p
                             | otherwise                             = desenhaChoque hs m p
                              where
                               (a,b) = dimensaoMatriz m

-- | Devolve as posicoesGrelha afetadas por um disparoChoque, partindo da posicaoGrelha do jogador que originou o disparo
areaChoq :: [Disparo] -> [Jogador] -> [PosicaoGrelha]
areaChoq [] _ = []
areaChoq ((DisparoChoque j t):ds) js = areaEfeito (pospl' (js!!j))
                                                where
                                                  pospl' (Jogador (x,y) d a b c) = (x,y)
                                                  areaEfeito (x,y) = (area6x6 (x,y))\\[(x,y),(x+1,y),(x,y+1),(x+1,y+1)]
                                                                   -- ^ a função area6x6 pertence à tarefa2
--------------------------------------------------------------------------------

-- | Desenha as tabelas com os dados dos jogadores (n.º de vidas, disparos, etc)
desenhadados :: [Jogador] -> [Picture] -> Int -> [Picture]         
desenhadados [] _ _ = []
desenhadados ((Jogador pos d a b c):js) ps x = [translate (-400) (fromIntegral(195-(x*100)) ) $ color blue $ rectangleWire 250 100] ++ 
                                               [translate (-470) (fromIntegral(200-(x*100)) ) $ (ps!!x)] ++ 
                                               (state a [translate (-400) (fromIntegral(215-(x*100)) ) $ scale 0.15 0.15 $ (text ("vidas "  ++(show a)))]) ++ 
                                               (state b [translate (-400) (fromIntegral(190-(x*100)) ) $ scale 0.15 0.15 $ (text ("lasers " ++(show b)))]) ++ 
                                               (state c [translate (-400) (fromIntegral(165-(x*100)) ) $ scale 0.15 0.15 $ (text ("choques "++(show c)))]) ++ 
                                               (desenhadados js ps (x+1) )
                                                where
                                                  state x (p:ps) | x>1  = [color white $ p]
                                                                 | x==1 = [color yellow $ p]
                                                                 | x<1  = [color red $ p]

-- | Desenha o relógio
relogio :: Clock -> Picture
relogio (x,y,w,z) = translate (-60) 240 $ scale 0.2 0.2 $ color white $ text ((show x)++(show y)++":"++(show w)++(show (floor z)))
-- ^ z é um Float, logo é necessário aplicar a funçao floor para evitar que o relógio se apresente como 00:00.0.

-- | Função necessária em todos os menus de seleção, que mostra se determinado texto está selecionado (amarelo) ou não (branco).
--   Também foi usada no ecrã inicial para permitir o alternar autónomo do texto entre amarelo e branco.
pickcolor :: Int -> Picture -> Picture
pickcolor x p | x==0      = color yellow $ p 
              | otherwise = color white  $ p 

-------------------------------

-- * Componentes estáticos do jogo
--
-- * Instruções 
-- | Texto no canto superior direito durante o jogo
ctext :: [Picture]
ctext = [(translate 200 270 $ scale 0.15 0.15 $ color white $ text "[ P ] para colocar em pausa/ver opcoes"), 
         (translate 200 240 $ scale 0.15 0.15 $ color white $ text "[ C ] para ver/esconder comandos")]

-- | Instruções relativas aos comandos do jogo
comandos :: [Picture]
comandos = [(translate 360  85 $ color black $ rectangleSolid 540 270),
            (translate 360  85 $ color blue  $ rectangleWire  540 270),
            (translate 105 180 $ color green $ scale 0.13 0.13 $ text "Jogador 1"), 
            (translate 105 160 $ color white $ scale 0.13 0.13 $ text "Movimento: Setas | Canhao: , | Laser: . | Choque: -"),
            (translate 105 120 $ color red   $ scale 0.13 0.13 $ text "Jogador 2"), 
            (translate 105 100 $ color white $ scale 0.13 0.13 $ text "Movimento: W A S D | Canhao: 1 | Laser: 2 | Choque: 3"),
            (translate 105  60 $ color yellow $ scale 0.13 0.13 $ text "Jogador 3"), 
            (translate 105  40 $ color white  $ scale 0.13 0.13 $ text "Movimento: T F G H | Canhao: 4 | Laser: 5 | Choque: 6"),
            (translate 105   0 $ color violet $ scale 0.13 0.13 $ text "Jogador 4"), 
            (translate 105 (-20) $ color white $ scale 0.13 0.13 $ text "Movimento: I J K L | Canhao: 7 | Laser: 8 | Choque: 9")]

-- | Texto à esquerda durante a criação de um novo mapa
mtext :: [Picture]
mtext = [(translate (-410)  70 $ color blue $ rectangleWire 330 330),
         (translate (-560) 200 $ color white $ scale 0.15 0.15 $ text "Mover: Setas | Rodar: D"),
         (translate (-560) 170 $ color white $ scale 0.15 0.15 $ text "Trocar Peca: S"),
         (translate (-560) 140 $ color white $ scale 0.15 0.15 $ text "Mudar Tipo: A"),
         (translate (-560) 110 $ color white $ scale 0.15 0.15 $ text "Colocar Peca: X"),
         (translate (-560)  60 $ color white $ scale 0.15 0.15 $ text "Blocos Azuis - Indestrutiveis"), 
         (translate (-560)  30 $ color white $ scale 0.15 0.15 $ text "Blocos Cinza - Destrutiveis"),
         (translate (-560) (-50) $ color white $ scale 0.2 0.2 $ text "Jogar: Z")]

-- | Texto no canto superior esquerdo em menus que requiram seleção
stext :: [Picture]
stext = [(translate (-600) 260 $ color white $ scale 0.15 0.15 $ text "Selecionar: Setas  |  Confirmar: X"),
         (translate (-420) 270 $ color blue $ rectangleWire 390 40)]

stext2 :: [Picture]
stext2 = [(translate (-600) 210 $ color white $ scale 0.15 0.15 $ text "Criar Novo Mapa: Z"),
          (translate (-490) 220 $ color blue $ rectangleWire 250 40)]


-- * Ecrã de pausa
pause :: MenuState -> [Picture]
pause x = [(color black $ rectangleSolid 500 400),(color blue $ rectangleWire 500 400),
           (translate (-200) 0 $ scale 0.6  0.6 $ color white $ text "I I PAUSA"),
           pickcolor (x-11) (translate (-200) ( -70) $ scale 0.15 0.15 $ text "Continuar"),
           pickcolor (x-12) (translate (-200) (-100) $ scale 0.15 0.15 $ text "Reset"),
           pickcolor (x-13) (translate (-200) (-130) $ scale 0.15 0.15 $ text "Selecionar Novo Mapa"),
           pickcolor (x-14) (translate (-200) (-160) $ scale 0.15 0.15 $ text "Alterar Numero de Bots")]                   

-- * Menu de seleção de mapas
-- | Mapas que podem ser escolhidos, desenhados em menor escala
desenhaOpcoes :: [Estado] -> [Picture] -> Picture
desenhaOpcoes es ps = scale 0.5 0.5 $ pictures $ [translate (-900)    0  $ (desenhaEst (es!!0) ps)] ++
                                                 [translate (-480)    0  $ (desenhaEst (es!!1) ps)] ++
                                                 [translate   200   350  $ (desenhaEst (es!!2) ps)] ++
                                                 [translate   450 (-200) $ (desenhaEst (es!!3) ps)]

-- | Nome dos mapas
desenhaNomeMapa :: MenuState -> [Picture]
desenhaNomeMapa x = [pickcolor (x-2) $ translate (-500)  120  $ scale 0.2 0.2 $ text "Mapa 1"] ++
                    [pickcolor (x-3) $ translate (-300)  120  $ scale 0.2 0.2 $ text "Mapa 2"] ++
                    [pickcolor (x-4) $ translate (-120)  250  $ scale 0.2 0.2 $ text "Mapa 3"] ++
                    [pickcolor (x-5) $ translate    10  (-30) $ scale 0.2 0.2 $ text "Mapa 4"]

-- * Menu de seleção de bots
-- | Texto com as opções (0/1/2/3/4 bots)
desenhaMenuBots :: MenuState -> [Picture]
desenhaMenuBots x = [color blue  $ translate 0 30 $ rectangleWire 450 230] ++
                    [translate (-120) 100 $ scale 0.2 0.2 $ color white $ text "Jogar contra bots?"] ++
                    [pickcolor (x-16) $ translate (-120) 60 $ scale 0.15 0.15 $ text "Nao"] ++
                    [pickcolor (x-17) $ translate (-120) 30 $ scale 0.15 0.15 $ text "Jogar contra 1 bot"] ++
                    [pickcolor (x-18) $ translate (-120)  0 $ scale 0.15 0.15 $ text "Jogar contra 2 bots"] ++
                    [pickcolor (x-19) $ translate (-120) (-30) $ scale 0.15 0.15 $ text "Jogar contra 3 bots"] ++
                    [pickcolor (x-20) $ translate (-120) (-60) $ scale 0.15 0.15 $ text "Jogar com 4 bots"]

-- * Menu de EndGame
-- | Texto com as opções disponíveis após terminar o jogo
opcoesEndGame :: [Jogador] -> MenuState -> [Picture] -> [Picture]
opcoesEndGame js x ps = [color blue  $ translate 0 30 $ rectangleWire 450 230] ++
                        [pickcolor (x-8)  $ translate (-10)   30  $ scale 0.15 0.15 $ text "Jogar Novamente"] ++
                        [pickcolor (x-9)  $ translate (-10)    0  $ scale 0.15 0.15 $ text "Selecionar Novo Mapa"] ++
                        [pickcolor (x-10) $ translate (-10) (-30) $ scale 0.15 0.15 $ text "Alterar N. de Bots"] ++
                        (venceu js ps)
                         where
                          venceu [] _ = [color white $ translate (-10) 70 $ scale 0.3 0.3 $ text "Empate..."]
                          venceu (j:js) (p:ps) | vidas j > 0 = [color white $ translate (-10) 70 $ scale 0.3 0.3 $ text "Venceu!"] ++
                                                               [scale 1.5 1.5  $ translate (-80) 20 $ p]
                                               | otherwise   = venceu js ps
-------------------------------------------------------------------------
--
-- * Função principal da Tarefa 5
main :: IO ()
main = do 
          player0 <- loadBMP "player0.bmp"
          player1 <- loadBMP "player1.bmp"
          player2 <- loadBMP "player2.bmp"
          player3 <- loadBMP "player3.bmp"
          canhao  <- loadBMP "canhao.bmp"
          laser   <- loadBMP "laser.bmp"
          choque  <- loadBMP "shock1.bmp"
          tanks   <- loadBMP "Tanks.bmp"
          play (InWindow "Tanks" (500,500) (0,0))  -- janela onde irá correr o jogo (nome/dimensão/posição no ecrã)
               black                    -- cor do fundo da janela
               4                        -- frame rate
               (estadoInicial [player0,player1,player2,player3,canhao,laser,choque,tanks]) -- estadoGloss inicial
               desenhaEstado            -- desenha o estado do jogo
               reageEvento              -- reage a um evento
               reageTempo               -- reage ao passar do tempo


type EstadoGloss = (MenuState,Clock,Estado,String,Instrucoes,Bots,[Picture])
-- ^ O String corresponde ao Estado original do jogo, comprimido pela função 'comprime' da tarefa3

type Clock = (Int,Int,Int,Float)
-- ^ Como o relógio avança 0.25 segundos de cada vez (ver reageTempo), o valor dos segundos terá de ser um Float

type Bots = Int
-- ^ Indica quantos bots existem no jogo

type MenuState = Int
-- ^ Cada MenuState corresponde à seleção de uma opção ou ao estado autónomo de um menu. Desse modo, os MenuStates existentes são:

-- 0;1      -> Ecrã inicial (title screen). Existem 2 MenuState para permitir que o texto alterne entre cores espontaneamente (recorrendo à funçao pickcolor, que depende do MenuState)
-- 2;3;4;5  -> Menu de seleção de mapas. Cada MenuState corresponde à seleção (do nome) de um dos mapas.
-- 6;7      -> O jogo em si, com 0 bots. O 7 inclui a janela dos comandos, o 6 não.
-- 8;9;10   -> Menu de final de jogo (endgame). 8 corresponde ao reset, 9 à seleção de mapas, 10 à seleção de bots.
-- 11;12;13;14 -> Menu de pausa. 11 continua o jogo, 12,13 e 14 fazem o mesmo que o 8,9 e 10, respetivamente.
-- 15       -> Ecrã de criação de um novo mapa.
-- 16;17;18;19;20 -> Menu de seleção de bots. Cada MenuState corresponde a um número de bots: 16-0 bots; 17-1; 18-2; 19-3; 20-4.
-- 99       -> Ecrã de transição (loading screen). Existe para permitir/facilitar o reset de um estado do jogo (ver reageTempo).

-- | EstadoGloss inicial
estadoInicial :: [Picture] -> EstadoGloss
estadoInicial x = (1, (0,0,0,0), (Estado [[]] [] []), [], [], 0, x)

-- | Transforma cada EstadoGloss numa picture
desenhaEstado :: EstadoGloss -> Picture       
desenhaEstado (x, c, est, s, i, b, ps) | x<=1              = pictures [(last ps), pickcolor x (translate (-90) (-80) $ scale 0.15 0.15 $ text "[ X ] para jogar")]
                                       | elem x [2,3,4,5]  = pictures ((desenhaOpcoes (estados) ps) : (desenhaNomeMapa x) ++ stext ++ stext2)
                                       | x==6              = pictures (relogio c : (desenhadados (pls est) (take 4 ps) 0) ++ [desenhaEst est ps] ++ ctext)
                                       | x==7              = pictures (desenhaEstado (6, c, est, s, i, b, ps) : comandos)
                                       | elem x [8,9,10]   = pictures ((opcoesEndGame (pls est) x (take 4 ps)) ++ stext)
                                       | x>=11 && x<=14    = pictures (desenhaEstado (6, c, est, s, i, b, ps) : (pause x) ++ [translate 420 (-130) $ pictures $ stext])
                                       | x>=16 && x<=20    = pictures (desenhaMenuBots x ++ stext)
                                       | x==15             = pictures ((desenhaEst (criaEst (constroi i)) ps) : (desenhaed ( instrucoes i (editorInicial i) )) ++ mtext )
                                       | otherwise         = (text "...") -- ecrã de transição
                                        where
                                         pls (Estado m pl dp) = pl

-- | Altera o EstadoGloss dado determinados eventos
reageEvento :: Event -> EstadoGloss -> EstadoGloss
reageEvento ev (x, c, est, s, i, b, ps) | elem x [2,3,4,5] = ((reageEvMenu ev x), (0,0,0,0), (estados!!(x-2)), (comprime (estados!!(x-2))), [], 0, ps)
                                        | x>=16 && x<=20   = ((reageEvMenu ev x), (0,0,0,0), (descomprime s), s, [], (x-16), ps)
                                        | x==15            = ((reageEvMenu ev x), c, (criaEst (constroi i)), (comprime (criaEst (constroi i))), (reageEvInstrucoes ev i), b, ps)
                                        | x==6             = ((reageEvMenu ev x), c, (reageEv ev est b), s, i, b, ps)
                                        | otherwise        = ((reageEvMenu ev x), c, est, s, i, b, ps)
                                         where
                                          reageEv ev est b = reageEvPl (3-b) ev $ reageEvPl (2-b) ev $ reageEvPl (1-b) ev $ reageEvPl (0-b) ev est

-- | Altera o EstadoGloss a cada frame que passa
reageTempo :: Float -> EstadoGloss -> EstadoGloss
reageTempo n (x, c, est, s, i, b, ps) | x==0         = ((x+1), c, est, s, i, b, ps)
                                      | x==1         = ((x-1), c, est, s, i, b, ps)
                                      | x==6         = ((reageEstado est x), (addsec c), (jogabot b (tick est)), s, i, b, ps)
                                      | x==99        = (6, (0,0,0,0), (descomprime s), s, i, b, ps) -- reset do jogo
                                      | otherwise    = (x, c, est, s, i, b, ps)
                                       where
                                        jogabot b est | b>0 && (bot (4-b) est)/=Nothing = jogabot (b-1) (jogada (4-b) (fromJust(bot (4-b) est)) est)
                                                      | b>0 && (bot (4-b) est)==Nothing = jogabot (b-1) est
                                                      | otherwise = est                -- 1 bot -> player 4 é bot; 2 bots -> player 4 e 3 são bots;...
                                        addsec (x,y,w,z) | z < 9.75                     = (x,y,w,z+0.25)
                                                         | z == 9.75 && w < 5           = (x,y,w+1,0)
                                                         | z == 9.75 && w == 5 && y < 9 = (x,y+1,0,0)
                                                         | otherwise                    = (x+1,0,0,0)
                                                         -- ^ frame rate = 4 -> o relógio avança 1/4 seg de cada vez

-------------------------------------------------------

-- | Traduz um evento que ocorra durante o jogo numa jogada (tarefa2) de um determinado jogador. 
--  (A distinçao permite excluir jogadores controlados por bots em reageEvento)
reageEvPl :: Int -> Event -> Estado -> Estado
reageEvPl n (EventKey x Down _ _) est | mov n x == []  = est
                                      | otherwise      = jogada n (head(mov n x)) est
                                       where
                                        mov n x | (n==0 && x==(SpecialKey KeyUp))   ||(n==1 && (x==(Char 'w') || x==(Char 'W')))||(n==2 && (x==(Char 't') || x==(Char 'T')))||(n==3 && (x==(Char 'i') || x==(Char 'I'))) = [Movimenta C]
                                                | (n==0 && x==(SpecialKey KeyDown)) ||(n==1 && (x==(Char 's') || x==(Char 'S')))||(n==2 && (x==(Char 'g') || x==(Char 'G')))||(n==3 && (x==(Char 'k') || x==(Char 'K'))) = [Movimenta B]
                                                | (n==0 && x==(SpecialKey KeyLeft)) ||(n==1 && (x==(Char 'a') || x==(Char 'A')))||(n==2 && (x==(Char 'f') || x==(Char 'F')))||(n==3 && (x==(Char 'j') || x==(Char 'J'))) = [Movimenta E]
                                                | (n==0 && x==(SpecialKey KeyRight))||(n==1 && (x==(Char 'd') || x==(Char 'D')))||(n==2 && (x==(Char 'h') || x==(Char 'H')))||(n==3 && (x==(Char 'l') || x==(Char 'L'))) = [Movimenta D]
                                                | (n==0 && x==(Char ','))           ||(n==1 && x==(Char '1'))                   ||(n==2 && x==(Char '4'))                   ||(n==3 && x==(Char '7'))                    = [Dispara Canhao]
                                                | (n==0 && x==(Char '.'))           ||(n==1 && x==(Char '2'))                   ||(n==2 && x==(Char '5'))                   ||(n==3 && x==(Char '8'))                    = [Dispara Laser]
                                                | (n==0 && x==(Char '-'))           ||(n==1 && x==(Char '3'))                   ||(n==2 && x==(Char '6'))                   ||(n==3 && x==(Char '9'))                    = [Dispara Choque]
                                                | otherwise                         = []                                           
reageEvPl _ _ v = v -- ignora qualquer outro evento


-- | Reage a um evento, alterando o MenuState. (Permite selecionar opções e avançar/retroceder entre menus)
reageEvMenu :: Event -> MenuState -> MenuState
reageEvMenu (EventKey x Down _ _) m | elem m [0,1,9,13]      && (x==(Char 'x')||x==(Char 'X')) = 2   -- vai para o menu de seleção de mapas
                                    | elem m [2,3,4,5,10,14] && (x==(Char 'x')||x==(Char 'X')) = 16  -- vai para o menu de seleção de bots 
                                    | elem m [11,16,17,18,19,20] && (x==(Char 'x')||x==(Char 'X')) = 6   -- vai para o jogo em si
                                    | elem m [2,3,4,8,9,11,12,13,16,17,18,19]  && (x==(SpecialKey KeyRight)||x==(SpecialKey KeyDown)) = m+1 -- avança para a próxima opção
                                    | elem m [3,4,5,9,10,12,13,14,17,18,19,20] && (x==(SpecialKey KeyLeft) ||x==(SpecialKey KeyUp) )  = m-1 -- retrocede para a opção anterior
                                    | elem m [2,3,4,5] && (x==(Char 'z')||x==(Char 'Z')) = 15  -- vai para o construtor de mapas
                                    | m==6             && (x==(Char 'c')||x==(Char 'C')) = 7   -- mostra os comandos
                                    | m==7             && (x==(Char 'c')||x==(Char 'C')) = 6   -- esconde os comandos
                                    | elem m [6,7]     && (x==(Char 'p')||x==(Char 'P')) = 11  -- coloca o jogo em pausa
                                    | elem m [8,12]    && (x==(Char 'x')||x==(Char 'X')) = 99  -- recomeça o jogo (no seu estado inicial)
                                    | m==15            && (x==(Char 'z')||x==(Char 'Z')) = 16  -- vai para o menu de seleção de bots
reageEvMenu _ m = m

-- | Traduz um evento para uma instrucao (tarefa1) e adiciona-la à lista de instrucoes
reageEvInstrucoes :: Event -> Instrucoes -> Instrucoes
reageEvInstrucoes (EventKey x Down _ _) i | x==(SpecialKey KeyRight) = i ++ [Move D]
                                          | x==(SpecialKey KeyLeft)  = i ++ [Move E]
                                          | x==(SpecialKey KeyUp)    = i ++ [Move C]
                                          | x==(SpecialKey KeyDown)  = i ++ [Move B]
                                          | x==(Char 'a')||x==(Char 'A') = i ++ [MudaParede]
                                          | x==(Char 's')||x==(Char 'S') = i ++ [MudaTetromino]
                                          | x==(Char 'd')||x==(Char 'D') = i ++ [Roda]
                                          | x==(Char 'x')||x==(Char 'X') = i ++ [Desenha]
reageEvInstrucoes _ i = i

----------------

-- | Dado um mapa, a funçao cria um Estado com esse mapa e um jogador em cada canto
criaEst :: Mapa -> Estado
criaEst m = (Estado m [(Jogador (1,1) D 5 3 3),(Jogador (1,(dim2 m)) B 5 3 3),(Jogador ((dim1 m),1) C 5 3 3),(Jogador ((dim1 m),(dim2 m)) E 5 3 3)] [])
            where
             dim2 m = (snd (dimensaoMatriz m))-3
             dim1 m = (fst (dimensaoMatriz m))-3

-- | Se existir apenas um jogador 'vivo', ou estiverem todos mortos, a função altera o menu para o menu de EndGame
reageEstado :: Estado -> MenuState -> MenuState
reageEstado (Estado ma js ds) m | length (filter (/=0) (vidas' js) )<=1  = 8
                                | otherwise                              = m
                                 where
                                  vidas' [] = []
                                  vidas' (j:js) = vidas j : vidas' js

-- | Opções no menu de seleção de mapas
estados :: [Estado]
estados = [(Estado (mapReader ["HHHHHHHHHHHHH",
                               "H***********H",
                               "H***********H",
                               "H***O***O***H",
                               "H***O***O***H",
                               "H***********H",
                               "H***********H",
                               "H*O*******O*H",
                               "H**O*****O**H",
                               "H***O***O***H",
                               "H****OOO****H",
                               "H***********H",
                               "HHHHHHHHHHHHH"]) 
                   [(Jogador (1,1) D 5 3 3),(Jogador (1,10) B 5 3 3),(Jogador (10,1) C 5 3 3),(Jogador (10,10) E 5 3 3)] []),
           (Estado (mapReader ["HHHHHHHHHHHHH",
                               "HOO*******OOH",
                               "HOO*******OOH",
                               "H***********H",
                               "H*****H*****H",
                               "H*****H*****H",
                               "H***HHHHH***H",
                               "H*****H*****H",
                               "H*****H*****H",
                               "H***********H",
                               "HOO*******OOH",
                               "HOO*******OOH",
                               "HHHHHHHHHHHHH"]) 
                   [(Jogador (4,4) E 5 3 3),(Jogador (4,7) C 5 3 3),(Jogador (7,4) B 5 3 3),(Jogador (7,7) D 5 3 3)] []),
           (Estado (mapReader ["HHHHHHHHHHHHHHHHHHHHHHHHH",
                               "H****HH*******OO********H",
                               "H****HH*********OO******H",
                               "H****HH***********OO****H",
                               "H****HH***OOOOO*****OO**H",
                               "H****HH***OHHHO*******OOH",
                               "HOO**HH***OHHHO*********H",
                               "H**OOHH***OHHHO*********H",
                               "H*********OHHHO***HHOO**H",
                               "H*********OHHHO***HH**OOH",
                               "HOO*******OHHHO***HH****H",
                               "H**OO*****OOOOO***HH****H",
                               "H****OO***********HH****H",
                               "H******OO*********HH****H",
                               "H********OO*******HH****H",
                               "HHHHHHHHHHHHHHHHHHHHHHHHH"]) 
                   [(Jogador (1,1) B 5 3 3),(Jogador (1,22) E 5 3 3),(Jogador (13,1) D 5 3 3),(Jogador (13,22) C 5 3 3)] []),
           (Estado (mapReader ["HHHHHHHHHHHHHHHHHHHHHHHHH",
                               "H*********OHHHO*********H",
                               "H*********OHHHO*********H",
                               "H*********OHHHO*********H",
                               "H*********OHHHO*********H",
                               "H****OOOOOOOOOOOOOOO****H",
                               "HOOOOOO**OOOOOOO**OOOOOOH",
                               "HHHHHO*OO*OHHHO*OO*OHHHHH",
                               "HHHHHO*OO*OHHHO*OO*OHHHHH",
                               "HOOOOOO**OOOOOOO**OOOOOOH",
                               "H****OOOOOOOOOOOOOOO****H",
                               "H*********OHHHO*********H",
                               "H*********OHHHO*********H",
                               "H*********OHHHO*********H",
                               "H*********OHHHO*********H",
                               "HHHHHHHHHHHHHHHHHHHHHHHHH"]) 
                   [(Jogador (2,2) D 5 3 3),(Jogador (2,21) E 5 3 3),(Jogador (12,2) D 5 3 3),(Jogador (12,21) E 5 3 3)] [])]

-- Transforma um String num Mapa. (Facilita a escrita e compreensão de Mapas no código)
mapReader :: [String] -> Mapa
mapReader [] = []
mapReader (x:xs) = readLine x:mapReader xs
                  where
                   readLine [] = []
                   readLine (x:xs) | x=='*' = (Vazia):readLine xs
                                   | x=='O' = (Bloco Destrutivel):readLine xs
                                   | x=='H' = (Bloco Indestrutivel):readLine xs






