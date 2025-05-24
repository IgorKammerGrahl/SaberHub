package com.elearning.model;

public enum Permissao {
    ADMIN_LEITURA("admin:leitura"),
    ADMIN_ESCRITA("admin:escrita"),
    USER_LEITURA("user:leitura");
    

    private final String permissao;

    Permissao(String permissao) {
        this.permissao = permissao;
    }

    public String getPermissao() {
        return permissao;
    }
}