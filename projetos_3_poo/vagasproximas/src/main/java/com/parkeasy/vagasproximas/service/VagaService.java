package com.parkeasy.vagasproximas.service;

import com.parkeasy.vagasproximas.model.Vaga;
import com.parkeasy.vagasproximas.repository.VagaRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service 
public class VagaService {

    private static final double EARTH_RADIUS_METERS = 6371000; // Raio da Terra em metros
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

    @Transactional (readOnly = true)
    public List<com.parkeasy.vagasproximas.model.Vaga> buscarProximas(double lat, double lon, double raioMetros) {
        double deltaLat = Math.toDegrees(raioMetros / EARTH_RADIUS_METERS);
        double deltaLon = Math.toDegrees(raioMetros / (EARTH_RADIUS_METERS * Math.cos(Math.toRadians(lat))));

        double minLat = lat - deltaLat;
        double maxLat = lat + deltaLat;
        double minLon = lon - deltaLon;
        double maxLon = lon + deltaLon;

        List<com.parkeasy.vagasproximas.model.Vaga> vagas = vagaRepository.findVagasProximas(minLat, maxLat, minLon, maxLon);

        vagas.forEach(v -> v.setDistanciaMetros(
            distancia(lat, lon, v.getLatitude(), v.getLongitude())));
        
        return vagas.stream()
                .filter(v -> v.getDistanciaMetros() <= raioMetros)
                .sorted(Comparator.comparingDouble(com.parkeasy.vagasproximas.model.Vaga::getDistanciaMetros))
                .toList();

}

private static double distancia (double lat1, double lon1, double lat2, double lon2) {
    double a = Math.pow(Math.toRadians(lat2 - lat1) / 2, 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.pow(Math.toRadians(lon2 - lon1) / 2, 2);
    return 2 * EARTH_RADIUS_METERS * Math.asin(Math.sqrt(a));
}
}
