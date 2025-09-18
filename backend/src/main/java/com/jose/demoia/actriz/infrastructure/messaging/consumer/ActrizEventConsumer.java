package com.jose.demoia.actriz.infrastructure.messaging.consumer;

import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizCreadaEvent;
import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizActualizadaEvent;
import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizEliminadaEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Servicio consumidor de eventos de Kafka para actrices
 */
@Service
public class ActrizEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ActrizEventConsumer.class);

    /**
     * Escucha eventos de actrices - SIMPLIFICADO para mostrar solo lo esencial
     */
    @KafkaListener(topics = "actriz-events", groupId = "actriz-events-group")
    public void handleActrizEvent(@Payload Object event,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                 @Header(KafkaHeaders.OFFSET) long offset) {

        try {
            // MANEJO CORRECTO DE ConsumerRecord
            Object actualEvent = event;
            if (event instanceof ConsumerRecord) {
                ConsumerRecord<?, ?> consumerRecord = (ConsumerRecord<?, ?>) event;
                actualEvent = consumerRecord.value();
            }

            // Procesar diferentes tipos de eventos
            if (actualEvent instanceof ActrizCreadaEvent) {
                handleActrizCreada((ActrizCreadaEvent) actualEvent);
            } else if (actualEvent instanceof ActrizActualizadaEvent) {
                handleActrizActualizada((ActrizActualizadaEvent) actualEvent);
            } else if (actualEvent instanceof ActrizEliminadaEvent) {
                handleActrizEliminada((ActrizEliminadaEvent) actualEvent);
            }

        } catch (Exception e) {
            logger.error("❌ ERROR AL PROCESAR EVENTO KAFKA: {}", e.getMessage());
        }
    }

    /**
     * Procesa evento de actriz creada - SOLO LO ESENCIAL
     */
    private void handleActrizCreada(ActrizCreadaEvent event) {
        // SOLO LA NOTIFICACIÓN PRINCIPAL - LO QUE REALMENTE IMPORTA
        logger.info("🎭 NUEVA ACTRIZ REGISTRADA: {} (ID: {}) - Calificación: ⭐{}",
                   event.getActrizNombre(),
                   event.getActrizId(),
                   event.getCalificacion());

        enviarNotificacionSimple("Nueva actriz registrada: " + event.getActrizNombre());
    }

    /**
     * Procesa evento de actriz actualizada - SOLO LO ESENCIAL
     */
    private void handleActrizActualizada(ActrizActualizadaEvent event) {
        logger.info("📝 ACTRIZ ACTUALIZADA: {} (ID: {})", event.getActrizNombre(), event.getActrizId());

        enviarNotificacionSimple("Actriz actualizada: " + event.getActrizNombre());
    }

    /**
     * Procesa evento de actriz eliminada - SOLO LO ESENCIAL
     */
    private void handleActrizEliminada(ActrizEliminadaEvent event) {
        logger.info("🗑️ ACTRIZ ELIMINADA: {} (ID: {})", event.getActrizNombre(), event.getActrizId());

        enviarNotificacionSimple("Actriz eliminada: " + event.getActrizNombre());
    }

    /**
     * Simula el envío de una notificación simple
     */
    private void enviarNotificacionSimple(String mensaje) {
        logger.info("📢📢📢 NOTIFICACIÓN: {}", mensaje);

        // Aquí podrías integrar con:
        // - Servicio de email
        // - Slack/Discord
        // - Sistema de push notifications
        // - Webhook a otro sistema
    }
}
