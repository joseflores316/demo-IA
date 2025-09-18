package com.jose.demoia.actriz.application;

import com.jose.demoia.actriz.domain.model.Actriz;
import com.jose.demoia.actriz.domain.model.ActrizCaracteristica;
import com.jose.demoia.actriz.domain.model.Caracteristica;
import com.jose.demoia.actriz.domain.ports.in.ActrizUseCase;
import com.jose.demoia.actriz.domain.ports.out.ActrizRepository;
import com.jose.demoia.actriz.domain.ports.out.ActrizCaracteristicaRepository;
import com.jose.demoia.actriz.domain.ports.out.CaracteristicaRepository;
import com.jose.demoia.actriz.infrastructure.messaging.producer.ActrizEventProducer;
import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizCreadaEvent;
import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizActualizadaEvent;
import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizEliminadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Primary  // Forzar a Spring a usar esta implementación
@Transactional
public class ActrizService implements ActrizUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ActrizService.class);

    private final ActrizRepository actrizRepository;
    private final ActrizCaracteristicaRepository actrizCaracteristicaRepository;
    private final CaracteristicaRepository caracteristicaRepository;

    // Hacer opcional la inyección del eventProducer para cuando Kafka esté deshabilitado
    @Autowired(required = false)
    private ActrizEventProducer eventProducer;

    public ActrizService(ActrizRepository actrizRepository,
                        ActrizCaracteristicaRepository actrizCaracteristicaRepository,
                        CaracteristicaRepository caracteristicaRepository) {
        this.actrizRepository = actrizRepository;
        this.actrizCaracteristicaRepository = actrizCaracteristicaRepository;
        this.caracteristicaRepository = caracteristicaRepository;

        // Log para verificar que el servicio se inicializa correctamente
        logger.info("🔧🔧🔧 ActrizService inicializado");
    }

    @Override
    @CacheEvict(value = "actrices", allEntries = true)
    public Actriz crearActriz(Actriz actriz) {
        logger.info("📝 Creando actriz: {}", actriz.getNombre());

        Actriz actrizCreada = actrizRepository.save(actriz);
        logger.info("💾 Actriz guardada en BD con ID: {}", actrizCreada.getId());

        // 🚀 Publicar evento de actriz creada de forma segura
        try {
            ActrizCreadaEvent evento = new ActrizCreadaEvent(
                actrizCreada.getId(),
                actrizCreada.getNombre(),
                actrizCreada.getFechaNacimiento(),
                actrizCreada.getCalificacion(),
                actrizCreada.getPais() != null ? actrizCreada.getPais().getNombre() : null,
                actrizCreada.getImagenUrl()
            );

            publishEventSafely(evento);

        } catch (Exception e) {
            logger.warn("Error al publicar evento de actriz creada: {}", e.getMessage());
        }

        logger.info("🏁 Actriz creada exitosamente ID: {}", actrizCreada.getId());
        return actrizCreada;
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
        Actriz actrizActualizada = actrizRepository.save(actriz);

        // 🔄 Publicar evento de actriz actualizada de forma segura
        try {
            ActrizActualizadaEvent evento = new ActrizActualizadaEvent(
                actrizActualizada.getId(),
                actrizActualizada.getNombre(),
                actrizActualizada.getFechaNacimiento(),
                actrizActualizada.getCalificacion(),
                actrizActualizada.getPais() != null ? actrizActualizada.getPais().getNombre() : null,
                actrizActualizada.getImagenUrl(),
                "Información de actriz actualizada"
            );
            publishEventSafely(evento);
        } catch (Exception e) {
            logger.warn("Error al publicar evento de actriz actualizada: {}", e.getMessage());
        }

        return actrizActualizada;
    }

    @Override
    @CacheEvict(value = "actrices", allEntries = true)
    public void eliminarActriz(Long id) {
        // Obtener información de la actriz antes de eliminarla
        Optional<Actriz> actrizOptional = actrizRepository.findById(id);

        actrizRepository.deleteById(id);

        // 🗑️ Publicar evento de actriz eliminada de forma segura
        if (actrizOptional.isPresent()) {
            try {
                Actriz actriz = actrizOptional.get();
                ActrizEliminadaEvent evento = new ActrizEliminadaEvent(
                    actriz.getId(),
                    actriz.getNombre(),
                    "Eliminación manual del sistema",
                    actriz.getImagenUrl()
                );
                publishEventSafely(evento);
            } catch (Exception e) {
                logger.warn("Error al publicar evento de actriz eliminada: {}", e.getMessage());
            }
        }
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

    // Método helper para publicar eventos de manera segura
    private void publishEventSafely(Object event) {
        if (eventProducer != null) {
            logger.info("📨 Kafka habilitado - Publicando evento...");
            if (event instanceof ActrizCreadaEvent) {
                eventProducer.publishEvent((ActrizCreadaEvent) event);
            } else if (event instanceof ActrizActualizadaEvent) {
                eventProducer.publishEvent((ActrizActualizadaEvent) event);
            } else if (event instanceof ActrizEliminadaEvent) {
                eventProducer.publishEvent((ActrizEliminadaEvent) event);
            }
        } else {
            logger.info("📨 Kafka deshabilitado - Evento no enviado: {}", event.getClass().getSimpleName());
        }
    }
}
