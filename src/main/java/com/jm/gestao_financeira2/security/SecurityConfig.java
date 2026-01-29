package com.jm.gestao_financeira2.security;

import com.jm.gestao_financeira2.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração de segurança da API:
 * - Stateless (JWT)
 * - Rotas /auth públicas
 * - Todo o resto protegido
 * - CORS liberado para o front Vite
 */
@Configuration
public class SecurityConfig {

    // =========================
    // 🔐 PASSWORD ENCODER (BCrypt)
    // =========================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================
    // 👤 USER DETAILS (email = username)
    // =========================
    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return email -> usuarioRepository.findByEmail(email)
                .map(u -> User.withUsername(u.getEmail())
                        .password(u.getSenha()) // senha já deve estar BCRYPT no banco
                        .roles("USER")
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado: " + email)
                );
    }

    // =========================
    // 🔑 JWT FILTER
    // =========================
    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtAuthFilter(jwtService, userDetailsService);
    }

    // =========================
    // 🔥 SECURITY FILTER CHAIN
    // =========================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                // ✅ API REST: CSRF off
                .csrf(csrf -> csrf.disable())

                // ✅ CORS (front Vite)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ✅ JWT: sem sessão
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅ desativa login padrão do Spring
                .httpBasic(b -> b.disable())
                .formLogin(f -> f.disable())

                // ✅ mensagem padrão quando não autenticado
                .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("text/plain; charset=UTF-8");
                    res.getWriter().write("Não autenticado. Faça login novamente.");
                }))

                // ✅ regras de autorização
                .authorizeHttpRequests(auth -> auth
                        // Preflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔓 AUTH PÚBLICO
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/auth/forgot-password",
                                "/auth/reset-password"
                        ).permitAll()

                        // 🔓 Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 🔒 o resto exige token
                        .anyRequest().authenticated()
                )

                // ⚠️ JWT filter antes do UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // =========================
    // 🌐 CORS CONFIG (Vite)
    // =========================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

       config.setAllowedOrigins(List.of(
    "http://localhost:5173",
    "https://gestao-financeira-frontend.onrender.com"
));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // =========================
    // 🔧 AUTHENTICATION MANAGER
    // =========================
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
