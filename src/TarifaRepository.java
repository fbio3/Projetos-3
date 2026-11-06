package com.parkeasy.vagasproximas.repository;

import com.parkeasy.vagasproximas.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    List<Tarifa> findByVagaId(Long vagaId);
}
