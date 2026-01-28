package com.jm.gestao_financeira2.auth.dto;

import java.math.BigDecimal;

/**
 * DTO usado em relatórios de despesas agrupadas por categoria.
 */
public class DespesaPorCategoriaDTO {

    private String categoria;
    private BigDecimal total;

    // Construtor vazio (necessário para Jackson)
    public DespesaPorCategoriaDTO() {
    }

    // Construtor usado no service para projeções
    public DespesaPorCategoriaDTO(String categoria, BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
