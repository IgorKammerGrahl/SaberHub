package com.elearning.security.payload;

public record LoginRequest(
    String email,
    String senha
) {}