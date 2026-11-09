
package com.parkeasy.vagasproximas.model;

import jakarta.persistence.*;

@Entity
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vagaId;
    private String tipoProblema;

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
}

