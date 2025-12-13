package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AtualizarProduto {
	
	public static void atualizar() {
		String sql = "UPDATE produto SET produto = ?, descricao = ?, preco_compra = ?, preco_venda = ? WHERE id_produto = ?";
		
		try (Scanner scanner = new Scanner(System.in);
			 Connection conn = Conexao.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			System.out.print("Digite o ID do produto que deseja alterar: ");
			int id = Integer.parseInt(scanner.nextLine());
			
			System.out.print("Novo nome do produto: ");
			String produto = scanner.nextLine();
			
			System.out.print("Nova descrição do produto: ");
			String descricao = scanner.nextLine();
			
			System.out.print("Novo preço de compra do produto: ");
			String preco_compra = scanner.nextLine();
			
			System.out.print("Novo preco de venda do produto: ");
			String preco_venda = scanner.nextLine();
			
			stmt.setString(1, produto);
			stmt.setString(2, descricao);
			stmt.setString(3, preco_compra);
			stmt.setString(4, preco_venda);
			stmt.setInt(5, id);
			
			int linhasAfetadas = stmt.executeUpdate();
			
			if (linhasAfetadas > 0) {
				System.out.println("Produto atualizado com suceso!");
			} else {
				System.out.println("ID de produto não encontrado");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao atualizar produto");
			e.printStackTrace();
		}
	}
}