package src.chamados;

public class Usuario {

    private Long id;
    private String nome;
    private String senhaHash;
    private Perfil perfil;

    public Usuario(String nome, String senhaHash, Perfil perfil) {
        this.nome = nome;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Perfil getPerfil() {
        return perfil;
    }
}