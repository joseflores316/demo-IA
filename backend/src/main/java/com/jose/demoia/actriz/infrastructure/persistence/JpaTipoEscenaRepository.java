package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.TipoEscena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTipoEscenaRepository extends JpaRepository<TipoEscena, Long> {

    boolean existsByNombre(String nombre);
}
