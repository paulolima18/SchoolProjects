-- | Este modulo define funções comuns da Tarefa 1 do trabalho prático.
module Tarefa1_2018li1g004 where

import LI11819

-- * Testes

-- | Testes unitários da Tarefa 1.
--
-- Cada teste é uma sequencia de 'Instrucoes'.
testesT1 :: [Instrucoes]
testesT1 = [[MudaParede,MudaTetromino,Desenha],[Move D,Desenha,Move B,Move B,Move B,Roda,MudaParede,Move D,Move D,Move D,Desenha,Move B],[Move C,Move D,Move E,Move B],[Move D,Roda,Move E,Roda,Desenha,Move B,MudaParede,MudaTetromino,Move B,Desenha],[Move C,Roda,Roda,Move B,MudaTetromino,MudaParede],[MudaParede,MudaParede,MudaTetromino,MudaTetromino],[MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino,MudaTetromino]]

-- * Funções principais da Tarefa 1.

-- | Muda o 'Tetromino' atual para o seguinte lógico.
nextTet :: Tetromino -> Tetromino
nextTet I = J
nextTet J = L
nextTet L = O
nextTet O = S
nextTet S = T
nextTet T = Z
nextTet Z = I

-- | Altera o tipo de 'Parede'.
nextParede :: Parede -> Parede
nextParede Indestrutivel = Destrutivel
nextParede Destrutivel = Indestrutivel

-- | Muda uma 'Direcao' no sentido horário.
nextDir :: Direcao -> Direcao
nextDir D = B
nextDir B = E
nextDir E = C
nextDir C = D

-- | Uma 'Matriz' de um tipo.
type Matriz a = [[a]]

-- | Muda a 'Posicao' do cursor (canto superior esquerdo do @Tetromino@ actual) conforme a 'Direcao'.
nextMov :: Direcao -> Posicao -> Posicao
nextMov E (x,y) = (x,y-1)
nextMov B (x,y) = (x+1,y)
nextMov D (x,y) = (x,y+1)
nextMov C (x,y) = (x-1,y)

-- | Converte um 'Tetromino' (orientado para cima) numa 'Matriz' de Bool.
tetrominoParaMatriz :: Tetromino -> Matriz Bool
tetrominoParaMatriz I = [[False,True,False,False],[False,True,False,False],[False,True,False,False],[False,True,False,False]]
tetrominoParaMatriz J = [[False,True,False],[False,True,False],[True,True,False]]
tetrominoParaMatriz L = [[False,True,False],[False,True,False],[False,True,True]]
tetrominoParaMatriz O = [[True,True],[True,True]]
tetrominoParaMatriz S = [[False,True,True],[True,True,False],[False,False,False]]
tetrominoParaMatriz T = [[False,False,False],[True,True,True],[False,True,False]]
tetrominoParaMatriz Z = [[True,True,False],[False,True,True],[False,False,False]]

-- | Roda Matriz de um 'Tetromino' 90º para a direita.
rodaTet :: Matriz Bool -> Matriz Bool
rodaTet ([]:_) = []
rodaTet tet = reverse (map head tet) : rodaTet (map tail tet)

-- | Roda 'Tetromino' N vezes de acordo com a 'Direcao'.
rodaN :: Direcao -- ^ A 'Direcao' do 'Tetromino' atual.
      -> Tetromino -- ^ O 'Tetromino' atual.
      -> Matriz Bool -- ^ A Matriz final correspondente ao 'Tetromino' atual, de acordo com a sua 'Direcao'.
rodaN C tet = tetrominoParaMatriz tet
rodaN D tet = rodaTet (tetrominoParaMatriz tet)
rodaN B tet = rodaTet (rodaTet (tetrominoParaMatriz tet))
rodaN E tet = rodaTet (rodaTet (rodaTet (tetrominoParaMatriz tet)))

-- | Substitui 'Peca's de ['Peca'] por 'Bloco's pretendidos:
--
-- Quando um Bool do 'Tetromino' é __True__, substitui pela 'Parede' pretendida.
--
-- Quando um Bool do 'Tetromino' é __False__, mantém a 'Peca' anterior.
drawLine :: Int -- ^ A 'Posicao' da coluna do 'Tetromino' atual.
         -> Parede -- ^ O tipo de 'Parede' atual.
         -> [Bool] -- ^ Uma linha da /Matriz Bool/ do 'Tetromino' atual.
         -> [Peca] -- ^ A linha do 'Mapa' que se vai substituir.
         -> [Peca] -- ^ A linha do 'Mapa' com a implementação das 'Parede's pretendidas.
drawLine 0 parede _ [] = []
drawLine 0 parede [] (h:t) = h:t
drawLine 0 parede (x:xs) (h:t)
  | x = Bloco parede : drawLine 0 parede xs t
  | otherwise = h:drawLine 0 parede xs t
drawLine indice parede mbool (h:t) = h:drawLine (indice-1) parede mbool t

-- | Desenha um 'Tetromino' no 'Mapa'.
--
-- Dimensões dos 'Tetromino's => / 2x2 / | / 3x3 / | / 4x4 /
drawTet :: Int -- ^ A dimensão do 'Tetromino' atual.
        -> Posicao -- ^ A 'Posicao' do canto superior esquerdo do 'Tetromino' atual.
        -> Parede -- ^ O tipo de 'Parede' atual.
        -> Matriz Bool -- ^ A Matriz correspondente ao 'Tetromino' atual.
        -> Mapa -- ^ O 'Mapa' anterior.
        -> Mapa -- ^ O 'Mapa' resultante após a inclusão do 'Tetromino' pretendido.
drawTet 0 (a,b) parede [] (mapl:t) = mapl:t
drawTet 0 (a,b) parede _ [] = []
drawTet dim (a,b) parede (x:xs) (mapl:t)
   = drawLine b parede x mapl : drawTet (dim-1) (a,b) parede xs t

-- | Mantém as 'Peca's o 'Mapa' até à 'Posicao' do 'Tetromino' que se pretende desenhar.
draw :: Posicao -- ^ A 'Posicao' do canto superior esquerdo do 'Tetromino' atual.
     -> Tetromino -- ^ O 'Tetromino' atual.
     -> Parede -- ^ O tipo de 'Parede' atual.
     -> Direcao -- ^ A 'Direcao' do 'Tetromino' atual.
     -> Mapa -- ^ O 'Mapa' anterior.
     -> Mapa -- ^ O 'Mapa' resultante após a inclusão do 'Tetromino' pretendido.
draw (0,y) tet parede dir mapa
   = drawTet (length (rodaN C tet)) (0,y) parede (rodaN dir tet) mapa
draw (x,y) tet parede dir (h:t)
   = h:draw (x-1,y) tet parede dir t


-- | Aplica uma 'Instrucao' num 'Editor'.
--
--    * 'Move' - move numa dada 'Direcao'.
--
--    * 'MudaTetromino' - seleciona a 'Peca' seguinte (usar a ordem lexica na estrutura de dados),
--       sem alterar os outros parametros.
--
--    * 'MudaParede' - muda o tipo de 'Parede'.
--
--    * 'Desenha' - altera o 'Mapa' para incluir o 'Tetromino' atual, sem alterar os outros parâmetros.

instrucao :: Instrucao -- ^ A 'Instrucao' a aplicar.
          -> Editor    -- ^ O 'Editor' anterior.
          -> Editor    -- ^ O 'Editor' resultante após aplicar a 'Instrucao'.
instrucao (Move mov) (Editor posicao direcao tetromino parede mapa)
   = Editor (nextMov mov posicao) direcao tetromino parede mapa
instrucao Roda (Editor posicao direcao tetromino parede mapa)
   = Editor posicao (nextDir direcao) tetromino parede mapa
instrucao MudaTetromino (Editor posicao direcao tetromino parede mapa)
   = Editor posicao direcao (nextTet tetromino) parede mapa
instrucao MudaParede (Editor posicao direcao tetromino parede mapa)
   = Editor posicao direcao tetromino (nextParede parede) mapa
instrucao Desenha (Editor posicao direcao tetromino parede mapa)
   = Editor posicao direcao tetromino parede (draw posicao tetromino parede direcao mapa)


-- | Aplica uma sequencia de 'Instrucoes' num 'Editor'.
--
-- __NB:__ Deve chamar a função 'instrucao'.
instrucoes :: Instrucoes -- ^ As 'Instrucoes' a aplicar.
           -> Editor     -- ^ O 'Editor' anterior.
           -> Editor     -- ^ O 'Editor' resultante após aplicar as 'Instrucoes'.
instrucoes t editado = foldl (flip instrucao) editado t

-- | Cria um 'Mapa' inicial com 'Parede's nas bordas e o resto vazio.
mapaInicial :: Dimensao -- ^ A 'Dimensao' do 'Mapa' a criar.
            -> Mapa     -- ^ O 'Mapa' resultante com a 'Dimensao' dada.
mapaInicial (0,c) = []
mapaInicial (l,c) | l > 0   = aux c:mapaInicial (-(l-1),c)
                  | l < -1  = aux c:mapaInicial (l+1,c)
                  | l == -1 =  aux c:mapaInicial (l+1,c)
 where
      aux 0 = []
      aux count | c == count || count == 1 || l == -1 || l > 0 = Bloco Indestrutivel:aux (count-1)
                | otherwise = Vazia:aux (count-1)

-- | Cria um 'Editor' inicial.
--
-- __NB:__ Deve chamar as funções 'mapaInicial', 'dimensaoInicial', e 'posicaoInicial'.
editorInicial :: Instrucoes  -- ^ Uma sequencia de 'Instrucoes' de forma a poder calcular a  'dimensaoInicial' e a 'posicaoInicial'.
              -> Editor      -- ^ O 'Editor' inicial, usando a 'Peca' 'I' 'Indestrutivel' voltada para 'C'.
editorInicial list = Editor (posicaoInicial list) C I Indestrutivel (mapaInicial(dimensaoInicial list))

-- | Constroi um 'Mapa' dada uma sequencia de 'Instrucoes'.
--
-- __NB:__ Deve chamar as funções 'instrucoes' e 'editorInicial'.
constroi :: Instrucoes -- ^ Uma sequencia de 'Instrucoes' dadas a um 'Editor' para construir um 'Mapa'.
         -> Mapa       -- ^ O 'Mapa' resultante.
constroi l = mapaEditor e1
 where e0 = editorInicial l :: Editor
       e1 = instrucoes  l e0 :: Editor
