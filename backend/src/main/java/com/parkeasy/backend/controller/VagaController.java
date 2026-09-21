package com.parkeasy.backend.controller;

import com.parkeasy.backend.model.Vaga;
import com.parkeasy.backend.service.EnderecoService;
import com.parkeasy.backend.service.EnderecoService.Localizacao;
import com.parkeasy.backend.service.VagaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaController {

    private final VagaService vagaService;
    private final EnderecoService enderecoService;

    public VagaController(VagaService vagaService, EnderecoService enderecoService) {
        this.vagaService = vagaService;
        this.enderecoService = enderecoService;
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<?> pesquisarVagas(
            @RequestParam(required = false) String endereco,
            @RequestParam(defaultValue = "5.0") Double raioKm) {

        // Validação básica de parâmetro ausente ou em branco
        if (endereco == null || endereco.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro: O parâmetro 'endereco' é obrigatório.");
        }

        // Cenário 2 da HU3: Verificar se o endereço é localizado/válido
        Localizacao localizacao = enderecoService.localizarEndereco(endereco);

        if (localizacao == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Endereço não localizado. Por favor, verifique o texto digitado.");
        }

        // Cenário 1 da HU3: Buscar vagas próximas com base na localização do endereço
        List<Vaga> vagasProximas = vagaService.buscarVagasProximas(
                localizacao.getLatitude(),
                localizacao.getLongitude(),
                raioKm
        );

        return ResponseEntity.ok(vagasProximas);
    }
}