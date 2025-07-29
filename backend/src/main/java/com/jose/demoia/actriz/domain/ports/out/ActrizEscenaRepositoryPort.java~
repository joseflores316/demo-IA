package com.jose.demoia.actriz.domain.ports.out;

import com.jose.demoia.actriz.domain.model.ActrizEscena;

import java.util.List;
import java.util.Optional;

public interface ActrizEscenaRepositoryPort {

    ActrizEscena save(ActrizEscena actrizEscena);

    Optional<ActrizEscena> findByActrizIdAndEscenaId(Long actrizId, Long escenaId);

    List<ActrizEscena> findByEscenaId(Long escenaId);

    List<ActrizEscena> findByActrizId(Long actrizId);

    void deleteByActrizIdAndEscenaId(Long actrizId, Long escenaId);

    void deleteAllByEscenaId(Long escenaId);
}
