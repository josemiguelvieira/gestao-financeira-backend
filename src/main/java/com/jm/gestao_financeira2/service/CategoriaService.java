package com.jm.gestao_financeira2.service;

import com.jm.gestao_financeira2.dto.CategoriaRequest;
import com.jm.gestao_financeira2.dto.CategoriaResponse;
import com.jm.gestao_financeira2.entity.Categoria;
import com.jm.gestao_financeira2.entity.Usuario;
import com.jm.gestao_financeira2.repository.CategoriaRepository;
import com.jm.gestao_financeira2.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // =========================
    // 👤 Usuário logado
    // =========================
    private Usuario getUsuarioLogado(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // =========================
    // ✍️ Normalização de nome
    // =========================
    private String normalizarNome(String nome) {
        if (nome == null) return "";
        // trim + remove múltiplos espaços no meio
        return nome.trim().replaceAll("\\s+", " ");
    }

    // =========================
    // 📄 Listar
    // =========================
    public List<CategoriaResponse> listar(String email) {
        Usuario usuario = getUsuarioLogado(email);

        return categoriaRepository.findByUsuarioOrderByNomeAsc(usuario)
                .stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNome()))
                .toList();
    }

    // =========================
    // ➕ Criar
    // =========================
    public CategoriaResponse criar(String email, CategoriaRequest request) {
        Usuario usuario = getUsuarioLogado(email);

        String nome = normalizarNome(request.getNome());
        if (nome.isBlank()) {
            throw new RuntimeException("Nome da categoria é obrigatório");
        }

        if (categoriaRepository.existsByNomeIgnoreCaseAndUsuario(nome, usuario)) {
            throw new RuntimeException("Categoria já existe");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setUsuario(usuario);

        Categoria salva = categoriaRepository.save(categoria);

        return new CategoriaResponse(salva.getId(), salva.getNome());
    }

    // =========================
    // ✏️ Atualizar
    // =========================
    public CategoriaResponse atualizar(Long id, String email, CategoriaRequest request) {
        Usuario usuario = getUsuarioLogado(email);

        Categoria categoria = categoriaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        String novoNome = normalizarNome(request.getNome());
        if (novoNome.isBlank()) {
            throw new RuntimeException("Nome da categoria é obrigatório");
        }

        // Se mudou o nome, checa duplicidade
        boolean mudouNome = !categoria.getNome().equalsIgnoreCase(novoNome);
        if (mudouNome && categoriaRepository.existsByNomeIgnoreCaseAndUsuario(novoNome, usuario)) {
            throw new RuntimeException("Já existe uma categoria com esse nome");
        }

        categoria.setNome(novoNome);

        Categoria salva = categoriaRepository.save(categoria);

        return new CategoriaResponse(salva.getId(), salva.getNome());
    }

    // =========================
    // 🗑️ Excluir
    // =========================
    public void excluir(Long id, String email) {
        Usuario usuario = getUsuarioLogado(email);

        Categoria categoria = categoriaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoriaRepository.delete(categoria);
    }
}
