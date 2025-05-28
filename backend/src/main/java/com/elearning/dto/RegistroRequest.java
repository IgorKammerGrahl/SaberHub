package com.elearning.security.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para registro de novos usuários via formulário Thymeleaf ou API.
 * Ajuste o construtor vazio {@code new RegistroRequest("", "", "")} caso você
 * tenha adicionado um campo adicional no record, como 'nome'.
 */
public record RegistroRequest(
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 40, message = "Senha deve ter entre 6 e 40 caracteres")
    String senha
    // Se você quiser capturar o nome completo do usuário, descomente abaixo:
    // , @NotBlank(message = "Nome é obrigatório") String nome
) {
    // Construtor canônico gerado automaticamente pelo record.
    // Thymeleaf instanciará com new RegistroRequest("", "");
}