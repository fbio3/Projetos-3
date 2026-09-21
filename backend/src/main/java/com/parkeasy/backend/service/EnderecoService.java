package com.parkeasy.backend.service;

import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    public Localizacao localizarEndereco(String endereco) {

        if (endereco == null || endereco.isBlank()) {
            return null;
        }

        String enderecoNormalizado = endereco.toLowerCase().trim();

        if (enderecoNormalizado.contains("rua do futuro")) {
            return new Localizacao(-8.0476, -34.8770);
        }

        if (enderecoNormalizado.contains("boa viagem")) {
            return new Localizacao(-8.1195, -34.9030);
        }

        if (enderecoNormalizado.contains("rua do sol")) {
            return new Localizacao(-8.0600, -34.8850);
        }

        return null;
    }

    public static class Localizacao {

        private final double latitude;
        private final double longitude;

        public Localizacao(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}