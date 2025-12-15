package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EstoqueSaida {

    public static void saida() {
        String sqlSaldo = "SELECT quantidade, est_seguro FROM estoque WHERE id_fk_produto = ? ORDER BY id_movimento DESC LIMIT 1";
        String sqlSaida = "INSERT INTO estoque (id_fk_produto, quantidade, entrada, saida, est_seguro, data_movimento) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Scanner scanner = new Scanner(System.in);
             Connection conn = Conexao.getConnection();
             PreparedStatement stmtSaldo = conn.prepareStatement(sqlSaldo);
             PreparedStatement stmtSaida = conn.prepareStatement(sqlSaida)) {

            System.out.print("Digite o ID do produto: ");
            int id_fk_produto = Integer.parseInt(scanner.nextLine());

            System.out.print("Digite a quantidade vendida do produto: ");
            int qtdSaida = Integer.parseInt(scanner.nextLine());

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

            int novoSaldo = saldo - qtdSaida;

            stmtSaida.setInt(1, id_fk_produto);
            stmtSaida.setInt(2, novoSaldo);
            stmtSaida.setInt(3, 0);
            stmtSaida.setInt(4, qtdSaida);
            stmtSaida.setInt(5, estSeguro);

            int linhasAfetadas = stmtSaida.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Saída registrada com sucesso!");
            } else {
                System.out.println("Erro ao registrar dados.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco de dados.");
            e.printStackTrace();
        }
    }
}