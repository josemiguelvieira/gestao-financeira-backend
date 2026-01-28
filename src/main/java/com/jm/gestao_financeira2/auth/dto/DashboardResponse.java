package com.jm.gestao_financeira2.auth.dto;

import java.math.BigDecimal;

public class DashboardResponse {

    private BigDecimal saldoAtual;
    private BigDecimal receitasMes;
    private BigDecimal despesasMes;

    // 🔹 Construtor vazio (OBRIGATÓRIO para Jackson)
    public DashboardResponse() {
    }

    // 🔹 Construtor usado no service
    public DashboardResponse(
            BigDecimal saldoAtual,
            BigDecimal receitasMes,
            BigDecimal despesasMes
    ) {
        this.saldoAtual = saldoAtual;
        this.receitasMes = receitasMes;
        this.despesasMes = despesasMes;
    }

    // ===== Getters =====

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public BigDecimal getReceitasMes() {
        return receitasMes;
    }

    public BigDecimal getDespesasMes() {
        return despesasMes;
    }

    // ===== Setters =====

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public void setReceitasMes(BigDecimal receitasMes) {
        this.receitasMes = receitasMes;
    }

    public void setDespesasMes(BigDecimal despesasMes) {
        this.despesasMes = despesasMes;
    }
}
