package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListarTodosProduto {
	
	public static void listarTodos() {
		String sql = "SELECT id_produto, produto, descricao, preco_compra, preco_venda FROM produto ORDER BY produto";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			
			System.out.println("========== LISTA DE PRODUTOS ==========");
			System.out.println("");
			System.out.printf("%-3s | %-30s | %-9s | %-9s | %-50s%n",
							  "ID", "PRODUTO", "R$ COMPRA", "R$ VENDA", "DESCRIÇÃO");
			System.out.println("----+--------------------------------+-----------+-----------+-------------------------------------------------------------------------");
			
			while (rs.next()) {
				int id = rs.getInt("id_produto");
				String produto = rs.getString("produto");
				String descricao = rs.getString("descricao");
				Double preco_compra = rs.getDouble("preco_compra");
				Double preco_venda = rs.getDouble("preco_venda");
				
				System.out.printf("%-3d | %-30s | %-9.2f | %-9.2f | %-50s%n",
						id, produto, preco_compra, preco_venda, descricao);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao listar produtos");
			e.printStackTrace();
		}
	}
}