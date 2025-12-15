package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RelProdutoNome {
	
	public static void relProdutoNome() {
		String sql = "SELECT id_produto, produto, descricao, preco_compra, preco_venda FROM produto WHERE produto LIKE ?";
		
		try (Scanner scanner = new Scanner(System.in);
			 Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			System.out.print("Digite o produto que deseja buscar: ");
			String buscaNome = scanner.nextLine();
			System.out.println();
			
			stmt.setString(1, "%"+buscaNome+"%");
			
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("========== RESULTADO DA BUSCA DE PRODUTOS ==========");
			System.out.println();
			System.out.printf("%-3s | %-30s | %-9s | %-9s | %-50s%n",
							  "ID", "PRODUTO", "R$ COMPRA", "R$ VENDA", "DESCRIÇÃO");
			System.out.println("----+--------------------------------+-----------+-----------+-------------------------------------------------------------------------");
			
			while (rs.next()) {
				int id = rs.getInt("id_produto");
				String produto = rs.getString("produto");
				String descricao = rs.getString("descricao");
				Double preco_compra = rs.getDouble("preco_compra");
				Double preco_venda = rs.getDouble("preco_venda");
				
				System.out.printf("%-3d | %-30s | %-9.2f | %-9.2f | %-50s%n",
						id, produto, preco_compra, preco_venda, descricao);
			}
				
		} catch (SQLException e) {
			System.out.println("Erro ao realizar a busca.");
			e.printStackTrace();
		}
	}
}