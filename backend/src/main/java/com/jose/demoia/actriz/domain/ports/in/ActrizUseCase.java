package com.jose.demoia.actriz.domain.ports.in;

import com.jose.demoia.actriz.domain.model.Actriz;
import java.util.List;
import java.util.Optional;

public interface ActrizUseCase {

    Actriz crearActriz(Actriz actriz);

    Optional<Actriz> obtenerActrizPorId(Long id);

    List<Actriz> obtenerTodasLasActrices();

    List<Actriz> obtenerActricesPorPais(Long paisId);

    List<Actriz> obtenerActricesPorCalificacion(Double calificacionMinima);

    Actriz actualizarActriz(Actriz actriz);

    void eliminarActriz(Long id);

    void asignarCaracteristicaAActriz(Long actrizId, Long caracteristicaId);

    void eliminarCaracteristicaDeActriz(Long actrizId, Long caracteristicaId);
}
