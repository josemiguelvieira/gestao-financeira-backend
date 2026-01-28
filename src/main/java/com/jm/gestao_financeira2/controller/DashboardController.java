package com.jm.gestao_financeira2.controller;

import com.jm.gestao_financeira2.auth.dto.DashboardResponse;
import com.jm.gestao_financeira2.auth.dto.DespesaPorCategoriaDTO;
import com.jm.gestao_financeira2.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Resumo mensal do dashboard.
     *
     * Exemplos:
     *  GET /dashboard?mes=1&ano=2026
     *  GET /dashboard (usa mês/ano atual)
     */
    @GetMapping
    public DashboardResponse resumo(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            Authentication auth
    ) {
        LocalDate hoje = LocalDate.now();

        int m = (mes != null) ? mes : hoje.getMonthValue();
        int a = (ano != null) ? ano : hoje.getYear();

        validarMesAno(m, a);

        return dashboardService.obterResumoMes(auth.getName(), m, a);
    }

    /**
     * Total de despesas agrupadas por categoria.
     *
     * Exemplos:
     *  GET /dashboard/despesas-por-categoria?mes=1&ano=2026
     *  GET /dashboard/despesas-por-categoria (usa mês/ano atual)
     */
    @GetMapping("/despesas-por-categoria")
    public List<DespesaPorCategoriaDTO> despesasPorCategoria(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            Authentication auth
    ) {
        LocalDate hoje = LocalDate.now();

        int m = (mes != null) ? mes : hoje.getMonthValue();
        int a = (ano != null) ? ano : hoje.getYear();

        validarMesAno(m, a);

        return dashboardService.despesasPorCategoria(auth.getName(), m, a);
    }

    /**
     * Validação simples de mês e ano.
     */
    private void validarMesAno(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido. Use 1 a 12.");
        }

        if (ano < 1900 || ano > 3000) {
            throw new IllegalArgumentException("Ano inválido.");
        }
    }
}
