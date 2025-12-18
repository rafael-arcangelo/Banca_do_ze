package br.com.iti.loja;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Relatorio {
	
	public static void produtosMaisVendidos(Scanner scanner) {
	    String sql = "SELECT pr.id_produto, pr.produto, SUM(i.quantidade) AS qtd_total, SUM(i.valor_total) AS valor_total " +
	                 "FROM itens_venda i " +
	                 "JOIN produto pr ON i.id_fk_produto = pr.id_produto " +
	                 "GROUP BY pr.id_produto, pr.produto " +
	                 "ORDER BY qtd_total DESC";

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	    	System.out.println();
	    	System.out.println("========== RELATÓRIO PRODUTOS MAIS VENDIDOS ==========");
	        System.out.println();
	        System.out.printf("%-3s | %-30s | %-10s | %-12s%n", "ID", "PRODUTO", "QTD VENDIDA", "R$ TOTAL");
	        System.out.println("----+--------------------------------+------------+--------------");

	        boolean encontrou = false;
	        while (rs.next()) {
	            encontrou = true;
	            int id = rs.getInt("id_produto");
	            String produto = rs.getString("produto");
	            int qtd_total = rs.getInt("qtd_total");
	            double valor_total = rs.getDouble("valor_total");

	            System.out.printf("%-3d | %-30s | %-10d | %-12.2f%n",
	                    id, produto, qtd_total, valor_total);
	        }

	        if (!encontrou) {
	            System.out.println();
	            System.out.println("Nenhum produto vendido até o momento.");
	        }

	    } catch (SQLException e) {
	    	System.out.println();
	        System.out.println("Erro ao gerar relatório de produtos mais vendidos.");
	        e.printStackTrace();
	    }
	}
	
	public static void estoqueMinimo(Scanner scanner) {
	    String sql = "SELECT p.id_produto, p.produto, e.quantidade, e.est_seguro " +
	                 "FROM produto p " +
	                 "JOIN estoque e ON p.id_produto = e.id_fk_produto " +
	                 "WHERE e.id_movimento = ( " +
	                 "    SELECT MAX(e2.id_movimento) " +
	                 "    FROM estoque e2 " +
	                 "    WHERE e2.id_fk_produto = p.id_produto " +
	                 ") " +
	                 "AND e.quantidade < e.est_seguro " +
	                 "ORDER BY p.produto ASC";

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	    	System.out.println();
	    	System.out.println("========== RELATORIO PRODUTOS ESTOQUE BAIXO ==========");
	        System.out.println();
	        System.out.printf("%-3s | %-30s | %-9s | %-9s%n", "ID", "PRODUTO", "EST ATUAL", "EST SEGURO");
	        System.out.println("----+--------------------------------+-----------+------------");

	        boolean encontrou = false;
	        while (rs.next()) {
	            encontrou = true;
	            int id = rs.getInt("id_produto");
	            String produto = rs.getString("produto");
	            int est_atual = rs.getInt("quantidade");
	            int est_seguro = rs.getInt("est_seguro");

	            System.out.printf("%-3d | %-30s | %-9d | %-9d%n",
	                    id, produto, est_atual, est_seguro);
	        }

	        if (!encontrou) {
	            System.out.println();
	            System.out.println("Nenhum produto abaixo do estoque mínimo.");
	        }

	    } catch (SQLException e) {
	    	System.out.println();
	        System.out.println("Erro ao realizar a busca.");
	        e.printStackTrace();
	    }
	}
	
	public static void pgtoListarTodos() {
		String sql = "SELECT id_pgto, nome FROM pagamento ORDER BY nome";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			
			System.out.println();
			System.out.println("===== LISTA DE FORMAS DE PAGAMENTO =====");
			System.out.println();
			System.out.printf("%-3s | %-25s%n", "ID", "NOME");
			System.out.println("----+---------------------------");
			
			while (rs.next()) {
				int id = rs.getInt("id_pgto");
				String nome = rs.getString("nome");
				
				System.out.printf("%-3d | %-15s%n",
						id, nome);
			}
				
		} catch (SQLException e) {
			System.out.println();
			System.out.println("Erro ao listar clientes");
			e.printStackTrace();
		}
	}
	
	public static void vendasCliente(Scanner scanner) {
	    String sql = "SELECT v.id_venda, v.data_venda, " +
	                        "c.nome AS cliente, " +
	                        "p.nome AS pagamento, " +
	                        "SUM(i.valor_total) AS valor_total " +
	                 "FROM venda v " +
	                 "JOIN itens_venda i ON v.id_venda = i.id_fk_venda " +
	                 "JOIN cliente c ON v.id_fk_cliente = c.id_cliente " +
	                 "JOIN pagamento p ON v.id_fk_pgto = p.id_pgto " +
	                 "WHERE c.id_cliente = ? " +
	                 "GROUP BY v.id_venda, v.data_venda, c.nome, p.nome";

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        System.out.print("Digite o ID do cliente que deseja buscar: ");
	        int buscaID = Integer.parseInt(scanner.nextLine());
	        System.out.println();

	        stmt.setInt(1, buscaID);

	        ResultSet rs = stmt.executeQuery();

	        System.out.println();
	        System.out.println("========== RELATORIO VENDAS POR CLIENTE ==========");
	        System.out.println();
	        System.out.printf("%-3s | %-12s | %-20s | %-10s |%-9s%n",
	                          "ID", "DATA VENDA", "CLIENTE", "PAGAMENTO", "R$ TOTAL");
	        System.out.println("----+--------------+----------------------+------------+---------");

	        boolean encontrou = false;
	        double somaTotal = 0;

	        while (rs.next()) {
	            encontrou = true;
	            int id = rs.getInt("id_venda");
	            Date data_venda = rs.getDate("data_venda");
	            String cliente = rs.getString("cliente");
	            String pgto = rs.getString("pagamento");
	            Double v_total = rs.getDouble("valor_total");

	            somaTotal += v_total;

	            System.out.printf("%-3d | %-12s | %-20s | %-10s | %-9.2f%n",
	                    id, data_venda, cliente, pgto, v_total);
	        }

	        if (!encontrou) {
	        	System.out.println();
	            System.out.println("Nenhuma venda encontrada para este cliente.");
	        } else {
	            System.out.println("--------------------------------------------------");
	            System.out.println();
	            System.out.printf("TOTAL DE VENDAS DO CLIENTE: R$ %.2f%n", somaTotal);
	        }

	    } catch (SQLException e) {
	    	System.out.println();
	        System.out.println("Erro ao realizar a busca.");
	        e.printStackTrace();
	    }
	}
	
	public static void vendasPeriodo(Scanner scanner) {
	    String sql = "SELECT v.id_venda, v.data_venda, " +
	                        "c.nome AS cliente, " +
	                        "p.nome AS pagamento, " +
	                        "SUM(i.valor_total) AS valor_total " +
	                 "FROM venda v " +
	                 "JOIN itens_venda i ON v.id_venda = i.id_fk_venda " +
	                 "JOIN cliente c ON v.id_fk_cliente = c.id_cliente " +
	                 "JOIN pagamento p ON v.id_fk_pgto = p.id_pgto " +
	                 "WHERE v.data_venda BETWEEN ? AND ? " +
	                 "GROUP BY v.id_venda, v.data_venda, c.nome, p.nome";

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        System.out.print("Digite a data inicial (YYYY-MM-DD): ");
	        Date data1 = Date.valueOf(scanner.nextLine());

	        System.out.print("Digite a data final (YYYY-MM-DD): ");
	        Date data2 = Date.valueOf(scanner.nextLine());
	        System.out.println();

	        stmt.setDate(1, data1);
	        stmt.setDate(2, data2);

	        ResultSet rs = stmt.executeQuery();

	        System.out.println();
	        System.out.println("========== RELATORIO VENDAS POR PERÍODO ==========");
	        System.out.println();
	        System.out.printf("%-3s | %-12s | %-20s | %-10s |%-9s%n",
	                          "ID", "DATA VENDA", "CLIENTE", "PAGAMENTO", "R$ TOTAL");
	        System.out.println("----+--------------+----------------------+------------+---------");

	        boolean encontrou = false;
	        double somaTotal = 0;

	        while (rs.next()) {
	            encontrou = true;
	            int id = rs.getInt("id_venda");
	            Date data_venda = rs.getDate("data_venda");
	            String cliente = rs.getString("cliente");
	            String pgto = rs.getString("pagamento");
	            Double v_total = rs.getDouble("valor_total");

	            somaTotal += v_total;

	            System.out.printf("%-3d | %-12s | %-20s | %-10s | %-9.2f%n",
	                    id, data_venda, cliente, pgto, v_total);
	        }

	        if (!encontrou) {
	        	System.out.println();
	            System.out.println("Nenhuma venda encontrada no período informado.");
	        } else {
	        	System.out.println();
	            System.out.println("--------------------------------------------------");
	            System.out.printf("TOTAL DE VENDAS NO PERÍODO: R$ %.2f%n", somaTotal);
	        }

	    } catch (SQLException e) {
	    	System.out.println();
	        System.out.println("Erro ao realizar a busca.");
	        e.printStackTrace();
	    }
	}
}
