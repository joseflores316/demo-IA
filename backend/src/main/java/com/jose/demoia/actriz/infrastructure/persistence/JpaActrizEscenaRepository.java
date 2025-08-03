package com.jose.demoia.actriz.infrastructure.persistence;

import com.jose.demoia.actriz.domain.model.ActrizEscena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaActrizEscenaRepository extends JpaRepository<ActrizEscena, Long> {
    
    Optional<ActrizEscena> findByActrizIdAndEscenaId(Long actrizId, Long escenaId);
    
    List<ActrizEscena> findByEscenaId(Long escenaId);
    
    List<ActrizEscena> findByActrizId(Long actrizId);
    
    void deleteByActrizIdAndEscenaId(Long actrizId, Long escenaId);
    
    void deleteAllByEscenaId(Long escenaId);
}
