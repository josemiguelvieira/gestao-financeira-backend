package com.jm.gestao_financeira2.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "categorias",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categoria_nome_usuario",
                        columnNames = {"nome", "usuario_id"}
                )
        }
)
public class Categoria {

    /**
     * 🔹 ID da categoria
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 🔹 Nome da categoria
     * Ex: Alimentação, Aluguel, Lazer...
     */
    @Column(nullable = false, length = 80)
    private String nome;

    /**
     * 🔹 Usuário dono da categoria
     * Relação N:1 com Usuario
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // =========================
    // Getters
    // =========================

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    // =========================
    // Setters
    // =========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
