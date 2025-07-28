package com.jose.demoia.actriz.domain.ports.out;

import com.jose.demoia.actriz.domain.model.Actriz;
import java.util.List;
import java.util.Optional;

public interface ActrizRepository {

    Actriz save(Actriz actriz);

    Optional<Actriz> findById(Long id);

    List<Actriz> findAll();

    List<Actriz> findByPaisId(Long paisId);

    List<Actriz> findByCalificacionGreaterThanEqual(Double calificacion);

    void deleteById(Long id);

    boolean existsById(Long id);
}
