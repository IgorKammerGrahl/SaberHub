package com.elearning.service;

import com.elearning.model.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService extends UserDetailsService {
    Usuario buscarPorEmail(String email);
}