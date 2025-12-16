package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RelClienteID {
	
	public static void relClienteID(Scanner scanner) {
		String sql = "SELECT id_cliente, nome, telefone, email FROM cliente WHERE id_cliente = ?";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			System.out.print("Digite o ID do cliente que deseja buscar: ");
			int buscaID = Integer.parseInt(scanner.nextLine());
			
			stmt.setInt(1, buscaID);
			
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("========== RESULTADO DA BUSCA DE CLIENTES ==========");
			System.out.println();
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
			System.out.println("Erro ao realizar a busca.");
			e.printStackTrace();
		}
	}
}