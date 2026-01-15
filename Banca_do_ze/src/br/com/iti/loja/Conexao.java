package br.com.iti.loja;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/Banca_do_ze";
    private static final String USER = "root";
    private static final String PASSWORD = "05171503";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
            
        } catch (ClassNotFoundException e) {
        	throw new RuntimeException("Driver do MySQL não encontrado. Verifique o Build Path.", e);

        
        } catch (SQLException e) {
        	throw new RuntimeException("Erro ao conectar ao banco de dados.", e);

        }
    }
}