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
            case "1":
                perfil = Perfil.USUARIO;
                break;
            case "2":
                perfil = Perfil.TECNICO;
                break;
            case "3":
                perfil = Perfil.ADMIN;
                break;
            default:
                System.out.println("Perfil inválido! Cadastro cancelado.");
                return;
        }

        System.out.print("Crie sua senha: ");
        String senha = teclado.nextLine();

        Usuario usuario = new Usuario(nome, senha, perfil);
        usuarios.add(usuario);

        System.out.println("✓ Usuário cadastrado com sucesso: " + usuario.getNome() + " (" + usuario.getPerfil() + ")");
    }

    private static void abrirChamado(Scanner teclado, List<Usuario> usuarios, List<Chamado> chamados) {
        System.out.println("\n--- ABRIR NOVO CHAMADO ---");

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado ainda. Cadastre um usuário primeiro (opção 1)!");
            return;
        }

        System.out.println("Selecione o solicitante:");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            System.out.println((i + 1) + " - " + u.getNome() + " (" + u.getPerfil() + ")");
        }
        System.out.print("Digite o número do usuário: ");
        String inputIndice = teclado.nextLine();

        int index;
        try {
            index = Integer.parseInt(inputIndice) - 1;
            if (index < 0 || index >= usuarios.size()) {
                System.out.println("Opção de usuário inválida! Abertura de chamado cancelada.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Número inválido! Abertura de chamado cancelada.");
            return;
        }

        Usuario solicitante = usuarios.get(index);

        System.out.print("Digite o título do chamado: ");
        String titulo = teclado.nextLine();

        System.out.print("Digite a descrição do chamado: ");
        String descricao = teclado.nextLine();

        System.out.println("Selecione a prioridade:");
        System.out.println("1 - BAIXA | 2 - MEDIA | 3 - ALTA | 4 - CRITICA");
        System.out.print("Opção: ");
        String opcaoPrioridade = teclado.nextLine();

        Prioridade prioridade;
        switch (opcaoPrioridade) {
            case "1":
                prioridade = Prioridade.BAIXA;
                break;
            case "2":
                prioridade = Prioridade.MEDIA;
                break;
            case "3":
                prioridade = Prioridade.ALTA;
                break;
            case "4":
                prioridade = Prioridade.CRITICA;
                break;
            default:
                System.out.println("Opção não reconhecida! Definindo prioridade como MEDIA por padrão.");
                prioridade = Prioridade.MEDIA;
                break;
        }

        Chamado chamado = new Chamado(titulo, descricao, prioridade, solicitante);
        chamados.add(chamado);

        System.out.println("✓ Chamado criado com sucesso para o solicitante " + solicitante.getNome() + "!");
    }

    private static void listarChamados(List<Chamado> chamados) {
        System.out.println("\n--- LISTA DE CHAMADOS ---");

        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado registrado até o momento.");
            return;
        }

        System.out.println("Total de chamados cadastrados: " + chamados.size());
        for (int i = 0; i < chamados.size(); i++) {
            System.out.println("Chamado #" + (i + 1) + " registrado no sistema.");
        }
    }
}
