-- [a] Quantos jogadores fazem parte do plantel do “Porto”;
SELECT COUNT(jogador_a.ID_JOGADOR) 
FROM jogador jogador_a, clube clube_a 
WHERE clube_a.NOME = 'FC Porto' AND jogador_a.ID_CLUBE = clube_a.ID_CLUBE;

-- [b] Listar todos os jogadores que são “Defesa Direito” de clubes fundados em 1910;
SELECT jogador_a.NOME 
FROM jogador jogador_a, clube clube_a, posicao posicao_a
WHERE clube_a.ANO_FUNDACAO = 1910 
    AND jogador_a.ID_CLUBE = clube_a.ID_CLUBE
    AND posicao_a.DESC_POSICAO = 'Defesa Direito'
    AND jogador_a.ID_POSICAO = posicao_a.ID_POSICAO;

-- [c] Qual a média de idades, com 1 casa decimal, dos jogadores do “Braga” por posição em campo;
SELECT SUM(jogador_a.IDADE)/COUNT(jogador_a.ID_POSICAO)
FROM jogador jogador_a, clube clube_a
WHERE clube_a.NOME = 'Braga' AND jogador_a.ID_CLUBE = clube_a.ID_CLUBE
GROUP BY jogador_a.ID_POSICAO;

-- [d] Quais os cargos e os nomes dos treinadores dos clubes formados antes de 1950;
SELECT treinador_a.NOME, cargo_a.DESC_CARGO, clube_a.ano_fundacao
from treinador treinador_a, clube clube_a, cargo cargo_a
where treinador_a.ID_CLUBE = clube_a.ID_CLUBE
    AND treinador_a.ID_CARGO = cargo_a.ID_CARGO
    AND clube_a.ANO_FUNDACAO <= 1950;
    
-- [e] Listar todos o nome do treinador, nome do clube, cargo do treinador, cidade do clube e ano de fundação de todos os clubes fundados após 1945
SELECT treinador_a.NOME, clube_a.NOME, cargo_a.DESC_CARGO, clube_a.CIDADE, clube_a.ano_fundacao
from treinador treinador_a, clube clube_a, cargo cargo_a
where treinador_a.ID_CLUBE = clube_a.ID_CLUBE
    AND treinador_a.ID_CARGO = cargo_a.ID_CARGO
    AND clube_a.ANO_FUNDACAO >= 1945;
