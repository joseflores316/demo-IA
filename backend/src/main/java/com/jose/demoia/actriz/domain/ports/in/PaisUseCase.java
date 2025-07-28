package com.jose.demoia.actriz.domain.ports.in;

import com.jose.demoia.actriz.domain.model.Pais;
import java.util.List;
import java.util.Optional;

public interface PaisUseCase {

    Pais crearPais(Pais pais);

    Optional<Pais> obtenerPaisPorId(Long id);

    List<Pais> obtenerTodosLosPaises();

    Optional<Pais> obtenerPaisPorCodigoIso(String codigoIso);

    Pais actualizarPais(Pais pais);

    void eliminarPais(Long id);
}
