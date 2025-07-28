package com.jose.demoia.actriz.domain.ports.out;

import com.jose.demoia.actriz.domain.model.Pais;
import java.util.List;
import java.util.Optional;

public interface PaisRepository {

    Pais save(Pais pais);

    Optional<Pais> findById(Long id);

    List<Pais> findAll();

    Optional<Pais> findByCodigoIso(String codigoIso);

    void deleteById(Long id);

    boolean existsById(Long id);
}
