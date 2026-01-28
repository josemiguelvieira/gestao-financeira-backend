package com.jm.gestao_financeira2.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaRequest {

    @NotBlank(message = "Nome da categoria é obrigatório")
    private String nome;

    public String getNome() {
        return nome != null ? nome.trim() : null;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
