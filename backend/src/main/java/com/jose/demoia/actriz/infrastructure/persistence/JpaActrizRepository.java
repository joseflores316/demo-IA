package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.Actriz;
import com.jose.demoia.actriz.domain.ports.out.ActrizRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaActrizRepository extends JpaRepository<Actriz, Long>, ActrizRepository {

    @Query("SELECT a FROM Actriz a WHERE a.pais.id = :paisId")
    List<Actriz> findByPaisId(@Param("paisId") Long paisId);

    @Query("SELECT a FROM Actriz a WHERE a.calificacion >= :calificacion")
    List<Actriz> findByCalificacionGreaterThanEqual(@Param("calificacion") Double calificacion);
}
