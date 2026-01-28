package com.jm.gestao_financeira2.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requisição para exclusão de conta.
 * 
 * Usado no endpoint:
 * DELETE /users/me
 * 
 * Exige que o usuário informe a senha atual
 * como confirmação de identidade antes de apagar a conta.
 */
public class DeleteAccountRequest {

    /**
     * Senha atual do usuário.
     * 
     * Campo obrigatório para validar a identidade
     * antes de permitir a exclusão da conta.
     */
    @NotBlank(message = "Senha atual é obrigatória")
    private String senhaAtual;

    /**
     * Retorna a senha atual informada pelo usuário.
     */
    public String getSenhaAtual() {
        return senhaAtual;
    }
}
