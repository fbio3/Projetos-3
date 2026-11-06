
package com.parkeasy.vagasproximas.repository;

import com.parkeasy.vagasproximas.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

}
