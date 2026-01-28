package com.jm.gestao_financeira2.controller;

import com.jm.gestao_financeira2.dto.CategoriaRequest;
import com.jm.gestao_financeira2.dto.CategoriaResponse;
import com.jm.gestao_financeira2.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * Lista categorias do usuário autenticado.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar(Authentication auth) {
        return ResponseEntity.ok(categoriaService.listar(auth.getName()));
    }

    /**
     * Cria categoria para o usuário autenticado.
     * Retorna 201 + Location: /categorias/{id}
     */
    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(
            @Valid @RequestBody CategoriaRequest request,
            Authentication auth
    ) {
        CategoriaResponse criada = categoriaService.criar(auth.getName(), request);

        return ResponseEntity
                .created(URI.create("/categorias/" + criada.getId()))
                .body(criada);
    }

    /**
     * Atualiza uma categoria do usuário autenticado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request,
            Authentication auth
    ) {
        CategoriaResponse atualizada = categoriaService.atualizar(id, auth.getName(), request);
        return ResponseEntity.ok(atualizada);
    }

    /**
     * Exclui uma categoria do usuário autenticado.
     * Retorna 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            Authentication auth
    ) {
        categoriaService.excluir(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
