package com.jm.gestao_financeira2.repository;

import com.jm.gestao_financeira2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório de Usuário.
 * Centraliza consultas relacionadas à entidade Usuario.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo e-mail.
     * Usado em login, registro e autenticação JWT.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se já existe um usuário cadastrado com o e-mail informado.
     * Usado no fluxo de registro.
     */
    boolean existsByEmail(String email);
}
