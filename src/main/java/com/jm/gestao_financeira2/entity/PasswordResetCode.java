package com.jm.gestao_financeira2.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "password_reset_codes")
public class PasswordResetCode {

    /**
     * 🔹 ID do registro do código de reset
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 🔹 E-mail do usuário que solicitou o reset
     */
    @Column(nullable = false)
    private String email;

    /**
     * 🔹 Hash do código (NUNCA salvar o código puro)
     */
    @Column(nullable = false, length = 255)
    private String codeHash;

    /**
     * 🔹 Data/hora de expiração do código
     */
    @Column(nullable = false)
    private Instant expiresAt;

    /**
     * 🔹 Indica se o código já foi utilizado
     */
    @Column(nullable = false)
    private boolean used = false;

    /**
     * 🔹 Quantidade de tentativas inválidas
     */
    @Column(nullable = false)
    private int attempts = 0;

    /**
     * 🔹 Data/hora de criação do código
     */
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // =========================
    // Construtores
    // =========================

    // 🔹 construtor vazio (OBRIGATÓRIO pro JPA/Jackson)
    public PasswordResetCode() {}

    // 🔹 construtor usado no AuthService
    public PasswordResetCode(String email, String codeHash, Instant expiresAt) {
        this.email = email;
        this.codeHash = codeHash;
        this.expiresAt = expiresAt;
    }

    // =========================
    // Getters
    // =========================

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public int getAttempts() {
        return attempts;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // =========================
    // Setters
    // =========================

    // ✅ necessários pro fluxo de reset no AuthService

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
