package br.com.iti.loja;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteConexao {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/banca_do_ze";
		String user = "root";
		String password = "123456";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			try (Connection conn = DriverManager.getConnection(url, user, password)) {
				System.out.println("Conectado ao banco de dados com sucesso");
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
