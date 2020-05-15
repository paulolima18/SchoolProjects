-- | Este módulo define funções comuns da Tarefa 2 do trabalho prático.
module Tarefa2_2018li1g004 where
--
import LI11819
--
-- * Testes
--
-- | Testes unitários da Tarefa 2.
--
-- Cada teste é um triplo (/identificador do 'Jogador'/,/'Jogada' a efetuar/,/'Estado' anterior/).
testesT2 :: [(Int,Jogada,Estado)]
testesT2 = [(0,Movimenta C,playk)
           ,(0,Movimenta D,playk)
           ,(0,Movimenta B,playk)
           ,(0,Movimenta E,playk)
           ,(1,Movimenta C,playk)
           ,(1,Movimenta D,playk)
           ,(0,Dispara Choque,playk)
           ,(0,Dispara Laser,playk)
           ,(0,Dispara Canhao,playk)
           ,(1,Movimenta B,playk)
           ,(1,Movimenta E,playk)]

-- | 'Mapa' 13x13 para testesT2.
mapa :: Mapa
mapa = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]

-- | 'Jogador' para testesT2.
jog1 :: Jogador
jog1 = Jogador (1,2) D 3 3 3

-- | 'Jogador' para testesT2.
jog2 :: Jogador
jog2 = Jogador (3,2) B 10 20 2

-- | 'Jogador' para testesT2.
jog3 :: Jogador
jog3 = Jogador (3,2) B 0 20 2

-- | 'Disparo' para testesT2.
dispa :: Disparo
dispa = DisparoChoque 0 3

-- | 'Disparo' para testesT2.
dispa2 :: Disparo
dispa2 = DisparoCanhao 0 (1,2) D

-- | 'Disparo' para testesT2.
dispa3 :: Disparo
dispa3 = DisparoLaser 2 (3,2) B


-- | 'Estado' para testesT2.
playk :: Estado
playk = Estado mapa [jog1,jog2,jog3] [dispa,dispa2,dispa3]

-- * Funções principais da Tarefa 2.
--
-- | Encontra a 'Posicao' do 'Jogador' atual na lista de 'Jogador'es.
playerFinder :: [Jogador] -- ^ Lista de 'Jogador'es, com identificador igual ao índice na lista.
             -> Int -- ^ O índice do 'Jogador' atual.
             -> (Int,Int) -- ^ A 'Posicao' do 'Jogador' atual.
playerFinder (Jogador (a,b) mov vidas laser choque:t) 0 = (a,b)
playerFinder (h:t) iJog = playerFinder t (iJog-1)

-- | Compara as posições dos 'Jogador'es com a 'Posicao' do 'Jogador' atual.
--
-- Verifica se as posições adjacentes ao 'Jogador' atual se encontram ocupadas, atendendo à 'Direcao' para a qual se pretende mover.
--
-- Quando o resultado é __True__, o 'Jogador' atual não se pode mexer nessa 'Direcao'.
comparaInter :: [PosicaoGrelha] -- ^ Lista de posições dos 'Jogador'es.
             -> PosicaoGrelha -- ^ A 'Posicao' do 'Jogador' atual.
             -> Direcao -- ^ A 'Direcao' do 'Jogador' atual.
             -> Bool -- ^ O resultado da verificação da possibilidade do movimento do 'Jogador' atual.
comparaInter [] _ _= False
comparaInter ((ax,bx):t) (ay,by) mv
  | ((by-bx) == (-2)) && ((ay-ax) >= (-1)) && ((ay-ax) <= 1) && (mv == D) = True
  | ((ay-ax) == (-2)) && ((by-bx) >= (-1)) && ((by-bx) <= 1) && (mv == B) = True
  | ((by-bx) == 2) && ((ay-ax) >= (-1)) && ((ay-ax) <= 1) && (mv == E) = True
  | ((ay-ax) == 2) && ((by-bx) >= (-1)) && ((by-bx) <= 1) && (mv == C) = True
  | otherwise = comparaInter t (ay,by) mv

-- | Verifica se o 'Jogador' atual se pode movimentar numa dada 'Direcao'.
--
-- Quando o resultado é __True__, o 'Jogador' atual não se pode mexer na 'Direcao' pretendida.
comparaPos :: [Jogador] -- ^ Lista de 'Jogador'es, com identificador igual ao índice na lista.
           -> Direcao -- ^ A 'Direcao' do 'Jogador' atual.
           -> Int -- ^ O índice do 'Jogador' atual.
           -> Bool -- ^ O resultado da verificação da possibilidade do movimento do 'Jogador' atual.
comparaPos list dir iJog
   = comparaInter (map posicaoJogador (filter (\x -> vidasJogador x > 0) list)) (playerFinder list iJog) dir

-- | Verifica se as 'Peca's dadas estão ocupadas.
ocupacao :: Peca -> Peca -> Bool
ocupacao Vazia Vazia = False -- Pode se mexer
ocupacao _ _ = True -- Nao se pode mexer

-- | Determina se as posições para as quais o 'Jogador' atual se quer movimentar estão ocupadas.
--
--  Quando o resultado é __True__, as posições do 'Mapa' encontram-se ocupadas por 'Peca's.
posOcupada :: PosicaoGrelha -- ^ A 'Posicao' do 'Jogador' atual.
           -> Direcao -- ^ A 'Direcao' do 'Jogador' atual.
           -> Mapa -- ^ O 'Mapa' atual.
           -> Bool -- ^ O resultado da verificação da ocupação das posições do 'Mapa'.
posOcupada (a,b) C mapa = ocupacao (mapa!!(a-2)!!b) (mapa!!(a-2)!!(b-1))
posOcupada (a,b) B mapa = ocupacao (mapa!!(a+1)!!b) (mapa!!(a+1)!!(b-1))
posOcupada (a,b) E mapa = ocupacao (mapa!!(a-1)!!(b-2)) (mapa!!a!!(b-2))
posOcupada (a,b) D mapa = ocupacao (mapa!!a!!(b+1)) (mapa!!(a-1)!!(b+1))

-- | Muda a 'Direcao' do 'Jogador' atual ou a sua 'Posicao', quando possível.
posJogador :: [Jogador] -- ^ Lista de 'Jogador'es, com identificador igual ao índice na lista.
           -> [Jogador] -- ^ Lista de 'Jogador'es, com identificador igual ao índice na lista.
           -> Int -- ^ O índice do 'Jogador' atual.
           -> Int -- ^ O índice do 'Jogador' atual.
           -> Direcao -- ^ A 'Direcao' do 'Jogador' atual.
           -> Mapa -- ^ O 'Mapa' atual.
           -> [Jogador] -- ^ O resultado de alterar a 'Posicao' ou 'Direcao' do 'Jogador' atual na lista de 'Jogador'es.
posJogador (Jogador (a,b) dirAtual vidas laser choque:t) l2 reserva 0 mov mapa
  | dirAtual /= mov
  || comparaPos l2 mov reserva
  || posOcupada (a+1,b+1) mov mapa
  = Jogador (a,b) mov vidas laser choque:t
  | dirAtual == C = Jogador (a-1,b) C vidas laser choque:t
  | dirAtual == B = Jogador (a+1,b) B vidas laser choque:t
  | dirAtual == D = Jogador (a,b+1) D vidas laser choque:t
  | dirAtual == E = Jogador (a,b-1) E vidas laser choque:t
posJogador (h:t) l2 reserva iJog mov mapa = h:posJogador t l2 reserva (iJog-1) mov mapa
posJogador [] _ _ _ _ _ = []

-- | Determina a 'Posicao' do 'Disparo' consoante a 'Posicao' do 'Jogador' atual.
posDisparo :: PosicaoGrelha -- ^ A 'Posicao' do 'Jogador' atual.
           -> Direcao -- ^ A 'Direcao' do 'Jogador' atual.
           -> PosicaoGrelha -- ^ A 'Posicao' do 'Disparo' do 'Jogador' atual.
posDisparo (a,b) C = (a-1,b)
posDisparo (a,b) B = (a+1,b)
posDisparo (a,b) D = (a,b+1)
posDisparo (a,b) E = (a,b-1)

-- | Atualiza a lista de 'Disparo's em curso, adicionando a 'Arma' do 'Jogador' atual.
ataqueJogador :: [Jogador] -- ^ Lista de 'Jogador'es, com identificador igual ao índice na lista.
              -> (Int,Int) -- ^ Par do índice do 'Jogador' atual.
              -> [Disparo] -- ^ Lista 'Disparo's em curso.
              -> Arma -- ^ Tipo de 'Arma' da jogada do 'Jogador' atual.
              -> [Disparo] -- ^ O resultado de adicionar um 'Disparo' à lista de 'Disparo's em curso.
ataqueJogador [Jogador pos mov vidas laser choque] (0,reserva) shotlist Choque
   = DisparoChoque reserva 5:shotlist
ataqueJogador [Jogador pos mov vidas laser choque] (0,reserva) shotlist Laser
   = DisparoLaser reserva (posDisparo pos mov) mov:shotlist
ataqueJogador [Jogador pos mov vidas laser choque] (0,reserva) shotlist Canhao
   = DisparoCanhao reserva (posDisparo pos mov) mov:shotlist
ataqueJogador (h:t) (0,reserva) shotlist shot
   = ataqueJogador [h] (0,reserva) shotlist shot
ataqueJogador (h:t) (iJog,reserva) shotlist shot
   = ataqueJogador t (iJog-1,reserva) shotlist shot

-- | Modifica o número de munições que o 'Jogador' atual tem, dependendo da 'Arma' da jogada.
municoes :: [Jogador] -- ^ Lista de 'Jogador'es, com identificador igual ao índice na lista.
         -> Int -- ^ O índice do 'Jogador' atual.
         -> Arma -- ^ Tipo de 'Arma' da jogada do 'Jogador' atual.
         -> [Jogador] -- ^ A lista de 'Jogador'es com as munições do 'Jogador' atual alteradas.
municoes (Jogador (a,b) mov vidas laser choque:t) 0 Choque
  | choque > 0 = Jogador (a,b) mov vidas laser (choque-1):t
  | otherwise = Jogador (a,b) mov vidas laser choque:t
municoes (Jogador (a,b) mov vidas laser choque:t) 0 Laser
  | laser > 0 = Jogador (a,b) mov vidas (laser-1) choque:t
  | otherwise = Jogador (a,b) mov vidas laser choque:t
municoes list 0 Canhao = list
municoes (h:t) iJog shot = h:municoes t (iJog-1) shot

-- | Devolve a lista de 'Choque's em curso dada a lista de 'Disparo's em curso.
chocante :: [Disparo] -> [Disparo]
chocante (DisparoChoque x y:t) = DisparoChoque x y : chocante t
chocante (_:t) = chocante t
chocante _ = []

-- | Determina se o índice do 'Jogador' atual é diferente do(s) índice(s) do(s) 'Jogador'(es) que disparou 'Choque'.
--
-- Quando o resultado é __True__, então o 'Jogador' atual não disparou 'Choque'.
indices :: Int -> [Disparo] -> Bool
indices iJog (h:t) | iJog == jogadorDisparo h = False
                   | otherwise = indices iJog t
indices _ _ = True

-- | Verifica se o 'Jogador' atual está na área de 'Choque' do 'Jogador' que o disparou.
--
-- Quando o resultado é __True__, então o 'Jogador' atual está na área de 'Choque' de outro 'Jogador'.
shockin :: PosicaoGrelha -- ^ A 'Posicao' do 'Jogador' atual.
        -> PosicaoGrelha -- ^ A 'Posicao' do 'Jogador' que disparou 'Choque'.
        -> Bool -- ^ O resultado da verificação de o 'Jogador' atual estar na área de 'Choque'.
shockin (a,b) (x,y) = abs (a-x) <= 3 && abs (b-y) <= 3

-- | Verifica se o 'Jogador' atual está na área de 'Choque', para todos os 'Choque's na lista de 'Disparo's em curso.
--
-- Quando o resultado é __True__, então o 'Jogador' atual está numa área de 'Choque'.
areaChoque :: PosicaoGrelha -- ^ A 'Posicao' do 'Jogador' atual.
           -> [Disparo] -- ^ Lista de 'DisparoChoque's em curso.
           -> [Jogador] -- ^ Lista de 'Jogador'es, com identificador igual ao índice na lista.
           -> Bool -- ^ O resultado da verificação de o 'Jogador' atual estar em alguma área de 'Choque'.
areaChoque _ [] _ = False
areaChoque (a,b) (disp1:ds) playersList
   | shockin (a,b) (posicaoJogador (playersList!!jogadorDisparo disp1)) = True
   | otherwise = areaChoque (a,b) ds playersList

-- | Verifica se o 'Jogador' atual está numa área de 'Choque'.
--
-- Quando o resultado é __True__, o 'Jogador' atual foi atingido por um 'Choque'.
shook :: Int -- ^ O índice do 'Jogador' atual.
      -> PosicaoGrelha -- ^ A 'Posicao' do 'Jogador' atual.
      -> [Disparo] -- ^ Lista 'Disparo's em curso.
      -> [Jogador] -- ^ Lista de 'Jogador'es, com identificador igual ao índice na lista.
      -> Bool -- ^ O resultado da verificação do 'Jogador' atual ser atingido por um 'Choque'.
shook iJog (a,b) dispList playersList
   = indices iJog (chocante dispList)
   && areaChoque (a,b) (chocante dispList) playersList

-- | Altera a lista de 'Jogador'es, mudando a 'Direcao' do 'Jogador' atual.
direciona :: Int -> Direcao -> [Jogador] -> [Jogador]
direciona 0 mov (Jogador pos dir v l c:t) = Jogador pos mov v l c:t
direciona iJog mov (h:t) = h: direciona (iJog-1) mov t

lolito playersList = (filter (\x -> vidasJogador x > 0) playersList)
-- | Efetua uma jogada.
jogada :: Int -- ^ O identificador do 'Jogador' que efetua a jogada.
       -> Jogada -- ^ A 'Jogada' a efetuar.
       -> Estado -- ^ O 'Estado' anterior.
       -> Estado -- ^ O 'Estado' resultante após o jogador efetuar a jogada.
jogada iJog (Movimenta mov) (Estado mapa playersList shotlist)
  | vidasJogador (playersList!!iJog) > 0
  && not (shook iJog (posicaoJogador (playersList!!iJog)) shotlist (lolito playersList))
    = Estado mapa (posJogador playersList playersList iJog iJog mov mapa) shotlist
  | vidasJogador (playersList!!iJog) > 0
  && shook iJog (posicaoJogador (playersList!!iJog)) shotlist (lolito playersList)
    = Estado mapa (direciona iJog mov playersList) shotlist
  | otherwise = Estado mapa playersList shotlist
jogada iJog (Dispara shot) (Estado mapa playersList shotlist)
  | Dispara shot == Dispara Laser && (lasersJogador(playersList!!iJog)) <= 0
    = Estado mapa playersList shotlist
  | vidasJogador (playersList!!iJog) > 0
    = Estado mapa (municoes playersList iJog shot) (ataqueJogador playersList (iJog,iJog) shotlist shot)
  | otherwise = Estado mapa playersList shotlist
