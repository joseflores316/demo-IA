package com.jose.demoia.actriz.domain.ports.out;

import com.jose.demoia.actriz.domain.model.Escena;

import java.util.List;
import java.util.Optional;

public interface EscenaRepositoryPort {

    /**
     * Guardar una escena
     * @param escena la escena a guardar
     * @return la escena guardada
     */
    Escena save(Escena escena);

    /**
     * Buscar una escena por ID
     * @param id el ID de la escena
     * @return la escena encontrada o Optional.empty()
     */
    Optional<Escena> findById(Long id);

    /**
     * Obtener todas las escenas
     * @return lista de todas las escenas
     */
    List<Escena> findAll();

    /**
     * Buscar escenas por tipo de escena
     * @param tipoEscenaId el ID del tipo de escena
     * @return lista de escenas del tipo especificado
     */
    List<Escena> findByTipoEscenaId(Long tipoEscenaId);

    /**
     * Buscar escenas por actriz
     * @param actrizId el ID de la actriz
     * @return lista de escenas en las que participa la actriz
     */
    List<Escena> findByActrizId(Long actrizId);

    /**
     * Eliminar una escena por ID
     * @param id el ID de la escena a eliminar
     */
    void deleteById(Long id);

    /**
     * Verificar si existe una escena con el ID dado
     * @param id el ID de la escena
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Long id);
}
