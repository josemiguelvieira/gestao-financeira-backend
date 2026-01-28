package com.jm.gestao_financeira2.auth;

import com.jm.gestao_financeira2.auth.dto.AuthResponse;
import com.jm.gestao_financeira2.auth.dto.ForgotPasswordRequest;
import com.jm.gestao_financeira2.auth.dto.LoginRequest;
import com.jm.gestao_financeira2.auth.dto.RegisterRequest;
import com.jm.gestao_financeira2.auth.dto.ResetPasswordRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints públicos de autenticação e recuperação de senha.
 * Base path: /auth
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    /**
     * Cria uma conta e retorna token + dados do usuário.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody final RegisterRequest request) {
        final AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Efetua login e retorna token + dados do usuário.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody final LoginRequest request) {
        final AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Envia código de redefinição por e-mail (sem revelar se existe conta).
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody final ForgotPasswordRequest request
    ) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("Se existir uma conta, enviamos um código para o e-mail.");
    }

    /**
     * Valida o código e redefine a senha.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody final ResetPasswordRequest request
    ) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

    // (Opcional) Ping público pra testar se API está online
    // @GetMapping("/health")
    // public ResponseEntity<String> health() {
    //     return ResponseEntity.ok("OK");
    // }
}
