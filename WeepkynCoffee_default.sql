DROP DATABASE IF EXISTS db_cafeteria;
CREATE DATABASE db_cafeteria;
USE db_cafeteria;

CREATE TABLE produtos (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    nome       VARCHAR(100) NOT NULL,
    preco      DOUBLE NOT NULL,
    categoria  VARCHAR(50),
    quantidade INT NOT NULL DEFAULT 0,
    ativo      TINYINT NOT NULL DEFAULT 1
);

CREATE TABLE comandas (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    nome_cliente    VARCHAR(100) NOT NULL,
    data_abertura   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          VARCHAR(20) NOT NULL DEFAULT 'Aberta',
    forma_pagamento VARCHAR(30)
);

CREATE TABLE itens_comanda (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    comanda_id     INT NOT NULL,
    produto_id     INT NOT NULL,
    quantidade     INT NOT NULL,
    preco_unitario DOUBLE NOT NULL,
    FOREIGN KEY (comanda_id) REFERENCES comandas(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE TABLE itens_removidos (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    comanda_id   INT NOT NULL,
    produto_id   INT NOT NULL,
    quantidade   INT NOT NULL,
    motivo       VARCHAR(255),
    data_remocao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comanda_id) REFERENCES comandas(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

INSERT INTO produtos (nome, preco, categoria, quantidade) VALUES
    ('Cafe Expresso',      5.00, 'Bebidas Quentes', 50),
    ('Cappuccino',         8.50, 'Bebidas Quentes', 40),
    ('Cafe com Leite',     7.00, 'Bebidas Quentes', 45),
    ('Frappuccino',       12.00, 'Bebidas Frias',   30),
    ('Suco de Laranja',    9.00, 'Bebidas Frias',   20),
    ('Pao de Queijo',      5.50, 'Lanches',          4),
    ('Croissant',          8.00, 'Lanches',          15),
    ('Bolo de Chocolate', 10.00, 'Doces',            10),
    ('Brigadeiro',         4.00, 'Doces',             3);