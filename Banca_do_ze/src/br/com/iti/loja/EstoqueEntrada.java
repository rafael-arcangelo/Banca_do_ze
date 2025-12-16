package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EstoqueEntrada {

    public static void entrada(Scanner scanner) {
        String sqlSaldo = "SELECT quantidade, est_seguro FROM estoque WHERE id_fk_produto = ? ORDER BY id_movimento DESC LIMIT 1";
        String sqlEntrada = "INSERT INTO estoque (id_fk_produto, quantidade, entrada, saida, est_seguro, data_movimento) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtSaldo = conn.prepareStatement(sqlSaldo);
             PreparedStatement stmtEntrada = conn.prepareStatement(sqlEntrada)) {

            System.out.print("Digite o ID do produto: ");
            int id_fk_produto = Integer.parseInt(scanner.nextLine());

            System.out.print("Digite a quantidade comprada do produto: ");
            int qtdEntrada = Integer.parseInt(scanner.nextLine());

            stmtSaldo.setInt(1, id_fk_produto);
            ResultSet rs = stmtSaldo.executeQuery();

            int saldo = 0;
            int estSeguro = 0;

            if (rs.next()) {
                saldo = rs.getInt("quantidade");
                estSeguro = rs.getInt("est_seguro");
            } else {
                System.out.println("Produto sem cadastro inicial!");
                return;
            }

            int novoSaldo = saldo + qtdEntrada;

            stmtEntrada.setInt(1, id_fk_produto);
            stmtEntrada.setInt(2, novoSaldo);
            stmtEntrada.setInt(3, qtdEntrada);
            stmtEntrada.setInt(4, 0);
            stmtEntrada.setInt(5, estSeguro);

            int linhasAfetadas = stmtEntrada.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Entrada registrada com sucesso!");
            } else {
                System.out.println("Erro ao registrar dados.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco de dados.");
            e.printStackTrace();
        }
    }
}