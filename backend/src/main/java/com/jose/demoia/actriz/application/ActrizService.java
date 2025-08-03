package com.jose.demoia.actriz.application;

import com.jose.demoia.actriz.domain.model.Actriz;
import com.jose.demoia.actriz.domain.model.ActrizCaracteristica;
import com.jose.demoia.actriz.domain.model.Caracteristica;
import com.jose.demoia.actriz.domain.ports.in.ActrizUseCase;
import com.jose.demoia.actriz.domain.ports.out.ActrizRepository;
import com.jose.demoia.actriz.domain.ports.out.ActrizCaracteristicaRepository;
import com.jose.demoia.actriz.domain.ports.out.CaracteristicaRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActrizService implements ActrizUseCase {

    private final ActrizRepository actrizRepository;
    private final ActrizCaracteristicaRepository actrizCaracteristicaRepository;
    private final CaracteristicaRepository caracteristicaRepository;

    public ActrizService(ActrizRepository actrizRepository,
                        ActrizCaracteristicaRepository actrizCaracteristicaRepository,
                        CaracteristicaRepository caracteristicaRepository) {
        this.actrizRepository = actrizRepository;
        this.actrizCaracteristicaRepository = actrizCaracteristicaRepository;
        this.caracteristicaRepository = caracteristicaRepository;
    }

    @Override
    @CacheEvict(value = "actrices", allEntries = true) // Limpia todo el caché cuando se crea una nueva
    public Actriz crearActriz(Actriz actriz) {
        return actrizRepository.save(actriz);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "actrices", key = "#id", unless = "#result == null")
    public Optional<Actriz> obtenerActrizPorId(Long id) {
        return actrizRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "actrices", key = "'all'")
    public List<Actriz> obtenerTodasLasActrices() {
        return actrizRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Actriz> obtenerActricesPorPais(Long paisId) {
        return actrizRepository.findByPaisId(paisId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Actriz> obtenerActricesPorCalificacion(Double calificacionMinima) {
        return actrizRepository.findByCalificacionGreaterThanEqual(calificacionMinima);
    }

    @Override
    @CachePut(value = "actrices", key = "#actriz.id")
    @CacheEvict(value = "actrices", key = "'all'") // Limpia la lista completa
    public Actriz actualizarActriz(Actriz actriz) {
        return actrizRepository.save(actriz);
    }

    @Override
    @CacheEvict(value = "actrices", allEntries = true)
    public void eliminarActriz(Long id) {
        actrizRepository.deleteById(id);
    }

    @Override
    public void asignarCaracteristicaAActriz(Long actrizId, Long caracteristicaId) {
        Optional<Actriz> actriz = actrizRepository.findById(actrizId);
        Optional<Caracteristica> caracteristica = caracteristicaRepository.findById(caracteristicaId);

        if (actriz.isPresent() && caracteristica.isPresent()) {
            if (!actrizCaracteristicaRepository.existsByActrizIdAndCaracteristicaId(actrizId, caracteristicaId)) {
                ActrizCaracteristica actrizCaracteristica = new ActrizCaracteristica(actriz.get(), caracteristica.get());
                actrizCaracteristicaRepository.save(actrizCaracteristica);
            }
        }
    }

    @Override
    public void eliminarCaracteristicaDeActriz(Long actrizId, Long caracteristicaId) {
        actrizCaracteristicaRepository.deleteByActrizIdAndCaracteristicaId(actrizId, caracteristicaId);
    }
}
