package br.com.iti.loja;

import java.util.Scanner;

public class MenuPrincipal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

    	System.out.println();
    	System.out.println("========== BANCA DO ZÉ ==========");
        
        do {
        	System.out.println();
        	System.out.println("---- MENU PRINCIPAL ----");
            System.out.println("1 - Clientes");
            System.out.println("2 - Produtos");
            System.out.println("3 - Estoque");
            System.out.println("4 - Compra e Venda");
            System.out.println("5 - Relatórios");
            System.out.println("0 - Sair");
            System.out.println();
            System.out.print("Escolha uma opção: ");
            
            opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                	menuClientes(scanner);
                    break;
                case 2:
                    menuProdutos(scanner);
                    break;
                case 3:
                     menuEstoque(scanner);
                    break;
                case 4:
                    menuVendas(scanner);
                    break;
                case 5:
                    menuRelatorios(scanner);
                    break;
                case 0:
                    System.out.println("Sistema encerrado.");
                    System.out.println();
                	System.out.println("========== BANCA DO ZÉ ==========");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static int lerOpcao(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void menuClientes(Scanner scanner) {
        int opcao;
        do {
            System.out.println();
        	System.out.println("---- MENU CLIENTES ----");
            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Atualizar Cliente");
            System.out.println("3 - Deletar Cliente");
            System.out.println("0 - Voltar");
            System.out.println();
            System.out.print("Escolha uma opção: ");
            
            opcao = lerOpcao(scanner);
            
            switch (opcao) {
                case 1:
                    Cliente.cadastrar(scanner);
                    break;
                case 2:
                	Cliente.atualizar(scanner);
                    break;
                case 3:
                	 Cliente.deletar(scanner);
                    break;
                case 0:
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
            System.out.println("2 - Atualizar Produto");
            System.out.println("3 - Deletar Produto");
            System.out.println("0 - Voltar");
            System.out.println();
            System.out.print("Escolha uma opção: ");
            
            opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    Produto.cadastrar(scanner);
                    break;
                case 2:
                	Produto.atualizar(scanner);
                    break;
                case 3:
                	Produto.deletar(scanner);
                    break;
                case 0:
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
            System.out.println("1 - Ajustar Estoque");
            System.out.println("2 - Consultar Estoque Atual");
            System.out.println("0 - Voltar");
            System.out.println();
            System.out.print("Escolha uma opção: ");
            
            opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    Estoque.ajustar(scanner);
                    break;
                case 2:
                    Estoque.listarTodos();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuVendas(Scanner scanner) {
        int opcao;
        do {
            System.out.println();
            System.out.println("---- MENU COMPRA / VENDA ----");
            System.out.println("1 - Registrar COMPRA (Entrada no Estoque)");
            System.out.println("2 - Registrar VENDA (Saída do Estoque)");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Opção inválida. Digite um número.");
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    Estoque.entrada(scanner);
                    break;
                case 2:
                    System.out.println();
                    System.out.println("Registrando venda...");
                    int idVenda = Venda.venda(scanner);
                    if (idVenda > 0) {
                        Venda.cadastrarItens(scanner, idVenda);
                    }
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    private static void menuRelatorios(Scanner scanner) {
        int opcao;
        do {
            System.out.println();
            System.out.println("---- MENU RELATÓRIOS ----");
            System.out.println("1 - Listar todos os Clientes");
            System.out.println("2 - Listar todos os Produtos");
            System.out.println("3 - Estoque Atual");
            System.out.println("4 - Estoque abaixo do mínimo");
            System.out.println("5 - Listar Formas de Pagamento");
            System.out.println("6 - Vendas por Cliente");
            System.out.println("7 - Vendas por Período");
            System.out.println("8 - Vendas Gerais");
            System.out.println("9 - Produtos mais vendidos");
            System.out.println("0 - Voltar");
            System.out.println();
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Opção inválida. Digite um número.");
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    Cliente.listarTodos();
                    break;
                case 2:
                    Produto.listarTodos();
                    break;
                case 3:
                    Estoque.listarTodos();
                    break;
                case 4:
                    Relatorio.estoqueMinimo(scanner);
                    break;
                case 5:
                    Relatorio.pgtoListarTodos();
                    break;
                case 6:
                    Relatorio.vendasCliente(scanner);
                    break;
                case 7:
                    Relatorio.vendasPeriodo(scanner);
                    break;
                case 8:
                    Venda.listarTodas();
                    break;
                case 9:
                    Relatorio.produtosMaisVendidos(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }
}