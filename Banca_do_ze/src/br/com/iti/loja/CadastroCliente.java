package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class CadastroCliente {

	public static void cadastrar() {
		String sql = "INSERT INTO cliente (nome, telefone, email, senha) VALUES (?, ?, ?, ?)";
		
		try (Scanner scanner = new Scanner(System.in)) {
			
			System.out.print("Nome do cliente: ");
			String nome = scanner.nextLine();
			
			System.out.print("Telefone do cliente: ");
			String telefone = scanner.nextLine();
			
			System.out.print("E-mail do cliente: ");
			String email = scanner.nextLine();
			
			System.out.print("Senha do cliente: ");
			String senha = scanner.nextLine();
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				try (Connection conn = Conexao.getConnection();
					 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					
					stmt.setString(1, nome);
					stmt.setString(2, telefone);
					stmt.setString(3, email);
					stmt.setString(4, senha);
					
					int linhasAfetadas = stmt.executeUpdate();
					
					if (linhasAfetadas > 0) {
						try(ResultSet rs = stmt.getGeneratedKeys()) {
							if (rs.next()) {
								int idGerado = rs.getInt(1);
								System.out.println("Cliente cadastrado com sucesso! ID gerado: " + 	idGerado);
							} else {
								System.out.println("Cliente cadastrado com sucesso! (erro ao obter ID)");
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