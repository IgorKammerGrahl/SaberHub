package com.elearning.exception;

public class CursoNaoEncontradoException extends RuntimeException {
    public CursoNaoEncontradoException(String cursoId) {
        super("Curso não encontrado com ID: " + cursoId);
    }
}