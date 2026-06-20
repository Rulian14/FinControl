INSERT INTO usuario (nome, email, senha_hash, data_criacao)
SELECT 'Gustavo Teste', 'gustavo@email.com', '$2a$10$E2IdY..ficticio', CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM usuario
);

-- 2. CARGA INICIAL DE CATEGORIAS
INSERT INTO categoria (nome, tipo)
SELECT * FROM (
    VALUES
        ('Salário', 'RECEITA'),
        ('Freelance', 'RECEITA'),
        ('Investimentos', 'RECEITA'),
        ('Venda de Itens', 'RECEITA'),
        ('Outros Ganhos', 'RECEITA'),

        ('Aluguel', 'DESPESA'),
        ('Condomínio', 'DESPESA'),
        ('Energia Elétrica', 'DESPESA'),
        ('Água', 'DESPESA'),
        ('Gás', 'DESPESA'),
        ('Internet', 'DESPESA'),
        ('Telefone', 'DESPESA'),
        ('Mercado', 'DESPESA'),
        ('Transporte', 'DESPESA'),
        ('Carro', 'DESPESA'),
        ('Cartão de Crédito', 'DESPESA'),
        ('Educação', 'DESPESA'),
        ('Saúde', 'DESPESA'),
        ('Empréstimos', 'DESPESA'),
        ('Lazer', 'DESPESA'),
        ('Outras Despesas', 'DESPESA')
) AS dados(nome, tipo)
WHERE NOT EXISTS (
    SELECT 1 FROM categoria
);