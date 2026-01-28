package com.jm.gestao_financeira2.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de requisição para criação/atualização de receitas.
 */
public class ReceitaRequest {

    private BigDecimal valor;
    private String descricao;
    private String categoria;
    private LocalDate data;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
