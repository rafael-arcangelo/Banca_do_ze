package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Estoque {

    public static void estoqueInicial(Scanner scanner) {
        String sql = "INSERT INTO estoque (id_fk_produto, quantidade, entrada, saida, est_seguro) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.print("Digite o ID do produto: ");
            int id_fk_produto = Integer.parseInt(scanner.nextLine());

            System.out.print("Digite a quantidade inicial para o produto: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            System.out.print("Digite o estoque de segurança do produto: ");
            int est_seguro = Integer.parseInt(scanner.nextLine());

            stmt.setInt(1, id_fk_produto);
            stmt.setInt(2, quantidade);
            stmt.setInt(3, quantidade);
            stmt.setInt(4, 0);
            stmt.setInt(5, est_seguro);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Estoque inicial registrado com sucesso!");
            } else {
                System.out.println("Erro ao inserir dados.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco de dados.");
            e.printStackTrace();
        }
    }
}