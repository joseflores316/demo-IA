package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.Escena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaEscenaRepository extends JpaRepository<Escena, Long> {

    List<Escena> findByTipoEscenaId(Long tipoEscenaId);

    @Query("SELECT DISTINCT e FROM Escena e JOIN ActrizEscena ae ON e.id = ae.escena.id WHERE ae.actriz.id = :actrizId")
    List<Escena> findByActrizId(@Param("actrizId") Long actrizId);
}
