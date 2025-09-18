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
    private final ActrizEventProducer eventProducer;

    public ActrizService(ActrizRepository actrizRepository,
                        ActrizCaracteristicaRepository actrizCaracteristicaRepository,
                        CaracteristicaRepository caracteristicaRepository,
                        ActrizEventProducer eventProducer) {
        this.actrizRepository = actrizRepository;
        this.actrizCaracteristicaRepository = actrizCaracteristicaRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.eventProducer = eventProducer;

        // Log para verificar que el eventProducer se inyecta correctamente
        logger.error("🔧🔧🔧 ActrizService inicializado con eventProducer: {}",
                   eventProducer != null ? "INYECTADO CORRECTAMENTE" : "NULL - ERROR DE INYECCIÓN");

        // También imprimir en stderr para máxima visibilidad
        System.err.println("🔧🔧🔧 ActrizService inicializado con eventProducer: " +
                          (eventProducer != null ? "INYECTADO CORRECTAMENTE" : "NULL - ERROR DE INYECCIÓN"));
    }

    @Override
    @CacheEvict(value = "actrices", allEntries = true)
    public Actriz crearActriz(Actriz actriz) {
        logger.error("📝📝📝 INICIANDO creación de actriz: {}", actriz.getNombre());
        System.err.println("📝📝📝 INICIANDO creación de actriz: " + actriz.getNombre());

        Actriz actrizCreada = actrizRepository.save(actriz);
        logger.error("💾💾💾 Actriz guardada en BD con ID: {}", actrizCreada.getId());
        System.err.println("💾💾💾 Actriz guardada en BD con ID: " + actrizCreada.getId());

        // 🚀 Publicar evento de actriz creada
        logger.error("🚀🚀🚀 INTENTANDO publicar evento de Kafka para actriz ID: {}", actrizCreada.getId());
        System.err.println("🚀🚀🚀 INTENTANDO publicar evento de Kafka para actriz ID: " + actrizCreada.getId());

        if (eventProducer == null) {
            logger.error("❌❌❌ ERROR CRÍTICO: eventProducer es NULL!");
            System.err.println("❌❌❌ ERROR CRÍTICO: eventProducer es NULL!");
            return actrizCreada;
        }

        try {
            ActrizCreadaEvent evento = new ActrizCreadaEvent(
                actrizCreada.getId(),
                actrizCreada.getNombre(),
                actrizCreada.getFechaNacimiento(),
                actrizCreada.getCalificacion(),
                actrizCreada.getPais() != null ? actrizCreada.getPais().getNombre() : "N/A",
                actrizCreada.getImagenUrl()
            );

            logger.error("📦📦📦 Evento creado: {}", evento.getEventType());
            System.err.println("📦📦📦 Evento creado: " + evento.getEventType());

            logger.error("🔄🔄🔄 LLAMANDO a eventProducer.publishEvent()...");
            System.err.println("🔄🔄🔄 LLAMANDO a eventProducer.publishEvent()...");

            eventProducer.publishEvent(evento);

            logger.error("✅✅✅ LLAMADA a publishEvent completada sin excepción inmediata");
            System.err.println("✅✅✅ LLAMADA a publishEvent completada sin excepción inmediata");

        } catch (Exception e) {
            logger.error("💥💥💥 ERROR CRÍTICO al publicar evento de actriz creada: {}", e.getMessage(), e);
            System.err.println("💥💥💥 ERROR CRÍTICO al publicar evento de actriz creada: " + e.getMessage());
            e.printStackTrace();
        }

        logger.error("🏁🏁🏁 FINALIZANDO creación de actriz ID: {}", actrizCreada.getId());
        System.err.println("🏁🏁🏁 FINALIZANDO creación de actriz ID: " + actrizCreada.getId());
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

        // 🔄 Publicar evento de actriz actualizada
        try {
            ActrizActualizadaEvent evento = new ActrizActualizadaEvent(
                actrizActualizada.getId(),
                actrizActualizada.getNombre(),
                actrizActualizada.getFechaNacimiento(),
                actrizActualizada.getCalificacion(),
                actrizActualizada.getPais() != null ? actrizActualizada.getPais().getNombre() : "N/A",
                actrizActualizada.getImagenUrl(),
                "Información de actriz actualizada"
            );
            eventProducer.publishEvent(evento);
        } catch (Exception e) {
            // Log error pero no falla la transacción principal
            System.err.println("Error al publicar evento de actriz actualizada: " + e.getMessage());
        }

        return actrizActualizada;
    }

    @Override
    @CacheEvict(value = "actrices", allEntries = true)
    public void eliminarActriz(Long id) {
        // Obtener información de la actriz antes de eliminarla
        Optional<Actriz> actrizOptional = actrizRepository.findById(id);

        actrizRepository.deleteById(id);

        // 🗑️ Publicar evento de actriz eliminada
        if (actrizOptional.isPresent()) {
            try {
                Actriz actriz = actrizOptional.get();
                ActrizEliminadaEvent evento = new ActrizEliminadaEvent(
                    actriz.getId(),
                    actriz.getNombre(),
                    "Eliminación manual del sistema",
                    actriz.getImagenUrl()
                );
                eventProducer.publishEvent(evento);
            } catch (Exception e) {
                // Log error pero no falla la transacción principal
                System.err.println("Error al publicar evento de actriz eliminada: " + e.getMessage());
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
}
