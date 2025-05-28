package com.elearning.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data; // Lombok para getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor; // Lombok para construtor sem argumentos
import lombok.AllArgsConstructor; // Lombok para construtor com todos os argumentos

import java.util.Date;
import java.util.List;

@Data // Gera automaticamente getters, setters, toString, equals, hashCode
@NoArgsConstructor // Gera um construtor sem argumentos (necessário para JPA/MongoDB e frameworks)
@AllArgsConstructor // Gera um construtor com todos os argumentos (opcional, mas pode ser útil)
@Document(collection = "cursos") // Mapeia para a coleção "cursos" no MongoDB
public class Curso {
    @Id // Identificador único do MongoDB
    private String id;

    private String titulo;
    private String descricao;
    private List<String> categorias;
    private Date dataCriacao = new Date(); // Inicializa com a data atual

    // Campos relacionados a quem criou o curso
    private Long criadorId;       // ID do usuário (do PostgreSQL) que criou o curso
    private String criadorNome;   // Nome ou email do criador para fácil exibição

    // Campos opcionais que podem ser definidos na criação ou posteriormente
    private String imagemUrl;     // URL para a imagem de capa do curso
    private String duracao;       // Duração estimada do curso (ex: "20 horas", "3 semanas")
    private String nivel;         // Nível de dificuldade (ex: "Iniciante", "Intermediário", "Avançado")
    private Double preco;         // Preço do curso

    // Campos relacionados a avaliações (podem ser calculados/atualizados separadamente)
    private Double mediaAvaliacoes;
    private Integer totalAvaliacoes;

    // Construtor específico que você já tinha (pode ser útil para outros cenários)
    public Curso(String titulo, String descricao, List<String> categorias) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categorias = categorias;
        // dataCriacao é inicializada por padrão
    }

    // Construtor para quando um curso é criado por um usuário (exemplo de uso no service)
    public Curso(String titulo, String descricao, List<String> categorias, Long criadorId, String criadorNome) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categorias = categorias;
        this.criadorId = criadorId;
        this.criadorNome = criadorNome;
        // dataCriacao é inicializada por padrão
    }


}