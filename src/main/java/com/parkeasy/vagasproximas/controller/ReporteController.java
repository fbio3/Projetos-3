
package com.parkeasy.vagasproximas.controller;

import com.parkeasy.vagasproximas.model.Reporte;
import com.parkeasy.vagasproximas.repository.ReporteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteRepository reporteRepository;

    public ReporteController(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @PostMapping
    public ResponseEntity<String> reportar(
            @RequestBody Map<String, Object> dados) {

        Object tipo = dados.get("tipoProblema");
        Object vaga = dados.get("vagaId");

        if (tipo == null || tipo.toString().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Selecione o tipo de problema.");
        }

        if (!tipo.equals("VAGA_OCUPADA") &&
            !tipo.equals("INFORMACAO_INCORRETA")) {
            return ResponseEntity.badRequest()
                    .body("Tipo de problema invalido.");
        }

        if (vaga == null) {
            return ResponseEntity.badRequest()
                    .body("Informe a vaga.");
        }

        Long vagaId;

        try {
            vagaId = Long.valueOf(vaga.toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body("Numero da vaga invalido.");
        }

        if (vagaId <= 0) {
            return ResponseEntity.badRequest()
                    .body("Numero da vaga invalido.");
        }

        Reporte reporte = new Reporte();
        reporte.setVagaId(vagaId);
        reporte.setTipoProblema(tipo.toString());

        reporteRepository.save(reporte);

        return ResponseEntity.ok(
                "Problema enviado com sucesso!");
    }
}

