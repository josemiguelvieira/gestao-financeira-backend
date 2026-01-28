package com.jm.gestao_financeira2.service;

import com.jm.gestao_financeira2.auth.dto.AuthResponse;
import com.jm.gestao_financeira2.auth.dto.ChangePasswordRequest;
import com.jm.gestao_financeira2.auth.dto.UpdateProfileRequest;
import com.jm.gestao_financeira2.auth.dto.UserMeResponse;

/**
 * Contrato de serviços relacionados ao usuário autenticado.
 */
public interface UserService {

    // Retorna os dados básicos do usuário logado
    UserMeResponse me();

    // Atualiza perfil e retorna novo token (caso necessário)
    AuthResponse updateProfile(UpdateProfileRequest req);

    // Altera a senha e retorna novo token
    AuthResponse changePassword(ChangePasswordRequest req);

    // Remove definitivamente a conta do usuário
    void deleteAccount();
}
