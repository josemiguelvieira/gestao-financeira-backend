package com.jm.gestao_financeira2.auth;

import com.jm.gestao_financeira2.auth.dto.AuthResponse;
import com.jm.gestao_financeira2.auth.dto.ForgotPasswordRequest;
import com.jm.gestao_financeira2.auth.dto.LoginRequest;
import com.jm.gestao_financeira2.auth.dto.RegisterRequest;
import com.jm.gestao_financeira2.auth.dto.ResetPasswordRequest;
import com.jm.gestao_financeira2.entity.PasswordResetCode;
import com.jm.gestao_financeira2.entity.Usuario;
import com.jm.gestao_financeira2.repository.PasswordResetCodeRepository;
import com.jm.gestao_financeira2.repository.UsuarioRepository;
import com.jm.gestao_financeira2.security.JwtService;
import com.jm.gestao_financeira2.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Regras de autenticação + recuperação de senha.
 * Mantém respostas consistentes e sem vazar se um e-mail existe no sistema.
 */
@Service
public class AuthService {

    private static final int RESET_CODE_TTL_SECONDS = 300; // 5 min
    private static final int RESET_CODE_MIN = 100_000;
    private static final int RESET_CODE_RANGE = 900_000;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetCodeRepository resetCodeRepository;
    private final EmailService emailService;

    private final SecureRandom random = new SecureRandom();

    public AuthService(
            final UsuarioRepository usuarioRepository,
            final PasswordEncoder passwordEncoder,
            final JwtService jwtService,
            final AuthenticationManager authenticationManager,
            final PasswordResetCodeRepository resetCodeRepository,
            final EmailService emailService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.resetCodeRepository = resetCodeRepository;
        this.emailService = emailService;
    }

    // =========================
    // REGISTER
    // =========================
    public AuthResponse register(final RegisterRequest req) {
        final String email = normalizeEmail(req.getEmail());

        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        if (!req.getSenha().equals(req.getConfirmarSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas não conferem");
        }

        final Usuario usuario = new Usuario();
        usuario.setNome(req.getNome().trim());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(req.getSenha()));

        final Usuario salvo = usuarioRepository.save(usuario);

        final String token = jwtService.generateToken(salvo.getEmail(), buildClaims(salvo));
        return new AuthResponse(token, salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    // =========================
    // LOGIN
    // =========================
    public AuthResponse login(final LoginRequest req) {
        final String email = normalizeEmail(req.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, req.getSenha())
            );
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");
        }

        final Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos")
                );

        final String token = jwtService.generateToken(usuario.getEmail(), buildClaims(usuario));
        return new AuthResponse(token, usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    // =========================
    // FORGOT PASSWORD (PÚBLICO)
    // =========================
    public void forgotPassword(final ForgotPasswordRequest req) {
        final String email = normalizeEmail(req.getEmail());

        // Não revela se existe conta
        final Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) return;

        // Remove códigos anteriores para o mesmo e-mail
        resetCodeRepository.deleteByEmail(email);

        final String code = generate6DigitCode();
        final String codeHash = passwordEncoder.encode(code);
        final Instant expiresAt = Instant.now().plusSeconds(RESET_CODE_TTL_SECONDS);

        final PasswordResetCode prc = new PasswordResetCode(email, codeHash, expiresAt);
        resetCodeRepository.save(prc);

        emailService.enviarCodigoReset(email, code);
    }

    // =========================
    // RESET PASSWORD (PÚBLICO)
    // =========================
    public void resetPassword(final ResetPasswordRequest req) {
        final String email = normalizeEmail(req.getEmail());
        final String codigo = req.getCodigo().trim();

        if (!req.getNovaSenha().equals(req.getConfirmarNovaSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas não conferem");
        }

        final PasswordResetCode last = resetCodeRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido"));

        if (last.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código já utilizado");
        }

        if (Instant.now().isAfter(last.getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código expirado");
        }

        // Compara o código digitado com o hash salvo
        if (!passwordEncoder.matches(codigo, last.getCodeHash())) {
            last.setAttempts(last.getAttempts() + 1);
            resetCodeRepository.save(last);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido");
        }

        final Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setSenha(passwordEncoder.encode(req.getNovaSenha()));
        usuarioRepository.save(usuario);

        last.setUsed(true);
        resetCodeRepository.save(last);
    }

    // =========================
    // Helpers
    // =========================
    private String normalizeEmail(final String rawEmail) {
        return rawEmail == null ? null : rawEmail.trim().toLowerCase();
    }

    private Map<String, Object> buildClaims(final Usuario usuario) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("nome", usuario.getNome());
        return claims;
    }

    private String generate6DigitCode() {
        final int n = RESET_CODE_MIN + random.nextInt(RESET_CODE_RANGE);
        return String.valueOf(n);
    }
}
