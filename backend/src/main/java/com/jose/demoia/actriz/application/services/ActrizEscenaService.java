package com.jose.demoia.actriz.application.services;

import com.jose.demoia.actriz.domain.model.ActrizEscena;
import com.jose.demoia.actriz.domain.model.Actriz;
import com.jose.demoia.actriz.domain.model.Escena;
import com.jose.demoia.actriz.domain.ports.in.ActrizEscenaUseCase;
import com.jose.demoia.actriz.domain.ports.in.ActrizUseCase;
import com.jose.demoia.actriz.domain.ports.out.ActrizEscenaRepositoryPort;
import com.jose.demoia.actriz.domain.ports.out.EscenaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActrizEscenaService implements ActrizEscenaUseCase {

    private final ActrizEscenaRepositoryPort actrizEscenaRepositoryPort;
    private final ActrizUseCase actrizUseCase;
    private final EscenaRepositoryPort escenaRepositoryPort; // Usar el repository directamente

    public ActrizEscenaService(ActrizEscenaRepositoryPort actrizEscenaRepositoryPort,
                              ActrizUseCase actrizUseCase,
                              EscenaRepositoryPort escenaRepositoryPort) {
        this.actrizEscenaRepositoryPort = actrizEscenaRepositoryPort;
        this.actrizUseCase = actrizUseCase;
        this.escenaRepositoryPort = escenaRepositoryPort;
    }

    @Override
    public ActrizEscena asociarActrizConEscena(Long actrizId, Long escenaId, String papel) {
        // Verificar que la actriz existe
        Optional<Actriz> actriz = actrizUseCase.obtenerActrizPorId(actrizId);
        if (actriz.isEmpty()) {
            throw new IllegalArgumentException("La actriz con ID " + actrizId + " no existe");
        }

        // Verificar que la escena existe usando el repository directamente
        Optional<Escena> escena = escenaRepositoryPort.findById(escenaId);
        if (escena.isEmpty()) {
            throw new IllegalArgumentException("La escena con ID " + escenaId + " no existe");
        }

        // Verificar si ya existe la asociación
        Optional<ActrizEscena> existente = actrizEscenaRepositoryPort.findByActrizIdAndEscenaId(actrizId, escenaId);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("La actriz ya está asociada a esta escena");
        }

        // Crear nueva asociación
        ActrizEscena actrizEscena = new ActrizEscena(actriz.get(), escena.get(), papel);
        return actrizEscenaRepositoryPort.save(actrizEscena);
    }

    @Override
    public void desasociarActrizDeEscena(Long actrizId, Long escenaId) {
        actrizEscenaRepositoryPort.deleteByActrizIdAndEscenaId(actrizId, escenaId);
    }

    @Override
    public List<ActrizEscena> obtenerActricesPorEscena(Long escenaId) {
        return actrizEscenaRepositoryPort.findByEscenaId(escenaId);
    }

    @Override
    public List<ActrizEscena> obtenerEscenasPorActriz(Long actrizId) {
        return actrizEscenaRepositoryPort.findByActrizId(actrizId);
    }

    @Override
    public void eliminarTodasLasAsociacionesPorEscena(Long escenaId) {
        actrizEscenaRepositoryPort.deleteAllByEscenaId(escenaId);
    }
}
