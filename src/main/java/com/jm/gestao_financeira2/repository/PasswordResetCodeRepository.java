package com.jm.gestao_financeira2.repository;

import com.jm.gestao_financeira2.entity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório de códigos de redefinição de senha.
 * Responsável por armazenar, buscar e limpar códigos de reset por e-mail.
 */
@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {

    /**
     * Retorna o último código gerado para o e-mail,
     * ordenado pela data de criação (mais recente primeiro).
     */
    Optional<PasswordResetCode> findTopByEmailOrderByCreatedAtDesc(String email);

    /**
     * Remove todos os códigos associados a um e-mail.
     * Usado antes de gerar um novo código de reset.
     */
    @Transactional
    void deleteByEmail(String email);
}
