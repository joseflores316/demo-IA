package com.jose.demoia.actriz.domain.ports.in;

import com.jose.demoia.actriz.domain.model.ActrizEscena;

import java.util.List;

public interface ActrizEscenaUseCase {
    
    ActrizEscena asociarActrizConEscena(Long actrizId, Long escenaId, String papel);
    
    void desasociarActrizDeEscena(Long actrizId, Long escenaId);
    
    List<ActrizEscena> obtenerActricesPorEscena(Long escenaId);
    
    List<ActrizEscena> obtenerEscenasPorActriz(Long actrizId);
    
    void eliminarTodasLasAsociacionesPorEscena(Long escenaId);
}
