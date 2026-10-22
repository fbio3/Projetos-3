package com.parkeasy.vagasproximas.repository;

import com.parkeasy.vagasproximas.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    @Query("SELECT v FROM Vaga v WHERE v.status = 'DISPONIVEL' "
         + "AND v.latitude BETWEEN :minLat AND :maxLat "
         + "AND v.longitude BETWEEN :minLon AND :maxLon "
         + "AND (:somenteAcessiveis = false OR v.acessivel = true)")
    List<Vaga> findVagasProximas(@Param("minLat") Double minLat,
                                 @Param("maxLat") Double maxLat,
                                 @Param("minLon") Double minLon,
                                 @Param("maxLon") Double maxLon,
                                 @Param("somenteAcessiveis") boolean somenteAcessiveis);
}