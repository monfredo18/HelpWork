package src.chamados;

import java.time.LocalDateTime;

public class Chamado {

    private Long id;
    private String titulo;
    private String descricao;
    private Status status;
    private Prioridade prioridade;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private Usuario solicitante;
    private Usuario tecnico;

    public Chamado(String titulo, String descricao, Status status, Prioridade prioridade, LocalDateTime dataAbertura, Usuario solicitanteUsuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.dataAbertura = dataAbertura;
        this.solicitante = solicitante;
    }
}