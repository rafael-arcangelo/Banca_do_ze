package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ClienteListarTodos {
	
	public static void listarTodos() {
		String sql = "SELECT id_cliente, nome, telefone, email FROM cliente ORDER BY nome";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			
			System.out.println("========== LISTA DE CLIENTES ==========");
			System.out.println("");
			System.out.printf("%-3s | %-25s | %-15s | %-15s%n", "ID", "NOME", "TELEFONE", "E-MAIL");
			System.out.println("----+---------------------------+-----------------+--------------------");
			
			while (rs.next()) {
				int id = rs.getInt("id_cliente");
				String nome = rs.getString("nome");
				String telefone = rs.getString("telefone");
				String email = rs.getString("email");
				
				System.out.printf("%-3d | %-25s | %-15s | %-15s%n",
						id, nome, telefone, email);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao listar clientes");
			e.printStackTrace();
		}
	}
}