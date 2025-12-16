package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstoqueListarTodos {
	
	public static void listarTodos() {
		String sql = "SELECT p.id_produto, p.produto, e.quantidade, e.est_seguro " + 
					 "FROM produto p " +
					 "JOIN estoque e ON p.id_produto = e.id_fk_produto " +
					 "ORDER BY p.produto ASC";
					 		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			
			System.out.println("========== ESTOQUE DE PRODUTOS ==========");
			System.out.println("");
			System.out.printf("%-3s | %-30s | %-9s | %-10s%n",
							  "ID", "PRODUTO", "QTD", "ESTQ. MIN.");
			System.out.println("----+--------------------------------+-----------+-----------");
			
			while (rs.next()) {
				int id = rs.getInt("id_produto");
				String produto = rs.getString("produto");
				int quantidade = rs.getInt("quantidade");
				int est_seguro = rs.getInt("est_seguro");
				
				System.out.printf("%-3d | %-30s | %-9d | %-9d%n",
						id, produto, quantidade, est_seguro);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao listar produtos");
			e.printStackTrace();
		}
	}
}