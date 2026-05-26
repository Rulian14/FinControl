CREATE TABLE usuario (
    id_usuario INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    nome VARCHAR(100) NOT NULL,

    email VARCHAR(255) NOT NULL UNIQUE,

    senha_hash VARCHAR(255) NOT NULL,

    data_criacao TIMESTAMP NOT NULL
    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE telefone_usuario (
    id_telefone INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    numero VARCHAR(20) NOT NULL,

    id_usuario INT NOT NULL,

    CONSTRAINT fk_telefone_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario(id_usuario)
    ON DELETE CASCADE
);

CREATE TABLE categoria (

    id_categoria INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    nome VARCHAR(100) NOT NULL,

    tipo VARCHAR(10) NOT NULL,

    CONSTRAINT chk_categoria_tipo
    CHECK (
        tipo IN (
            'RECEITA',
            'DESPESA'
        )
    )
);

CREATE TABLE receita (

    id_receita INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    descricao TEXT NOT NULL,

    valor NUMERIC(10,2) NOT NULL,

    data TIMESTAMP NOT NULL,

    recorrencia VARCHAR(20) NOT NULL,

    id_usuario INT NOT NULL,

    id_categoria INT NOT NULL,

    CONSTRAINT chk_receita_recorrencia
    CHECK (
        recorrencia IN (
            'FIXA',
            'TEMPORARIA'
        )
    ),

    CONSTRAINT fk_receita_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario(id_usuario)
    ON DELETE CASCADE,

    CONSTRAINT fk_receita_categoria
    FOREIGN KEY (id_categoria)
    REFERENCES categoria(id_categoria)
);

CREATE TABLE despesa (

    id_despesa INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    descricao TEXT NOT NULL,

    valor NUMERIC(10,2) NOT NULL,

    data TIMESTAMP NOT NULL,

    recorrencia VARCHAR(20) NOT NULL,

    id_usuario INT NOT NULL,

    id_categoria INT NOT NULL,

    CONSTRAINT chk_despesa_recorrencia
    CHECK (
        recorrencia IN (
            'FIXA',
            'TEMPORARIA'
        )
    ),

    CONSTRAINT fk_despesa_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario(id_usuario)
    ON DELETE CASCADE,

    CONSTRAINT fk_despesa_categoria
    FOREIGN KEY (id_categoria)
    REFERENCES categoria(id_categoria)
);

CREATE TABLE meta_financeira (

    id_meta INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    descricao TEXT NOT NULL,

    valor_objetivo NUMERIC(10,2) NOT NULL,

    id_usuario INT NOT NULL,

    CONSTRAINT fk_meta_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario(id_usuario)
    ON DELETE CASCADE
);