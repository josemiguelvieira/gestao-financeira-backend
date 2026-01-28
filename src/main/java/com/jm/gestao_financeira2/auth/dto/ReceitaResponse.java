package com.jm.gestao_financeira2.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta para listagem e retorno de receitas.
 */
public class ReceitaResponse {

    private final Long id;
    private final BigDecimal valor;
    private final String descricao;
    private final String categoria;
    private final LocalDate data;

    public ReceitaResponse(
            Long id,
            BigDecimal valor,
            String descricao,
            String categoria,
            LocalDate data
    ) {
        this.id = id;
        this.valor = valor;
        this.descricao = descricao;
        this.categoria = categoria;
        this.data = data;
    }

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
}
