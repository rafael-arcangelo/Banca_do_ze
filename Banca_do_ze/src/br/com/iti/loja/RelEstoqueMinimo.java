package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RelEstoqueMinimo {
	
	public static void estoqueMinimo(Scanner scanner) {
		String sql = "SELECT p.id_produto, p.produto, e.quantidade, e.est_seguro " +
					 "FROM estoque e " +
					 "JOIN produto p ON e.id_fk_produto = p.id_produto " +
					 "WHERE e.quantidade < e.est_seguro";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
	         ResultSet rs = stmt.executeQuery();
			
			System.out.println("========== RELATORIO PRODUTOS ESTOQUE BAIXO ==========");
			System.out.println();
			System.out.printf("%-3s | %-30s | %-9s | %-9s%n", "ID", "PRODUTO", "EST ATUAL", "EST SEGURO");
			System.out.println("----+--------------------------------+-----------+------------");
			
			while (rs.next()) {
				int id = rs.getInt("id_produto");
				String produto = rs.getString("produto");
				int est_atual = rs.getInt("quantidade");
				int est_seguro = rs.getInt("est_seguro");
				
				System.out.printf("%-3d | %-30s | %-9d | %-9d",
						id, produto, est_atual, est_seguro);
				System.out.println();
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao realizar a busca.");
			e.printStackTrace();
		}
	}
}