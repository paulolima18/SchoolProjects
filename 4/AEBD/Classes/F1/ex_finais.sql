-- 5 Construir uma view denominada JOGADOR_NEW que contenha a seguinte informação: ID_JOGADOR; NOME; IDADE;NOME_CLUBE; DESCRICAO_POSICAO, NOME_LIGA; CIDADE_CLUBE; ANO DE FUNDACAO
CREATE VIEW JOGADOR_NEW AS
SELECT jogador_a.ID_JOGADOR AS ID_JOGADOR, jogador_a.NOME AS NOME_JOGADOR, jogador_a.IDADE AS IDADE_JOGADOR, 
        clube_a.NOME AS NOME_CLUBE, posicao_a.DESC_POSICAO AS DESCRICAO_POSICAO, 
        liga_a.NOME AS NOME_LIGA, clube_a.CIDADE AS CIDADE_CLUBE, clube_a.ANO_FUNDACAO AS ANO_DE_FUNDACAO
FROM jogador jogador_a, clube clube_a, liga liga_a, posicao posicao_a
WHERE jogador_a.ID_CLUBE = clube_a.ID_CLUBE
    AND posicao_a.ID_POSICAO = jogador_a.ID_POSICAO
    AND liga_a.ID_LIGA = clube_a.ID_LIGA;

-- 6 Listar todos os defesas direitos da Segunda Liga.
SELECT jogador_a.NOME, clube_a.NOME
FROM liga liga_a, posicao posicao_a, clube clube_a, jogador jogador_a
WHERE jogador_a.ID_CLUBE = clube_a.ID_CLUBE
    AND clube_a.ID_LIGA = liga_a.ID_LIGA
    AND liga_a.NOME = 'II Liga'
    AND posicao_a.ID_POSICAO = jogador_a.ID_POSICAO
    AND posicao_a.DESC_POSICAO = 'Defesa Direito';
    
-- 7 Listar todos os jogadores com menos de 27 anos cuja posição é Trinco e que não jogam na II Liga
SELECT jogador_a.NOME, clube_a.NOME, liga_a.NOME
FROM liga liga_a, posicao posicao_a, clube clube_a, jogador jogador_a
WHERE jogador_a.ID_CLUBE = clube_a.ID_CLUBE
    AND clube_a.ID_LIGA = liga_a.ID_LIGA
    AND liga_a.NOME != 'II Liga'
    AND posicao_a.ID_POSICAO = jogador_a.ID_POSICAO
    AND posicao_a.DESC_POSICAO = 'Trinco'
    AND jogador_a.IDADE < 27;

-- 8 Construir uma view denominada TREINADOR_NEW que contenha a seguinte informação: ID_TREINADOR; NOME_TREINADOR;NOME_CLUBE; DESCRICAO_DO_CARGO
CREATE VIEW TREINADOR_NEW AS
SELECT treinador_a.ID_TREINADOR AS ID_TREINADOR, treinador_a.NOME AS NOME_TREINADOR,
        clube_a.NOME AS NOME_CLUBE, cargo_a.DESC_CARGO AS DESCRICAO_DO_CARGO
FROM treinador treinador_a, clube clube_a, cargo cargo_a
where treinador_a.ID_CLUBE = clube_a.ID_CLUBE
    AND treinador_a.ID_CARGO = cargo_a.ID_CARGO;
    