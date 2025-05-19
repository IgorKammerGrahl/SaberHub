package com.elearning.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "cursos") // Mapeia para a coleção "cursos" no MongoDB
public class Curso {
    @Id // Identificador único do MongoDB
    private String id;
    
    private String titulo;
    private String descricao;
    private List<String> categorias;
    private Date dataCriacao = new Date(); // Valor padrão
    
    // Construtores
    public Curso() {}
    
    public Curso(String titulo, String descricao, List<String> categorias) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categorias = categorias;
    }
    
    // Getters e Setters (gerar via IDE ou Lombok)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<String> getCategorias() { return categorias; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }
    public Date getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }
}
