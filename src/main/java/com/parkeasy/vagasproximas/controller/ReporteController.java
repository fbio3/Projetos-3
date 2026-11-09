package com.parkeasy.vagasproximas.controller;

import com.parkeasy.vagasproximas.model.Reporte;
import com.parkeasy.vagasproximas.repository.ReporteRepository;
import com.parkeasy.vagasproximas.repository.VagaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** História 6 — API para reportar problemas nas vagas ou nas informações. */
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private static final Set<String> TIPOS_VALIDOS =
            Set.of("VAGA_OCUPADA", "INFORMACAO_INCORRETA");
    private static final int MAX_DESCRICAO = 500;

    private final ReporteRepository reporteRepository;
    private final VagaRepository vagaRepository;

    public ReporteController(ReporteRepository reporteRepository,
                             VagaRepository vagaRepository) {
        this.reporteRepository = reporteRepository;
        this.vagaRepository = vagaRepository;
    }

    /** Reportes mais recentes, para acompanhamento. */
    @GetMapping
    public List<Reporte> listarRecentes() {
        return reporteRepository.findTop10ByOrderByIdDesc();
    }

    @PostMapping
    public ResponseEntity<String> reportar(@RequestBody Map<String, Object> dados) {

        Object tipo = dados.get("tipoProblema");
        Object vaga = dados.get("vagaId");
        Object descricaoObj = dados.get("descricao");

        // Cenário 3: não envia sem o tipo de problema
        if (tipo == null || tipo.toString().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Selecione o tipo de problema.");
        }

        if (!TIPOS_VALIDOS.contains(tipo.toString())) {
            return ResponseEntity.badRequest()
                    .body("Tipo de problema inválido.");
        }

        if (vaga == null || vaga.toString().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Selecione a vaga.");
        }

        Long vagaId;
        try {
            vagaId = Long.valueOf(vaga.toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body("Número da vaga inválido.");
        }

        if (vagaId <= 0 || !vagaRepository.existsById(vagaId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vaga não encontrada.");
        }

        String descricao = descricaoObj == null ? null : descricaoObj.toString().trim();
        if (descricao != null && descricao.length() > MAX_DESCRICAO) {
            return ResponseEntity.badRequest()
                    .body("A descrição pode ter no máximo " + MAX_DESCRICAO + " caracteres.");
        }

        Reporte reporte = new Reporte();
        reporte.setVagaId(vagaId);
        reporte.setTipoProblema(tipo.toString());
        reporte.setDescricao(descricao == null || descricao.isEmpty() ? null : descricao);

        reporteRepository.save(reporte);

        // Cenários 1 e 2: confirma que o problema foi enviado
        return ResponseEntity.ok("Problema enviado com sucesso! Obrigado por ajudar a manter as informações corretas.");
    }
}
