package com.jm.gestao_financeira2.controller;

import com.jm.gestao_financeira2.dto.DespesaRequest;
import com.jm.gestao_financeira2.dto.DespesaResponse;
import com.jm.gestao_financeira2.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/despesas")
@CrossOrigin(origins = "http://localhost:5173")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    /**
     * Lista todas as despesas do usuário autenticado.
     */
    @GetMapping
    public List<DespesaResponse> listar(Authentication auth) {
        return despesaService.listar(auth.getName());
    }

    /**
     * Lista despesas por período.
     *
     * Exemplo:
     *  GET /despesas/periodo?inicio=2026-01-01&fim=2026-01-31
     *
     * Útil para dashboard e relatórios.
     */
    @GetMapping("/periodo")
    public List<DespesaResponse> listarPorPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim,
            Authentication auth
    ) {
        return despesaService.listarPorPeriodo(auth.getName(), inicio, fim);
    }

    /**
     * Cria uma nova despesa.
     */
    @PostMapping
    public DespesaResponse criar(
            @Valid @RequestBody DespesaRequest request,
            Authentication auth
    ) {
        return despesaService.criar(auth.getName(), request);
    }

    /**
     * Atualiza uma despesa existente.
     */
    @PutMapping("/{id}")
    public DespesaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DespesaRequest request,
            Authentication auth
    ) {
        return despesaService.atualizar(id, auth.getName(), request);
    }

    /**
     * Exclui uma despesa pelo ID.
     */
    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id,
            Authentication auth
    ) {
        despesaService.excluir(id, auth.getName());
    }
}
