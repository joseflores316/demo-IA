package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.TipoEscena;
import com.jose.demoia.actriz.domain.ports.out.TipoEscenaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TipoEscenaRepositoryAdapter implements TipoEscenaRepositoryPort {

    private final JpaTipoEscenaRepository jpaTipoEscenaRepository;

    public TipoEscenaRepositoryAdapter(JpaTipoEscenaRepository jpaTipoEscenaRepository) {
        this.jpaTipoEscenaRepository = jpaTipoEscenaRepository;
    }

    @Override
    public TipoEscena save(TipoEscena tipoEscena) {
        return jpaTipoEscenaRepository.save(tipoEscena);
    }

    @Override
    public Optional<TipoEscena> findById(Long id) {
        return jpaTipoEscenaRepository.findById(id);
    }

    @Override
    public List<TipoEscena> findAll() {
        return jpaTipoEscenaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaTipoEscenaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return jpaTipoEscenaRepository.existsByNombre(nombre);
    }
}
