package com.jm.gestao_financeira2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT executado 1x por request.
 * - Ignora rotas públicas (/auth/**, swagger, /error)
 * - Lê Authorization: Bearer <token>
 * - Valida token e seta Authentication no SecurityContext
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Rotas que NÃO devem passar pelo filtro.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Preflight (CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        if (path == null) return false;

        // Público: auth
        if (path.startsWith("/auth/")) return true;

        // Swagger / OpenAPI
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")) {
            return true;
        }

        // Evita loop em /error
        return path.equals("/error");
    }

    /**
     * Faz a autenticação via JWT quando houver Bearer token válido.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            // Sem header -> segue (SecurityConfig decide se bloqueia)
            if (authHeader == null || authHeader.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            // Deve começar com "Bearer "
            String header = authHeader.trim();
            if (header.length() < 7 || !header.regionMatches(true, 0, "Bearer ", 0, 7)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = header.substring(7).trim();

            // Token vazio
            if (jwt.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtService.extractEmail(jwt);

            // Não conseguiu extrair email
            if (email == null || email.isBlank()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write("Token inválido.");
                return;
            }

            // Só autentica se ainda não tiver auth no contexto
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Token inválido/expirado -> bloqueia aqui
                if (!jwtService.isTokenValid(jwt, userDetails)) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("text/plain; charset=UTF-8");
                    response.getWriter().write("Token inválido ou expirado.");
                    return;
                }

                // Token ok -> autentica
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Qualquer erro -> limpa e bloqueia
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("Erro de autenticação.");
        }
    }
}
