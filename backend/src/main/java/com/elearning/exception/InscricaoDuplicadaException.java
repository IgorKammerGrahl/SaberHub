package com.elearning.exception;

public class InscricaoDuplicadaException extends RuntimeException {
    public InscricaoDuplicadaException(Long usuarioId, String cursoId) {
        super("Usuário " + usuarioId + " já está inscrito no curso " + cursoId);
    }
}