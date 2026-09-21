package com.parkeasy.backend.service;

import com.parkeasy.backend.model.Vaga;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VagaService {

    private final List<Vaga> vagas = new ArrayList<>();

    public VagaService() {
        vagas.add(new Vaga(
                1L,
                "Rua do Futuro, Recife",
                -8.0476,
                -34.8770,
                true
        ));

        vagas.add(new Vaga(
                2L,
                "Av. Boa Viagem, Recife",
                -8.1195,
                -34.9030,
                true
        ));

        vagas.add(new Vaga(
                3L,
                "Rua do Sol, Recife",
                -8.0600,
                -34.8850,
                false
        ));
    }

    public List<Vaga> buscarVagasProximas(
            Double latitude,
            Double longitude,
            Double raioKm) {

        List<Vaga> resultado = new ArrayList<>();

        for (Vaga vaga : vagas) {

            if (!vaga.isDisponivel()) {
                continue;
            }

            double distancia = calcularDistancia(
                    latitude,
                    longitude,
                    vaga.getLatitude(),
                    vaga.getLongitude()
            );

            if (distancia <= raioKm) {
                resultado.add(vaga);
            }
        }

        return resultado;
    }

    private double calcularDistancia(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        double diferencaLatitude = Math.toRadians(lat2 - lat1);
        double diferencaLongitude = Math.toRadians(lon2 - lon1);

        double a = Math.sin(diferencaLatitude / 2)
                * Math.sin(diferencaLatitude / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(diferencaLongitude / 2)
                * Math.sin(diferencaLongitude / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        double raioTerraKm = 6371;

        return raioTerraKm * c;
    }
}