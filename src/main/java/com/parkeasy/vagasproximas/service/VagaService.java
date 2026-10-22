package com.parkeasy.vagasproximas.service;

import com.parkeasy.vagasproximas.model.Vaga;
import com.parkeasy.vagasproximas.repository.VagaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service 
public class VagaService {

    private static final double EARTH_RADIUS_METERS = 6371000;
    private final VagaRepository vagaRepository;

    public VagaService(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }

    public List<Vaga> listarVagas() {
        return vagaRepository.findAll();
    }

    public Vaga buscarVagaPorId(Long id) {
        return vagaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vaga não encontrada"));
    }

    public Vaga cadastrarVaga(Vaga vaga) {
        return vagaRepository.save(vaga);
    }

    public void removerVaga(Long id) {
        vagaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Vaga> buscarProximas(double lat, double lon, double raioMetros) {
        return buscarProximas(lat, lon, raioMetros, false);
    }

    // História 1
    @Transactional(readOnly = true)
    public List<Vaga> buscarProximas(double lat, double lon, double raioMetros, boolean somenteAcessiveis) {
        double deltaLat = Math.toDegrees(raioMetros / EARTH_RADIUS_METERS);
        double deltaLon = Math.toDegrees(raioMetros / (EARTH_RADIUS_METERS * Math.cos(Math.toRadians(lat))));

        double minLat = lat - deltaLat;
        double maxLat = lat + deltaLat;
        double minLon = lon - deltaLon;
        double maxLon = lon + deltaLon;

        List<Vaga> vagas = vagaRepository.findVagasProximas(minLat, maxLat, minLon, maxLon, somenteAcessiveis);

        vagas.forEach(v -> v.setDistanciaMetros(
                distancia(lat, lon, v.getLatitude(), v.getLongitude())));

        return vagas.stream()
                .filter(v -> v.getDistanciaMetros() <= raioMetros)
                .sorted(Comparator.comparingDouble(Vaga::getDistanciaMetros))
                .toList();
    }

    // Mantido para compatibilidade com as outras histórias
    @Transactional(readOnly = true)
    public List<Vaga> buscarVagasProximas(double lat, double lon, double raioKm) {
        return buscarVagasProximas(lat, lon, raioKm, false);
    }

    // História 1
    @Transactional(readOnly = true)
    public List<Vaga> buscarVagasProximas(double lat, double lon, double raioKm, boolean somenteAcessiveis) {
        return buscarProximas(lat, lon, raioKm * 1000.0, somenteAcessiveis);
    }

    private static double distancia(double lat1, double lon1, double lat2, double lon2) {
        double a = Math.pow(Math.toRadians(lat2 - lat1) / 2, 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.toRadians(lon2 - lon1) / 2, 2);
        return 2 * EARTH_RADIUS_METERS * Math.asin(Math.sqrt(a));
    }
}