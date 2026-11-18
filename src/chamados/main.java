package src.chamados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        List<Usuario> usuarios = new ArrayList<>();
        List<Chamado> chamados = new ArrayList<>();

        boolean executando = true;

        while (executando) {
            System.out.println("\n=== CALL-WORK - SISTEMA DE CHAMADOS ===");
            System.out.println("1 - Cadastrar novo usuário");
            System.out.println("2 - Abrir chamado");
            System.out.println("3 - Listar chamados");
            System.out.println("4 - Atribuir técnico a um chamado");
            System.out.println("5 - Fechar chamado");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = teclado.nextLine();

            switch (opcao) {
                case "1":
                    cadastrarUsuario(teclado, usuarios);
                    break;
                case "2":
                    abrirChamado(teclado, usuarios, chamados);
                    break;
                case "3":
                    listarChamados(chamados);
                    break;
                case "4":
                    atribuirTecnico(teclado, usuarios, chamados);
                    break;
                case "5":
                    fecharChamado(teclado, chamados);
                    break;
                case "0":
                    System.out.println("Encerrando o sistema. Até logo!");
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }

        teclado.close();
    }

    
    private static void cadastrarUsuario(Scanner teclado, List<Usuario> usuarios) {
        System.out.println("\n--- CADASTRO DE USUÁRIO ---");
        System.out.print("Digite seu nome: ");
        String nome = teclado.nextLine();

        System.out.print("Digite 1 para Usuário, 2 para Técnico e 3 para Admin: ");
        String opcaoPerfil = teclado.nextLine();

        Perfil perfil;
        switch (opcaoPerfil) {
            case "1": perfil = Perfil.USUARIO; break;
            case "2": perfil = Perfil.TECNICO; break;
            case "3": perfil = Perfil.ADMIN;   break;
            default:
                System.out.println("Perfil inválido! Cadastro cancelado.");
                return;
        }

        System.out.print("Crie sua senha: ");
        String senha = teclado.nextLine();

        usuarios.add(new Usuario(nome, senha, perfil));
        System.out.println("✓ Usuário cadastrado: " + nome + " (" + perfil + ")");
    }

    
    private static void abrirChamado(Scanner teclado, List<Usuario> usuarios, List<Chamado> chamados) {
        System.out.println("\n--- ABRIR NOVO CHAMADO ---");

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado. Cadastre um usuário primeiro (opção 1)!");
            return;
        }

        
        System.out.println("Selecione o solicitante:");
        listarUsuarios(usuarios);
        int indexSolicitante = lerIndice(teclado, usuarios.size());
        if (indexSolicitante == -1) return;

        Usuario solicitante = usuarios.get(indexSolicitante);

        System.out.print("Título do chamado: ");
        String titulo = teclado.nextLine();

        System.out.print("Descrição do chamado: ");
        String descricao = teclado.nextLine();

        System.out.println("Prioridade: 1 - BAIXA | 2 - MEDIA | 3 - ALTA | 4 - CRITICA");
        System.out.print("Opção: ");
        String opcaoPrioridade = teclado.nextLine();

        Prioridade prioridade;
        switch (opcaoPrioridade) {
            case "1": prioridade = Prioridade.BAIXA;   break;
            case "2": prioridade = Prioridade.MEDIA;   break;
            case "3": prioridade = Prioridade.ALTA;    break;
            case "4": prioridade = Prioridade.CRITICA; break;
            default:
                System.out.println("Opção inválida! Definindo prioridade como MEDIA.");
                prioridade = Prioridade.MEDIA;
        }

        Chamado chamado = new Chamado(titulo, descricao, prioridade, solicitante);
        chamados.add(chamado);
        System.out.println("✓ Chamado criado com sucesso!\n" + chamado);
    }

    
    private static void listarChamados(List<Chamado> chamados) {
        System.out.println("\n--- LISTA DE CHAMADOS (" + chamados.size() + " no total) ---");

        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado registrado até o momento.");
            return;
        }

        for (Chamado c : chamados) {
            System.out.println(c);
        }
    }

    
    private static void atribuirTecnico(Scanner teclado, List<Usuario> usuarios, List<Chamado> chamados) {
        System.out.println("\n--- ATRIBUIR TÉCNICO ---");

        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado aberto. Abra um chamado primeiro (opção 2)!");
            return;
        }

        System.out.println("Selecione o chamado:");
        listarChamados(chamados);
        int indexChamado = lerIndice(teclado, chamados.size());
        if (indexChamado == -1) return;

        System.out.println("Selecione o técnico ou admin:");
        listarUsuarios(usuarios);
        int indexTecnico = lerIndice(teclado, usuarios.size());
        if (indexTecnico == -1) return;

        chamados.get(indexChamado).atribuirTecnico(usuarios.get(indexTecnico));
        System.out.println("✓ Atualização: " + chamados.get(indexChamado));
    }

    
    private static void fecharChamado(Scanner teclado, List<Chamado> chamados) {
        System.out.println("\n--- FECHAR CHAMADO ---");

        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado registrado.");
            return;
        }

        System.out.println("Selecione o chamado a fechar:");
        listarChamados(chamados);
        int index = lerIndice(teclado, chamados.size());
        if (index == -1) return;

        chamados.get(index).fecharChamado();
        System.out.println("✓ Chamado fechado: " + chamados.get(index));
    }

    
    private static void listarUsuarios(List<Usuario> usuarios) {
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + " - " + usuarios.get(i).getNome()
                    + " (" + usuarios.get(i).getPerfil() + ")");
        }
    }

    
    private static int lerIndice(Scanner teclado, int tamanho) {
        System.out.print("Digite o número: ");
        try {
            int index = Integer.parseInt(teclado.nextLine()) - 1;
            if (index < 0 || index >= tamanho) {
                System.out.println("Número fora do intervalo. Operação cancelada.");
                return -1;
            }
            return index;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Operação cancelada.");
            return -1;
        }
    }
}
