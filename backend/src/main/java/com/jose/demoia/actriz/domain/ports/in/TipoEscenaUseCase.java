package com.jose.demoia.actriz.domain.ports.in;
import com.jose.demoia.actriz.domain.model.TipoEscena;

import java.util.List;
import java.util.Optional;

public interface TipoEscenaUseCase {

    TipoEscena crearTipoEscena(TipoEscena tipoEscena);

    Optional<TipoEscena> obtenerTipoEscenaPorId(Long id);

    List<TipoEscena> obtenerTodosLosTiposEscena();

    TipoEscena actualizarTipoEscena(TipoEscena tipoEscena);

    void eliminarTipoEscena(Long id);
}

