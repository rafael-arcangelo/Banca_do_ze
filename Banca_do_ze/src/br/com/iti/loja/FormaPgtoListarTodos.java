package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormaPgtoListarTodos {
	
	public static void listarTodos() {
		String sql = "SELECT id_pgto, nome FROM pagamento ORDER BY nome";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			
			System.out.println("===== LISTA DE FORMAS DE PAGAMENTO =====");
			System.out.println("");
			System.out.printf("%-3s | %-25s%n", "ID", "NOME");
			System.out.println("----+---------------------------");
			
			while (rs.next()) {
				int id = rs.getInt("id_pgto");
				String nome = rs.getString("nome");
				
				System.out.printf("%-3d | %-15s%n",
						id, nome);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao listar clientes");
			e.printStackTrace();
		}
	}
}