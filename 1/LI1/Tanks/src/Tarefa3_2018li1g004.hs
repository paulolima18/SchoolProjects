{- |
* Introdução
O desafio desta Tarefa é comprimir o máximo possível um estado e descomprimi-lo, de forma a retornar ao mesmo estado, sendo estes passos úteis quando, por exemplo, se pausa o jogo.
Após a realização da Tarefa, obtivemos uma compressão de 95.95%.

* Objetivos
A principal estratégia que utilizamos foi diminuir o comprimento do nome de identidades, como por exemplo, 'Bloco' 'Indestrutivel' = I.
Para além disso, agrupamos 'Peca's iguais para diminuir o tamanho da compressão do 'Mapa', separando as diferentes listas de 'Peca's por um caracter. Ainda nos 'Mapa's, eliminamos as bordas dos mesmos, pois são desnecessárias, visto serem sempre iguais.
Para além disso, substituímos por uma letra o caso em que uma linha inteira do 'Mapa' (sem bordas) só tem 'Peca's do mesmo tipo.
Nos 'Jogador'es, colocamos a 'Direcao' no meio da primeira e segunda coordenadas da 'Posicao' para evitar o uso de mais caracteres de separação, sendo depois o número de vidas e munições de cada 'Jogador' separados por vírgulas, e cada 'Jogador' separado do outro pelo caracter "n".
Nos 'Disparo's, a estratégia foi substituir o nome do 'Disparo' por um caracter que o identificasse e ocupasse menos espaço, por exemplo, 'DisparoLaser' = L.
A partir destas estratégias sumariadas, o objetivo final foi comprimir o máximo possível um 'Estado'.

* Conclusão
Nesta Tarefa, obtivemos um resultado muito bom de compressão de 95.95%, sendo que a descompressão de qualquer 'Estado' comprimido o faz voltar ao estado inicial.

 -}

-- | Este módulo define funções comuns da Tarefa 3 do trabalho prático.
module Tarefa3_2018li1g004 where

import LI11819
--
-- * Testes
-- | Testes unitários da Tarefa 3.
--
-- Cada teste é um 'Estado'.
testesT3 :: [Estado]
testesT3 = [play]

-- | 'Mapa' 13x13 para testesT3.
mapa :: Mapa
mapa = [[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Vazia,Bloco Indestrutivel],[Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel,Bloco Indestrutivel]]

-- | 'Jogador' para testesT3.
joga :: Jogador
joga = Jogador (1,1) C 312 3413 123
-- | 'Jogador' para testesT3.
joga2 :: Jogador
joga2 = Jogador (5,2) D 10 2300 1214
-- | 'Jogador' para testesT3.
joga3 :: Jogador
joga3 = Jogador (5,6) D 0 2300 3
-- | 'Jogador' para testesT3.
joga4 :: Jogador
joga4 = Jogador (7,2) D 9 2300 1214

-- | 'Disparo' para testesT3.
dispa :: Disparo
dispa = DisparoChoque 0 3
-- | 'Disparo' para testesT3.
dispa1 :: Disparo
dispa1 = DisparoCanhao 1 (1,1) C

-- | 'Estado' para testesT3.
play :: Estado
play = Estado mapa [joga2,joga3] [dispa1,dispa]

-- * Funções principais da Tarefa 3.

-- | Agrupa 'Peca's iguais
group :: String -> String
group list = takeWhile (== head list) list

-- | Determina número de vezes que um Char se repete.
--
-- Exemplo:
--
-- >>> agrupaN "IIII"
-- "4I"
agrupaN :: String -> String
agrupaN [] = []
agrupaN list
  | length (group list) == 1
  = head list : agrupaN (drop 1 list)
  | otherwise
  = show (length (group list))
  ++ head list : agrupaN (drop (length (group list)) list)

-- | Comprime lista de 'Peca's em String
--
-- n => Nova linha de lista.
--
-- I => 'Indestrutivel'
--
-- D => 'Destrutivel'
--
-- V => 'Vazia'
lineCompress :: [Peca] -> String
lineCompress [] = "n"
lineCompress (Bloco Indestrutivel:t) = 'I':lineCompress t
lineCompress (Bloco Destrutivel:t) = 'D':lineCompress t
lineCompress (Vazia:t) = 'V':lineCompress t

-- | Comprime um 'Mapa' em String
mapaCompress :: Mapa -> String
mapaCompress = foldr ((++) . lineCompress) "f"

-- | Elimina os catacteres fixos do 'Mapa'.
hole :: String -> String
hole ('I':'n':'f':t) = 'n':'f':t
hole ('I':'n':'I':t) = 'n':hole t
hole (h:t) = h:hole t

-- | Elimina os caracteres fixos do 'Mapa', nomeadamente as suas bordas.
only :: Mapa -> String
only mapa = hole (tail (mapaCompress (take ((-2)+length mapa) (tail mapa))))

-- | Determina se um dado caracter é ou não um dígito.
--
-- Quando o resultado é __True__, então o caracter dado é um dígito.
isDigit :: Char -> Bool
isDigit char = char >= '0' && char <= '9'

-- | Uma linha do 'Mapa' (sem bordas) com um só tipo de 'Peca' é substituída por uma String específica.
--
-- "W" => Quando 'Peca' é 'Vazia'
--
-- "Q" => Quando 'Peca' é 'Bloco' 'Destrutivel'
--
-- "E" => Quando 'Peca' é 'Bloco' 'Indestrutivel'
wMaker :: Char -> String
wMaker 'V' = "W"
wMaker 'D' = "Q"
wMaker 'I' = "E"

-- | Identifica a dimensão do 'Mapa' sem as bordas e devolve uma String específica se a primeira ['Peca'] tiver só um tipo de 'Peca'.
neutron :: Int -> String -> String
neutron size [] = []
neutron size (h:t)
  | isDigit h  && (read (takeWhile isDigit (h:t)) :: Int) == size
  = wMaker (head(dropWhile isDigit (h:t))) ++ "n"
  ++ neutron size (drop 1 (dropWhile (/='n') (h:t)))
  | otherwise
  = takeWhile (/='n') (h:t) ++ "n"
  ++ neutron size (drop 1 (dropWhile (/='n') (h:t)))

-- | Comprime o 'Estado' de um 'Jogador' em String.
jogadorCompress :: Jogador -> String
jogadorCompress (Jogador (a,b) dir v l c) = show a ++ show dir ++ show b
                                          ++ "," ++ show v
                                          ++ "," ++ show l
                                          ++ "," ++ show c ++ "n"

-- | Comprime uma lista de 'Jogador'es em String.
playersCompress :: [Jogador] -> String
playersCompress [] = "tf"
playersCompress  (h:t) = foldr ((++) . jogadorCompress) "f" (h:t)

-- | Comprime um 'Disparo' em String.
--
-- K => Identifica 'DisparoCanhao'
--
-- L => Identifica 'DisparoLaser'
tiroCompress :: Disparo -> String
tiroCompress (DisparoCanhao j (a,b) dir) = "K" ++ show j ++ show dir ++ show a ++ "," ++ show b ++ "n"
tiroCompress (DisparoLaser j (a,b) dir) = "L" ++ show j ++ show dir ++ show a ++ "," ++ show b ++ "n"
tiroCompress (DisparoChoque j ticks) = show j ++ show ticks ++ "n"

-- | Comprime uma lista de 'Disparo's em String.
shotsCompress :: [Disparo] -> String
shotsCompress  (h:t) = foldr ((++) . tiroCompress) "f" (h:t)
shotsCompress [] = "t"

-- | Comprime um 'Estado' para formato textual.
--
-- __NB:__ A função 'show' representa um 'Estado' num formato textual facilmente legível mas extenso.
--
-- __NB:__ Uma boa solução deve representar o 'Estado' dado no mínimo número de caracteres possível.

comprime :: Estado -> String
comprime (Estado [[Vazia]] [Jogador (-7,2) B (-9) (-59) (-100)] shotList)
  = shotsCompress shotList
comprime (Estado [[Vazia]] playersList shotList)
  = playersCompress playersList
  ++ comprime (Estado [[Vazia]] [Jogador (-7,2) B (-9) (-59) (-100)] shotList)
comprime (Estado mapa playersList shotList)
  = show (length(head mapa)-2) ++ ","
  ++ neutron ((-2)+length(head mapa)) (agrupaN (only mapa))
  ++ comprime (Estado [[Vazia]] playersList shotList)


-- | Descomprime uma lista de 'Peca's de String para ['Peca'].
lineDecompress :: String -> [Peca]
lineDecompress ('n':t) = []
lineDecompress ('I':t) = Bloco Indestrutivel:lineDecompress t
lineDecompress ('D':t) = Bloco Destrutivel:lineDecompress t
lineDecompress ('V':t) = Vazia:lineDecompress t

-- | Transforma um 'Mapa' em String para 'Mapa'.
mapaDecompress :: String -> Mapa
mapaDecompress ('f':t) = []
mapaDecompress list = lineDecompress (takeWhile (/='f') list)
                    : mapaDecompress (drop 1 (dropWhile (/='n') list))

-- | Lê o número de caracteres repetidos na String.
sizeRep :: String -> Int
sizeRep list = read (takeWhile isDigit list) :: Int

-- | Dado o número N de 'Peca's iguais, a função replica a respetiva 'Parede' N vezes.
func :: Int -> Char -> [Peca]
func 0 peca = []
func count 'I' = Bloco Indestrutivel:func (count-1) 'I'
func count 'D' = Bloco Destrutivel:func (count-1) 'D'
func count 'V' = Vazia:func (count-1) 'V'

-- | Dada a dimensão N do 'Mapa', a função replica N vezes a respetiva 'Parede'.
wholeList :: Int -> Char -> [Peca]
wholeList 0 peca = []
wholeList count 'W' = Vazia:wholeList (count-1) 'W'
wholeList count 'Q' = Bloco Destrutivel:wholeList (count-1) 'Q'
wholeList count 'E' = Bloco Indestrutivel:wholeList (count-1) 'E'

-- | Dada uma String do 'Mapa', a função devolve uma ['Peca'] do mesmo.
extra :: Int -> String -> [Peca]
extra mapsize ('n':t) = []
extra mapsize string
  | head string == 'W' = wholeList mapsize 'W' ++ extra mapsize (drop 1 string)
  | head string == 'Q' = wholeList mapsize 'Q' ++ extra mapsize (drop 1 string)
  | head string == 'E' = wholeList mapsize 'E' ++ extra mapsize (drop 1 string)
  | isDigit (head string)
  = func (sizeRep string) (head (drop (length (takeWhile isDigit string)) string))
  ++ extra mapsize (drop (1+length (takeWhile isDigit string)) string)
  | otherwise = func 1 (head string) ++ extra mapsize (drop 1 string)

-- | Acrescenta bordas laterais de 'Bloco' 'Indestrutivel' ao 'Mapa'.
addBordas :: Int -> String -> Mapa
addBordas mapsize ('f':t) = []
addBordas mapsize s
  = ([Bloco Indestrutivel] ++ extra mapsize (takeWhile (/='f') s)
  ++ [Bloco Indestrutivel]) : addBordas mapsize (drop 1 (dropWhile (/='n') s))

-- | Devolve a 'Posicao' de um 'Jogador'.
playerNator :: String -> PosicaoGrelha
playerNator s = (read (takeWhile isDigit s) :: Int
                ,read (takeWhile isDigit (drop 1 (dropWhile isDigit s))) :: Int)

-- | Identifica a posição na String do número de vidas e de munições de um 'Jogador' e devolve uma String apenas com esses valores.
base :: String -> String
base string = drop 1 (dropWhile isDigit (drop 1 (dropWhile isDigit (drop 1 string))))

-- | Descomprime uma String de um 'Jogador' em 'Jogador'.
jogadorDecompress :: String -> Jogador
jogadorDecompress s
  = Jogador (playerNator s)
   (read [head (dropWhile isDigit s)])
   (read (takeWhile isDigit (base s)))
   (read (takeWhile isDigit (drop 1 (dropWhile isDigit (base s)))))
   (read (takeWhile isDigit (drop 1 (dropWhile isDigit (drop 1 (dropWhile isDigit (base s)))))))

-- | Descomprime uma lista de 'Jogador'es de String para ['Jogador'].
playersDecompress :: String -> [Jogador]
playersDecompress ('t':t) = []
playersDecompress ('f':t) = []
playersDecompress list = jogadorDecompress list:playersDecompress (drop 1 (dropWhile (/='n') list))

-- | Descomprime a 'Posicao' de um 'Disparo'.
poString :: String -> PosicaoGrelha
poString s = (read (takeWhile isDigit (tail s)) :: Int
             ,read (takeWhile isDigit (drop 1 (dropWhile isDigit (tail s)))) :: Int)

-- | Descomprime a informação sobre um 'Disparo'.
tiroDecompress :: String -> Disparo
tiroDecompress ('K':t) = DisparoCanhao (read [head t]) (poString (tail t)) (read [t!!1])
tiroDecompress ('L':t) = DisparoLaser (read [head t]) (poString (tail t)) (read [t!!1])
tiroDecompress (h:t) = DisparoChoque (read [h]) (read [head t])

-- | Descomprime a informação sobre os 'Disparo's de uma String com todos os 'Disparo's em curso.
shotsDecompress :: String -> [Disparo]
shotsDecompress ('t':t) = []
shotsDecompress "f" = []
shotsDecompress s = tiroDecompress s: shotsDecompress (drop 1 (dropWhile (/='n') s))

-- | Determina as bordas (superior e inferior) de um 'Mapa'.
bordas :: Int -> [Peca]
bordas 0 = []
bordas x = Bloco Indestrutivel:bordas(x-1)

-- | Transforma uma String de Estado numa String de 'Disparo's.
stringShot :: String -> String
stringShot s = drop 1 (dropWhile (/= 'f') (drop 1 (dropWhile (/= 'f') s)))

-- | Transforma uma String de 'Estado' numa String de 'Jogador'es.
stringPlayers :: String -> String
stringPlayers string = drop 1 (drop 1 (dropWhile (/='f') string))

-- | Elimina caracteres irrelevantes para a continuação da descompressão.
stringMake :: String -> String
stringMake string = drop 1 (dropWhile isDigit string)

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
descomprime e = Estado (bordas (sizeRep e+2)
               :addBordas (sizeRep e) (stringMake e) ++ [bordas (sizeRep e + 2)])
               (playersDecompress (stringPlayers (stringMake e)))
               (shotsDecompress (stringShot (stringMake e)))
