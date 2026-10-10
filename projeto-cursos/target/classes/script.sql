-- Criação do banco de dados (Execute essa linha separadamente no seu SGBD se necessário)
-- CREATE DATABASE avaliacao_persistencia;

-- Conecte-se ao banco avaliacao_persistencia e execute:

CREATE TABLE curso (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    modalidade VARCHAR(50) NOT NULL CHECK (modalidade IN ('Presencial', 'EAD', 'Híbrido')),
    cargaHoraria INT NOT NULL CHECK (cargaHoraria > 0),
    valor DECIMAL(10, 2) NOT NULL CHECK (valor >= 0)
);

-- Inserindo registros para testes
INSERT INTO curso (nome, modalidade, cargaHoraria, valor) VALUES
('Engenharia de Software', 'Presencial', 3200, 1500.00),
('Desenvolvimento Web', 'EAD', 1200, 800.00),
('Ciência de Dados', 'Híbrido', 2400, 1200.00);

