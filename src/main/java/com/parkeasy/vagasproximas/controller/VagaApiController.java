package com.parkeasy.vagasproximas.controller;

import com.parkeasy.vagasproximas.model.Vaga;
import com.parkeasy.vagasproximas.service.EnderecoService;
import com.parkeasy.vagasproximas.service.EnderecoService.Localizacao;
import com.parkeasy.vagasproximas.service.VagaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaApiController {

    private final VagaService vagaService;
    private final EnderecoService enderecoService;

    public VagaApiController(VagaService vagaService, EnderecoService enderecoService) {
        this.vagaService = vagaService;
        this.enderecoService = enderecoService;
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<?> pesquisarVagas(
            @RequestParam(required = false) String endereco,
            @RequestParam(defaultValue = "5.0") Double raioKm,
            @RequestParam(defaultValue = "false") boolean acessivel) {

        if (endereco == null || endereco.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro: O parâmetro 'endereco' é obrigatório.");
        }

        Localizacao localizacao = enderecoService.localizarEndereco(endereco);

        if (localizacao == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Endereço não localizado. Por favor, verifique o texto digitado.");
        }

        List<Vaga> vagasProximas = vagaService.buscarVagasProximas(
                localizacao.getLatitude(),
                localizacao.getLongitude(),
                raioKm,
                acessivel
        );

        return ResponseEntity.ok(vagasProximas);
    }
}