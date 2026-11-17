package src.chamados;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Chamado {

    private static Long contadorId = 1L; 
    
    private Long id;
    private String titulo;
    private String descricao;
    private Status status;
    private Prioridade prioridade;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private Usuario solicitante;
    private Usuario tecnico;

    public Chamado(String titulo, String descricao, Prioridade prioridade, Usuario solicitante) {
        this.id = contadorId++;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.solicitante = solicitante;
        this.status = Status.ABERTO;
        this.dataAbertura = LocalDateTime.now();
    }

    public void atribuirTecnico(Usuario tecnico) {
        if (tecnico.getPerfil() != Perfil.TECNICO && tecnico.getPerfil() != Perfil.ADMIN) {
            System.out.println("Apenas técnicos ou admins podem assumir chamados!");
            return;
        }
        this.tecnico = tecnico;
        this.status = Status.EM_ANDAMENTO;
    }

    public void fecharChamado() {
        this.status = Status.FECHADO;
        this.dataFechamento = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public Status getStatus() { return status; }
    public Prioridade getPrioridade() { return prioridade; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public Usuario getSolicitante() { return solicitante; }
    public Usuario getTecnico() { return tecnico; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String nomeTecnico = (tecnico != null) ? tecnico.getNome() : "Não atribuído";
        return String.format("[#%d] %s | Prioridade: %s | Status: %s | Solicitante: %s | Técnico: %s | Aberto em: %s",
                id, titulo, prioridade, status, solicitante.getNome(), nomeTecnico, dataAbertura.format(fmt));
    }
}