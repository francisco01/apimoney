CREATE TABLE pessoa(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	logradouro VARCHAR(50) NOT NULL,
	numero VARCHAR(5) NOT NULL,
	complemento VARCHAR(50) NOT NULL,
	bairro VARCHAR(50) NOT NULL,
	cep VARCHAR(50) NOT NULL,
	cidade VARCHAR(50) NOT NULL,
	estado VARCHAR(50) NOT NULL,
	ativo TINYINT(1)NOT NULL

)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES ('João Alves', 'Rua Umbixeiro', '33', 'Casa', 'Pêra', '48970-000', 'Senhor do Bonfim', 'BA', '1');
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES ('Pedro Alves', 'Rua Amazonas', '40', 'Apto 21', 'Pituba', '40210-700', 'Salvador', 'BA', '0');
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES ('Maria Julia', 'Rua Alta', '100', 'Casa', 'Morumbi', '337890-009', 'São Paulo', 'SP', '1');
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES ('Neto', 'Rua Severo', '340', 'Apto 23', 'Federação', '40210-700', 'Salvador', 'BA', '1');