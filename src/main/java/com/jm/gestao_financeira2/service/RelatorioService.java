package com.jm.gestao_financeira2.service;

import com.jm.gestao_financeira2.auth.dto.DashboardResponse;
import com.jm.gestao_financeira2.auth.dto.DespesaPorCategoriaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    private final DashboardService dashboardService;

    public RelatorioService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Retorna resumo financeiro do mês (saldo, receitas e despesas)
    public DashboardResponse obterResumo(String email, int mes, int ano) {
        return dashboardService.obterResumoMes(email, mes, ano);
    }

    // Retorna totais de despesas agrupadas por categoria
    public List<DespesaPorCategoriaDTO> obterDespesasPorCategoria(
            String email,
            int mes,
            int ano
    ) {
        return dashboardService.despesasPorCategoria(email, mes, ano);
    }
}
