package com.jm.gestao_financeira2.controller;

import com.jm.gestao_financeira2.auth.dto.AuthResponse;
import com.jm.gestao_financeira2.auth.dto.ChangePasswordRequest;
import com.jm.gestao_financeira2.auth.dto.UpdateProfileRequest;
import com.jm.gestao_financeira2.auth.dto.UserMeResponse;
import com.jm.gestao_financeira2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 🔹 Retorna dados do usuário autenticado
     * GET /users/me
     */
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserMeResponse> me() {
        return ResponseEntity.ok(userService.me());
    }

    /**
     * 🔹 Atualiza perfil (nome/e-mail)
     * Retorna um novo token caso o e-mail seja alterado
     * PUT /users/me
     */
    @PutMapping(
            value = "/me",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    /**
     * 🔹 Troca a senha do usuário autenticado
     * Retorna um novo token
     * PUT /users/me/password
     */
    @PutMapping(
            value = "/me/password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    /**
     * 🔹 Exclui a conta do usuário autenticado
     * DELETE /users/me
     */
    @DeleteMapping(value = "/me", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAccount() {
        userService.deleteAccount();
        return ResponseEntity.ok("Conta excluída com sucesso.");
    }
}
