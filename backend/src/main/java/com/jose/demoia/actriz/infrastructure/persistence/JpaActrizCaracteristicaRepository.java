package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.ActrizCaracteristica;
import com.jose.demoia.actriz.domain.ports.out.ActrizCaracteristicaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaActrizCaracteristicaRepository extends JpaRepository<ActrizCaracteristica, Long>, ActrizCaracteristicaRepository {

    @Query("SELECT ac FROM ActrizCaracteristica ac WHERE ac.actriz.id = :actrizId")
    List<ActrizCaracteristica> findByActrizId(@Param("actrizId") Long actrizId);

    @Query("SELECT ac FROM ActrizCaracteristica ac WHERE ac.caracteristica.id = :caracteristicaId")
    List<ActrizCaracteristica> findByCaracteristicaId(@Param("caracteristicaId") Long caracteristicaId);

    @Query("SELECT ac FROM ActrizCaracteristica ac WHERE ac.actriz.id = :actrizId AND ac.caracteristica.id = :caracteristicaId")
    Optional<ActrizCaracteristica> findByActrizIdAndCaracteristicaId(@Param("actrizId") Long actrizId, @Param("caracteristicaId") Long caracteristicaId);

    @Modifying
    @Query("DELETE FROM ActrizCaracteristica ac WHERE ac.actriz.id = :actrizId AND ac.caracteristica.id = :caracteristicaId")
    void deleteByActrizIdAndCaracteristicaId(@Param("actrizId") Long actrizId, @Param("caracteristicaId") Long caracteristicaId);

    @Query("SELECT CASE WHEN COUNT(ac) > 0 THEN true ELSE false END FROM ActrizCaracteristica ac WHERE ac.actriz.id = :actrizId AND ac.caracteristica.id = :caracteristicaId")
    boolean existsByActrizIdAndCaracteristicaId(@Param("actrizId") Long actrizId, @Param("caracteristicaId") Long caracteristicaId);
}
