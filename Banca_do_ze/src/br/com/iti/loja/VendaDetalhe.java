package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class VendaDetalhe {

    public static void detalhar(Scanner scanner) {
        String sqlVenda = "SELECT v.id_venda, v.data_venda, c.nome AS cliente, p.nome AS pagamento " +
                          "FROM venda v " +
                          "JOIN cliente c ON v.id_fk_cliente = c.id_cliente " +
                          "JOIN pagamento p ON v.id_fk_pgto = p.id_pgto " +
                          "WHERE v.id_venda = ?";

        String sqlItens = "SELECT i.id_item, pr.produto, i.quantidade, i.valor_unitario, i.valor_total " +
                          "FROM itens_venda i " +
                          "JOIN produto pr ON i.id_fk_produto = pr.id_produto " +
                          "WHERE i.id_fk_venda = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda);
             PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
        	
			System.out.print("Digite o ID da venda que deseja detalhar: ");
			int id_venda = Integer.parseInt(scanner.nextLine());
			System.out.println();

            stmtVenda.setInt(1, id_venda);
            ResultSet rsVenda = stmtVenda.executeQuery();

            if (rsVenda.next()) {
                System.out.println("========== VENDA ==========");
                System.out.println("ID: " + rsVenda.getInt("id_venda"));
                System.out.println("Data: " + rsVenda.getDate("data_venda"));
                System.out.println("Cliente: " + rsVenda.getString("cliente"));
                System.out.println("Pagamento: " + rsVenda.getString("pagamento"));
                System.out.println();
            }

            stmtItens.setInt(1, id_venda);
            ResultSet rsItens = stmtItens.executeQuery();

            double totalVenda = 0;
            System.out.printf("%-3s | %-30s | %-3s | %-11s | %-8s%n", "ID", "PRODUTO", "QTD", "R$ UNITÁRIO", "R$ TOTAL");
            System.out.println("----+--------------------------------+-----+-------------+---------");

            while (rsItens.next()) {
                int id_item = rsItens.getInt("id_item");
                String produto = rsItens.getString("produto");
                int qtd = rsItens.getInt("quantidade");
                double unitario = rsItens.getDouble("valor_unitario");
                double total = rsItens.getDouble("valor_total");

                totalVenda += total;

                System.out.printf("%-3d | %-30s | %-3d | %-11.2f | %-8.2f%n",
                        id_item, produto, qtd, unitario, total);
            }

            System.out.println("-------------------------------------------------------------------");
            System.out.println();
            System.out.printf("TOTAL DA VENDA: R$%.2f", totalVenda);
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Erro ao listar venda.");
            e.printStackTrace();
        }
    }
}