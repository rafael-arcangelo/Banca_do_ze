package br.com.iti.loja;

import java.util.Scanner;

public class MenuPrincipal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
        	System.out.println();
        	System.out.println("========== BANCA DO ZÉ ==========");
            System.out.println("1 - Clientes");
            System.out.println("2 - Produtos");
            System.out.println("3 - Estoque");
            System.out.println("4 - Registrar Vendas");
            System.out.println("5 - Relatórios");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                	System.out.println();
                	menuClientes(scanner);
                    break;
                case 2:
                	System.out.println();
                    menuProdutos(scanner);
                    break;
                case 3:
                	System.out.println();
                    menuEstoque(scanner);
                    break;
                case 4:
                	System.out.println();
                    menuVendas(scanner);
                    break;
                case 5:
                	System.out.println();
                    menuRelatorios(scanner);
                    break;
                case 0:
                	System.out.println();
                    System.out.println("Sistema encerrado.");
                    break;
                default:
                	System.out.println();
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void menuClientes(Scanner scanner) {
        int opcao;
        do {
            System.out.println();
        	System.out.println("---- MENU CLIENTES ----");
            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Listar Clientes");
            System.out.println("3 - Atualizar Cliente");
            System.out.println("4 - Deletar Cliente");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    System.out.println();
                    ClienteCadastro.cadastrar(scanner);
                    break;
                case 2:
                    System.out.println();
                    ClienteListarTodos.listarTodos();
                    break;
                case 3:
                    System.out.println();
                    ClienteAtualizar.atualizar(scanner);
                    break;
                case 4:
                    System.out.println();
                    ClienteDeletar.deletar(scanner);
                    break;
                case 0:
                    System.out.println();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuProdutos(Scanner scanner) {
        int opcao;
        do {
            System.out.println();
        	System.out.println("---- MENU PRODUTOS ----");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("3 - Atualizar Produto");
            System.out.println("4 - Deletar Produto");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    System.out.println();
                    ProdutoCadastro.cadastrar(scanner);
                    break;
                case 2:
                    System.out.println();
                    ProdutoListarTodos.listarTodos();
                    break;
                case 3:
                    System.out.println();
                    ProdutoAtualizar.atualizar(scanner);
                    break;
                case 4:
                    System.out.println();
                    ProdutoDeletar.deletar(scanner);
                    break;
                case 0:
                    System.out.println();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuEstoque(Scanner scanner) {
        int opcao;
        do {
            System.out.println();
        	System.out.println("---- MENU ESTOQUE ----");
            System.out.println("1 - Estoque Inicial");
            System.out.println("2 - Entrada no Estoque (Compra)");
            System.out.println("3 - Consultar Estoque Atual");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    System.out.println();
                    Estoque.estoqueInicial(scanner);
                    break;
                case 2:
                    System.out.println();
                    EstoqueEntrada.entrada(scanner);
                    break;
                case 3:
                    System.out.println();
                    EstoqueListarTodos.listarTodos();
                    break;
                case 0:
                    System.out.println();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuVendas(Scanner scanner) {
        System.out.println();
    	System.out.println("---- MENU VENDAS ----");
        System.out.println("Registrando venda...");
        int idVenda = VendaCadastrar.venda(scanner);
        if (idVenda > 0) {
            VendaItens.cadastrarItens(scanner, idVenda);
        }
    }

    private static void menuRelatorios(Scanner scanner) {
        int opcao;
        do {
        	System.out.println();
            System.out.println("---- MENU RELATÓRIOS ----");
            System.out.println("1 - Relatório de Vendas por Período");
            System.out.println("2 - Relatório de Vendas por Cliente");
            System.out.println("3 - Relatório de Estoque Baixo");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            
            opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 1:
                    System.out.println();
                    RelVendaPeriodo.vendasPeriodo(scanner);
                    break;
                case 2:
                    System.out.println();
                    RelVendaCliente.vendasCliente(scanner);
                    break;
                case 3:
                    System.out.println();
                    RelEstoqueMinimo.estoqueMinimo(scanner);
                    break;
                case 0:
                    System.out.println();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}