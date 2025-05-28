package com.elearning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Retorna 409 Conflict
public class InscricaoDuplicadaException extends RuntimeException {
    public InscricaoDuplicadaException(String message) {
        super(message);
    }
}