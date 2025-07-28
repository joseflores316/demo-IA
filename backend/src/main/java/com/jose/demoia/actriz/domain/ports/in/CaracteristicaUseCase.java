package com.jose.demoia.actriz.domain.ports.in;

import com.jose.demoia.actriz.domain.model.Caracteristica;
import com.jose.demoia.actriz.domain.model.TipoCaracteristica;
import java.util.List;
import java.util.Optional;

public interface CaracteristicaUseCase {

    Caracteristica crearCaracteristica(Caracteristica caracteristica);

    Optional<Caracteristica> obtenerCaracteristicaPorId(Long id);

    List<Caracteristica> obtenerTodasLasCaracteristicas();

    List<Caracteristica> obtenerCaracteristicasPorTipo(TipoCaracteristica tipo);

    Caracteristica actualizarCaracteristica(Caracteristica caracteristica);

    void eliminarCaracteristica(Long id);
}
