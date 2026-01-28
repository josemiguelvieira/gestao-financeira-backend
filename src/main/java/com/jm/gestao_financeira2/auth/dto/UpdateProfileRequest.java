package com.jm.gestao_financeira2.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de requisição para atualização de perfil do usuário.
 */
public class UpdateProfileRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 150, message = "E-mail muito longo")
    private String email;

    @NotBlank(message = "Senha atual é obrigatória")
    @Size(min = 6, max = 100, message = "Senha atual inválida")
    private String senhaAtual;

    // 🔹 construtor vazio (Jackson precisa)
    public UpdateProfileRequest() {
    }

    // getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    // setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }
}
