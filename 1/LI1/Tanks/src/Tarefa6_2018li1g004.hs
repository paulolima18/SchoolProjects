-- | Este módulo define funções comuns da Tarefa 6 do trabalho prático.
module Tarefa6_2018li1g004 where

import LI11819
import Tarefa2_2018li1g004
import Tarefa4_2018li1g004

-- * Funções principais da Tarefa 6.

-- | Muda uma 'Direcao' no sentido horário.
nextDir :: Direcao -> Direcao
nextDir D = B
nextDir B = E
nextDir E = C
nextDir C = D

-- | Verifica na linha/coluna do Jogador se existem no mínimo 2 Blocos Destrutiveis
blocosLinha :: Int -> Int -> [Peca] -> Bool
blocosLinha nBlocos i [] = False
blocosLinha nBlocos i (h:t)
  | nBlocos == i = True
  | h == Bloco Indestrutivel = False
  | h == Bloco Destrutivel = blocosLinha nBlocos (i+1) t
  | otherwise = blocosLinha nBlocos i t

-- | Verifica se existem, no mínimo, 2 Blocos Destrutiveis para valer a pena disparar laser
haBlocos :: Int -> Mapa -> PosicaoGrelha -> Direcao -> Bool
haBlocos nBlocos mapa (a,b) dir
  | dir == C
    = blocosLinha nBlocos 0 (drop a ((rodaMapa mapa)!!(b+1)))
      || blocosLinha nBlocos 0 (drop a ((rodaMapa mapa)!!(b)))
  | dir == B
    = blocosLinha nBlocos 0 (drop a ((rotateMap mapa)!!(length mapa -b)))
      || blocosLinha nBlocos 0 (drop a ((rotateMap mapa)!!(length mapa - b-1)))
  | dir == D
    = blocosLinha nBlocos 0 (drop b (mapa!!a))
      || blocosLinha nBlocos 0 (drop b (mapa!!(a+1)))
  | dir == E
    = blocosLinha nBlocos 0 (reverse(take b (mapa!!a)))
      || blocosLinha nBlocos 0 (reverse(take b (mapa!!(a+1))))

-- | Verifica se há jogadores
danger :: PosicaoGrelha -> Direcao -> PosicaoGrelha -> Bool
danger (x,y) dir (a,b)
  | (x,y) == (a,b) = False
  | dir == B && abs(y - b) <= 1 && x < a = True
  | dir == C && abs(y - b) <= 1 && x > a = True
  | dir == D && abs(x - a) <= 1 && y < b = True
  | dir == E && abs(x - a) <= 1 && y > b = True
  | otherwise = False

-- | Verifica se há jogadores
dangerTrue :: PosicaoGrelha -> Direcao -> PosicaoGrelha -> Bool
dangerTrue (x,y) dir (a,b)
  | (x,y) == (a,b) = False
  | dir == B && x < a = True
  | dir == C && x > a = True
  | dir == D && y < b = True
  | dir == E && y > b = True
  | otherwise = False

-- | Verifica se há jogadores no caminho.
dirJog :: Mapa -- ^ 'Mapa' do jogo
      -> [Jogador] -- ^ Lista de Jogadores atualmente no jogo
      -> PosicaoGrelha -- ^ 'Posicao' atual do 'bot'
      -> Direcao -- ^ 'Direcao' atual do 'bot'
      -> Bool -- ^
dirJog mapa [] (x,y) dir = False
dirJog mapa jog@(j:js) (x,y) dir
  | dangerTrue (x,y) dir (posicaoJogador j) = True
  | otherwise = haJog mapa js (x,y) dir
-- |
chair :: [Peca] -> Int -> Bool
chair (h:t) 0
  | h == Bloco Indestrutivel = False
  | otherwise = True
chair (h:t) val
  | h == Bloco Indestrutivel = False
  | otherwise = chair t (val-1)
-- |
indestrutivelCheck :: Mapa -> Direcao -> PosicaoGrelha -> PosicaoGrelha -> Bool
indestrutivelCheck mapa D (a,b) (x,y)
  = chair (drop (b+1) (mapa!!a)) (abs(y-b))
    && chair (drop (b+1) (mapa!!(a+1))) (abs(y-b))
indestrutivelCheck mapa B (a,b) (x,y)
  = chair (drop (a+2) ((rotateMap mapa)!!(length mapa - b-1))) (abs(x-a-1))
    && chair (drop (a+2) ((rotateMap mapa)!!(length mapa - b -2))) (abs(x-a-1))
indestrutivelCheck mapa E (a,b) (x,y)
  = chair (drop (length mapa - b -1 ) (reverse (mapa!!a))) (abs(b-y))
    && chair (drop (length mapa - b -1) (reverse(mapa!!(a-1)))) (abs(b-y))
indestrutivelCheck mapa C (a,b) (x,y)
  = chair (drop a ((rodaMapa mapa)!!(b-1))) (abs(a-x))
    && chair (drop a ((rodaMapa mapa)!!(b))) (abs(a-x))

-- | Verifica se há jogadores no caminho.
haJog :: Mapa -- ^ 'Mapa' do jogo
      -> [Jogador] -- ^ Lista de Jogadores atualmente no jogo
      -> PosicaoGrelha -- ^ 'Posicao' atual do 'bot'
      -> Direcao -- ^ 'Direcao' atual do 'bot'
      -> Bool -- ^
haJog mapa [] (x,y) dir = False
haJog mapa jog@(j:js) (x,y) dir
  | danger (x,y) dir (posicaoJogador j) && indestrutivelCheck mapa dir (x,y) (posicaoJogador j) = True
  | otherwise = haJog mapa js (x,y) dir

-- |
moveBot :: Int -> Int -> Mapa -> PosicaoGrelha -> [Jogador] -> Direcao -> (Bool,Direcao)
moveBot 3 nBlocos mapa (a,b) (j:js) dir = (False,dir)
moveBot x nBlocos mapa (a,b) (j:js) dir
  | ((haBlocos 1 mapa (a,b) dir) || (haJog mapa (j:js) (a,b) dir))
   = (True,dir)
  | otherwise
   = moveBot (x+1) nBlocos mapa (a,b) (j:js) (nextDir dir)
-- |
emptyCheck :: Mapa -> [Jogador] -> PosicaoGrelha -> Direcao
emptyCheck mapa (j:js) (a,b)
  | (dirJog mapa (j:js) (a,b) D || haBlocos 1 mapa (a,b) D)
    && mapa!!(a+1)!!(b+2) == Vazia && mapa!!a!!(b+2) == Vazia = D
  | mapa!!(a+2)!!(b+1) == Vazia && mapa!!(a+2)!!b == Vazia
    && (dirJog mapa (j:js) (a,b) B || haBlocos 1 mapa (a,b) B) = B
  | mapa!!(a+1)!!(b-1) == Vazia && mapa!!a!!(b-1) == Vazia
    && (dirJog mapa (j:js) (a,b) E || haBlocos 1 mapa (a,b) E) = E
  | mapa!!(a-1)!!(b+1) == Vazia && mapa!!(a-1)!!b == Vazia
    && (dirJog mapa (j:js) (a,b) C || haBlocos 1 mapa (a,b) C) = C
  | otherwise = C

-- | Verifica se existem Jogadores no caminho, para disparar ou não.
obstaculos :: Jogador -- ^ Jogador associado ao 'bot'
           -> [Jogador] -- ^ Lista de Jogadores atualmente no jogo
           -> Mapa -- ^ 'Mapa' do jogo
           -> Maybe Jogada -- ^ Possível jogada do 'bot'
obstaculos bot@(Jogador (a,b) dir v laser c) (j:js) mapa
  | laser > 0 && (haBlocos 2 mapa (a,b) dir) = Just (Dispara Laser)
  | haBlocos 1 mapa (a,b) dir || haJog mapa (j:js) (a,b) dir = Just (Dispara Canhao)
  | otherwise
    = Just (Movimenta (emptyCheck mapa (j:js) (a,b)))

-- | Define um ro'bot' capaz de jogar autonomamente o jogo.
bot :: Int          -- ^ O identificador do 'Jogador' associado ao ro'bot'.
    -> Estado       -- ^ O 'Estado' para o qual o ro'bot' deve tomar uma decisão.
    -> Maybe Jogada -- ^ Uma possível 'Jogada' a efetuar pelo ro'bot'.
bot index (Estado mapa jogs disps) = obstaculos (jogs!!index) (take index jogs++drop (index+1)jogs) mapa
