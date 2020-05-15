{-
* Introdução
O desafio desta Tarefa é conferir uma boa vizualização do jogo implementado, atendendo à boa jogabilidade do mesmo.
Após realizada, verifica-se que apresenta gráficos apelativos e boa jogabilidade.

* Objetivos
A cada tipo de 'Bloco' atribuímos uma imagem diferente, para conferir bons gráficos ao jogo.
Adicionamos também um menu de início de jogo, com três diferentes opções de 'Mapa's. No fim, conforme o 'Jogador' ganha ou perde o jogo, aparece uma imagem alusiva à derrota (ou vitória).
O jogo possui também tabelas de informação sobre os 'Jogador'es (1 Jogador interativo e 2 bots), mostrando o 'Estado' atual do jogo, bem como o tempo de jogo já decorrido.
O objetivo final desta Tarefa é produzir um jogo com gráficos apelativos e boa jogabilidade.

* Conclusão
Obtivemos um jogo com gráficos apelativos e boa jogabilidade.

 -}

-- | Este módulo define funções comuns da Tarefa 5 do trabalho prático.
module Main where

import LI11819
import Tarefa2_2018li1g004
import Tarefa4_2018li1g004
import Tarefa6_2018li1g004
import Graphics.Gloss
import Graphics.Gloss.Interface.Pure.Game
import Graphics.Gloss.Data.Bitmap

-- | Função principal da Tarefa 5.
--
-- __NB:__ Esta Tarefa é completamente livre. Deve utilizar a biblioteca <http://hackage.haskell.org/package/gloss gloss> para animar o jogo, e reutilizar __de forma completa__ as funções das tarefas anteriores.

-- | 'Mapa'.
bugMap :: Mapa
bugMap = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
-- | 'Mapa'.
noirMap :: Mapa
noirMap = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
-- | 'Mapa'
butterFly :: Mapa
butterFly = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]

-- | 'Jogador'.
playerNoir1 :: Jogador
playerNoir1 = Jogador (5,4) E 6 3 3
-- | 'bot' 1.
botNoir1 :: Jogador
botNoir1 = Jogador (1,8) C 6 3 3
-- | 'bot'2.
botNoir2 :: Jogador
botNoir2 = Jogador (1,6) D 6 3 3
-- | 'Jogador'.
playerFly1 :: Jogador
playerFly1 = Jogador (1,2) E 6 3 3
-- | 'bot' 1.
botFly1 :: Jogador
botFly1 = Jogador (10,10) C 6 3 3
-- | 'bot' 2.
botFly2 :: Jogador
botFly2 = Jogador (1,10) C 6 3 3
-- | 'Jogador'.
playerBug1 :: Jogador
playerBug1 = Jogador (1,9) B 6 3 3
-- | 'bot'1.
botBug1 :: Jogador
botBug1 = Jogador (1,1) B 6 3 3
-- | 'bot'2.
botBug2 :: Jogador
botBug2 = Jogador (10,1) B 6 3 3

-- | 'Estado'.
playNoir :: Estado
playNoir = Estado noirMap [playerNoir1,botNoir2,botNoir1] []
-- | 'Estado'.
play2 :: Estado
play2 = Estado butterFly [playerFly1,botFly1,botFly2] []
-- | 'Estado'.
playBug :: Estado
playBug = Estado bugMap [playerBug1,botBug1,botBug2] []

-- | Type 'Pictures'.
type Pictures = [Picture]
-- | Type 'EstadoGloss'.
type EstadoGloss = (Estado,Pictures,Pictures,Bool,(Int,Int,Int),Ticks)

start :: IO EstadoGloss
start
  = do
    indestrutivel  <- loadBMP "images/indestrutivel.bmp"
    destrutivel    <- loadBMP "images/destrutivel.bmp"
    ladybug        <- loadBMP "images/ladybug.bmp"
    mainMenu1      <- loadBMP "images/mainMenu1.bmp"
    mainMenu2      <- loadBMP "images/mainMenu2.bmp"
    mainMenu3      <- loadBMP "images/mainMenu3.bmp"
    catnoir        <- loadBMP "images/catnoir.bmp"
    ladyBala       <- loadBMP "images/ladyBalas.bmp"
    ladyXoque      <- loadBMP "images/ladyXoque.bmp"
    ladyLaser      <- loadBMP "images/laserbug.bmp"
    score          <- loadBMP "images/score.bmp"
    vencedor       <- loadBMP "images/winner.bmp"
    perdedor       <- loadBMP "images/loser.bmp"
    return (play2,[ladybug],[ladybug,catnoir,ladyBala,ladyXoque,ladyLaser,mainMenu1,mainMenu2,mainMenu3,vencedor,perdedor,indestrutivel,destrutivel,score],False,(0,0,0),0)

-- | Transforma um Int num Float.
f :: Int -> Float
f = fromIntegral

-- | Altera a 'Posicao' do 'Jogador' para a posição correta no Gloss.
translatePlayer :: PosicaoGrelha -> PosicaoGrelha
translatePlayer (a,b) = (a+8,b-6)

-- | Altera a 'posicaoDisparo' para a posição correta no Gloss.
translateShot :: Disparo -> PosicaoGrelha
translateShot shot
  | dir == D = (a+8,b-6)
  | dir == E = (a+8,b-6)
  | dir == C = (a+8,b-6)
  | dir == B = (a+8,b-6)
    where
      dir = direcaoDisparo shot
      (a,b) = posicaoDisparo shot

-- | Roda o 'Jogador' conforme a sua 'Direcao'.
rotation :: Direcao -> Picture -> Picture
rotation C pic = pic
rotation D pic = Rotate 90 pic
rotation B pic = Rotate 180 pic
rotation E pic = Rotate 270 pic

-- | Desenha um 'Jogador' no jogo.
desenhaJogador :: Estado -- ^ 'Estado' atual do jogo.
               -> Pictures -- ^ Imagens associadas aos 'Jogador'es
               -> Pictures -- ^ 'Jogador'es desenhados no jogo
desenhaJogador (Estado mapa [] disps) tanks = [Blank]
desenhaJogador (Estado mapa (h:t) disps) (tank:ts)
  | vidasJogador h > 0
    = Translate (glossScale (f b)) (glossScale (f(length mapa - a))) (rotation dir tank)
      : desenhaJogador (Estado mapa t disps) ts
  | otherwise
    = Blank : desenhaJogador (Estado mapa t disps) ts
    where
      dir = direcaoJogador h
      (a,b) = translatePlayer (posicaoJogador h)

-- | Desenha um 'Disparo' no jogo.
desenhaShot :: Estado -- ^ 'Estado' atual do jogo.
            -> Pictures -- ^ Imagens associadas aos 'Disparo's
            -> Picture -- ^ 'Disparo' desenhado no jogo
desenhaShot (Estado mapa (h:t) []) [ladyBala,ladyXoque,ladyLaser] = Blank
desenhaShot (Estado mapa jogs (DisparoChoque x y:t)) [ladyBala,ladyXoque,ladyLaser]
  = Translate (glossScale (f b)) (glossScale (f(length mapa - a))) ladyXoque
  where
    (a,b) = translatePlayer (posicaoJogador (jogs!!x))
desenhaShot (Estado mapa jogs (DisparoCanhao x y z:t)) [ladyBala,ladyXoque,ladyLaser]
  = Translate (glossScale (f b)) (glossScale (f(length mapa - a))) ladyBala
  where
    (a,b) = translateShot (DisparoCanhao x y z)
desenhaShot (Estado mapa jogs (DisparoLaser x y z:t)) [ladyBala,ladyXoque,ladyLaser]
  = Translate (glossScale (f b)) (glossScale (f(length mapa - a))) ladyLaser
  where
    (a,b) = translateShot (DisparoLaser x y z)

-- | Desenha uma linha do 'Mapa'.
desenhaLinha :: Posicao -> [Peca] -> Pictures -> Pictures
desenhaLinha (a,b) [] _ = []
desenhaLinha (a,b) (h:t) [indestrutivel,destrutivel]
  | h == Bloco Indestrutivel = Translate (glossScale (f a-7.5)) (glossScale (f b-7.5)) indestrutivel:desenhaLinha (a-1,b) t [indestrutivel,destrutivel]
  | h == Bloco Destrutivel = Translate (glossScale (f a-7.5)) (glossScale (f b-7.5)) destrutivel:desenhaLinha (a-1,b) t [indestrutivel,destrutivel]
  | otherwise = desenhaLinha (a-1,b) t [indestrutivel,destrutivel]

-- | Desenha o 'Mapa'.
desenhaMapa :: Estado -> Pictures -> Pictures
desenhaMapa (Estado [] jogs disps) pics = []
desenhaMapa (Estado (h:t) jogs disps) pics
  = desenhaLinha (length h,length (h:t)) (reverse h) pics ++ desenhaMapa (Estado t jogs disps) pics

-- Desenha os 'Ticks' do jogo e as keys para movimentar o 'Jogador'.
desenhaTicks :: Ticks -> [Picture]
desenhaTicks tick
  =  [Translate 222 395 (Scale 0.3 0.3 (Text ("Ticks " ++ show tick))),
   Translate (-330) (-410)
     (Scale 0.15 0.15
        (Text "Keys ->  , : Canhao  . : Choque  - : Laser   q: Quit"))]

-- | Desenha a tabela da informação do 'Estado' atual do jogo sobre um 'Jogador'.
desenhaOne :: (Float,Float) -> Jogador -> Picture -> Pictures
desenhaOne (a,b) jog pic
  = [Translate a b pic,
   Translate (a - 50) (b + 6)
     (Scale 0.1 0.1 (Text (show $ vidasJogador jog))),
   Translate (a + 30) (b + 25) (Scale 0.1 0.1 (Text "oo")),
   Translate (a + 35) (b - 32)
     (Scale 0.1 0.1 (Text (show $ lasersJogador jog))),
   Translate (a + 35) (b - 5)
     (Scale 0.1 0.1 (Text (show $ choquesJogador jog)))]

-- | Desenha as tabelas de informação do 'Estado' atual do jogo.
desenhaScores :: (Float,Float) -- ^ 'Posicao' do 'Jogador' atual no Gloss
              -> [Jogador] -- ^ Lista de 'Jogador'es atualmente em jogo
              -> Picture -- ^ Quadro das informações
              -> Pictures -- ^ Quadro das informações com valores
desenhaScores _ [] _ = []
desenhaScores (a,b) (jog:jogs) pic
  = desenhaOne (a,b) jog pic ++ desenhaScores (a+200,b) jogs pic

-- | Desenha o 'Estado' atual do jogo.
drawEstado :: EstadoGloss -> Picture
drawEstado (a,b,[ladybug,catnoir,ladyBala,ladyXoque,ladyLaser,mainMenu1,mainMenu2,mainMenu3,vencedor,perdedor,indestrutivel,destrutivel,score],d,(0,0,0),tick) = mainMenu1
drawEstado (a,b,[ladybug,catnoir,ladyBala,ladyXoque,ladyLaser,mainMenu1,mainMenu2,mainMenu3,vencedor,perdedor,indestrutivel,destrutivel,score],d,(1,0,0),tick) = mainMenu2
drawEstado (a,b,[ladybug,catnoir,ladyBala,ladyXoque,ladyLaser,mainMenu1,mainMenu2,mainMenu3,vencedor,perdedor,indestrutivel,destrutivel,score],d,(2,0,0),tick) = mainMenu3
drawEstado (a,b,[ladybug,catnoir,ladyBala,ladyXoque,ladyLaser,mainMenu1,mainMenu2,mainMenu3,vencedor,perdedor,indestrutivel,destrutivel,score],d,(0,0,3),tick) = vencedor
drawEstado (a,b,[ladybug,catnoir,ladyBala,ladyXoque,ladyLaser,mainMenu1,mainMenu2,mainMenu3,vencedor,perdedor,indestrutivel,destrutivel,score],d,(0,0,4),tick) = perdedor
drawEstado (estado,tank:tanks,[ladybug,catnoir,ladyBala,ladyXoque,ladyLaser,mainMenu1,mainMenu2,mainMenu3,vencedor,perdedor,indestrutivel,destrutivel,score],walk,(0,0,2),tick)
  = Scale 0.97 0.9 ( Pictures (desenhaScores (-300,400) (jogadoresEstado estado) score
    ++ desenhaTicks tick
    ++ desenhaMapa estado [indestrutivel,destrutivel]
    ++ desenhaJogador estado [ladybug,catnoir,ladybug]
    ++ [desenhaShot estado [ladyBala,ladyXoque,ladyLaser]]))

-- | Redimensiona conforme o Gloss.
glossScale :: Float -> Float
glossScale value = value * 50

-- | Reage a eventos.
event :: Event -> EstadoGloss -> EstadoGloss
-- Player1 Move
event (EventKey (SpecialKey KeyUp) Down _ _) (state,tanks,pictures,walk,(0,0,2),tick) = (jogada 0 (Movimenta C) state,tanks,pictures,True,(0,0,2),tick)
event (EventKey (SpecialKey KeyUp) Up _ _) (play2,tanks,pictures,walk,(0,0,2),tick) = (play2,tanks,pictures,False,(0,0,2),tick)
event (EventKey (SpecialKey KeyDown) Down _ _) (play2,tanks,pictures,walk,(0,0,2),tick) = (jogada 0 (Movimenta B) play2,tanks,pictures,True,(0,0,2),tick)--((x,y-5),picture,True)
event (EventKey (SpecialKey KeyDown) Up _ _) (play2,tanks,pictures,walk,(0,0,2),tick) = (play2,tanks,pictures,False,(0,0,2),tick)
event (EventKey (SpecialKey KeyLeft) Down _ _) (play2,tanks,pictures,walk,(0,0,2),tick) = (jogada 0 (Movimenta E) play2,tanks,pictures,True,(0,0,2),tick)--((x-5,y),picture,True)
event (EventKey (SpecialKey KeyLeft) Up _ _) (play2,tanks,pictures,walk,(0,0,2),tick) = (play2,tanks,pictures,False,(0,0,2),tick)
event (EventKey (SpecialKey KeyRight) Down _ _) (play2,tanks,pictures,walk,(0,0,2),tick) =  (jogada 0 (Movimenta D) play2,tanks,pictures,True,(0,0,2),tick)--((x+5,y),picture,True)
event (EventKey (SpecialKey KeyRight) Up _ _) (play2,tanks,pictures,walk,(0,0,2),tick) = (play2,tanks,pictures,False,(0,0,2),tick)
--Player1 Shot
event (EventKey (Char ',') Down _ _) (state,tanks,pictures,walk,(0,0,2),tick) = (jogada 0 (Dispara Canhao) state,tanks,pictures,False,(0,0,2),tick)
event (EventKey (Char '.') Down _ _) (state,tanks,pictures,walk,(0,0,2),tick) = (jogada 0 (Dispara Choque) state,tanks,pictures,False,(0,0,2),tick)
event (EventKey (Char '-') Down _ _) (state,tanks,pictures,walk,(0,0,2),tick) = (jogada 0 (Dispara Laser) state,tanks,pictures,False,(0,0,2),tick)
--Menu Events
event (EventKey (SpecialKey KeyDown) Down _ _) (play2,tanks,pictures,walk,(0,0,0),tick) = (play2,tanks,pictures,walk,(1,0,0),tick)
event (EventKey (SpecialKey KeyDown) Down _ _) (play2,tanks,pictures,walk,(1,0,0),tick) = (play2,tanks,pictures,walk,(2,0,0),tick)
event (EventKey (SpecialKey KeyDown) Down _ _) (play2,tanks,pictures,walk,(2,0,0),tick) = (play2,tanks,pictures,walk,(0,0,0),tick)
event (EventKey (SpecialKey KeyUp) Down _ _) (play2,tanks,pictures,walk,(0,0,0),tick) = (play2,tanks,pictures,walk,(2,0,0),tick)
event (EventKey (SpecialKey KeyUp) Down _ _) (play2,tanks,pictures,walk,(1,0,0),tick) = (play2,tanks,pictures,walk,(0,0,0),tick)
event (EventKey (SpecialKey KeyUp) Down _ _) (play2,tanks,pictures,walk,(2,0,0),tick) = (play2,tanks,pictures,walk,(1,0,0),tick)
event (EventKey (Char 'q') Down _ _) (play2,tanks,pictures,walk,(0,0,2),tick) = (play2,tanks,pictures,walk,(0,0,0),tick)
event (EventKey (Char 'q') Down _ _) (play2,tanks,pictures,walk,(0,0,3),tick) = (play2,tanks,pictures,walk,(0,0,0),tick)
event (EventKey (Char 'q') Down _ _) (play2,tanks,pictures,walk,(0,0,4),tick) = (play2,tanks,pictures,walk,(0,0,0),tick)
event (EventKey (SpecialKey KeyEnter) Down _ _) (play2,tanks,pictures,walk,(0,0,0),tick) = (playNoir,tanks,pictures,walk,(0,0,2),0)
event (EventKey (SpecialKey KeyEnter) Down _ _) (play,tanks,pictures,walk,(1,0,0),tick) = (play2,tanks,pictures,walk,(0,0,2),0)
event (EventKey (SpecialKey KeyEnter) Down _ _) (play2,tanks,pictures,walk,(2,0,0),tick) = (playBug,tanks,pictures,walk,(0,0,2),0)
event (EventKey (SpecialKey KeyEnter) Down _ _) (play2,tanks,pictures,walk,(0,0,3),tick) = (play2,tanks,pictures,walk,(1,0,0),tick)
event (EventKey (SpecialKey KeyEnter) Down _ _) (play2,tanks,pictures,walk,(0,0,4),tick) = (play2,tanks,pictures,walk,(1,0,0),tick)

event _ s = s  -- ignora qualquer outro evento

-- | Tranforma uma /Maybe Jogada/ (do bot) num 'Estado' com essa jogada efetuada.
botGloss :: Int -> Maybe Jogada -> Estado -> Estado
botGloss x Nothing state = state
botGloss x (Just a) state = jogada x a state

-- | Permite que o bot seja incluído no jogo.
bueda :: Estado -> Int -> Estado
bueda state@(Estado mapa (jog:jogs) disp) x
  | length (jog:jogs) == x = state
  | x == 1 = bueda (tick (botGloss x (bot x state) (Estado mapa (jog:jogs) disp))) (x+1)
  | x == 2 = bueda (tick (botGloss x (bot x state) (Estado mapa (jog:jogs) disp))) (x+1)
  | otherwise = bueda (tick (jogada x (Movimenta (direcaoJogador jog)) (Estado mapa (jog:jogs) disp))) (x+1)

-- | Verifica se o 'Jogador' é ou não vencedor.
winner :: [Jogador] -> Bool
winner (jog:jogs)
  | vidasJogador jog > 0 = True
  | otherwise = False

-- | Reage a um 'Tick' de tempo.
time :: Float -> EstadoGloss -> EstadoGloss
time n (Estado mapa (jog:jogs) disp,tanks,pictures,True,(0,0,2),tick)
  | not (winner (jog:jogs)) = (play2,tanks,pictures,False,(0,0,4),0)
  | tick >= 200 && winner (jog:jogs) = (play2,tanks,pictures,False,(0,0,3),0)
  | otherwise = (bueda (Estado mapa (jog:jogs) disp) 1,tanks,pictures,True,(0,0,2),tick+1)
time n (Estado mapa (jog:jogs) disp,tanks,pictures,False,(0,0,2),tick)
  | winner (jog:jogs) && tick >= 200 = (play2,tanks,pictures,False,(0,0,3),0)
  | not (winner (jog:jogs))= (play2,tanks,pictures,False,(0,0,4),0)
  | otherwise = (bueda (Estado mapa (jog:jogs) disp) 1,tanks,pictures,False,(0,0,2),tick+1)
time n (Estado mapa (jog:jogs) disp,tanks,pictures,bool,mode,tick)
  = (Estado mapa (jog:jogs) disp,tanks,pictures,bool,mode,tick)

fr :: Int
fr = 5

dm :: Display
dm = InWindow "LadyTanks" (800,800) (550,200)

jogo :: EstadoGloss -> IO ()
jogo inicio
  = play dm
      (makeColorI 204 255 229 0)     -- cor do fundo da janela
      fr                             -- frame rate
      inicio                         -- estado inicial
      drawEstado                     -- desenha o estado do jogo
      event                          -- reage a um evento
      time

main :: IO ()
main
  = do
    inicio <- start
    jogo inicio
