package com.parkeasy.vagasproximas.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface VagaRepository extends JpaRepository<com.parkeasy.vagasproximas.model.Vaga, Long> {

    @Query("Select v from Vaga v where v.status = 'DISPONIVEL' and v.latitude between :minLat and :maxLat and v.longitude between :minLon and :maxLon"
    + " and v.longitude between :minLon and :maxLon")
    List<com.parkeasy.vagasproximas.model.Vaga> findVagasProximas(@Param("minLat") Double minLat, @Param("maxLat") Double maxLat, 
                                                                @Param("minLon") Double minLon, @Param("maxLon") Double maxLon);    
    
}
