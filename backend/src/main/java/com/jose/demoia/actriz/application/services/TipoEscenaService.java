package com.jose.demoia.actriz.application.services;

import com.jose.demoia.actriz.domain.model.TipoEscena;
import com.jose.demoia.actriz.domain.ports.in.TipoEscenaUseCase;
import com.jose.demoia.actriz.domain.ports.out.TipoEscenaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoEscenaService implements TipoEscenaUseCase {

    private final TipoEscenaRepositoryPort tipoEscenaRepositoryPort;

    public TipoEscenaService(TipoEscenaRepositoryPort tipoEscenaRepositoryPort) {
        this.tipoEscenaRepositoryPort = tipoEscenaRepositoryPort;
    }

    @Override
    public TipoEscena crearTipoEscena(TipoEscena tipoEscena) {
        if (tipoEscenaRepositoryPort.existsByNombre(tipoEscena.getNombre())) {
            throw new IllegalArgumentException("Ya existe un tipo de escena con ese nombre");
        }
        return tipoEscenaRepositoryPort.save(tipoEscena);
    }

    @Override
    public Optional<TipoEscena> obtenerTipoEscenaPorId(Long id) {
        return tipoEscenaRepositoryPort.findById(id);
    }

    @Override
    public List<TipoEscena> obtenerTodosLosTiposEscena() {
        return tipoEscenaRepositoryPort.findAll();
    }

    @Override
    public TipoEscena actualizarTipoEscena(TipoEscena tipoEscena) {
        return tipoEscenaRepositoryPort.save(tipoEscena);
    }

    @Override
    public void eliminarTipoEscena(Long id) {
        tipoEscenaRepositoryPort.deleteById(id);
    }
}
