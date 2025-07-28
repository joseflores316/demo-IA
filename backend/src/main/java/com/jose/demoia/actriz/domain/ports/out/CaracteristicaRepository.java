package com.jose.demoia.actriz.domain.ports.out;

import com.jose.demoia.actriz.domain.model.Caracteristica;
import com.jose.demoia.actriz.domain.model.TipoCaracteristica;
import java.util.List;
import java.util.Optional;

public interface CaracteristicaRepository {

    Caracteristica save(Caracteristica caracteristica);

    Optional<Caracteristica> findById(Long id);

    List<Caracteristica> findAll();

    List<Caracteristica> findByTipo(TipoCaracteristica tipo);

    void deleteById(Long id);

    boolean existsById(Long id);
}
