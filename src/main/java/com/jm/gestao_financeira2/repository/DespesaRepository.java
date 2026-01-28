package com.jm.gestao_financeira2.repository;

import com.jm.gestao_financeira2.entity.Despesa;
import com.jm.gestao_financeira2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de Despesa.
 * Centraliza queries para CRUD, filtros e agregações (dashboard/relatórios),
 * sempre garantindo isolamento por usuário.
 */
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    // =========================
    // CRUD básico (por usuário)
    // =========================

    /**
     * Lista despesas do usuário ordenadas pela data (mais recente primeiro).
     */
    List<Despesa> findByUsuarioOrderByDataDesc(Usuario usuario);

    /**
     * Busca uma despesa por id garantindo que pertence ao usuário.
     */
    Optional<Despesa> findByIdAndUsuario(Long id, Usuario usuario);

    // =========================
    // Filtro por período
    // =========================

    /**
     * Lista despesas do usuário dentro de um período, ordenadas pela data (desc).
     */
    List<Despesa> findByUsuarioAndDataBetweenOrderByDataDesc(
            Usuario usuario,
            LocalDate inicio,
            LocalDate fim
    );

    // =========================
    // Dashboard / Relatórios
    // =========================

    /**
     * Soma o total de despesas no período.
     * Usa COALESCE para garantir 0 quando não houver registros.
     */
    @Query("""
            select coalesce(sum(d.valor), 0)
            from Despesa d
            where d.usuario = :usuario
              and d.data between :inicio and :fim
            """)
    BigDecimal somarDespesasNoPeriodo(
            @Param("usuario") Usuario usuario,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    /**
     * Retorna totais por categoria no período.
     * Formato: [categoriaNome, total] (List<Object[]>)
     * Ordena do maior gasto para o menor (bom para gráfico).
     */
    @Query("""
            select d.categoria.nome, coalesce(sum(d.valor), 0)
            from Despesa d
            where d.usuario = :usuario
              and d.data between :inicio and :fim
            group by d.categoria.nome
            order by sum(d.valor) desc
            """)
    List<Object[]> totaisPorCategoriaNoPeriodo(
            @Param("usuario") Usuario usuario,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
}
