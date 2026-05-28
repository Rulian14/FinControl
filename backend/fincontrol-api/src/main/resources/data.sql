INSERT INTO usuario (nome, email, senha_hash, data_criacao) 
VALUES ('Gustavo Teste', 'gustavo@email.com', '$2a$10$E2IdY..ficticio', CURRENT_TIMESTAMP);

-- 2. CARGA INICIAL DE CATEGORIAS DE RECEITAS
INSERT INTO categoria (nome, tipo) VALUES ('Salário', 'RECEITA');
INSERT INTO categoria (nome, tipo) VALUES ('Freelance', 'RECEITA');
INSERT INTO categoria (nome, tipo) VALUES ('Investimentos', 'RECEITA');

-- 3. CARGA INICIAL DE CATEGORIAS DE DESPESAS
INSERT INTO categoria (nome, tipo) VALUES ('Moradia', 'DESPESA');
INSERT INTO categoria (nome, tipo) VALUES ('Alimentação', 'DESPESA');
INSERT INTO categoria (nome, tipo) VALUES ('Transporte', 'DESPESA');
INSERT INTO categoria (nome, tipo) VALUES ('Lazer', 'DESPESA');