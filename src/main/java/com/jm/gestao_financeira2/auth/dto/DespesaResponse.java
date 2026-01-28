package com.jm.gestao_financeira2.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta para listagem e visualização de despesas.
 */
public class DespesaResponse {

    private Long id;
    private BigDecimal valor;
    private String descricao;
    private LocalDate data;

    private String formaPagamento;
    private String observacao;

    private Long categoriaId;
    private String categoriaNome;

    public DespesaResponse(
            Long id,
            BigDecimal valor,
            String descricao,
            LocalDate data,
            String formaPagamento,
            String observacao,
            Long categoriaId,
            String categoriaNome
    ) {
        this.id = id;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.formaPagamento = formaPagamento;
        this.observacao = observacao;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
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

    public LocalDate getData() {
        return data;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }
}
