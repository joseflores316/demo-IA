package com.jose.demoia.actriz.domain.ports.in;

import com.jose.demoia.actriz.domain.model.Escena;

import java.util.List;
import java.util.Optional;

public interface EscenaUseCase {

    /**
     * Crear una nueva escena
     * @param escena la escena a crear
     * @return la escena creada
     */
    Escena crearEscena(Escena escena);

    /**
     * Obtener una escena por su ID
     * @param id el ID de la escena
     * @return la escena encontrada o Optional.empty()
     */
    Optional<Escena> obtenerEscenaPorId(Long id);

    /**
     * Obtener todas las escenas
     * @return lista de todas las escenas
     */
    List<Escena> obtenerTodasLasEscenas();

    /**
     * Obtener escenas por tipo
     * @param tipoEscenaId el ID del tipo de escena
     * @return lista de escenas del tipo especificado
     */
    List<Escena> obtenerEscenasPorTipo(Long tipoEscenaId);

    /**
     * Eliminar una escena
     * @param id el ID de la escena a eliminar
     */
    void eliminarEscena(Long id);
}
