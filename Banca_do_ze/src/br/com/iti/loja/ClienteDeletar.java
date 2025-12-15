package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeletarCliente {
	
	public static void deletar() {
		String sql = "DELETE FROM cliente WHERE id_cliente = ?";
		
		try (Scanner scanner = new Scanner(System.in);
			 Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			System.out.print("Digite o ID do cliente que deseja deletar: ");
			int id = Integer.parseInt(scanner.nextLine());
		
			stmt.setInt(1, id);
			
			int linhasAfetadas = stmt.executeUpdate();
			
			if (linhasAfetadas > 0) {
				System.out.println("Cliente deletado com sucesso!");
			} else {
				System.out.println("ID de cliente não encontrado");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao deletar cliente");
			e.printStackTrace();
		}
	}
}