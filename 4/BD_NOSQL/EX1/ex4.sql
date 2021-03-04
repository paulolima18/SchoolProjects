-- [a] Quantos títulos possui a coleção?
SELECT COUNT(*) AS "Numero de Títulos" FROM titulo;

-- [b] Quantas músicas no total possui toda a coleção?
SELECT COUNT(*) AS "Numero de Musicas" FROM musica;

-- [c] Quantos autores existem na coleção?
SELECT COUNT(*) AS "Numero de Autores" FROM autor;

-- [d] Quantas editoras distintas existem na coleção?
SELECT COUNT(*) AS "Numero de Editoras" FROM editora;

-- [e] O autor “Max Changmin” é o principal autor de quantos título?
SELECT COUNT(titulo_a.ID_TITULO) AS "Titulos de Max Changmin"
FROM autor autor_a,titulo titulo_a
WHERE autor_a.NOME = 'Max Changmin' AND titulo_a.ID_AUTOR = autor_a.ID_AUTOR;

-- [f] No ano de 1970, quais foram os títulos comprados pelo utilizador?
SELECT titulo_a.TITULO, titulo_a.DTA_COMPRA
FROM titulo titulo_a
WHERE EXTRACT(YEAR FROM titulo_a.DTA_COMPRA) = '1970';

-- [g] Qual o autor do título que foi adquirido em “01-02-2010”, cujo preço foi de 12€?
SELECT autor_a.NOME
FROM titulo titulo_a, autor autor_a
WHERE titulo_a.DTA_COMPRA = to_date('01-02-2010', 'dd-mm-yyyy') 
    AND titulo_a.PRECO = 12 
    AND titulo_a.ID_AUTOR = autor_a.ID_AUTOR;

-- [h] Na alínea anterior indique qual a editora desse título?
SELECT autor_a.NOME, editora_a.NOME, titulo_a.TITULO
FROM titulo titulo_a, autor autor_a, editora editora_a
WHERE titulo_a.DTA_COMPRA = to_date('01-02-2010', 'dd-mm-yyyy')
    AND titulo_a.PRECO = 12 
    AND titulo_a.ID_AUTOR = autor_a.ID_AUTOR
    AND titulo_a.ID_EDITORA = editora_a.ID_EDITORA;

-- [i] Quais as reviews (data e classificação) existentes para o título “oh whoa oh” ?
SELECT review_a.DTA_REVIEW AS "Data", review_a.CONTEUDO AS "Classificação", titulo_a.TITULO
FROM titulo titulo_a, review review_a
WHERE titulo_a.TITULO = 'oh whoa oh' 
    AND titulo_a.ID_TITULO = review_a.ID_TITULO;
  
-- [j] Quais as reviews(data e classificação) existentes para o título “pump”, ordenadas por data da mais antiga para a mais recente?
SELECT review_a.DTA_REVIEW AS "Data", review_a.CONTEUDO AS "Classificação", titulo_a.TITULO
FROM titulo titulo_a, review review_a
WHERE titulo_a.TITULO = 'pump' 
    AND titulo_a.ID_TITULO = review_a.ID_TITULO
ORDER BY review_a.DTA_REVIEW ASC;

-- [k] Quais os diversos autores das músicas do título lançado a ‘04-04-1970’ com o preço de 20€?
SELECT autor_a.NOME
FROM autor autor_a, titulo titulo_a, musica musica_a
WHERE autor_a.ID_AUTOR = musica_a.ID_AUTOR
    AND titulo_a.DTA_COMPRA = to_date('04-04-1970', 'dd-mm-yyyy')
    AND titulo_a.PRECO = 20
    AND musica_a.ID_TITULO = titulo_a.ID_TITULO;
  
-- [l] Qual foi o total de dinheiro investido em compras de título da editora ‘EMI’?
SELECT SUM(titulo_a.PRECO) as total
FROM titulo titulo_a, editora editora_a
WHERE editora_a.ID_EDITORA = titulo_a.ID_EDITORA
    AND editora_a.NOME = 'EMI';

-- [m] Qual o título mais antigo cujo preço foi de 20€?
SELECT titulo, DTA_COMPRA
FROM titulo
WHERE PRECO = 20
ORDER BY DTA_COMPRA ASC
FETCH FIRST 1 ROW ONLY;

-- [n] Quantos “MP3” tem a coleção?
SELECT COUNT(*)
FROM titulo titulo_a, suporte suporte_a
WHERE titulo_a.ID_SUPORTE = suporte_a.ID_SUPORTE
    AND suporte_a.NOME = 'MP3';

-- [o] Destes mp3 quais são o títulos cujo género é: Pop Rock?
SELECT titulo_a.TITULO, genero_a.NOME
FROM titulo titulo_a, suporte suporte_a, genero genero_a
WHERE titulo_a.ID_SUPORTE = suporte_a.ID_SUPORTE
    AND suporte_a.NOME = 'MP3'
    AND genero_a.ID_GENERO = titulo_a.ID_GENERO
    AND genero_a.NOME = 'Pop Rock';
    
-- [p] Qual o custo total com “Blue-Ray”?
SELECT SUM(titulo_a.PRECO)
FROM titulo titulo_a, suporte suporte_a
WHERE titulo_a.ID_SUPORTE = suporte_a.ID_SUPORTE
    AND suporte_a.NOME = 'Blue-Ray';
    
-- [q] Qual o custo total com “Blue-Ray” cuja editora é a EMI?
SELECT SUM(titulo_a.PRECO)
FROM titulo titulo_a, suporte suporte_a, editora editora_a
WHERE titulo_a.ID_SUPORTE = suporte_a.ID_SUPORTE
    AND suporte_a.NOME = 'Blue-Ray'
    AND editora_a.ID_EDITORA = titulo_a.ID_EDITORA
    AND editora_a.NOME = 'EMI';

-- [r] Qual o património total dos títulos da coleção?
SELECT SUM(PRECO) FROM titulo;

-- [s] Qual a editora na qual o colecionador investiu mais dinheiro?
SELECT SUM(titulo_a.PRECO) as total, editora_a.NOME, editora_a.ID_EDITORA
FROM titulo titulo_a, editora editora_a
WHERE editora_a.ID_EDITORA = titulo_a.ID_EDITORA
GROUP BY editora_a.ID_EDITORA, editora_a.NOME
ORDER BY total DESC
FETCH FIRST 1 ROW ONLY;

-- [t] Qual a editora que possui mais títulos de “Heavy Metal” na coleção? Quanto titulo possui essa editora?
SELECT COUNT(titulo_a.ID_TITULO) as numero_titulos, editora_a.NOME, editora_a.ID_EDITORA
FROM titulo titulo_a, editora editora_a, genero genero_a
WHERE editora_a.ID_EDITORA = titulo_a.ID_EDITORA
    AND genero_a.ID_GENERO = titulo_a.ID_GENERO
    AND genero_a.NOME = 'Heavy Metal'
GROUP BY editora_a.ID_EDITORA, editora_a.NOME
ORDER BY numero_titulos DESC
FETCH FIRST 1 ROW ONLY;