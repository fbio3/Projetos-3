package com.parkeasy.vagasproximas.service;

import com.parkeasy.vagasproximas.dto.TarifaRequest;
import com.parkeasy.vagasproximas.exception.ConflitoTarifaException;
import com.parkeasy.vagasproximas.exception.TarifaNaoEncontradaException;
import com.parkeasy.vagasproximas.model.Tarifa;
import com.parkeasy.vagasproximas.model.Vaga;
import com.parkeasy.vagasproximas.repository.TarifaRepository;
import com.parkeasy.vagasproximas.repository.VagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/** História 7 — Atualizar preços por horário. */
@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;
    private final VagaRepository vagaRepository;

    public TarifaService(TarifaRepository tarifaRepository, VagaRepository vagaRepository) {
        this.tarifaRepository = tarifaRepository;
        this.vagaRepository = vagaRepository;
    }

    // Cenário 1: cadastrar tarifas para períodos distintos
    @Transactional
    public Tarifa cadastrar(Long vagaId, TarifaRequest r) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new TarifaNaoEncontradaException("Vaga não encontrada."));
        Tarifa nova = new Tarifa(vaga, r.nome(), r.dias(), r.horaInicio(), r.horaFim(), r.precoHora());
        verificarConflito(vagaId, nova, null);
        return tarifaRepository.save(nova);
    }

    // Cenário 3: a alteração vale para as próximas consultas
    @Transactional
    public Tarifa atualizar(Long vagaId, Long tarifaId, TarifaRequest r) {
        Tarifa tarifa = tarifaRepository.findById(tarifaId)
                .filter(t -> t.getVaga().getId().equals(vagaId))
                .orElseThrow(() -> new TarifaNaoEncontradaException("Tarifa não encontrada."));
        // valida o novo estado numa cópia antes de alterar a entidade gerenciada
        Tarifa candidata = new Tarifa(tarifa.getVaga(), r.nome(), r.dias(), r.horaInicio(), r.horaFim(), r.precoHora());
        verificarConflito(vagaId, candidata, tarifaId);
        tarifa.atualizar(r.nome(), r.dias(), r.horaInicio(), r.horaFim(), r.precoHora());
        return tarifa;
    }

    @Transactional(readOnly = true)
    public List<Tarifa> listar(Long vagaId) {
        return tarifaRepository.findByVagaId(vagaId);
    }

    /** Tarifa aplicável ao momento consultado, lida sempre do banco. */
    @Transactional(readOnly = true)
    public Optional<Tarifa> buscarVigente(Long vagaId, LocalDateTime momento) {
        return tarifaRepository.findByVagaId(vagaId).stream()
                .filter(t -> t.vigenteEm(momento))
                .findFirst();
    }

    @Transactional(readOnly = true)
    public Tarifa obterVigente(Long vagaId, LocalDateTime momento) {
        return buscarVigente(vagaId, momento)
                .orElseThrow(() -> new TarifaNaoEncontradaException("Nenhuma tarifa configurada para este horário."));
    }

    // Cenário 2: horários sobrepostos não são salvos
    private void verificarConflito(Long vagaId, Tarifa candidata, Long ignorarId) {
        for (Tarifa existente : tarifaRepository.findByVagaId(vagaId)) {
            if (existente.getId().equals(ignorarId)) continue;
            if (existente.conflitaCom(candidata)) {
                throw new ConflitoTarifaException(
                        "Conflito de horários com a tarifa \"" + existente.getNome() + "\".");
            }
        }
    }
}
