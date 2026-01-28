package com.jm.gestao_financeira2.repository;

import com.jm.gestao_financeira2.entity.Receita;
import com.jm.gestao_financeira2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de Receita.
 * Centraliza queries relacionadas às receitas do usuário.
 */
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    /**
     * Lista todas as receitas do usuário em ordem decrescente por data.
     */
    List<Receita> findByUsuarioOrderByDataDesc(Usuario usuario);

    /**
     * Busca uma receita por id garantindo que pertence ao usuário.
     */
    Optional<Receita> findByIdAndUsuario(Long id, Usuario usuario);

    /**
     * Lista receitas do usuário dentro de um período.
     * Usado em relatórios e dashboard.
     */
    List<Receita> findByUsuarioAndDataBetweenOrderByDataDesc(
            Usuario usuario,
            LocalDate inicio,
            LocalDate fim
    );

    // =========================
    // DASHBOARD / RELATÓRIOS
    // =========================

    /**
     * Soma o total de receitas do usuário em um período.
     */
    @Query("""
        select coalesce(sum(r.valor), 0)
        from Receita r
        where r.usuario = :usuario
          and r.data between :inicio and :fim
    """)
    BigDecimal somarReceitasNoPeriodo(
            @Param("usuario") Usuario usuario,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
}
