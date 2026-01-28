package com.jm.gestao_financeira2.auth.dto;

/**
 * DTO de resposta para o endpoint /users/me.
 * Representa os dados básicos do usuário autenticado.
 */
public record UserMeResponse(
        Long id,
        String nome,
        String email
) {}
