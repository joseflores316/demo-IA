package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.Escena;
import com.jose.demoia.actriz.domain.ports.out.EscenaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EscenaRepositoryAdapter implements EscenaRepositoryPort {

    private final JpaEscenaRepository jpaEscenaRepository;

    public EscenaRepositoryAdapter(JpaEscenaRepository jpaEscenaRepository) {
        this.jpaEscenaRepository = jpaEscenaRepository;
    }

    @Override
    public Escena save(Escena escena) {
        return jpaEscenaRepository.save(escena);
    }

    @Override
    public Optional<Escena> findById(Long id) {
        return jpaEscenaRepository.findById(id);
    }

    @Override
    public List<Escena> findAll() {
        return jpaEscenaRepository.findAll();
    }

    @Override
    public List<Escena> findByTipoEscenaId(Long tipoEscenaId) {
        return jpaEscenaRepository.findByTipoEscenaId(tipoEscenaId);
    }

    @Override
    public List<Escena> findByActrizId(Long actrizId) {
        return jpaEscenaRepository.findByActrizId(actrizId);
    }

    @Override
    public void deleteById(Long id) {
        jpaEscenaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaEscenaRepository.existsById(id);
    }
}
