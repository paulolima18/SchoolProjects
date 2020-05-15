-- | Este módulo define funções comuns da Tarefa 4 do trabalho prático.
module Tarefa4_2018li1g004 where

import LI11819

-- * Testes
-- | Testes unitários da Tarefa 4.
--
-- Cada teste é um 'Estado'.
testesT4 :: [Estado]
testesT4 = [play1,play13,play3,play4,play5,play6,play7,play8,play9,play10,play11]
-- | 'Mapa' para testesT4.
mapa22 :: Mapa
mapa22 = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
-- | 'Mapa' para testesT4.
mapa3 :: Mapa
mapa3 = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
-- | 'Mapa' para testesT4.
mapa4 :: Mapa
mapa4 = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]
-- | 'Mapa' para testesT4.
mapaTest :: Mapa
mapaTest = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Destrutivel,Bloco Destrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]

-- | 'Jogador' para testesT4.
joga1 :: Jogador
joga1 = Jogador (4,5) D 0 1 0
-- | 'Jogador' para testesT4.
joga13 :: Jogador
joga13 = Jogador (4,9) C 2 1 3
-- | 'Jogador' para testesT4.
joga3 :: Jogador
joga3 = Jogador (4,12) C 2 2 4
-- | 'Jogador' para testesT4.
joga4 :: Jogador
joga4 = Jogador (7,12) C 2 2 2
-- | 'Jogador' para testesT4.
joga5 :: Jogador
joga5 = Jogador (4,5) D 2 1 2
-- | 'Jogador' para testesT4.
joga6 :: Jogador
joga6 = Jogador (4,12) C 2 2 4
-- | 'Jogador' para testesT4.
joga7 :: Jogador
joga7 = Jogador (9,9) C 2 1 3
-- | 'Jogador' para testesT4.
joga8 :: Jogador
joga8 = Jogador (7,12) C 2 2 2
-- | 'Jogador' para testesT4.
joga9 :: Jogador
joga9 = Jogador (4,5) D 2 1 2
-- | 'Jogador' para testesT4.
joga10 :: Jogador
joga10 = Jogador (4,12) C 2 2 4
-- | 'Jogador' para testesT4.
joga11 :: Jogador
joga11 = Jogador (9,9) C 2 1 3
-- | 'Jogador' para testesT4.
joga12 :: Jogador
joga12 = Jogador (7,12) C 2 2 2
-- | 'Jogador' para testesT4.
joga14 :: Jogador
joga14 = Jogador (4,12) C 2 2 4
-- | 'Jogador' para testesT4.
joga15 :: Jogador
joga15 = Jogador (9,9) C 2 1 3
-- | 'Jogador' para testesT4.
joga16 :: Jogador
joga16 = Jogador (7,12) C 2 2 2
-- | 'Jogador' para testesT4.
joga17 :: Jogador
joga17 = Jogador (5,5) E 1 1 1

-- | 'Disparo' para testesT4.
dispa1 :: Disparo
dispa1 = DisparoLaser 1 (4,6) D
-- | 'Disparo' para testesT4.
dispa2 :: Disparo
dispa2 = DisparoLaser 3 (6,12) C
-- | 'Disparo' para testesT4.
dispa3 :: Disparo
dispa3 = DisparoLaser 2 (8,9) C
-- | 'Disparo' para testesT4.
dispa4 :: Disparo
dispa4 = DisparoChoque 0 5
-- | 'Disparo' para testesT4.
dispa5 :: Disparo
dispa5 = DisparoCanhao 1 (4,5) C
-- | 'Disparo' para testesT4.
dispa6 :: Disparo
dispa6 = DisparoChoque 0 5
-- | 'Disparo' para testesT4.
dispa7 :: Disparo
dispa7 = DisparoCanhao 1 (4,5) C

-- | 'Estado' para testesT4.
play1 :: Estado
play1 = Estado mapa3 [joga1,joga13] [dispa1]
-- | 'Estado' para testesT4.
play13 :: Estado
play13 = Estado mapa4 [joga13,joga1,joga3,joga4] [dispa1,dispa2,dispa3]
-- | 'Estado' para testesT4.
play3 :: Estado
play3 = Estado mapa4 [joga1,joga13] [DisparoCanhao 0 (3,10) B]
-- | 'Estado' para testesT4.
play4 :: Estado
play4 = Estado mapa4 [joga13,joga4] [dispa1]
-- | 'Estado' para testesT4.
play5 :: Estado
play5 = Estado mapa4 [joga1,joga13] [dispa1]
-- | 'Estado' para testesT4.
play6 :: Estado
play6 = Estado mapa3 [joga13,joga15] [dispa1,dispa5]
-- | 'Estado' para testesT4.
play7 :: Estado
play7 = Estado mapa4 [joga1,joga13] [dispa1,dispa6]
-- | 'Estado' para testesT4.
play8 :: Estado
play8 = Estado mapa4 [joga4,joga13] [dispa1,dispa7]
-- | 'Estado' para testesT4.
play9 :: Estado
play9 = Estado mapa4 [joga3,joga13] [dispa1,dispa5]
-- | 'Estado' para testesT4.
play10 :: Estado
play10 = Estado mapa22 [joga17] [DisparoCanhao 0 (5,4) E]
-- | 'Estado' para testesT4.
play11 :: Estado
play11 = Estado mapaTest [Jogador (5,5) B 1 2 3,Jogador (1,9) B 4 1 2,Jogador (9,1) C 3 3 3] [DisparoCanhao 0 (6,5) B,DisparoCanhao 0 (7,5) B]

-- * Funções principais da Tarefa 4.

-- | Avança o 'Estado' do jogo um 'Tick' de tempo.
--
-- __NB:__ Apenas os 'Disparo's afetam o 'Estado' do jogo com o passar do tempo.
--
-- __NB:__ Deve chamar as funções 'tickChoques', 'tickCanhoes' e 'tickLasers' pela ordem definida.
tick :: Estado -- ^ O 'Estado' anterior.
     -> Estado -- ^ O 'Estado' após um 'Tick'.
tick = tickChoques . tickCanhoes . tickLasers

-- | Devolve a lista de 'Laser's em curso dada a lista de 'Disparo's em curso.
findLaser :: [Disparo] -- ^ Lista de 'Disparo's em curso
          -> [Disparo] -- ^ Lista de 'DisparoLaser' em curso
findLaser (DisparoLaser x y z:t) = DisparoLaser x y z : findLaser t
findLaser (_:t) = findLaser t
findLaser _ = []

-- | Devolve a lista de 'Canhao' em curso dada a lista de 'Disparo's em curso.
findCanhao :: [Disparo] -- ^ Lista de 'Disparo's em curso
           -> [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
findCanhao (DisparoCanhao x y z:t) = DisparoCanhao x y z : findCanhao t
findCanhao (_:t) = findCanhao t
findCanhao _ = []

-- | Devolve a lista de 'Choque' em curso dada a lista de 'Disparo's em curso.
findChoque :: [Disparo] -- ^ Lista de 'Disparo's em curso
           -> [Disparo] -- ^ Lista de 'DisparoChoque' em curso
findChoque (DisparoChoque x y:t) = DisparoChoque x y : findChoque t
findChoque (_:t) = findChoque t
findChoque _ = []

-- | Elimina todos 'DisparoCanhao' afetados por um 'DisparoLaser' nos 'Disparo's em curso.
destroiCanhao :: [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
              -> Disparo -- ^ Um 'DisparoLaser' em curso
              -> [Disparo] -- ^ Lista de 'DisparoCanhao' não atingidos por esse 'DisparoLaser'
destroiCanhao [] _ = []
destroiCanhao (DisparoCanhao ij (a,b) mov : t) (DisparoLaser i (x,y) dir)
   | a == x && (b-y) > 0 && dir == D = destroiCanhao t (DisparoLaser i (x,y) dir)
   | a == x && (y-b) > 0 && dir == E = destroiCanhao t (DisparoLaser i (x,y) dir)
   | b == y && (x-a) > 0 && dir == C = destroiCanhao t (DisparoLaser i (x,y) dir)
   | b == y && (a-x) > 0 && dir == B = destroiCanhao t (DisparoLaser i (x,y) dir)
   | otherwise = DisparoCanhao ij (a,b) mov : destroiCanhao t (DisparoLaser i (x,y) dir)

-- | Elimina todos 'DisparoCanhao' afetados por qualquer 'DisparoLaser' nos 'Disparo's em curso.
destroiCanhoes :: [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
               -> [Disparo] -- ^ Lista de 'DisparoLaser' em curso
               -> [Disparo] -- ^ Lista de 'DisparoCanhao' não atingidos por nenhum 'DisparoLaser'
destroiCanhoes canhaoList (laser:l)
   = destroiCanhao canhaoList laser ++ destroiCanhoes canhaoList l
destroiCanhoes canhaoList _ = canhaoList

-- | Remove 'DisparoCanhao' repetidos da lista de 'DisparoCanhao' afetados por 'DisparoLaser's, no caso do mesmo 'DisparoCanhao' ser afetado por mais do que um 'DisparoLaser'.
removeDupl :: [Disparo] -- ^ Lista de 'DisparoCanhao' não atingindos por qualquer 'DisparoLaser'
           -> [Disparo] -- ^ Lista de 'DisparoCanhao' final sem repetições
removeDupl [] = []
removeDupl (h:t) | h `elem` t = removeDupl t
                 | otherwise = h : removeDupl t

-- | Verifica se o 'Jogador' em questão foi atingido por 'DisparoLaser' em questão.
laserOver :: PosicaoGrelha -- ^ 'Posicao' do 'Jogador' atual
          -> Disparo -- ^ 'DisparoLaser' em curso
          -> Mapa -- ^ 'Mapa' do jogo
          -> Bool -- ^ Resultado do 'Jogador' atual ser, ou não, atingido por um 'DisparoLaser'
laserOver (a,b) (DisparoLaser i (x,y) dir) mapa
  | dir == D && (mapa!!(x+1)!!(y+1) /= Bloco Indestrutivel
             && mapa!!x!!(y+1) /= Bloco Indestrutivel) && (y-b) == -1
    = True
  | dir == D && (mapa!!(x+1)!!(y+1) /= Bloco Indestrutivel
             && mapa!!x!!(y+1) /= Bloco Indestrutivel) && (y-b) /= -1
    = laserOver (a,b) (DisparoLaser i (x,y+1) dir) mapa
  | dir == E && (mapa!!(x+1)!!y /= Bloco Indestrutivel
             && mapa!!x!!y /= Bloco Indestrutivel) && (y-b) == 1
    = True
  | dir == E && (mapa!!(x+1)!!y /= Bloco Indestrutivel
             && mapa!!x!!y /= Bloco Indestrutivel) && (y-b) /= 1
    = laserOver (a,b) (DisparoLaser i (x,y-1) dir) mapa
  | dir == B && (mapa!!(x+1)!!y /= Bloco Indestrutivel
             && mapa!!(x+1)!!(y+1) /= Bloco Indestrutivel) && (x-a) == -1
    = True
  | dir == B && (mapa!!(x+1)!!y /= Bloco Indestrutivel
             && mapa!!(x+1)!!(y+1) /= Bloco Indestrutivel) && (x-a) /= -1
    = laserOver (a,b) (DisparoLaser i (x+1,y) dir) mapa
  | dir == C && (mapa!!x!!y /= Bloco Indestrutivel
             && mapa!!x!!(y+1) /= Bloco Indestrutivel) && (x-a) == 1
    = True
  | dir == C && (mapa!!x!!y /= Bloco Indestrutivel
             && mapa!!x!!(y+1) /= Bloco Indestrutivel) && (x-a) /= 1
    = laserOver (a,b) (DisparoLaser i (x-1,y) dir) mapa
  | otherwise
    = False

-- | Devolve a lista de 'Jogador'es afetados por 'DisparoLaser' em questão, diminuindo o número de vidas restantes.
laserZone :: [Jogador] -- ^ Lista de 'Jogador'es em curso no jogo
          -> Disparo -- ^ Um 'DisparoLaser' em curso
          -> Mapa -- ^ 'Mapa' do jogo
          -> [Jogador] -- ^ Lista de 'Jogador'es em curso com as vidas dos 'Jogador'es atingidos diminuídas
laserZone [] _ _ = []
laserZone (Jogador (a,b) mov 0 l c:j) (DisparoLaser i (x,y) dir) mapa
  = Jogador (a,b) mov 0 l c : laserZone j (DisparoLaser i (x,y) dir) mapa
laserZone (Jogador (a,b) mov v l c:j) (DisparoLaser i (x,y) dir) mapa
  | dir == C && (x-a) >= 1 && (y-b) <= 1 && (y-b) >= (-1)
             && laserOver (a,b) (DisparoLaser i (x,y) dir) mapa
    = Jogador (a,b) mov (v-1) l c : laserZone j (DisparoLaser i (x,y) dir) mapa
  | dir == B && (x-a) <= (-1) && (y-b) <= 1 && (y-b) >= (-1)
             && laserOver (a,b) (DisparoLaser i (x,y) dir) mapa
    = Jogador (a,b) mov (v-1) l c : laserZone j (DisparoLaser i (x,y) dir) mapa
  | dir == D && (y-b) <= (-1) && (x-a) <= 1 && (x-a) >= (-1)
             && laserOver (a,b) (DisparoLaser i (x,y) dir) mapa
    = Jogador (a,b) mov (v-1) l c : laserZone j (DisparoLaser i (x,y) dir) mapa
  | dir == E && (y-b) >= 1 && (x-a) <= 1 && (x-a) >= (-1)
             && laserOver (a,b) (DisparoLaser i (x,y) dir) mapa
    = Jogador (a,b) mov (v-1) l c : laserZone j (DisparoLaser i (x,y) dir) mapa
  | dir == mov && dir == D && (a,b+1) == (x,y)
    = Jogador (a,b) mov v l c : laserZone j (DisparoLaser i (x,y) dir) mapa
  | dir == mov && dir == E && (a,b-1) == (x,y)
    = Jogador (a,b) mov v l c : laserZone j (DisparoLaser i (x,y) dir) mapa
  | dir == mov && dir == C && (a-1,b) == (x,y)
    = Jogador (a,b) mov v l c : laserZone j (DisparoLaser i (x,y) dir) mapa
  | dir == mov && dir == B && (a+1,b) == (x,y)
    = Jogador (a,b) mov v l c : laserZone j (DisparoLaser i (x,y) dir) mapa
  | otherwise = Jogador (a,b) mov v l c : laserZone j (DisparoLaser i (x,y) dir) mapa

-- | Lista de 'Jogador'es afetados por algum dos 'DisparoLaser'.
lasersZona :: [Jogador] -- ^ Lista de 'Jogador'es do jogo
           -> [Disparo] -- ^ Lista de 'DisparoLaser' em curso
           -> Mapa -- ^ 'Mapa' do jogo.
           -> [Jogador] -- ^ Lista de 'Jogador'es em curso com as vidas dos 'Jogador'es atingidos diminuídas
lasersZona playersList (h:t) mapa
   = lasersZona (laserZone playersList h mapa) t mapa
lasersZona pl [] _ = pl

-- | Elimina os 'Bloco's Destrutiveis do 'Mapa' que são atingidos por 'DisparoLaser'.
destrutivel :: [Peca] -- ^ Lista de 'Peca's do 'Mapa' da primeira linha que pode ser atingida
            -> [Peca] -- ^ Lista de 'Peca's do 'Mapa' da segunda linha que pode ser atingida
            -> [Peca] -- ^ Lista de 'Peca's com 'Bloco's 'Destrutivel' removidos
destrutivel [] _ = []
destrutivel (Bloco Indestrutivel:t) (peca:pecas) = Bloco Indestrutivel : t
destrutivel (_:t) (Bloco Indestrutivel:pecas) = Vazia : t
destrutivel (_:t) (_:pecas) = Vazia : destrutivel t pecas

-- | Elimina 'Bloco's Destrutiveis das listas do 'Mapa' atingidas.
laserBlock :: Mapa -- ^ 'Mapa' do jogo
           -> Disparo -- ^ Um 'DisparoLaser' em curso
           -> Mapa -- ^ 'Mapa' resultante de remover 'Bloco's 'Destrutivel' atingidos
laserBlock (h:t)  (DisparoLaser i (1,y) dir)
   = (take y h
   ++ destrutivel (drop y h) (drop y (head t)))
   : (take y (head t)
   ++ destrutivel (drop y (head t)) (drop y h)) : tail t
laserBlock (h:t)  (DisparoLaser i (x,y) dir)
 = h : laserBlock t  (DisparoLaser i (x-1,y) dir)

-- | Roda o 'Mapa' 90º para a direita (Clockwise).
rodaMapa :: Mapa -> Mapa
rodaMapa ([]:_) = []
rodaMapa mapp = reverse (map head mapp):rodaMapa(map tail mapp)

-- | Roda o 'Mapa' 90º para a esquerda (Anti-Clockwise).
rotateMap :: Mapa -> Mapa
rotateMap ([]:_) = []
rotateMap mapp = map last mapp:rotateMap(map init mapp)

-- | Elimina 'Bloco's Destrutiveis atingidos por algum 'DisparoLaser'.
laserMap :: Mapa -- ^ 'Mapa' do jogo
         -> [Disparo] -- ^ Lista de 'DisparoLaser' em curso
         -> Mapa -- ^ 'Mapa' resultante de remover 'Bloco's 'Destrutivel' atingidos
laserMap mapp (DisparoLaser i (x,y) dir : t)
 | dir == C
   = laserMap (rotateMap (laserBlock (rodaMapa mapp)
   (DisparoLaser i (y+1,length mapp - x -1) dir))) t
 | dir == B
   = laserMap (rodaMapa (laserBlock (rotateMap mapp)
   (DisparoLaser i (length mapp -y-1,x+1) dir))) t
 | dir == E
   = laserMap (rotateMap(rotateMap(laserBlock (rotateMap(rotateMap mapp)) (DisparoLaser i (length(head mapp)-x-1,length mapp-y-1) dir)))) t
 | otherwise
   = laserMap (laserBlock mapp  (DisparoLaser i (x+1,y+1) dir)) t
laserMap mapp [] = mapp

-- | Avança o 'Estado' do jogo um 'Tick' de tempo, considerando apenas os efeitos dos tiros de 'Laser' disparados.
tickLasers :: Estado -> Estado
tickLasers (Estado mapa playersList shotList)
   = Estado (laserMap mapa (findLaser shotList))
            (lasersZona playersList (findLaser shotList) mapa)
            (removeDupl (destroiCanhoes (findCanhao shotList) (findLaser shotList))
            ++ findChoque shotList)


-- | Elimina os 'Bloco's Destrutiveis do 'Mapa' atingidos por 'DisparoCanhao'.
destroy :: [Peca] -- ^ Lista de 'Peca's do 'Mapa'
        -> [Peca] -- ^ Lista de 'Peca's com 'Bloco's 'Destrutivel' removidos
destroy [] = []
destroy (Bloco Indestrutivel:t) = Bloco Indestrutivel : t
destroy (Vazia:t) = Vazia :  t
destroy (Bloco Destrutivel:t) = Vazia : t

-- | Elimina 'Bloco's Destrutiveis das listas do 'Mapa' atingidas.
canhaoBlock :: Mapa -- ^ 'Mapa' do jogo
            -> Disparo -- ^ Um 'DisparoLaser' em curso
            -> Mapa -- ^ 'Mapa' resultante de remover 'Bloco's 'Destrutivel' atingidos
canhaoBlock (h:t)  (DisparoCanhao i (1,y) dir)
   = (take y h
     ++ destroy (drop y h))
     : (take y (head t)
     ++ destroy (drop y (head t))) : tail t
canhaoBlock (h:t)  (DisparoCanhao i (x,y) dir)
   = h : canhaoBlock t  (DisparoCanhao i (x-1,y) dir)

-- | Elimina 'Bloco's Destrutiveis atingidos por algum 'DisparoCanhao'.
canhaoMap :: Mapa -- ^ 'Mapa' do jogo
         -> [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
         -> Mapa -- ^ 'Mapa' resultante de remover 'Bloco's 'Destrutivel' atingidos
canhaoMap mapp (DisparoCanhao i (x,y) dir : t)
  | dir == C
    = canhaoMap (rotateMap (canhaoBlock (rodaMapa mapp)
    (DisparoCanhao i (y+1,length mapp - x -1) D))) t -- troca ordem das coordenadas
  | dir == B
    = canhaoMap (rodaMapa (canhaoBlock (rotateMap mapp)
    (DisparoCanhao i (length mapp - y - 1,x+1) D))) t -- troca ordem das coordenadas
  | dir == E
    = canhaoMap (rotateMap(rotateMap(canhaoBlock (rotateMap(rotateMap mapp))
                (DisparoCanhao i (length(head mapp)-x-1,length mapp -y-1) dir)))) t
  | dir == D
    = canhaoMap (canhaoBlock mapp  (DisparoCanhao i (x+1,y+1) dir)) t
canhaoMap mapp [] = mapp

-- | Elimina 'DisparoCanhao' da lista de 'Disparo's em curso quando este embate num objeto.
canhaoDestruct :: [Disparo] -- ^ Lista de DisparoCanhao em curso
               -> Mapa -- ^ 'Mapa' do jogo
               -> [Disparo] -- ^ Lista de DisparoCanhao em curso (não colididos com Blocos)
canhaoDestruct [] mapa = []
canhaoDestruct (DisparoCanhao i (x,y) dir:disps) mapa
  | (mapa!!(x+1)!!(y+1) /= Vazia || mapa!!x!!(y+1) /= Vazia) && dir == D
    = canhaoDestruct disps mapa
  | (mapa!!(x+1)!!y /= Vazia || mapa!!x!!y /= Vazia) && dir == E
    = canhaoDestruct disps mapa
  | (mapa!!(x+1)!!y /= Vazia || mapa!!(x+1)!!(y+1) /= Vazia) && dir == B
    = canhaoDestruct disps mapa
  | (mapa!!x!!y /= Vazia || mapa!!x!!(y+1) /= Vazia) && dir == C
    = canhaoDestruct disps mapa
  | otherwise = DisparoCanhao i (x,y) dir:canhaoDestruct disps mapa

-- | Diminui as vidas aos 'Jogador'es atingidos por 'Canhao'.
canhaoZone :: [Jogador] -- ^ Lista de 'Jogador'es em curso
           -> Disparo -- ^ Um 'DisparoCanhao' em curso
           -> [Jogador] -- ^ Lista de 'Jogador'es com as vidas dos atingidos alteradas
canhaoZone [] _ = []
canhaoZone (Jogador (a,b) mov 0 l c:j) (DisparoCanhao i (x,y) dir)
  = Jogador (a,b) mov 0 l c : canhaoZone j (DisparoCanhao i (x,y) dir)
canhaoZone (Jogador (a,b) mov v l c:j) (DisparoCanhao i (x,y) dir)
  | (b-1) == y && (x-a) >= -1 && (x-a) <= 1 && dir == D
    = Jogador (a,b) mov (v-1) l c : canhaoZone j (DisparoCanhao i (x,y) dir)
  | (b+1) == y && (x-a) >= -1 && (x-a) <= 1 && dir == E
    = Jogador (a,b) mov (v-1) l c : canhaoZone j (DisparoCanhao i (x,y) dir)
  | (a+1) == x && (y-b) >= -1 && (y-b) <= 1 && dir == C
    = Jogador (a,b) mov (v-1) l c : canhaoZone j (DisparoCanhao i (x,y) dir)
  | (a-1) == x && (y-b) >= -1 && (y-b) <= 1 && dir == B
    = Jogador (a,b) mov (v-1) l c : canhaoZone j (DisparoCanhao i (x,y) dir)
  | otherwise = Jogador (a,b) mov v l c : canhaoZone j (DisparoCanhao i (x,y) dir)

-- | Lista de 'Jogador'es afetados por algum 'DisparoCanhao'.
canhaoZona :: [Jogador] -- ^ Lista de 'Jogadore's do jogo
           -> [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
           -> [Jogador] -- ^ Lista de 'Jogador'es alteradas as vidas dos atingidos
canhaoZona = foldl canhaoZone

-- | Destrói 'Canhao' quando colide com 'Jogador'.
canhaoShot :: Disparo -- ^ Um 'DisparoCanhao' em curso
           -> [Jogador] -- ^ Lista de 'Jogador'es em curso
           -> Bool -- ^ Resultado de o 'Jogador' ser atingido pelo 'DisparoCanhao' em questão
--
-- Quando o resultado é __True__, então o 'Jogador' foi atingido.
--
-- Quando o resultado é __False__, então nenhum 'Jogador' no jogo foi atingido pelo 'DisparoCanhao' em questão.
canhaoShot _ [] = False
canhaoShot (DisparoCanhao i (x,y) dir) (Jogador (a,b) mov v l c:t)
  | (b-1) == y && (x-a) >= -1 && (x-a) <= 1 && dir == D = True
  | (b+1) == y && (x-a) >= -1 && (x-a) <= 1 && dir == E = True
  | (a-1) == x && (y-b) >= -1 && (x-a) <= 1 && dir == B = True
  | (a+1) == x && (y-b) >= -1 && (x-a) <= 1 && dir == C = True
  | otherwise = canhaoShot (DisparoCanhao i (x,y) dir) t

-- | Destroi qualquer 'Canhao' que colida com algum 'Jogador'es.
canhoesColision :: [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
                -> [Jogador] -- ^ Lista de 'Jogador'es em jogo
                -> [Disparo] -- ^ Lista de 'DisparoCanhao' com os que colidiram eliminados
canhoesColision [] playersList = []
canhoesColision (h:t) playersList
  | canhaoShot h playersList = canhoesColision t playersList
  | otherwise = h : canhoesColision t playersList

-- | Destrói 'DisparoCanhao' que colidam entre si.
canhaoCanhao :: [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
             -> Bool -- ^ Indicador de colisão
             -> Disparo -- ^ Um 'DisparoCanhao' em curso
             -> [Disparo] -- ^ Lista de 'DisparoCanhao' que não colidiram com o 'DisparoCanhao' em questão
canhaoCanhao [] _ _ = []
canhaoCanhao (DisparoCanhao i (x,y) dir:t) boolean (DisparoCanhao j (a,b) mov)
  | dir /= mov && (x,y) == (a,b)
    = canhaoCanhao t True (DisparoCanhao j (a,b) mov)
  | DisparoCanhao i (x,y) dir == DisparoCanhao j (a,b) mov && boolean
    = canhaoCanhao t boolean (DisparoCanhao j (a,b) mov)
  | otherwise = DisparoCanhao i (x,y) dir : canhaoCanhao t boolean (DisparoCanhao j (a,b) mov)

-- | Lista dos 'DisparoCanhao' final, não colididos com nenhum obstáculo.
listaCanhao :: [Disparo] -- ^ Lista de 'DisparoCanhao' que não colidiram (com 'Bloco's ou 'Jogador'es)
            -> [Disparo] -- ^ Lista de 'DisparoCanhao' que não colidiram (com 'Bloco's ou 'Jogador'es)
            -> [Disparo] -- ^ Lista de 'DisparoCanhao' que não colidiu (com outros 'DisparoCanhao' e 'Bloco's e 'Jogador'es)
listaCanhao shotlist (h:t) = listaCanhao (canhaoCanhao shotlist False h) t
listaCanhao shotList [] = shotList

-- | Move o 'DisparoCanhao' uma 'Posicao' quando não tem obstáculos.
moveCanhao :: [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
          -> [Disparo] -- ^ Lista de 'DisparoCanhao' em curso, movidos um 'Tick' conforme a sua 'Direcao'
moveCanhao [] = []
moveCanhao (DisparoCanhao i (x,y) C:t) = DisparoCanhao i (x-1,y) C: moveCanhao t
moveCanhao (DisparoCanhao i (x,y) B:t) = DisparoCanhao i (x+1,y) B: moveCanhao t
moveCanhao (DisparoCanhao i (x,y) D:t) = DisparoCanhao i (x,y+1) D: moveCanhao t
moveCanhao (DisparoCanhao i (x,y) E:t) = DisparoCanhao i (x,y-1) E: moveCanhao t

-- | Devolve a lista de 'DisparoCanhao' ainda em curso, após todas as colisões averiguadas.
minus :: [Disparo] -- ^ Lista de 'DisparoCanhao' em curso (não colididos com outros 'DisparoCanhao', 'Bloco's ou 'Jogador'es)
      -> [Jogador] -- ^ Lista de 'Jogador'es em curso
      -> Mapa -- ^ 'Mapa' do jogo
      -> [Disparo] -- ^ Lista de 'DisparoCanhao' em curso
minus shotList playersList mapa
  = moveCanhao (listaCanhao (canhoesColision (canhaoDestruct (findCanhao shotList) mapa)
                                             playersList)
                            (canhoesColision (canhaoDestruct (findCanhao shotList) mapa)
                                             playersList))

-- | Avança o 'Estado' do jogo um 'Tick' de tempo, considerando apenas os efeitos das balas de 'Canhao' disparadas.
tickCanhoes :: Estado -> Estado
tickCanhoes (Estado mapa playersList shotList)
   = Estado (canhaoMap mapa (findCanhao shotList))
            (canhaoZona playersList (findCanhao shotList))
            (minus shotList playersList mapa ++ findChoque shotList)


-- | Decrementa Ticks de 'DisparoChoque' e remove 'DisparoChoque' da lista de 'Disparo's quando Ticks == 0.
choqueOut :: [Disparo] -- ^ Lista de 'Disparo's em curso
          -> [Disparo] -- ^ Lista de 'Disparo's em curso depois de alterados os Ticks de 'DisparoChoque's e removidos os 'DisparoChoque' com Ticks == 0
choqueOut [] = []
choqueOut (DisparoChoque i 0 : ds) = choqueOut ds
choqueOut (DisparoChoque i t : ds) = DisparoChoque i (t-1) : choqueOut ds
choqueOut (h:t) = h : choqueOut t

-- | Avança o 'Estado' do jogo um 'Tick' de tempo, considerando apenas os efeitos dos campos de 'Choque' disparados.
tickChoques :: Estado -> Estado
tickChoques (Estado mapa playersList shotList)
   = Estado mapa
            playersList
            (choqueOut shotList)
