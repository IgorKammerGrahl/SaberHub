package com.elearning.security.payload;

import java.util.List;

public record LoginResponse(
    String token,
    Long id,            // ID do usuário
    String email,
    List<String> roles // Lista de roles/authorities
) {
    public LoginResponse(String token) {
        this(token, null, null, null);
    }
}