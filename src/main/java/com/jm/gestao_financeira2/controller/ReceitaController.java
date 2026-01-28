package com.jm.gestao_financeira2.controller;

import com.jm.gestao_financeira2.dto.ReceitaRequest;
import com.jm.gestao_financeira2.dto.ReceitaResponse;
import com.jm.gestao_financeira2.service.ReceitaService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
@CrossOrigin(origins = "http://localhost:5173")
public class ReceitaController {

    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    /**
     * Lista todas as receitas do usuário autenticado.
     */
    @GetMapping
    public List<ReceitaResponse> listar(Authentication auth) {
        return receitaService.listar(auth.getName());
    }

    /**
     * Cria uma nova receita.
     */
    @PostMapping
    public ReceitaResponse criar(
            @Valid @RequestBody ReceitaRequest request,
            Authentication auth
    ) {
        return receitaService.criar(auth.getName(), request);
    }

    /**
     * Atualiza uma receita existente.
     */
    @PutMapping("/{id}")
    public ReceitaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReceitaRequest request,
            Authentication auth
    ) {
        return receitaService.atualizar(id, auth.getName(), request);
    }

    /**
     * Exclui uma receita pelo ID.
     */
    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id,
            Authentication auth
    ) {
        receitaService.excluir(id, auth.getName());
    }
}
