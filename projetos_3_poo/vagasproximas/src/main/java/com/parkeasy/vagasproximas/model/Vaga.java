package com.parkeasy.vagasproximas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
public class Vaga {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "A descrição da vaga não pode estar em branco")
    private String descricao;

    @NotNull 
    @DecimalMin ("-90.0")
    @DecimalMax ("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin ("-180.0")
    @DecimalMax ("180.0")
    private Double longitude;

    @NotNull 
    @Enumerated (EnumType.STRING)
    private StatusVaga status = StatusVaga.DISPONIVEL;

    @Transient 
    private Double distanciaMetros;

    // Getters and Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescricao(String descricao){
        return descricao;
    }
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public StatusVaga getStatus() {
        return status;
    }
    public void setStatus(StatusVaga status) {
        this.status = status;
    }
    public Double getDistanciaMetros() {
        return distanciaMetros;
    }
    public void setDistanciaMetros(Double distanciaMetros) {
        this.distanciaMetros = distanciaMetros;
    }
}