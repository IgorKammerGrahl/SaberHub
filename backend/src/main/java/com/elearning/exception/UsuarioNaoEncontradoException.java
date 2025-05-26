package com.elearning.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    
    // Construtor para buscar por ID
    public UsuarioNaoEncontradoException(Long usuarioId) {
        super("Usuário não encontrado com ID: " + usuarioId);
    }

    // Novo construtor para buscar por email
    public UsuarioNaoEncontradoException(String email) {
        super("Usuário não encontrado com email: " + email);
    }
}