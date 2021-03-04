-- a)

-- b)

CREATE TABLE treinador
(
    id_treinador NUMBER(*, 0) NOT NULL ENABLE,
    nome         VARCHAR2(200 byte) NOT NULL ENABLE,
    id_cargo     NUMBER(*, 0) NOT NULL ENABLE,
    id_clube     NUMBER NOT NULL ENABLE,
    CONSTRAINT "TREINADOR_PK" PRIMARY KEY(id_treinador)
);

CREATE TABLE clube
(
    id_clube     NUMBER(*, 0) NOT NULL ENABLE,
    nome         VARCHAR2(200 byte) NOT NULL ENABLE,
    ano_fundacao NUMBER NOT NULL ENABLE,
    cidade       VARCHAR2(200 byte) NOT NULL ENABLE,
    id_liga      NUMBER NOT NULL ENABLE,
    CONSTRAINT "CLUBE_PK" PRIMARY KEY(id_clube)
);

CREATE TABLE cargo
(
    id_cargo   NUMBER NOT NULL ENABLE,
    desc_cargo VARCHAR2(200 byte) NOT NULL ENABLE,
    CONSTRAINT "CARGO_PK" PRIMARY KEY(id_cargo)
);

CREATE TABLE liga
(
    id_liga          NUMBER(*, 0) NOT NULL ENABLE,
    nome             VARCHAR2(200 byte) NOT NULL ENABLE,
    ano_fundacao     NUMBER(*, 0) NOT NULL ENABLE,
    id_patrocionador NUMBER NOT NULL ENABLE,
    CONSTRAINT "LIGA_PK" PRIMARY KEY(id_liga)
);

CREATE TABLE jogador
(
    id_jogador   NUMBER(*, 0) NOT NULL ENABLE,
    nome         VARCHAR2(200 byte) NOT NULL ENABLE,
    idade        NUMBER NOT NULL ENABLE,
    cidade       VARCHAR2(200 byte) NOT NULL ENABLE,
    id_posicao   NUMBER NOT NULL ENABLE,
    id_clube     NUMBER NOT NULL ENABLE,
    CONSTRAINT "JOGADOR_PK" PRIMARY KEY(id_jogador)
);

CREATE TABLE patrocinador
(
    id_patrocinador NUMBER(*, 0) NOT NULL ENABLE,
    nome             VARCHAR2(200 byte) NOT NULL ENABLE,
    montante         NUMBER NOT NULL ENABLE,
    CONSTRAINT "PATRICINADOR_PK" PRIMARY KEY(id_patrocinador)
);

CREATE TABLE posicao
(
    id_posicao   NUMBER(*, 0) NOT NULL ENABLE,
    desc_posicao VARCHAR2(200 byte) NOT NULL ENABLE,
    CONSTRAINT "POSICAO_PK" PRIMARY KEY(id_posicao)
);
-- c)