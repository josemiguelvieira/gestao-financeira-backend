package com.jm.gestao_financeira2.repository;

import com.jm.gestao_financeira2.entity.Categoria;
import com.jm.gestao_financeira2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de Categoria.
 * Usa Spring Data JPA (query methods) para filtrar sempre por usuário logado.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Lista categorias de um usuário em ordem alfabética.
     */
    List<Categoria> findByUsuarioOrderByNomeAsc(Usuario usuario);

    /**
     * Busca categoria por id garantindo que pertence ao usuário.
     */
    Optional<Categoria> findByIdAndUsuario(Long id, Usuario usuario);

    /**
     * Verifica se já existe uma categoria com o mesmo nome (case-insensitive) para o usuário.
     */
    boolean existsByNomeIgnoreCaseAndUsuario(String nome, Usuario usuario);
}
