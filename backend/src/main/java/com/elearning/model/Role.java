package com.elearning.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    ADMIN(List.of(Permissao.ADMIN_LEITURA, Permissao.ADMIN_ESCRITA)),
    USER(List.of(Permissao.USER_LEITURA));

    private final List<Permissao> permissoes;

    Role(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

   public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        System.out.println("DEBUG - Adicionando ROLE: ROLE_" + this.name()); // Log para debug
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        for (Permissao permissao : permissoes) {
            System.out.println("DEBUG - Adicionando permissão: " + permissao.getPermissao()); // Log para debug
            authorities.add(new SimpleGrantedAuthority(permissao.getPermissao()));
        }
        return authorities;
    }
}