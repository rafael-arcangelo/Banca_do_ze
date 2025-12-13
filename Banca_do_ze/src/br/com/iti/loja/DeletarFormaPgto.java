package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeletarFormaPgto {
	
	public static void deletar() {
		String sql = "DELETE FROM pagamento WHERE id_pgto = ?";
		
		try (Scanner scanner = new Scanner(System.in);
			 Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			System.out.print("Digite o ID da forma de pagamento que deseja deletar: ");
			int id = Integer.parseInt(scanner.nextLine());
		
			stmt.setInt(1, id);
			
			int linhasAfetadas = stmt.executeUpdate();
			
			if (linhasAfetadas > 0) {
				System.out.println("Forma de Pagameno deletada com sucesso!");
			} else {
				System.out.println("ID da forma de pagamento não encontrado");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao deletar forma de pagamento");
			e.printStackTrace();
		}
	}
}