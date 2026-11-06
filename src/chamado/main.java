package src.chamado;

import java.util.Scanner;

public class main {
    public static void main(String[] args)     {
        Scanner teclado = new Scanner(System.in);

        System.out.println("Digite seu nome: ");
        String nome = teclado.nextLine();

        System.out.println("Digite 1 para Usuário, 2 para Técnico e 3 para Admin: ");
        String opção = teclado.nextLine();

        System.out.println("Crie sua senha: ");
        String senha = teclado.nextLine();

        Perfil perfil;
        switch (opcao) {
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
                System.out.println("Opção inválida. ");
                teclado.close();
                return;
        }
        Usuario usuario = new Usuario(nome, senha, perfil);
        
        System.out.println("Usuário cadastrado: " + usuario.getNome() + " (" + usuario.getPerfil() + ") ");

        teclado.close();
}
}