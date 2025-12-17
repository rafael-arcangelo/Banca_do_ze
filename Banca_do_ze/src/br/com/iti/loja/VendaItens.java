package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class VendaItens {
    
    public static void cadastrarItens(Scanner scanner, int idVenda) {
        String sql = "INSERT INTO itens_venda (id_fk_venda, id_fk_produto, quantidade, valor_unitario, valor_total) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection()) {
            
            boolean continuar = true;
            while (continuar) {
                System.out.print("Digite o ID do produto (ou 0 para encerrar): ");
                int id_produto = Integer.parseInt(scanner.nextLine());
                
                if (id_produto == 0) {
                    continuar = false;
                    break;
                }

                System.out.print("Digite a quantidade do produto: ");
                int qtd = Integer.parseInt(scanner.nextLine());

                double valorUnitario = precoProduto(conn, id_produto);
                double valorTotal = qtd * valorUnitario;

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idVenda);
                    stmt.setInt(2, id_produto);
                    stmt.setInt(3, qtd);
                    stmt.setDouble(4, valorUnitario);
                    stmt.setDouble(5, valorTotal);
                    stmt.executeUpdate();
                }

                System.out.println("Item registrado com sucesso!");
                atualizarEstoque(conn, id_produto, qtd);
            }

            System.out.println("Cadastro de itens finalizado.");
            VendaListar.listarVenda(idVenda);
            System.out.println();

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

    private static void atualizarEstoque(Connection conn, int id_produto, int qtd) throws SQLException {
        String sql = "UPDATE estoque SET quantidade = quantidade - ?, saida = saida + ? WHERE id_fk_produto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, qtd);
            stmt.setInt(2, qtd);
            stmt.setInt(3, id_produto);
            stmt.executeUpdate();
            System.out.println("Estoque atualizado com sucesso!");
        }
    }
}