package com.parkeasy.vagasproximas.repository;

import com.parkeasy.vagasproximas.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    /** Os 10 reportes mais recentes, para a lista da página. */
    List<Reporte> findTop10ByOrderByIdDesc();
}
