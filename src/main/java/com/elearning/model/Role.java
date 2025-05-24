package com.elearning.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(List.of(Permissao.ADMIN_LEITURA, Permissao.ADMIN_ESCRITA)),
    USER(List.of(Permissao.USER_LEITURA));

    private final List<Permissao> permissoes;

    Role(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 1. Adicione a ROLE primeiro
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name())); // ROLE_ADMIN
        
        // 2. Adicione as permissões
        authorities.addAll(
            permissoes.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermissao()))
                .collect(Collectors.toList())
        );
        
        return authorities;
    }
}