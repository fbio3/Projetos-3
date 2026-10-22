package com.parkeasy.vagasproximas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Vaga {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição/endereço da vaga não pode estar em branco")
    private String descricao;

    @NotNull 
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @NotNull 
    @Enumerated(EnumType.STRING)
    private StatusVaga status = StatusVaga.DISPONIVEL;

    // História 1
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean acessivel = false;

    @Transient 
    private Double distanciaMetros;

    public Vaga() {}

    public Vaga(Long id, String descricao, Double latitude, Double longitude, StatusVaga status) {
        this.id = id;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    // Compatibilidade com a História 3
    public String getEndereco() { return descricao; }
    public void setEndereco(String endereco) { this.descricao = endereco; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public StatusVaga getStatus() { return status; }
    public void setStatus(StatusVaga status) { this.status = status; }

    // Compatibilidade com História 3
    public boolean isDisponivel() { return this.status == StatusVaga.DISPONIVEL; }
    public void setDisponivel(boolean disponivel) {
        this.status = disponivel ? StatusVaga.DISPONIVEL : StatusVaga.OCUPADA;
    }

    public boolean isAcessivel() { return acessivel; }
    public void setAcessivel(boolean acessivel) { this.acessivel = acessivel; }

    public Double getDistanciaMetros() { return distanciaMetros; }
    public void setDistanciaMetros(Double distanciaMetros) { this.distanciaMetros = distanciaMetros; }
}