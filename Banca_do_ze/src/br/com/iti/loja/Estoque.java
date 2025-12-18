package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Estoque {

	public static void Inicial(Scanner scanner, int id_fk_produto) {
	    String sql = "INSERT INTO estoque (id_fk_produto, quantidade, entrada, saida, est_seguro, data_movimento) VALUES (?, ?, ?, ?, ?, NOW())";
	    
	    String sqlProduto = "SELECT produto FROM produto WHERE id_produto = ?";

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {

	        System.out.println(); 
	        System.out.print("Digite a quantidade inicial para o produto: ");
	        int quantidade;
	        try {
	            quantidade = Integer.parseInt(scanner.nextLine());
	        } catch (NumberFormatException ex) {
	            System.out.println();
	            System.out.println("Quantidade inválida.");
	            return;
	        }

	        System.out.print("Digite o estoque de segurança do produto: ");
	        int est_seguro;
	        try {
	            est_seguro = Integer.parseInt(scanner.nextLine());
	        } catch (NumberFormatException ex) {
	            System.out.println();
	            System.out.println("Estoque de segurança inválido.");
	            return;
	        }

	        stmt.setInt(1, id_fk_produto);
	        stmt.setInt(2, quantidade);
	        stmt.setInt(3, quantidade);
	        stmt.setInt(4, 0);
	        stmt.setInt(5, est_seguro);

	        int linhasAfetadas = stmt.executeUpdate();

	        if (linhasAfetadas > 0) {
	            stmtProduto.setInt(1, id_fk_produto);
	            ResultSet rs = stmtProduto.executeQuery();
	            String nomeProduto = "";
	            if (rs.next()) {
	                nomeProduto = rs.getString("produto");
	            }

	            System.out.println();
	            System.out.println("Estoque inicial registrado com sucesso!");
	            System.out.printf("ID: %d | Produto: %s | Quantidade: %d | Estoque mínimo: %d%n",
	                    id_fk_produto, nomeProduto, quantidade, est_seguro);
	        } else {
	            System.out.println();
	            System.out.println("Erro ao inserir dados.");
	        }

	    } catch (SQLException e) {
	        System.out.println();
	        System.out.println("Erro ao registrar estoque inicial: " + e.getMessage());
	    }
	}

    public static void entrada(Scanner scanner) {
        String sqlSaldo = "SELECT quantidade, est_seguro FROM estoque WHERE id_fk_produto = ? ORDER BY id_movimento DESC LIMIT 1";
        String sqlEntrada = "INSERT INTO estoque (id_fk_produto, quantidade, entrada, saida, est_seguro, data_movimento) VALUES (?, ?, ?, ?, ?, NOW())";
        
        String sqlProduto = "SELECT produto FROM produto WHERE id_produto = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtSaldo = conn.prepareStatement(sqlSaldo);
             PreparedStatement stmtEntrada = conn.prepareStatement(sqlEntrada);
        	 PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {

            System.out.println();
            System.out.print("Digite o ID do produto: ");
            int id_fk_produto;
            try {
                id_fk_produto = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println();
                System.out.println("Número de ID inválido.");
                return;
            }
            
            stmtProduto.setInt(1, id_fk_produto);
            ResultSet rsProd = stmtProduto.executeQuery();
            String nomeProduto = "";
            if (rsProd.next()) {
                nomeProduto = rsProd.getString("produto");
            }
            

            System.out.print("Digite a quantidade comprada do produto: ");
            int qtdEntrada;
            try {
                qtdEntrada = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println();
                System.out.println("Quantidade inválida.");
                return;
            }

            stmtSaldo.setInt(1, id_fk_produto);
            ResultSet rs = stmtSaldo.executeQuery();

            int saldo = 0;
            int estSeguro = 0;

            if (rs.next()) {
                saldo = rs.getInt("quantidade");
                estSeguro = rs.getInt("est_seguro");
            } else {
                System.out.println();
                System.out.println("Produto sem cadastro inicial!");
                return;
            }

            int novoSaldo = saldo + qtdEntrada;

            stmtEntrada.setInt(1, id_fk_produto);
            stmtEntrada.setInt(2, novoSaldo);
            stmtEntrada.setInt(3, qtdEntrada);
            stmtEntrada.setInt(4, 0);
            stmtEntrada.setInt(5, estSeguro);

            int linhasAfetadas = stmtEntrada.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println();
                System.out.println("Entrada registrada com sucesso!");
                System.out.printf("ID: %d | Produto: %s | Quantidade atual: %d | Estoque mínimo: %d%n",
                        id_fk_produto, nomeProduto, novoSaldo, estSeguro);
            } else {
                System.out.println();
                System.out.println("Erro ao registrar entrada.");
            }

        } catch (SQLException e) {
            System.out.println();
            System.out.println("Erro ao registrar entrada: " + e.getMessage());
        }
    }

    public static void saida(Scanner scanner) {
        String sqlSaldo = "SELECT quantidade, est_seguro FROM estoque WHERE id_fk_produto = ? ORDER BY id_movimento DESC LIMIT 1";
        String sqlSaida = "INSERT INTO estoque (id_fk_produto, quantidade, entrada, saida, est_seguro, data_movimento) VALUES (?, ?, ?, ?, ?, NOW())";
        
        String sqlProduto = "SELECT produto FROM produto WHERE id_produto = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtSaldo = conn.prepareStatement(sqlSaldo);
             PreparedStatement stmtSaida = conn.prepareStatement(sqlSaida);
        	 PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {

            System.out.println();
            System.out.print("Digite o ID do produto: ");
            int id_fk_produto;
            try {
                id_fk_produto = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println();
                System.out.println("Número de ID inválido.");
                return;
            }
            
            stmtProduto.setInt(1, id_fk_produto);
            ResultSet rsProd = stmtProduto.executeQuery();
            String nomeProduto = "";
            if (rsProd.next()) {
                nomeProduto = rsProd.getString("produto");
            }

            System.out.print("Digite a quantidade vendida do produto: ");
            int qtdSaida;
            try {
                qtdSaida = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println();
                System.out.println("Quantidade inválida.");
                return;
            }

            stmtSaldo.setInt(1, id_fk_produto);
            ResultSet rs = stmtSaldo.executeQuery();

            int saldo = 0;
            int estSeguro = 0;

            if (rs.next()) {
                saldo = rs.getInt("quantidade");
                estSeguro = rs.getInt("est_seguro");
            } else {
                System.out.println();
                System.out.println("Produto sem cadastro inicial!");
                return;
            }

            if (qtdSaida > saldo) {
                System.out.println();
                System.out.println("Erro: quantidade vendida maior que o saldo disponível!");
                System.out.printf("ID: %d | Produto: %s | Saldo atual: %d | Tentativa de saída: %d%n",
                        id_fk_produto, nomeProduto, saldo, qtdSaida);
                return;
            }

            int novoSaldo = saldo - qtdSaida;

            stmtSaida.setInt(1, id_fk_produto);
            stmtSaida.setInt(2, novoSaldo);
            stmtSaida.setInt(3, 0);
            stmtSaida.setInt(4, qtdSaida);
            stmtSaida.setInt(5, estSeguro);

            int linhasAfetadas = stmtSaida.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println();
                System.out.println("Saída registrada com sucesso!");
                System.out.printf("Produto ID: %d | Quantidade atual: %d | Estoque mínimo: %d%n",
                        id_fk_produto, novoSaldo, estSeguro);
            } else {
                System.out.println();
                System.out.println("Erro ao registrar saída.");
            }

        } catch (SQLException e) {
            System.out.println();
            System.out.println("Erro ao registrar saída: " + e.getMessage());
        }
    }

    public static void listarTodos() {
        String sql = "SELECT p.id_produto, p.produto, e.quantidade, e.est_seguro " +
                     "FROM produto p " +
                     "JOIN estoque e ON p.id_produto = e.id_fk_produto " +
                     "WHERE e.id_movimento = ( " +
                     	"SELECT MAX(e2.id_movimento) " +
                     	"FROM estoque e2 " +
                     	"WHERE e2.id_fk_produto = p.id_produto " +
                     ") " +
                     "ORDER BY p.produto ASC";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println();
            System.out.println("========== ESTOQUE DE PRODUTOS ==========");
            System.out.printf("%-3s | %-30s | %-9s | %-10s%n",
                              "ID", "PRODUTO", "QTD", "ESTQ. MIN.");
            System.out.println("----+--------------------------------+-----------+-----------");

            boolean encontrou = false;
            while (rs.next()) {
                encontrou = true;
                System.out.printf("%-3d | %-30s | %-9d | %-9d%n",
                        rs.getInt("id_produto"),
                        rs.getString("produto"),
                        rs.getInt("quantidade"),
                        rs.getInt("est_seguro"));
            }
            if (!encontrou) {
                System.out.println();
                System.out.println("Nenhum produto em estoque.");
            }

        } catch (SQLException e) {
            System.out.println();
            System.out.println("Erro ao listar estoque: " + e.getMessage());
        }
    }
    
    public static void ajustar(Scanner scanner) {
        String sqlBuscaUltimo = "SELECT id_movimento, quantidade, est_seguro " +
                                "FROM estoque " +
                                "WHERE id_fk_produto = ? " +
                                "ORDER BY id_movimento DESC " +
                                "LIMIT 1";
        
        String sqlUpdate = "UPDATE estoque " +
                           "SET quantidade = ?, entrada = ?, saida = ?, data_movimento = NOW() " +
                           "WHERE id_movimento = ?";
        
        String sqlProduto = "SELECT produto FROM produto WHERE id_produto = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtBusca = conn.prepareStatement(sqlBuscaUltimo);
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
             PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {

            System.out.println();
            System.out.print("Digite o ID do produto para ajustar: ");
            int id_fk_produto;
            try {
                id_fk_produto = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println();
                System.out.println("ID inválido.");
                return;
            }

            stmtBusca.setInt(1, id_fk_produto);
            ResultSet rs = stmtBusca.executeQuery();

            int idMovimento;
            int saldoAtual;

            if (rs.next()) {
                idMovimento = rs.getInt("id_movimento");
                saldoAtual = rs.getInt("quantidade");
            } else {
                System.out.println();
                System.out.println("Produto sem cadastro.");
                return;
            }

            stmtProduto.setInt(1, id_fk_produto);
            ResultSet rsProd = stmtProduto.executeQuery();
            String nomeProduto = "";
            if (rsProd.next()) {
                nomeProduto = rsProd.getString("produto");
            }

            System.out.print("Digite o novo saldo desejado: ");
            int novoSaldo;
            try {
                novoSaldo = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println();
                System.out.println("Quantidade inválida.");
                return;
            }

            if (novoSaldo < 0) {
                System.out.println();
                System.out.println("Erro: o saldo não pode ser negativo!");
                System.out.printf("Produto ID: %d | Produto: %s | Saldo atual: %d%n",
                        id_fk_produto, nomeProduto, saldoAtual);
                return;
            }

            int entrada = 0;
            int saida = 0;
            if (novoSaldo > saldoAtual) {
                entrada = novoSaldo - saldoAtual;
            } else if (novoSaldo < saldoAtual) {
                saida = saldoAtual - novoSaldo;
            }

            stmtUpdate.setInt(1, novoSaldo);
            stmtUpdate.setInt(2, entrada);
            stmtUpdate.setInt(3, saida);
            stmtUpdate.setInt(4, idMovimento);

            int linhasAfetadas = stmtUpdate.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println();
                System.out.println("Ajuste de estoque realizado com sucesso!");
                System.out.printf("Produto ID: %d | Produto: %s | Saldo anterior: %d | Novo saldo: %d%n",
                        id_fk_produto, nomeProduto, saldoAtual, novoSaldo);
            } else {
                System.out.println();
                System.out.println("Nenhuma linha atualizada. Verifique o produto.");
            }

        } catch (SQLException e) {
            System.out.println();
            System.out.println("Erro ao ajustar estoque: " + e.getMessage());
        }
    }
}