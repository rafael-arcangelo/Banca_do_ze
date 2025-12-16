package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class VendaItens {
	
    public static void cadastrarItens(int id_venda) {
        String sql = "INSERT INTO itens_venda (id_fk_venda, id_fk_produto, quantidade, valor_unitario, valor_total) VALUES (?, ?, ?, ?, ?)";

        try (Scanner scanner = new Scanner(System.in);
             Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.print("Digite o ID do produto d venda: ");
            int id_produto = Integer.parseInt(scanner.nextLine());

            System.out.print("Digite a quantidade do produto: ");
            int qtd = Integer.parseInt(scanner.nextLine());

            // buscar preço do produto
            double valorUnitario = precoProduto(conn, id_produto);
            double valorTotal = qtd * valorUnitario;

            stmt.setInt(1, id_venda);
            stmt.setInt(2, id_produto);
            stmt.setInt(3, qtd);
            stmt.setDouble(4, valorUnitario);
            stmt.setDouble(5, valorTotal);

            stmt.executeUpdate();
            System.out.println("Item registrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao registrar item.");
            e.printStackTrace();
        }
    }

    private static double precoProduto(Connection conn, int id_produto) throws SQLException {
        String sql = "SELECT preco_venda FROM produto WHERE id_produto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_produto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("preco_venda");
            }
        }
        throw new SQLException("Produto não encontrado!");
    }
}