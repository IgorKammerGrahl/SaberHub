package com.elearning.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(Long usuarioId) {
        super("Usuário não encontrado com ID: " + usuarioId);
    }
}