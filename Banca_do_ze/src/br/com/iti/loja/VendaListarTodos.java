package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendaListarTodos {


    public static void listarTodas() {
        String sqlVenda = "SELECT v.id_venda, v.data_venda, c.nome AS cliente, p.nome AS pagamento " +
                          "FROM venda v " +
                          "JOIN cliente c ON v.id_fk_cliente = c.id_cliente " +
                          "JOIN pagamento p ON v.id_fk_pgto = p.id_pgto " +
                          "ORDER BY v.id_venda";

        String sqlTotal = "SELECT SUM(valor_total) AS total_venda " +
                          "FROM itens_venda WHERE id_fk_venda = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda);
             PreparedStatement stmtTotal = conn.prepareStatement(sqlTotal)) {

            ResultSet rsVenda = stmtVenda.executeQuery();

            System.out.println("========== LISTAGEM DE VENDAS ==========");
            while (rsVenda.next()) {
                int id_venda = rsVenda.getInt("id_venda");
                String data_venda = rsVenda.getString("data_venda");
                String cliente = rsVenda.getString("cliente");
                String pagamento = rsVenda.getString("pagamento");

                // calcular total da venda
                stmtTotal.setInt(1, id_venda);
                ResultSet rsTotal = stmtTotal.executeQuery();
                double totalVenda = 0;
                if (rsTotal.next()) {
                    totalVenda = rsTotal.getDouble("total_venda");
                }

                System.out.printf("ID: %-3d | Data: %-10s | Cliente: %-15s | Pgto: %-10s | Total: R$ %.2f%n",
                        id_venda, data_venda, cliente, pagamento, totalVenda);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas.");
            e.printStackTrace();
        }
    }
	
}
