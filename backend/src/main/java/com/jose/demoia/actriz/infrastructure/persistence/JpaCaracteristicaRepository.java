package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.Caracteristica;
import com.jose.demoia.actriz.domain.model.TipoCaracteristica;
import com.jose.demoia.actriz.domain.ports.out.CaracteristicaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCaracteristicaRepository extends JpaRepository<Caracteristica, Long>, CaracteristicaRepository {

    List<Caracteristica> findByTipo(TipoCaracteristica tipo);
}
