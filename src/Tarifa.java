package com.parkeasy.vagasproximas.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * História 7 — Tarifa de uma vaga para um período: dias da semana + janela de horário + preço/hora.
 * Janela que cruza a meia-noite (ex.: 18:00 às 06:00) é permitida.
 * horaInicio == horaFim significa o dia inteiro (ex.: tarifa de fim de semana).
 */
@Entity
public class Tarifa {

    private static final int DIA = 1440;
    private static final int SEMANA = 7 * DIA;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vaga vaga;

    @Column(nullable = false)
    private String nome;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tarifa_dia", joinColumns = @JoinColumn(name = "tarifa_id"))
    @Column(name = "dia")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> dias = new HashSet<>();

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFim;

    @Column(nullable = false)
    private BigDecimal precoHora;

    protected Tarifa() {}

    public Tarifa(Vaga vaga, String nome, Set<DayOfWeek> dias,
                  LocalTime horaInicio, LocalTime horaFim, BigDecimal precoHora) {
        this.vaga = vaga;
        atualizar(nome, dias, horaInicio, horaFim, precoHora);
    }

    public void atualizar(String nome, Set<DayOfWeek> dias,
                          LocalTime horaInicio, LocalTime horaFim, BigDecimal precoHora) {
        this.nome = nome;
        this.dias = new HashSet<>(dias);
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.precoHora = precoHora;
    }

    /** Intervalos [inicio, fim) em minutos desde a segunda-feira 00:00. */
    private List<int[]> intervalos() {
        List<int[]> saida = new ArrayList<>();
        int ini = horaInicio.getHour() * 60 + horaInicio.getMinute();
        int fim = horaFim.getHour() * 60 + horaFim.getMinute();
        for (DayOfWeek d : dias) {
            int base = (d.getValue() - 1) * DIA;
            if (ini < fim) saida.add(new int[]{base + ini, base + fim});
            else if (ini == fim) saida.add(new int[]{base, base + DIA});
            else saida.add(new int[]{base + ini, base + DIA + fim});
        }
        return saida;
    }

    /** Dois períodos conflitam se compartilham pelo menos um minuto da semana. */
    public boolean conflitaCom(Tarifa outra) {
        for (int[] a : intervalos())
            for (int[] b : outra.intervalos())
                for (int deslocamento : new int[]{-SEMANA, 0, SEMANA})
                    if (a[0] < b[1] + deslocamento && b[0] + deslocamento < a[1]) return true;
        return false;
    }

    public boolean vigenteEm(LocalDateTime momento) {
        int m = (momento.getDayOfWeek().getValue() - 1) * DIA
                + momento.getHour() * 60 + momento.getMinute();
        for (int[] a : intervalos())
            if ((m >= a[0] && m < a[1]) || (m + SEMANA >= a[0] && m + SEMANA < a[1])) return true;
        return false;
    }

    public Long getId() { return id; }
    public Vaga getVaga() { return vaga; }
    public String getNome() { return nome; }
    public Set<DayOfWeek> getDias() { return dias; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public BigDecimal getPrecoHora() { return precoHora; }
}
