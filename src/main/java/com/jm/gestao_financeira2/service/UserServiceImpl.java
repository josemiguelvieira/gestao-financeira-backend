package com.jm.gestao_financeira2.service;

import com.jm.gestao_financeira2.auth.dto.AuthResponse;
import com.jm.gestao_financeira2.auth.dto.ChangePasswordRequest;
import com.jm.gestao_financeira2.auth.dto.UpdateProfileRequest;
import com.jm.gestao_financeira2.auth.dto.UserMeResponse;
import com.jm.gestao_financeira2.entity.Usuario;
import com.jm.gestao_financeira2.repository.UsuarioRepository;
import com.jm.gestao_financeira2.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Obtém o usuário autenticado a partir do SecurityContext.
     */
    private Usuario usuarioLogado() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        // Proteções extras contra null/anonymous
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        String email = auth.getName();

        if (email.isBlank() || "anonymousUser".equalsIgnoreCase(email) || !email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado"));
    }

    /**
     * Gera resposta padrão de autenticação (token + dados do usuário),
     * mantendo o mesmo padrão do AuthService.
     */
    private AuthResponse gerarAuthResponse(Usuario u) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", u.getId());
        claims.put("nome", u.getNome());

        String token = jwtService.generateToken(u.getEmail(), claims);
        return new AuthResponse(token, u.getId(), u.getNome(), u.getEmail());
    }

    @Override
    public UserMeResponse me() {
        Usuario u = usuarioLogado();
        return new UserMeResponse(u.getId(), u.getNome(), u.getEmail());
    }

    @Override
    public AuthResponse updateProfile(UpdateProfileRequest req) {
        Usuario u = usuarioLogado();

        if (!passwordEncoder.matches(req.getSenhaAtual(), u.getSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }

        String nome = (req.getNome() == null) ? "" : req.getNome().trim();
        String email = (req.getEmail() == null) ? "" : req.getEmail().trim().toLowerCase();

        if (nome.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome é obrigatório");
        }
        if (email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail é obrigatório");
        }

        // Se mudou o email, verifica duplicidade
        if (!email.equalsIgnoreCase(u.getEmail())) {
            boolean jaExiste = usuarioRepository.findByEmail(email).isPresent();
            if (jaExiste) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esse e-mail já está em uso");
            }
        }

        u.setNome(nome);
        u.setEmail(email);
        usuarioRepository.save(u);

        // Retorna token novo (email pode ter mudado)
        return gerarAuthResponse(u);
    }

    @Override
    public AuthResponse changePassword(ChangePasswordRequest req) {
        Usuario u = usuarioLogado();

        if (!passwordEncoder.matches(req.getSenhaAtual(), u.getSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }

        if (req.getNovaSenha() == null || req.getNovaSenha().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A nova senha deve ter no mínimo 8 caracteres");
        }

        if (!req.getNovaSenha().equals(req.getConfirmarNovaSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas não conferem");
        }

        u.setSenha(passwordEncoder.encode(req.getNovaSenha()));
        usuarioRepository.save(u);

        return gerarAuthResponse(u);
    }

    @Override
    public void deleteAccount() {
        Usuario u = usuarioLogado();
        usuarioRepository.delete(u);
    }
}
