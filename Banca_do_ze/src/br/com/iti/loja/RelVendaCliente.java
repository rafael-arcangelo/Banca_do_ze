package br.com.iti.loja;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RelVendaCliente {
	
	public static void vendasCliente(Scanner scanner) {
		String sql = "SELECT v.id_venda, v.data_venda, " + 
							"c.nome AS cliente, " +
							"p.nome AS pagamento, " +
							"SUM(i.valor_total) AS valor_total " +
					 "FROM venda v " +
					 "JOIN itens_venda i ON v.id_venda = i.id_fk_venda " +
					 "JOIN cliente c ON v.id_fk_cliente = c.id_cliente " +
					 "JOIN pagamento p ON v.id_fk_pgto = p.id_pgto " +
					 "WHERE c.nome LIKE ? " +
					 "GROUP BY v.id_venda, v.data_venda, c.nome, p.nome";
		
		try (Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			System.out.print("Digite o nome do cliente que deseja buscar: ");
			String buscaNome = scanner.nextLine();
			System.out.println();
			
			stmt.setString(1, "%" + buscaNome + "%");
			
	        ResultSet rs = stmt.executeQuery();
			
			System.out.println("========== RELATORIO VENDAS POR CLIENTE ==========");
			System.out.println();
			System.out.printf("%-3s | %-12s | %-20s | %-10s |%-9s%n", "ID", "DATA VENDA", "CLIENTE", "PAGAMENTO", "R$ TOTAL");
			System.out.println("----+--------------+----------------------+------------+---------");
			
			while (rs.next()) {
				int id = rs.getInt("id_venda");
				Date data_venda = rs.getDate("data_venda");
				String cliente = rs.getString("cliente");
				String pgto = rs.getString("pagamento");
				Double v_total = rs.getDouble("valor_total");
				
				System.out.printf("%-3d | %-12s | %-20s | %-10s | %-9.2f%n",
						id, data_venda, cliente, pgto, v_total);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao realizar a busca.");
			e.printStackTrace();
		}
	}
}