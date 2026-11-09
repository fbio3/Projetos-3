package com.parkeasy.vagasproximas.controller;

import com.parkeasy.vagasproximas.dto.TarifaRequest;
import com.parkeasy.vagasproximas.dto.TarifaResponse;
import com.parkeasy.vagasproximas.service.TarifaService;
import com.parkeasy.vagasproximas.service.VagaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/** História 7 — API de tarifas por horário. */
@RestController
@RequestMapping("/api/vagas")
public class TarifaApiController {

    private final TarifaService tarifaService;
    private final VagaService vagaService;

    public TarifaApiController(TarifaService tarifaService, VagaService vagaService) {
        this.tarifaService = tarifaService;
        this.vagaService = vagaService;
    }

    /** Lista simples de vagas para o operador escolher qual configurar. */
    @GetMapping
    public List<Map<String, Object>> listarVagas() {
        return vagaService.listarVagas().stream()
                .map(v -> Map.<String, Object>of("id", v.getId(), "descricao", v.getDescricao()))
                .toList();
    }

    @GetMapping("/{vagaId}/tarifas")
    public List<TarifaResponse> listar(@PathVariable Long vagaId) {
        return tarifaService.listar(vagaId).stream().map(TarifaResponse::de).toList();
    }

    @PostMapping("/{vagaId}/tarifas")
    @ResponseStatus(HttpStatus.CREATED)
    public TarifaResponse cadastrar(@PathVariable Long vagaId, @Valid @RequestBody TarifaRequest req) {
        return TarifaResponse.de(tarifaService.cadastrar(vagaId, req));
    }

    @PutMapping("/{vagaId}/tarifas/{tarifaId}")
    public TarifaResponse atualizar(@PathVariable Long vagaId, @PathVariable Long tarifaId,
                                    @Valid @RequestBody TarifaRequest req) {
        return TarifaResponse.de(tarifaService.atualizar(vagaId, tarifaId, req));
    }

    /** Consulta do motorista: tarifa aplicável a um instante (padrão: agora). */
    @GetMapping("/{vagaId}/preco")
    public TarifaResponse precoNoHorario(@PathVariable Long vagaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime momento) {
        return TarifaResponse.de(tarifaService.obterVigente(vagaId,
                momento != null ? momento : LocalDateTime.now()));
    }
}
