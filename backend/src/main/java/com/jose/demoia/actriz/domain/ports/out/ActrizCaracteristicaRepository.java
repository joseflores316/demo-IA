package com.jose.demoia.actriz.domain.ports.out;

import com.jose.demoia.actriz.domain.model.ActrizCaracteristica;
import java.util.List;
import java.util.Optional;

public interface ActrizCaracteristicaRepository {

    ActrizCaracteristica save(ActrizCaracteristica actrizCaracteristica);

    Optional<ActrizCaracteristica> findById(Long id);

    List<ActrizCaracteristica> findByActrizId(Long actrizId);

    List<ActrizCaracteristica> findByCaracteristicaId(Long caracteristicaId);

    Optional<ActrizCaracteristica> findByActrizIdAndCaracteristicaId(Long actrizId, Long caracteristicaId);

    void deleteById(Long id);

    void deleteByActrizIdAndCaracteristicaId(Long actrizId, Long caracteristicaId);

    boolean existsByActrizIdAndCaracteristicaId(Long actrizId, Long caracteristicaId);
}
