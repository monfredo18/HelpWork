package src.chamados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        List<Usuario> usuarios = new ArrayList<>();
        List<Chamado> chamados = new ArrayList<>();

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      CALL-WORK - SISTEMA DE CHAMADOS ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean rodando = true;
        while (rodando) {
            Usuario logado = telaLogin(teclado, usuarios);
            if (logado == null) {
                // Usuário escolheu sair na tela de login
                rodando = false;
                break;
            }

            // Direciona para o menu certo baseado no perfil
            switch (logado.getPerfil()) {
                case USUARIO:
                    menuUsuario(teclado, logado, chamados);
                    break;
                case TECNICO:
                    menuTecnico(teclado, logado, chamados);
                    break;
                case ADMIN:
                    menuAdmin(teclado, usuarios, chamados);
                    break;
            }
        }

        System.out.println("Sistema encerrado. Até logo!");
        teclado.close();
    }

    // ================================================================
    // TELA DE LOGIN
    // ================================================================
    private static Usuario telaLogin(Scanner teclado, List<Usuario> usuarios) {
        System.out.println("\n--- LOGIN ---");

        // Primeiro acesso: força criação de um admin
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado. Crie a conta de administrador:");
            System.out.print("Nome: ");
            String nome = teclado.nextLine();
            System.out.print("Senha: ");
            String senha = teclado.nextLine();
            Usuario admin = new Usuario(nome, senha, Perfil.ADMIN);
            usuarios.add(admin);
            System.out.println("✓ Administrador \"" + nome + "\" criado com sucesso!\n");
            return admin;
        }

        System.out.println("Digite 0 para sair.");
        System.out.print("Nome: ");
        String nome = teclado.nextLine();
        if (nome.equals("0")) return null;

        System.out.print("Senha: ");
        String senha = teclado.nextLine();

        for (Usuario u : usuarios) {
            if (u.getNome().equalsIgnoreCase(nome) && u.verificarSenha(senha)) {
                System.out.println("✓ Bem-vindo, " + u.getNome() + "! (" + u.getPerfil() + ")");
                return u;
            }
        }

        System.out.println("✗ Nome ou senha incorretos. Tente novamente.");
        return telaLogin(teclado, usuarios); // Tenta de novo
    }

    // ================================================================
    // MENU: USUÁRIO
    // Pode: Abrir chamados (como solicitante) e ver os seus chamados
    // ================================================================
    private static void menuUsuario(Scanner teclado, Usuario logado, List<Chamado> chamados) {
        boolean logado_ = true;
        while (logado_) {
            System.out.println("\n=== MENU USUÁRIO (" + logado.getNome() + ") ===");
            System.out.println("1 - Abrir chamado");
            System.out.println("2 - Meus chamados");
            System.out.println("0 - Logout");
            System.out.print("Opção: ");

            switch (teclado.nextLine()) {
                case "1":
                    abrirChamado(teclado, logado, chamados);
                    break;
                case "2":
                    meusChamados(logado, chamados);
                    break;
                case "0":
                    System.out.println("Logout realizado.");
                    logado_ = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // ================================================================
    // MENU: TÉCNICO
    // Pode: Ver chamados abertos, assumir chamado, fechar chamado
    // ================================================================
    private static void menuTecnico(Scanner teclado, Usuario logado, List<Chamado> chamados) {
        boolean logado_ = true;
        while (logado_) {
            System.out.println("\n=== MENU TÉCNICO (" + logado.getNome() + ") ===");
            System.out.println("1 - Ver chamados abertos");
            System.out.println("2 - Assumir chamado");
            System.out.println("3 - Fechar chamado");
            System.out.println("0 - Logout");
            System.out.print("Opção: ");

            switch (teclado.nextLine()) {
                case "1":
                    listarChamadosPorStatus(chamados, Status.ABERTO);
                    break;
                case "2":
                    assumirChamado(teclado, logado, chamados);
                    break;
                case "3":
                    fecharChamado(teclado, chamados);
                    break;
                case "0":
                    System.out.println("Logout realizado.");
                    logado_ = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // ================================================================
    // MENU: ADMIN
    // Acesso total a todas as funções
    // ================================================================
    private static void menuAdmin(Scanner teclado, List<Usuario> usuarios, List<Chamado> chamados) {
        boolean logado_ = true;
        while (logado_) {
            System.out.println("\n=== MENU ADMINISTRADOR ===");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Listar usuários");
            System.out.println("3 - Listar todos os chamados");
            System.out.println("4 - Abrir chamado");
            System.out.println("5 - Atribuir técnico");
            System.out.println("6 - Fechar chamado");
            System.out.println("0 - Logout");
            System.out.print("Opção: ");

            switch (teclado.nextLine()) {
                case "1":
                    cadastrarUsuario(teclado, usuarios);
                    break;
                case "2":
                    listarUsuariosMenu(usuarios);
                    break;
                case "3":
                    listarTodosChamados(chamados);
                    break;
                case "4":
                    abrirChamadoAdmin(teclado, usuarios, chamados);
                    break;
                case "5":
                    atribuirTecnico(teclado, usuarios, chamados);
                    break;
                case "6":
                    fecharChamado(teclado, chamados);
                    break;
                case "0":
                    System.out.println("Logout realizado.");
                    logado_ = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // ================================================================
    // AÇÕES — compartilhadas ou específicas por perfil
    // ================================================================

    /** ADMIN: Cadastrar usuário no sistema */
    private static void cadastrarUsuario(Scanner teclado, List<Usuario> usuarios) {
        System.out.println("\n--- CADASTRO DE USUÁRIO ---");
        System.out.print("Nome: ");
        String nome = teclado.nextLine();

        System.out.print("Perfil (1-Usuário / 2-Técnico / 3-Admin): ");
        Perfil perfil;
        switch (teclado.nextLine()) {
            case "1": perfil = Perfil.USUARIO; break;
            case "2": perfil = Perfil.TECNICO; break;
            case "3": perfil = Perfil.ADMIN;   break;
            default:
                System.out.println("Perfil inválido! Cadastro cancelado.");
                return;
        }

        System.out.print("Senha: ");
        String senha = teclado.nextLine();

        usuarios.add(new Usuario(nome, senha, perfil));
        System.out.println("✓ Usuário \"" + nome + "\" (" + perfil + ") cadastrado!");
    }

    /** USUARIO: Abre chamado com ele mesmo como solicitante */
    private static void abrirChamado(Scanner teclado, Usuario solicitante, List<Chamado> chamados) {
        System.out.println("\n--- ABRIR CHAMADO ---");
        System.out.print("Título: ");
        String titulo = teclado.nextLine();
        System.out.print("Descrição: ");
        String descricao = teclado.nextLine();

        System.out.println("Prioridade: 1-BAIXA | 2-MEDIA | 3-ALTA | 4-CRITICA");
        System.out.print("Opção: ");
        Prioridade prioridade = lerPrioridade(teclado.nextLine());

        Chamado chamado = new Chamado(titulo, descricao, prioridade, solicitante);
        chamados.add(chamado);
        System.out.println("✓ Chamado criado!\n" + chamado);
    }

    /** ADMIN: Abre chamado escolhendo qualquer usuário como solicitante */
    private static void abrirChamadoAdmin(Scanner teclado, List<Usuario> usuarios, List<Chamado> chamados) {
        System.out.println("\n--- ABRIR CHAMADO (como Admin) ---");

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado ainda.");
            return;
        }

        System.out.println("Selecione o solicitante:");
        listarUsuarios(usuarios);
        int idx = lerIndice(teclado, usuarios.size());
        if (idx == -1) return;

        abrirChamado(teclado, usuarios.get(idx), chamados);
    }

    /** USUARIO: Lista apenas os chamados em que ele é o solicitante */
    private static void meusChamados(Usuario usuario, List<Chamado> chamados) {
        System.out.println("\n--- MEUS CHAMADOS ---");
        List<Chamado> meus = new ArrayList<>();
        for (Chamado c : chamados) {
            if (c.getSolicitante().getNome().equalsIgnoreCase(usuario.getNome())) {
                meus.add(c);
            }
        }

        if (meus.isEmpty()) {
            System.out.println("Você não possui chamados abertos.");
            return;
        }

        for (Chamado c : meus) {
            System.out.println(c);
        }
    }

    /** ADMIN: Lista todos os chamados sem filtro */
    private static void listarTodosChamados(List<Chamado> chamados) {
        System.out.println("\n--- TODOS OS CHAMADOS (" + chamados.size() + ") ---");
        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado registrado.");
            return;
        }
        for (Chamado c : chamados) {
            System.out.println(c);
        }
    }

    /** TECNICO: Lista chamados filtrados por status */
    private static void listarChamadosPorStatus(List<Chamado> chamados, Status status) {
        System.out.println("\n--- CHAMADOS " + status + " ---");
        boolean encontrou = false;
        for (Chamado c : chamados) {
            if (c.getStatus() == status) {
                System.out.println(c);
                encontrou = true;
            }
        }
        if (!encontrou) System.out.println("Nenhum chamado com status " + status + ".");
    }

    /** TECNICO: Atribui ele mesmo como técnico de um chamado */
    private static void assumirChamado(Scanner teclado, Usuario tecnico, List<Chamado> chamados) {
        System.out.println("\n--- ASSUMIR CHAMADO ---");
        List<Chamado> abertos = new ArrayList<>();
        for (Chamado c : chamados) {
            if (c.getStatus() == Status.ABERTO) abertos.add(c);
        }

        if (abertos.isEmpty()) {
            System.out.println("Nenhum chamado aberto disponível.");
            return;
        }

        for (int i = 0; i < abertos.size(); i++) {
            System.out.println((i + 1) + " - " + abertos.get(i));
        }
        int idx = lerIndice(teclado, abertos.size());
        if (idx == -1) return;

        abertos.get(idx).atribuirTecnico(tecnico);
        System.out.println("✓ Chamado assumido: " + abertos.get(idx));
    }

    /** ADMIN: Atribui qualquer técnico a qualquer chamado */
    private static void atribuirTecnico(Scanner teclado, List<Usuario> usuarios, List<Chamado> chamados) {
        System.out.println("\n--- ATRIBUIR TÉCNICO ---");
        if (chamados.isEmpty()) { System.out.println("Nenhum chamado registrado."); return; }

        listarTodosChamados(chamados);
        int idxChamado = lerIndice(teclado, chamados.size());
        if (idxChamado == -1) return;

        System.out.println("Selecione o técnico:");
        listarUsuarios(usuarios);
        int idxTecnico = lerIndice(teclado, usuarios.size());
        if (idxTecnico == -1) return;

        chamados.get(idxChamado).atribuirTecnico(usuarios.get(idxTecnico));
        System.out.println("✓ Atualizado: " + chamados.get(idxChamado));
    }

    /** ADMIN / TECNICO: Fecha um chamado */
    private static void fecharChamado(Scanner teclado, List<Chamado> chamados) {
        System.out.println("\n--- FECHAR CHAMADO ---");
        if (chamados.isEmpty()) { System.out.println("Nenhum chamado registrado."); return; }

        listarTodosChamados(chamados);
        int idx = lerIndice(teclado, chamados.size());
        if (idx == -1) return;

        chamados.get(idx).fecharChamado();
        System.out.println("✓ Chamado fechado: " + chamados.get(idx));
    }

    /** ADMIN: Exibe lista de usuários com perfil */
    private static void listarUsuariosMenu(List<Usuario> usuarios) {
        System.out.println("\n--- USUÁRIOS (" + usuarios.size() + ") ---");
        if (usuarios.isEmpty()) { System.out.println("Nenhum usuário cadastrado."); return; }
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + " - " + usuarios.get(i).getNome()
                    + " | " + usuarios.get(i).getPerfil());
        }
    }

    // ================================================================
    // HELPERS
    // ================================================================
    private static void listarUsuarios(List<Usuario> usuarios) {
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + " - " + usuarios.get(i).getNome()
                    + " (" + usuarios.get(i).getPerfil() + ")");
        }
    }

    private static Prioridade lerPrioridade(String opcao) {
        switch (opcao) {
            case "1": return Prioridade.BAIXA;
            case "3": return Prioridade.ALTA;
            case "4": return Prioridade.CRITICA;
            default:  return Prioridade.MEDIA;
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
