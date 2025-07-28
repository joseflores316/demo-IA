package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.ActrizEscena;
import com.jose.demoia.actriz.domain.ports.out.ActrizEscenaRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class ActrizEscenaRepositoryAdapter implements ActrizEscenaRepositoryPort {

    private final JpaActrizEscenaRepository jpaActrizEscenaRepository;

    public ActrizEscenaRepositoryAdapter(JpaActrizEscenaRepository jpaActrizEscenaRepository) {
        this.jpaActrizEscenaRepository = jpaActrizEscenaRepository;
    }

    @Override
    public ActrizEscena save(ActrizEscena actrizEscena) {
        return jpaActrizEscenaRepository.save(actrizEscena);
    }

    @Override
    public Optional<ActrizEscena> findByActrizIdAndEscenaId(Long actrizId, Long escenaId) {
        return jpaActrizEscenaRepository.findByActrizIdAndEscenaId(actrizId, escenaId);
    }

    @Override
    public List<ActrizEscena> findByEscenaId(Long escenaId) {
        return jpaActrizEscenaRepository.findByEscenaId(escenaId);
    }

    @Override
    public List<ActrizEscena> findByActrizId(Long actrizId) {
        return jpaActrizEscenaRepository.findByActrizId(actrizId);
    }

    @Override
    @Transactional
    public void deleteByActrizIdAndEscenaId(Long actrizId, Long escenaId) {
        jpaActrizEscenaRepository.deleteByActrizIdAndEscenaId(actrizId, escenaId);
    }

    @Override
    @Transactional
    public void deleteAllByEscenaId(Long escenaId) {
        jpaActrizEscenaRepository.deleteAllByEscenaId(escenaId);
    }
}
