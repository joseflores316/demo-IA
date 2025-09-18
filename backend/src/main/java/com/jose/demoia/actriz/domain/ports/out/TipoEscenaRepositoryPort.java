package com.jose.demoia.actriz.domain.ports.out;

import com.jose.demoia.actriz.domain.model.TipoEscena;

import java.util.List;
import java.util.Optional;

public interface TipoEscenaRepositoryPort {
    
    TipoEscena save(TipoEscena tipoEscena);
    
    Optional<TipoEscena> findById(Long id);
    
    List<TipoEscena> findAll();
    
    void deleteById(Long id);
    
    boolean existsByNombre(String nombre);
}
