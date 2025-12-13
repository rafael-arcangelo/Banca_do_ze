package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListarTodosFormaPgto {
	
	public static void listarTodos() {
		String sql = "SELECT id_pgto, nome FROM pagamento ORDER BY nome";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			
			System.out.println("===== LISTA DE FORMAS DE PAGAMENTO =====");
			
			while (rs.next()) {
				int id = rs.getInt("id_pgto");
				String nome = rs.getString("nome");
				
				System.out.printf("ID: %d | Forma de Pagamento: %s%n",
						id, nome);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao listar clientes");
			e.printStackTrace();
		}
	}
}