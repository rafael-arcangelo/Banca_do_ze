CREATE TABLE cliente (
id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(30),
    email VARCHAR(50),
    senha VARCHAR(20)
);
 
CREATE TABLE produto (
id_produto INT PRIMARY KEY AUTO_INCREMENT,
    produto VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(500) NOT NULL,
    preco_compra DECIMAL(10,2) NOT NULL,
    preco_venda DECIMAL(10,2) NOT NULL
);
 
CREATE TABLE estoque (
id_movimento INT PRIMARY KEY AUTO_INCREMENT,
    id_fk_produto INT NOT NULL, FOREIGN KEY (id_fk_produto) REFERENCES produto(id_produto),
    quantidade INT NOT NULL,
entrada INT,
    saida INT,
    est_seguro INT NOT NULL
);
 
CREATE TABLE venda (
id_venda INT PRIMARY KEY AUTO_INCREMENT,
    data_venda DATE NOT NULL,
    id_fk_cliente INT NOT NULL, FOREIGN KEY (id_fk_cliente) REFERENCES cliente(id_cliente),
    id_fk_produto INT NOT NULL, FOREIGN KEY (id_fk_produto) REFERENCES produto(id_produto),
    id_fk_pgto INT NOT NULL, FOREIGN KEY (id_fk_pgto) REFERENCES pagamento(id_pgto)
);
 
CREATE TABLE pagamento (
id_pgto INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(30) NOT NULL
);