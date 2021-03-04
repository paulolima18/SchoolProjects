CREATE TABLE autor
(
    id_autor    NUMBER(3, 0) NOT NULL ENABLE,
    nome        VARCHAR2(200 byte) NOT NULL ENABLE,
    CONSTRAINT  "AUTOR_PK" PRIMARY KEY (id_autor)
);

-- INSERT INTO autor (id_autor, nome) VALUES (1, 'Justin Bieber');

CREATE TABLE editora
(
    id_editora  NUMBER(3, 0) NOT NULL ENABLE,
    nome        VARCHAR2(200 byte) NOT NULL ENABLE,
    CONSTRAINT  "EDITORA_PK" PRIMARY KEY (id_editora)
);

-- INSERT INTO editora (id_editora, nome) VALUES (1, 'Vevo');

CREATE TABLE genero
(
    id_genero   NUMBER(3, 0) NOT NULL ENABLE,
    nome        VARCHAR2(200 byte) NOT NULL ENABLE,
    CONSTRAINT  "GENERO_PK" PRIMARY KEY (id_genero)
);

-- INSERT INTO genero (id_genero, nome) VALUES (1,'Pop');

CREATE TABLE suporte
(
    id_suporte NUMBER(3, 0) NOT NULL ENABLE,
    nome       VARCHAR2(200 byte) NOT NULL ENABLE,
    CONSTRAINT "SUPORTE_PK" PRIMARY KEY (id_suporte)
);

-- INSERT INTO suporte (id_suporte, nome) VALUES (1,'digital');

CREATE TABLE titulo
(
    id_titulo  NUMBER(3, 0) NOT NULL ENABLE,
    titulo     VARCHAR2(200 byte) NOT NULL ENABLE,
    preco      NUMBER(3, 0) NOT NULL ENABLE,
    dta_compra DATE NOT NULL ENABLE,
    id_editora NUMBER(3, 0) NOT NULL ENABLE,
    id_suporte NUMBER(3, 0) NOT NULL ENABLE,
    id_genero  NUMBER(3, 0) NOT NULL ENABLE,
    id_autor   NUMBER(3, 0) NOT NULL ENABLE,
    CONSTRAINT "TITULO_PK" PRIMARY KEY (id_titulo)
);

-- INSERT INTO titulo (id_titulo, titulo, preco, dta_compra, id_editora, id_suporte, id_genero, id_autor) VALUES (1, 'My World 2.0', 10, to_date('2010-03-19','YYYY-MM-DD'), 1, 1, 1, 1);

CREATE TABLE musica
(
    id_musica  NUMBER(3, 0) NOT NULL ENABLE,
    nome       VARCHAR2(200 byte) NOT NULL ENABLE,
    id_autor   NUMBER(3, 0) NOT NULL ENABLE,
    id_titulo  NUMBER(3, 0) NOT NULL ENABLE,
    CONSTRAINT "MUSICA_PK" PRIMARY KEY (id_musica)
);

-- INSERT INTO musica (id_musica, nome, id_autor, id_titulo) VALUES (1, 'Baby', 1, 1);

CREATE TABLE review
(
    id_review  NUMBER(3, 0) NOT NULL ENABLE,
    id_titulo  NUMBER(3, 0) NOT NULL ENABLE,
    dta_review DATE NOT NULL ENABLE,
    conteudo   VARCHAR2(200 byte) NOT NULL ENABLE,
    CONSTRAINT "REVIEW_PK" PRIMARY KEY (id_review)
);

-- INSERT INTO review (id_review, id_titulo, dta_review, conteudo) VALUES (1, 1, to_date('2010-03-21','YYYY-MM-DD'), '5');