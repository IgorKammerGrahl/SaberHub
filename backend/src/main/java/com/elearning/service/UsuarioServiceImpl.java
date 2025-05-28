package com.elearning.service;

import com.elearning.model.Usuario;
import com.elearning.repository.postgres.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        
        System.out.println("DEBUG - Role do usuário: " + usuario.getRole()); // Deve imprimir "ADMIN"
        System.out.println("DEBUG - Autoridades: " + usuario.getRole().getAuthorities()); // Deve listar ROLE_ADMIN + permissões
        
        return usuario;
    }

    @Override // Agora válido (vem de UsuarioService)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
}