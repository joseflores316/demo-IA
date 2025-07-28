package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.Pais;
import com.jose.demoia.actriz.domain.ports.out.PaisRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPaisRepository extends JpaRepository<Pais, Long>, PaisRepository {

    Optional<Pais> findByCodigoIso(String codigoIso);
}
