package com.jm.gestao_financeira2.auth.dto;

public class AuthResponse {

    private final String token;
    private final Long id;
    private final String nome;
    private final String email;

    public AuthResponse(String token, Long id, String nome, String email) {
        this.token = token;
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
