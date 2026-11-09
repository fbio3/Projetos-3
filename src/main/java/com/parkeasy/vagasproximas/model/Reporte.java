package com.parkeasy.vagasproximas.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** História 6 — reporte de problema feito por um motorista. */
@Entity
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vagaId;
    private String tipoProblema;

    /** Detalhes opcionais escritos pelo motorista. */
    @Column(length = 500)
    private String descricao;

    private LocalDateTime criadoEm;

    @PrePersist
    void aoCriar() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }

    public Reporte() {
    }

    public Long getId() {
        return id;
    }

    public Long getVagaId() {
        return vagaId;
    }

    public void setVagaId(Long vagaId) {
        this.vagaId = vagaId;
    }

    public String getTipoProblema() {
        return tipoProblema;
    }

    public void setTipoProblema(String tipoProblema) {
        this.tipoProblema = tipoProblema;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
