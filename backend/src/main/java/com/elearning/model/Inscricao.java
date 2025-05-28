package com.elearning.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp; 

@Entity
@Table(name = "inscricoes")
public class Inscricao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Adicionar fetch = FetchType.LAZY é uma boa prática
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "curso_id_mongo", nullable = false) // Sugestão: ser explícito
    private String cursoIdMongo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) // Adicionar nullable e length para o status
    private StatusInscricao status;

    @Column(name = "data_inscricao", nullable = false, updatable = false)
    private LocalDateTime dataInscricao;

    // Enum
    public enum StatusInscricao {
        ATIVA, CANCELADA, CONCLUIDA
    }

    @PrePersist
    protected void onCreate() {
        if (dataInscricao == null) { // Garante que só é setado na criação
            dataInscricao = LocalDateTime.now();
        }
    }

    // Getters e Setters (Lombok @Data é mais conciso, mas isso funciona)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getCursoIdMongo() { return cursoIdMongo; } // Nome do getter atualizado
    public void setCursoIdMongo(String cursoIdMongo) { this.cursoIdMongo = cursoIdMongo; } // Nome do setter atualizado

    public StatusInscricao getStatus() { return status; }
    public void setStatus(StatusInscricao status) { this.status = status; }

    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDateTime dataInscricao) { this.dataInscricao = dataInscricao; }
}