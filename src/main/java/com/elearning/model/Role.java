// Role.java
package com.elearning.model;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(Permissao.ADMIN_LEITURA, Permissao.ADMIN_ESCRITA)),
    USER(Set.of(Permissao.USER_LEITURA));

    private final Set<Permissao> permissoes;

    Role(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public Set<Permissao> getPermissoes() {
        return permissoes;
    }
}
