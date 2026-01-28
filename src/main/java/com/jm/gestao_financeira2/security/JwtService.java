package com.jm.gestao_financeira2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço responsável por:
 * - Gerar tokens JWT
 * - Validar tokens
 * - Extrair claims (email, expiração, etc.)
 */
@Service
public class JwtService {

    // 🔑 Chave secreta em Base64 (application.properties)
    @Value("${app.jwt.secret}")
    private String secretKeyBase64;

    // ⏳ Expiração padrão: 24h
    @Value("${app.jwt.expiration-ms:86400000}")
    private long expirationMs;

    // ⏱️ Tolerância de relógio (segundos)
    @Value("${app.jwt.clock-skew-seconds:30}")
    private long clockSkewSeconds;

    // =========================
    // 🔐 GERAÇÃO DE TOKEN
    // =========================

    public String generateToken(String email) {
        return generateToken(email, new HashMap<>());
    }

    public String generateToken(String subject, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject) // email
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // ✅ VALIDAÇÃO
    // =========================

    // Compat: valida usando e-mail
    public boolean isTokenValid(String token, String email) {
        try {
            String tokenEmail = extractEmail(token);

            return tokenEmail != null
                    && !tokenEmail.isBlank()
                    && tokenEmail.equals(email)
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Validação correta usando UserDetails
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String tokenEmail = extractEmail(token);
            String username = userDetails.getUsername(); // email

            return tokenEmail != null
                    && !tokenEmail.isBlank()
                    && tokenEmail.equals(username)
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date exp = extractExpiration(token);
            return exp == null || exp.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // =========================
    // 🔎 EXTRAÇÃO DE CLAIMS
    // =========================

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(clockSkewSeconds)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // =========================
    // 🔑 CHAVE DE ASSINATURA
    // =========================

    private Key getSigningKey() {
        if (secretKeyBase64 == null || secretKeyBase64.isBlank()) {
            throw new IllegalStateException("app.jwt.secret não configurado no application.properties");
        }

        byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64.trim());

        // 32 bytes = 256 bits (HS256)
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret muito curta. Use uma chave >= 256 bits em base64.");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
