package com.jm.gestao_financeira2.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "despesas")
public class Despesa {

    /**
     * 🔹 ID da despesa
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 🔹 Valor da despesa
     * Precision 19, scale 2 → até bilhões com 2 casas decimais
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    /**
     * 🔹 Descrição da despesa
     * Ex: Supermercado, Aluguel, Uber...
     */
    @Column(nullable = false)
    private String descricao;

    /**
     * 🔹 Data da despesa
     */
    @Column(nullable = false)
    private LocalDate data;

    /**
     * 🔹 Forma de pagamento
     * Ex: Débito, Crédito, Pix...
     */
    @Column(name = "forma_pagamento")
    private String formaPagamento;

    /**
     * 🔹 Observação opcional
     */
    @Column(columnDefinition = "TEXT")
    private String observacao;

    /**
     * 🔹 Categoria associada à despesa
     * Relação N:1 com Categoria
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    /**
     * 🔹 Usuário dono da despesa
     * Relação N:1 com Usuario
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
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

    public LocalDate getData() {
        return data;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    // =========================
    // Setters
    // =========================

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
