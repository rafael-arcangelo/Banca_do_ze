package br.com.iti.loja;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/banca_do_ze";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado ao banco de dados com sucesso");
            return conn;
            
        } catch (ClassNotFoundException e) {
        	System.out.println("Driver do MySql não encontrado. Verifique o Build Path");
        	e.printStackTrace();
        	return null;
        
        } catch (SQLException e) {
        	System.out.println("Erro ao conectar ao banco de dados");
        	e.printStackTrace();
        	return null;
        }
    }
}