package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ListarTodosCliente {
	
	public static void listarTodos() {
		String sql = "SELECT id_cliente, nome, telefone, email FROM cliente ORDER BY nome";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			
			System.out.println("===== LISTA DE CLIENTES =====");
			
			while (rs.next()) {
				int id = rs.getInt("id_cliente");
				String nome = rs.getString("nome");
				String telefone = rs.getString("telefone");
				String email = rs.getString("email");
				
				System.out.printf("ID: %d | Nome: %s | Telefone: %s | E-mail: %s%n",
						id, nome, telefone, email);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao listar clientes");
			e.printStackTrace();
		}
	}
}