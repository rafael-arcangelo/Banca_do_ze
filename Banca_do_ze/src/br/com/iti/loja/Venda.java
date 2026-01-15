package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Venda {

    public static int venda(Scanner scanner) {
        String sql = "INSERT INTO venda (data_venda, id_fk_cliente, id_fk_pgto) VALUES (NOW(), ?, ?)";
        
        int idGerado = -1;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println();
        	System.out.print("Digite o ID do cliente (ou 1 para Venda Balcão): ");
            int id_cliente;
            
            try {
                id_cliente = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("ID de cliente inválido.");
                return -1;
            }

            System.out.print("Digite o ID da forma de pagamento: ");
            int id_pgto;
            
            try {
                id_pgto = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
            	System.out.println();
                System.out.println("ID da forma de pagamento inválido.");
                return -1;
            }

            stmt.setInt(1, id_cliente);
            stmt.setInt(2, id_pgto);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1);
                        System.out.println();
                        System.out.println("Venda registrada com sucesso! ID gerado: " + idGerado);
                    }
                }
            } else {
            	System.out.println();
                System.out.println("Erro ao registrar venda.");
            }

        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao registrar venda.");
            e.printStackTrace();
        }
        return idGerado;
    }

    public static void cadastrarItens(Scanner scanner, int idVenda) {
        String sql = "INSERT INTO itens_venda (id_fk_venda, id_fk_produto, quantidade, valor_unitario, valor_total) " +
                     "VALUES (?, ?, ?, ?, ?)";

        if (idVenda <= 0) {
        	System.out.println();
            System.out.println("ID da venda inválido. Cadastre a venda primeiro.");
            return;
        }

        try (Connection conn = Conexao.getConnection()) {

            boolean continuar = true;
            while (continuar) {
            	System.out.println();
                System.out.print("Digite o ID do produto (ou 0 para encerrar): ");
                int id_produto;
                try {
                    id_produto = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException ex) {
                	System.out.println();
                    System.out.println("ID de produto inválido.");
                    continue;
                }

                if (id_produto == 0) {
                    continuar = false;
                    break;
                }

                System.out.print("Digite a quantidade do produto: ");
                int qtd;
                try {
                    qtd = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException ex) {
                	System.out.println();
                    System.out.println("Quantidade inválida.");
                    continue;
                }

                if (qtd <= 0) {
                	System.out.println();
                    System.out.println("Erro: a quantidade deve ser positiva.");
                    continue;
                }

                if (!verificarSaldo(conn, id_produto, qtd)) {
                    continue;
                }

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

                System.out.println();
                System.out.println("Item registrado com sucesso!");
                atualizarEstoque(conn, id_produto, qtd);
            }

            System.out.println();
            System.out.println("Cadastro de itens finalizado.");
            listarVenda(idVenda);
            System.out.println();

        } catch (SQLException e) {
        	System.out.println();
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

    private static boolean verificarSaldo(Connection conn, int id_produto, int qtd) throws SQLException {
        String sql = "SELECT e.quantidade, p.produto " +
                     "FROM estoque e " +
                     "JOIN produto p ON e.id_fk_produto = p.id_produto " +
                     "WHERE e.id_fk_produto = ? " +
                     "ORDER BY e.id_movimento DESC " +
                     "LIMIT 1";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_produto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int saldo = rs.getInt("quantidade");
                String nomeProduto = rs.getString("produto");

                if (qtd > saldo) {
                    System.out.println();
                    System.out.printf("Erro: saldo insuficiente para o produto %s (ID %d). Saldo atual: %d | Tentativa: %d%n",
                            nomeProduto, id_produto, saldo, qtd);
                    return false;
                }
                return true;
                
            } else {
            	System.out.println();
                System.out.println("Produto sem estoque!");
                return false;
            }
        }
    }

    private static void atualizarEstoque(Connection conn, int id_produto, int qtd) throws SQLException {
        String sqlSaldo = "SELECT e.quantidade, e.est_seguro, p.produto " +
                          "FROM estoque e " +
                          "JOIN produto p ON e.id_fk_produto = p.id_produto " +
                          "WHERE e.id_fk_produto = ? " +
                          "ORDER BY e.id_movimento DESC " +
                          "LIMIT 1";
        
        String sqlUpdate = "UPDATE estoque SET quantidade = quantidade - ?, saida = saida + ?, data_movimento = NOW() " +
                           "WHERE id_fk_produto = ?";

        try (PreparedStatement stmtSaldo = conn.prepareStatement(sqlSaldo);
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

            stmtSaldo.setInt(1, id_produto);
            ResultSet rs = stmtSaldo.executeQuery();

            if (rs.next()) {
                int saldoAtual = rs.getInt("quantidade");
                int estSeguro = rs.getInt("est_seguro");
                String nomeProduto = rs.getString("produto");

                int novoSaldo = saldoAtual - qtd;

                stmtUpdate.setInt(1, qtd);
                stmtUpdate.setInt(2, qtd);
                stmtUpdate.setInt(3, id_produto);
                stmtUpdate.executeUpdate();

                System.out.println();
                System.out.println("Estoque atualizado com sucesso!");
                System.out.printf("Produto ID: %d | Produto: %s | Saldo anterior: %d | Saída: %d | Novo saldo: %d | Estoque mínimo: %d%n",
                        id_produto, nomeProduto, saldoAtual, qtd, novoSaldo, estSeguro);
            } else {
            	System.out.println();
                throw new SQLException("Produto sem estoque inicial!");
            }
        }
    }

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
            	System.out.println();
                System.out.println("========== VENDA ==========");
                System.out.println("ID: " + rsVenda.getInt("id_venda"));
                System.out.println("Data: " + rsVenda.getDate("data_venda"));
                System.out.println("Cliente: " + rsVenda.getString("cliente"));
                System.out.println("Pagamento: " + rsVenda.getString("pagamento"));
                System.out.println();
            } else {
            	System.out.println();
                System.out.println("Venda não encontrada.");
                return;
            }

            stmtItens.setInt(1, id_venda);
            ResultSet rsItens = stmtItens.executeQuery();

            double totalVenda = 0;
            System.out.printf("%-30s | %-3s | %-11s | %-8s%n", "PRODUTO", "QTD", "R$ UNITÁRIO", "R$ TOTAL");
            System.out.println("--------------------------------+-----+-------------+---------");

            while (rsItens.next()) {
                String produto = rsItens.getString("produto");
                int qtd = rsItens.getInt("quantidade");
                double unitario = rsItens.getDouble("valor_unitario");
                double total = rsItens.getDouble("valor_total");

                totalVenda += total;

                System.out.printf("%-30s | %-3d | %-11.2f | %-8.2f%n",
                        produto, qtd, unitario, total);
            }

            System.out.println("--------------------------------------------------------------");
            System.out.println();
            System.out.printf("TOTAL DA VENDA: R$%.2f%n", totalVenda);

        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao listar venda.");
            e.printStackTrace();
        }
    }

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

            System.out.println();
            System.out.println("========== RELATÓRIO DE VENDAS ==========");
            System.out.println();
            System.out.printf("%-3s | %-12s | %-20s | %-10s | %-9s%n",
                              "ID", "DATA VENDA", "CLIENTE", "PAGAMENTO", "R$ TOTAL");
            System.out.println("----+--------------+----------------------+------------+---------");

            boolean encontrou = false;
            double somaTotal = 0;

            while (rsVenda.next()) {
                encontrou = true;
                int id_venda = rsVenda.getInt("id_venda");
                String data_venda = rsVenda.getString("data_venda");
                String cliente = rsVenda.getString("cliente");
                String pagamento = rsVenda.getString("pagamento");

                stmtTotal.setInt(1, id_venda);
                ResultSet rsTotal = stmtTotal.executeQuery();
                double totalVenda = 0;
                if (rsTotal.next()) {
                    totalVenda = rsTotal.getDouble("total_venda");
                }

                somaTotal += totalVenda;

                System.out.printf("%-3d | %-12s | %-20s | %-10s | %-9.2f%n",
                        id_venda, data_venda, cliente, pagamento, totalVenda);
            }

            if (!encontrou) {
            	System.out.println();
                System.out.println("Nenhuma venda encontrada.");
            } else {
                System.out.println("-----------------------------------------------------------------");
                System.out.println();
                System.out.printf("TOTAL GERAL DE VENDAS: R$%.2f%n", somaTotal);
            }

        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao listar vendas.");
            e.printStackTrace();
        }
    }
}