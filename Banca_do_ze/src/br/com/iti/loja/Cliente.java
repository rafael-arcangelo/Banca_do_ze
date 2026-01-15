package br.com.iti.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Cliente {

    public static void cadastrar(Scanner scanner) {
        String sql = "INSERT INTO cliente (nome, telefone, email) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        	System.out.println();
        	System.out.print("Nome do cliente: ");
            String nome = scanner.nextLine();

            System.out.print("Telefone do cliente: ");
            String telefone = scanner.nextLine();

            System.out.print("E-mail do cliente: ");
            String email = scanner.nextLine();

            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, email);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        System.out.println();
                        System.out.println("Cliente abaixo cadastrado com sucesso:");
                        System.out.printf("ID: %d | Nome: %s | Telefone: %s | E-mail: %s%n",
                                id, nome, telefone, email);
                    }
                }
            }
        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    public static void atualizar(Scanner scanner) {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ? WHERE id_cliente = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        	System.out.println();
        	System.out.print("Digite o ID do cliente: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Novo nome: ");
            String nome = scanner.nextLine();

            System.out.print("Novo telefone: ");
            String telefone = scanner.nextLine();

            System.out.print("Novo e-mail: ");
            String email = scanner.nextLine();

            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, email);
            stmt.setInt(4, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
            	System.out.println();
                System.out.println("Cliente atualizado com sucesso!");
                System.out.printf("ID: %d | Nome: %s | Telefone: %s | E-mail: %s%n",
                        id, nome, telefone, email);
            } else {
            	System.out.println();
                System.out.println("ID não encontrado.");
            }
        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    public static void deletar(Scanner scanner) {
        String sqlSelect = "SELECT id_cliente, nome, telefone, email FROM cliente WHERE id_cliente = ?";
        
        String sqlDelete = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
             PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {

        	System.out.println();
        	System.out.print("Digite o ID do cliente que deseja deletar: ");
            
            int id;
            
            try {
                id = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("ID inválido. Digite um número.");
                return;
            }

            stmtSelect.setInt(1, id);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");

                stmtDelete.setInt(1, id);
                int linhasAfetadas = stmtDelete.executeUpdate();

                if (linhasAfetadas > 0) {
                	System.out.println();
                    System.out.println("Cliente abaixo deletado com sucesso!");
                    System.out.printf("ID: %d | Nome: %s | Telefone: %s | E-mail: %s%n",
                            id, nome, telefone, email);
                }
            } else {
            	System.out.println();
                System.out.println("ID de cliente não encontrado.");
            }

        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao deletar cliente: " + e.getMessage());
        }
    }


    public static void listarTodos() {
        String sql = "SELECT id_cliente, nome, telefone, email FROM cliente ORDER BY nome";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

        	System.out.println();
        	System.out.println("========== LISTA DE CLIENTES ==========");
            System.out.printf("%-3s | %-25s | %-15s | %-15s%n", "ID", "NOME", "TELEFONE", "E-MAIL");
            System.out.println("----+---------------------------+-----------------+--------------------");

            boolean encontrou = false;
            while (rs.next()) {
                encontrou = true;
                System.out.printf("%-3d | %-25s | %-15s | %-15s%n",
                        rs.getInt("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"));
            }
            if (!encontrou) {
            	System.out.println();
                System.out.println("Nenhum cliente cadastrado.");
            }
        } catch (SQLException e) {
        	System.out.println();
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }
    }
}