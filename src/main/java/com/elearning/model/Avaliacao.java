package com.elearning.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.Date;

@Data
@Document(collection = "avaliacoes")
public class Avaliacao {
    @Id
    private String id;
    private Integer nota;
    private String comentario;
    private Date dataAvaliacao = new Date();
    
    @DBRef
    private Curso curso;
    
    @DBRef
    private Usuario usuario;

    // Construtor padrão (necessário para o Spring Data)
    public Avaliacao() {}

    // Construtor com parâmetros
    public Avaliacao(Integer nota, String comentario, Curso curso, Usuario usuario) {
        this.nota = nota;
        this.comentario = comentario;
        this.curso = curso;
        this.usuario = usuario;
    }
}