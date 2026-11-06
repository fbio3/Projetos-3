package com.parkeasy.vagasproximas.dto;

import com.parkeasy.vagasproximas.model.Tarifa;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record TarifaResponse(Long id, String nome, Set<DayOfWeek> dias,
                             LocalTime horaInicio, LocalTime horaFim, BigDecimal precoHora) {
    public static TarifaResponse de(Tarifa t) {
        return new TarifaResponse(t.getId(), t.getNome(), t.getDias(),
                t.getHoraInicio(), t.getHoraFim(), t.getPrecoHora());
    }
}
