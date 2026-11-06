package com.parkeasy.vagasproximas.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record TarifaRequest(
        @NotBlank String nome,
        @NotEmpty Set<DayOfWeek> dias,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFim,
        @NotNull @Positive BigDecimal precoHora) {}
