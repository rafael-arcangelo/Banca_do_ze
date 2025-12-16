package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendaListar {

    public static void listarVenda(int id_venda) {
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

            stmtVenda.setInt(1, id_venda);
            ResultSet rsVenda = stmtVenda.executeQuery();

            if (rsVenda.next()) {
                System.out.println("========== VENDA ==========");
                System.out.println("ID: " + rsVenda.getInt("id_venda"));
                System.out.println("Data: " + rsVenda.getDate("data_venda"));
                System.out.println("Cliente: " + rsVenda.getString("cliente"));
                System.out.println("Pagamento: " + rsVenda.getString("pagamento"));
            }

            stmtItens.setInt(1, id_venda);
            ResultSet rsItens = stmtItens.executeQuery();

            double totalVenda = 0;
            System.out.printf("%-3s | %-20s | %-5s | %-10s | %-10s%n", "ID", "PRODUTO", "QTD", "UNITÁRIO", "TOTAL");
            System.out.println("----+----------------------+-----+------------+------------");

            while (rsItens.next()) {
                int id_item = rsItens.getInt("id_item");
                String produto = rsItens.getString("produto");
                int qtd = rsItens.getInt("quantidade");
                double unitario = rsItens.getDouble("valor_unitario");
                double total = rsItens.getDouble("valor_total");

                totalVenda += total;

                System.out.printf("%-3d | %-20s | %-5d | %-10.2f | %-10.2f%n",
                        id_item, produto, qtd, unitario, total);
            }

            System.out.println("---------------------------");
            System.out.println("TOTAL DA VENDA: R$ " + totalVenda);

        } catch (SQLException e) {
            System.out.println("Erro ao listar venda.");
            e.printStackTrace();
        }
    }
}