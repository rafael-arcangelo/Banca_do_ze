package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class VendaCadastrar {
    
    public static int venda(Scanner scanner) {
        String sql = "INSERT INTO venda (data_venda, id_fk_cliente, id_fk_pgto) VALUES (NOW(), ?, ?)";
        int idGerado = -1;
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            System.out.print("Digite o ID do cliente (ou 1 para Venda Balcão): ");
            int id_cliente = Integer.parseInt(scanner.nextLine());
           
            System.out.print("Digite o ID da forma de pagamento: ");
            int id_pgto = Integer.parseInt(scanner.nextLine());

            stmt.setInt(1, id_cliente);
            stmt.setInt(2, id_pgto);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1);
                        System.out.println("Venda registrada com sucesso! ID gerado: " + idGerado);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao registrar venda.");
            e.printStackTrace();
        }
        return idGerado;
    }
}
