package com.jm.gestao_financeira2.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receitas")
public class Receita {

    /**
     * 🔹 ID da receita
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 🔹 Valor da receita
     */
    @Column(nullable = false)
    private BigDecimal valor;

    /**
     * 🔹 Descrição da receita
     */
    @Column(nullable = false)
    private String descricao;

    /**
     * 🔹 Categoria (string simples, ex: "Salário", "Freela", etc.)
     */
    @Column(nullable = false)
    private String categoria;

    /**
     * 🔹 Data da receita
     */
    @Column(nullable = false)
    private LocalDate data;

    /**
     * 🔹 Usuário dono da receita
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // =========================
    // Getters
    // =========================

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDate getData() {
        return data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    // =========================
    // Setters
    // =========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
