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
			
			System.out.println("===== LISTA DE PRODUTOS =====");
			
			while (rs.next()) {
				int id = rs.getInt("id_produto");
				String produto = rs.getString("produto");
				String descricao = rs.getString("descricao");
				String preco_compra = rs.getString("preco_compra");
				String preco_venda = rs.getString("preco_venda");
				
				System.out.printf("ID: %d | Produto: %s | $ Compra: %s | $ Venda: %s | Descrição: %s%n",
						id, produto, preco_compra, preco_venda, descricao);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao listar produtos");
			e.printStackTrace();
		}
	}
}