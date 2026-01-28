package com.jm.gestao_financeira2.service;

import com.jm.gestao_financeira2.dto.DespesaRequest;
import com.jm.gestao_financeira2.dto.DespesaResponse;
import com.jm.gestao_financeira2.entity.Categoria;
import com.jm.gestao_financeira2.entity.Despesa;
import com.jm.gestao_financeira2.entity.Usuario;
import com.jm.gestao_financeira2.repository.CategoriaRepository;
import com.jm.gestao_financeira2.repository.DespesaRepository;
import com.jm.gestao_financeira2.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public DespesaService(
            DespesaRepository despesaRepository,
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.despesaRepository = despesaRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // =========================
    // 👤 Usuário logado
    // =========================
    private Usuario getUsuarioLogado(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // =========================
    // 🧾 Mapper (Entity -> DTO)
    // =========================
    private DespesaResponse toResponse(Despesa d) {
        return new DespesaResponse(
                d.getId(),
                d.getValor(),
                d.getDescricao(),
                d.getData(),
                d.getFormaPagamento(),
                d.getObservacao(),
                d.getCategoria().getId(),
                d.getCategoria().getNome()
        );
    }

    // =========================
    // 📄 Listagem
    // =========================
    public List<DespesaResponse> listar(String email) {
        Usuario usuario = getUsuarioLogado(email);

        return despesaRepository.findByUsuarioOrderByDataDesc(usuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Filtro por período (útil pra relatórios/dash)
    public List<DespesaResponse> listarPorPeriodo(String email, LocalDate inicio, LocalDate fim) {
        Usuario usuario = getUsuarioLogado(email);

        return despesaRepository.findByUsuarioAndDataBetweenOrderByDataDesc(usuario, inicio, fim)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // ➕ Criar
    // =========================
    public DespesaResponse criar(String email, DespesaRequest request) {
        Usuario usuario = getUsuarioLogado(email);

        Categoria categoria = categoriaRepository.findByIdAndUsuario(request.getCategoriaId(), usuario)
                .orElseThrow(() -> new RuntimeException("Categoria inválida"));

        Despesa d = new Despesa();
        d.setUsuario(usuario);
        d.setCategoria(categoria);
        d.setValor(request.getValor());
        d.setDescricao(request.getDescricao());
        d.setData(request.getData());
        d.setFormaPagamento(request.getFormaPagamento());
        d.setObservacao(request.getObservacao());

        Despesa salva = despesaRepository.save(d);
        return toResponse(salva);
    }

    // =========================
    // ✏️ Atualizar
    // =========================
    public DespesaResponse atualizar(Long id, String email, DespesaRequest request) {
        Usuario usuario = getUsuarioLogado(email);

        Despesa d = despesaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        Categoria categoria = categoriaRepository.findByIdAndUsuario(request.getCategoriaId(), usuario)
                .orElseThrow(() -> new RuntimeException("Categoria inválida"));

        d.setCategoria(categoria);
        d.setValor(request.getValor());
        d.setDescricao(request.getDescricao());
        d.setData(request.getData());
        d.setFormaPagamento(request.getFormaPagamento());
        d.setObservacao(request.getObservacao());

        Despesa salva = despesaRepository.save(d);
        return toResponse(salva);
    }

    // =========================
    // 🗑️ Excluir
    // =========================
    public void excluir(Long id, String email) {
        Usuario usuario = getUsuarioLogado(email);

        Despesa d = despesaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        despesaRepository.delete(d);
    }
}
