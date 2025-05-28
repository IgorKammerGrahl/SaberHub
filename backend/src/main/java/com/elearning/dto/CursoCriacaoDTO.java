// CursoCriacaoDTO.java
package com.elearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor; // Para Thymeleaf poder instanciar

import java.util.List;

@Data
@NoArgsConstructor // Adicionado para Thymeleaf/Spring poder instanciar
public class CursoCriacaoDTO {
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 5, max = 100, message = "Título deve ter entre 5 e 100 caracteres")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 20, max = 1000, message = "Descrição deve ter entre 20 e 1000 caracteres")
    private String descricao;

    // MUDANÇA: Campo para receber a string de categorias do formulário
    @NotBlank(message = "Pelo menos uma categoria é obrigatória")
    private String categoriasInput; // Recebe a string "Java, Web, Programação"

    // O List<String> categorias será preenchido no service/controller a partir do categoriasInput
    private List<String> categorias;


    private String imagemUrl;
    private String duracao;
    private String nivel;
    private Double preco; // Mantido como Double, o controller converterá a string do form

    // Construtor para o model.addAttribute no PageController
    public CursoCriacaoDTO(String titulo, String descricao, String categoriasInput) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoriasInput = categoriasInput;
    }
}