package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FormaPgtoAtualizar {

	public static void atualizar() {
		String sql = "UPDATE pagamento SET nome = ? WHERE id_pgto = ?";
		
		try (Scanner scanner = new Scanner(System.in);
			 Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			System.out.print("Digite o ID da forma de pagamento que deseja alterar: ");
			int id = Integer.parseInt(scanner.nextLine());
			
			System.out.print("Novo nome para a foma de pagamento: ");
			String nome = scanner.nextLine();

			
			stmt.setString(1, nome);
			stmt.setInt(2, id);
			
			int linhasAfetadas = stmt.executeUpdate();
			
			if (linhasAfetadas > 0) {
				System.out.println("Forma de pagamento atualizada com sucesso!");
			} else {
				System.out.println("ID da forma de pagamento não encontrado");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao atualizar forma da pagamento");
			e.printStackTrace();
		}
	}
}