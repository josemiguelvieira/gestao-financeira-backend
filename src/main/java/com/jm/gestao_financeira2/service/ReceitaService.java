package com.jm.gestao_financeira2.service;

import com.jm.gestao_financeira2.dto.ReceitaRequest;
import com.jm.gestao_financeira2.dto.ReceitaResponse;
import com.jm.gestao_financeira2.entity.Receita;
import com.jm.gestao_financeira2.entity.Usuario;
import com.jm.gestao_financeira2.repository.ReceitaRepository;
import com.jm.gestao_financeira2.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReceitaService(
            ReceitaRepository receitaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.receitaRepository = receitaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Obtém o usuário autenticado a partir do e-mail
    private Usuario usuarioLogado(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Lista receitas do usuário ordenadas por data
    public List<ReceitaResponse> listar(String email) {
        Usuario usuario = usuarioLogado(email);

        return receitaRepository.findByUsuarioOrderByDataDesc(usuario)
                .stream()
                .map(r -> new ReceitaResponse(
                        r.getId(),
                        r.getValor(),
                        r.getDescricao(),
                        r.getCategoria(),
                        r.getData()
                ))
                .toList();
    }

    // Cria nova receita vinculada ao usuário
    public ReceitaResponse criar(String email, ReceitaRequest request) {
        Usuario usuario = usuarioLogado(email);

        Receita r = new Receita();
        r.setValor(request.getValor());
        r.setDescricao(request.getDescricao());
        r.setCategoria(request.getCategoria());
        r.setData(request.getData());
        r.setUsuario(usuario);

        Receita salva = receitaRepository.save(r);

        return new ReceitaResponse(
                salva.getId(),
                salva.getValor(),
                salva.getDescricao(),
                salva.getCategoria(),
                salva.getData()
        );
    }

    // Atualiza receita existente do usuário
    public ReceitaResponse atualizar(Long id, String email, ReceitaRequest request) {
        Usuario usuario = usuarioLogado(email);

        Receita r = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));

        r.setValor(request.getValor());
        r.setDescricao(request.getDescricao());
        r.setCategoria(request.getCategoria());
        r.setData(request.getData());

        Receita salva = receitaRepository.save(r);

        return new ReceitaResponse(
                salva.getId(),
                salva.getValor(),
                salva.getDescricao(),
                salva.getCategoria(),
                salva.getData()
        );
    }

    // Remove receita do usuário
    public void excluir(Long id, String email) {
        Usuario usuario = usuarioLogado(email);

        Receita r = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));

        receitaRepository.delete(r);
    }
}
