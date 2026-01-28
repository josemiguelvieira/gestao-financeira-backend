package com.jm.gestao_financeira2.controller;

import com.jm.gestao_financeira2.auth.dto.DashboardResponse;
import com.jm.gestao_financeira2.auth.dto.DespesaPorCategoriaDTO;
import com.jm.gestao_financeira2.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@CrossOrigin(origins = "http://localhost:5173")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    /**
     * 🔹 Resumo financeiro do mês
     * Ex: GET /relatorios/resumo?mes=1&ano=2026
     */
    @GetMapping("/resumo")
    public ResponseEntity<DashboardResponse> resumo(
            @RequestParam int mes,
            @RequestParam int ano,
            Authentication auth
    ) {
        String email = auth.getName();

        DashboardResponse response =
                relatorioService.obterResumo(email, mes, ano);

        return ResponseEntity.ok(response);
    }

    /**
     * 🔹 Despesas agrupadas por categoria
     * Ex: GET /relatorios/despesas-por-categoria?mes=1&ano=2026
     */
    @GetMapping("/despesas-por-categoria")
    public ResponseEntity<List<DespesaPorCategoriaDTO>> despesasPorCategoria(
            @RequestParam int mes,
            @RequestParam int ano,
            Authentication auth
    ) {
        String email = auth.getName();

        List<DespesaPorCategoriaDTO> response =
                relatorioService.obterDespesasPorCategoria(email, mes, ano);

        return ResponseEntity.ok(response);
    }
}
