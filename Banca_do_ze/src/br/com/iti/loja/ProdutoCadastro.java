package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ProdutoCadastro {

	public static void cadastrar() {
		String sql = "INSERT INTO produto (produto, descricao, preco_compra, preco_venda) VALUES (?, ?, ?, ?)";
		
		try (Scanner scanner = new Scanner(System.in)) {
			
			System.out.print("Digite o produto: ");
			String produto = scanner.nextLine();
			
			System.out.print("Digite uma descrição para o produto: ");
			String descricao = scanner.nextLine();
			
			System.out.print("Digite o valor de compra do produto: ");
			String preco_compra = scanner.nextLine();
			
			System.out.print("Digite o valor de venda do produto: ");
			String preco_venda = scanner.nextLine();
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				try (Connection conn = Conexao.getConnection();
					 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					
					stmt.setString(1, produto);
					stmt.setString(2, descricao);
					stmt.setString(3, preco_compra);
					stmt.setString(4, preco_venda);
					
					int linhasAfetadas = stmt.executeUpdate();
					
					if (linhasAfetadas > 0) {
						try(ResultSet rs = stmt.getGeneratedKeys()) {
							if (rs.next()) {
								int idGerado = rs.getInt(1);
								System.out.println("Produto cadastrado com sucesso! ID gerado: " + 	idGerado);
							} else {
								System.out.println("Produto cadastrado com sucesso! (erro ao obter ID)");
							}
						}
					} else {
						System.out.println("Erro ao inserir dados.");
					}
				}
			} catch (ClassNotFoundException e) {
				System.out.println("Driver do MySQL não encontrado. Verifique o BUild Path");
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("Erro ao conectar no banco de dados.");
				e.printStackTrace();
			}
		}
	}
}