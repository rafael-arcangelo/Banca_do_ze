package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Produto {

    public static void cadastrar(Scanner scanner) {
        String sql = "INSERT INTO produto (produto, descricao, preco_compra, preco_venda) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println();
        	System.out.print("Digite o nome do produto: ");
            String produto = scanner.nextLine();

            System.out.print("Digite uma descrição para o produto: ");
            String descricao = scanner.nextLine();

            System.out.print("Digite o valor de compra do produto: ");
            double precoCompra;
            try {
                precoCompra = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException ex) {
            	System.out.println();
                System.out.println("Preço de compra inválido.");
                return;
            }

            System.out.print("Digite o valor de venda do produto: ");
            double precoVenda;
            try {
                precoVenda = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException ex) {
            	System.out.println();
                System.out.println("Preço de venda inválido.");
                return;
            }

            stmt.setString(1, produto);
            stmt.setString(2, descricao);
            stmt.setDouble(3, precoCompra);
            stmt.setDouble(4, precoVenda);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        System.out.println();
                        System.out.println("Produto abaixo cadastrado com sucesso:");
                        System.out.printf("ID: %d | Produto: %s | Compra: R$%.2f | Venda: R$%.2f | Descrição: %s%n",
                                id, produto, precoCompra, precoVenda, descricao);
                        
                        Estoque.Inicial(scanner, id);
                    }
                }
            } else {
            	System.out.println();
                System.out.println("Erro ao inserir dados.");
            }
        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    public static void atualizar(Scanner scanner) {
        String sql = "UPDATE produto SET produto = ?, descricao = ?, preco_compra = ?, preco_venda = ? WHERE id_produto = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        	System.out.println();
        	System.out.print("Digite o ID do produto que deseja alterar: ");
            int id;
            try {
                id = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
            	System.out.println();
                System.out.println("ID inválido.");
                return;
            }

            System.out.print("Novo nome do produto: ");
            String produto = scanner.nextLine();

            System.out.print("Nova descrição do produto: ");
            String descricao = scanner.nextLine();

            System.out.print("Novo preço de compra do produto: ");
            double precoCompra;
            try {
                precoCompra = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException ex) {
            	System.out.println();
                System.out.println("Preço de compra inválido.");
                return;
            }

            System.out.print("Novo preço de venda do produto: ");
            double precoVenda;
            try {
                precoVenda = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException ex) {
            	System.out.println();
                System.out.println("Preço de venda inválido.");
                return;
            }

            stmt.setString(1, produto);
            stmt.setString(2, descricao);
            stmt.setDouble(3, precoCompra);
            stmt.setDouble(4, precoVenda);
            stmt.setInt(5, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println();
                System.out.println("Produto atualizado com sucesso!");
                System.out.printf("ID: %d | Produto: %s | Compra: R$%.2f | Venda: R$%.2f | Descrição: %s%n",
                        id, produto, precoCompra, precoVenda, descricao);
            } else {
            	System.out.println();
                System.out.println("ID de produto não encontrado.");
            }
        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public static void deletar(Scanner scanner) {
        String sqlSelect = "SELECT id_produto, produto, descricao, preco_compra, preco_venda FROM produto WHERE id_produto = ?";
        
        String sqlDelete = "DELETE FROM produto WHERE id_produto = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
             PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {

        	System.out.println();
        	System.out.print("Digite o ID do produto que deseja deletar: ");
            int id;
            try {
                id = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
            	System.out.println();
                System.out.println("ID inválido.");
                return;
            }

            stmtSelect.setInt(1, id);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                String produto = rs.getString("produto");
                String descricao = rs.getString("descricao");
                double precoCompra = rs.getDouble("preco_compra");
                double precoVenda = rs.getDouble("preco_venda");

                stmtDelete.setInt(1, id);
                int linhasAfetadas = stmtDelete.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println();
                    System.out.println("Produto abaixo deletado com sucesso!");
                    System.out.printf("ID: %d | Produto: %s | Compra: R$%.2f | Venda: R$%.2f | Descrição: %s%n",
                            id, produto, precoCompra, precoVenda, descricao);
                }
            } else {
            	System.out.println();
                System.out.println("ID de produto não encontrado.");
            }
        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao deletar produto. Já possui venda cadastrada");
            System.out.println(e.getMessage());
        }
    }

    public static void listarTodos() {
        String sql = "SELECT id_produto, produto, descricao, preco_compra, preco_venda FROM produto ORDER BY produto";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println();
        	System.out.println("========== LISTA DE PRODUTOS ==========");
            System.out.printf("%-3s | %-30s | %-9s | %-9s | %-50s%n",
                    "ID", "PRODUTO", "R$ COMPRA", "R$ VENDA", "DESCRIÇÃO");
            System.out.println("----+--------------------------------+-----------+-----------+-------------------------------------------------------------------------");

            boolean encontrou = false;
            while (rs.next()) {
                encontrou = true;
                System.out.printf("%-3d | %-30s | %-9.2f | %-9.2f | %-50s%n",
                        rs.getInt("id_produto"),
                        rs.getString("produto"),
                        rs.getDouble("preco_compra"),
                        rs.getDouble("preco_venda"),
                        rs.getString("descricao"));
            }
            if (!encontrou) {
            	System.out.println();
                System.out.println("Nenhum produto cadastrado.");
            }
        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
    }
}