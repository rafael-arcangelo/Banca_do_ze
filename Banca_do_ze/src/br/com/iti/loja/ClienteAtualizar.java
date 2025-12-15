package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;


public class ClienteAtualizar {
	
	public static void atualizar() {
		String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ?, senha = ? WHERE id_cliente = ?";
		
		try (Scanner scanner = new Scanner(System.in);
			 Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			System.out.print("Digite o ID do cliente que deseja alterar: ");
			int id = Integer.parseInt(scanner.nextLine());
			
			System.out.print("Novo nome: ");
			String nome = scanner.nextLine();
			
			System.out.print("Novo telefone: ");
			String telefone = scanner.nextLine();
			
			System.out.print("Novo e-mail: ");
			String email = scanner.nextLine();
			
			System.out.print("Nova senha: ");
			String senha = scanner.nextLine();
			
			stmt.setString(1, nome);
			stmt.setString(2, telefone);
			stmt.setString(3, email);
			stmt.setString(4, senha);
			stmt.setInt(5, id);
			
			int linhasAfetadas = stmt.executeUpdate();
			
			if (linhasAfetadas > 0) {
				System.out.println("Cliente atualizado com sucesso!");
			} else {
				System.out.println("ID de cliente não encontrado");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao atualizar cliente");
			e.printStackTrace();
		}
	}
}