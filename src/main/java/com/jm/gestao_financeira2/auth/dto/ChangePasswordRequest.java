package com.jm.gestao_financeira2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank(message = "Senha atual é obrigatória")
    @Size(min = 6, max = 100, message = "Senha atual inválida")
    private String senhaAtual;

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 8, max = 100, message = "A nova senha deve ter no mínimo 8 caracteres")
    private String novaSenha;

    @NotBlank(message = "Confirmação da nova senha é obrigatória")
    @Size(min = 8, max = 100, message = "Confirmação de senha inválida")
    private String confirmarNovaSenha;

    // ✅ Construtor vazio (necessário para Jackson)
    public ChangePasswordRequest() {
    }

    // ===== Getters =====

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public String getConfirmarNovaSenha() {
        return confirmarNovaSenha;
    }

    // ===== Setters =====

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public void setConfirmarNovaSenha(String confirmarNovaSenha) {
        this.confirmarNovaSenha = confirmarNovaSenha;
    }
}
