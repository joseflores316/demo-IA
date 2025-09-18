package com.jose.demoia.actriz.infrastructure.messaging.consumer;

import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizCreadaEvent;
import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizActualizadaEvent;
import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizEliminadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
     * Escucha eventos de actrices creadas
     */
    @KafkaListener(topics = "actriz-events", groupId = "actriz-events-group")
    public void handleActrizEvent(@Payload Object event,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                 @Header(KafkaHeaders.OFFSET) long offset,
                                 Acknowledgment acknowledgment) {

        try {
            logger.info("📨 Evento recibido del topic: {} partition: {} offset: {}", topic, partition, offset);

            // Procesar diferentes tipos de eventos
            if (event instanceof ActrizCreadaEvent) {
                handleActrizCreada((ActrizCreadaEvent) event);
            } else if (event instanceof ActrizActualizadaEvent) {
                handleActrizActualizada((ActrizActualizadaEvent) event);
            } else if (event instanceof ActrizEliminadaEvent) {
                handleActrizEliminada((ActrizEliminadaEvent) event);
            } else {
                logger.warn("⚠️ Tipo de evento no reconocido: {}", event.getClass().getSimpleName());
            }

            // Confirmar que el mensaje fue procesado exitosamente
            acknowledgment.acknowledge();
            logger.info("✅ Evento procesado y confirmado exitosamente");

        } catch (Exception e) {
            logger.error("❌ Error al procesar evento: {}", e.getMessage(), e);
            // En un entorno de producción, aquí podrías implementar retry logic o DLQ
        }
    }

    /**
     * Procesa evento de actriz creada
     */
    private void handleActrizCreada(ActrizCreadaEvent event) {
        logger.info("🎭 ACTRIZ CREADA:");
        logger.info("   ID: {}", event.getActrizId());
        logger.info("   Nombre: {}", event.getActrizNombre());
        logger.info("   País: {}", event.getPaisNombre());
        logger.info("   Calificación: {}", event.getCalificacion());
        logger.info("   Fecha nacimiento: {}", event.getFechaNacimiento());
        logger.info("   Imagen: {}", event.getImagenUrl() != null ? "✅ Sí" : "❌ No");
        logger.info("   Timestamp: {}", event.getTimestamp());

        // Aquí puedes agregar lógica adicional como:
        // - Enviar email de bienvenida
        // - Crear entrada en sistema de auditoría
        // - Actualizar estadísticas
        // - Notificar a otros microservicios

        // Ejemplo de notificación simple
        enviarNotificacionSimple("Nueva actriz registrada: " + event.getActrizNombre());
    }

    /**
     * Procesa evento de actriz actualizada
     */
    private void handleActrizActualizada(ActrizActualizadaEvent event) {
        logger.info("🔄 ACTRIZ ACTUALIZADA:");
        logger.info("   ID: {}", event.getActrizId());
        logger.info("   Nombre: {}", event.getActrizNombre());
        logger.info("   País: {}", event.getPaisNombre());
        logger.info("   Calificación: {}", event.getCalificacion());
        logger.info("   Cambios: {}", event.getCambiosRealizados());
        logger.info("   Timestamp: {}", event.getTimestamp());

        // Aquí puedes agregar lógica adicional como:
        // - Registro de auditoría de cambios
        // - Notificación a usuarios suscritos
        // - Actualización de caché distribuido

        enviarNotificacionSimple("Actriz actualizada: " + event.getActrizNombre() + " - " + event.getCambiosRealizados());
    }

    /**
     * Procesa evento de actriz eliminada
     */
    private void handleActrizEliminada(ActrizEliminadaEvent event) {
        logger.info("🗑️ ACTRIZ ELIMINADA:");
        logger.info("   ID: {}", event.getActrizId());
        logger.info("   Nombre: {}", event.getActrizNombre());
        logger.info("   Motivo: {}", event.getMotivoEliminacion());
        logger.info("   Imagen eliminada: {}", event.getImagenUrlEliminada());
        logger.info("   Timestamp: {}", event.getTimestamp());

        // Aquí puedes agregar lógica adicional como:
        // - Limpieza de datos relacionados
        // - Archivado de información
        // - Notificación a administradores

        enviarNotificacionSimple("Actriz eliminada: " + event.getActrizNombre() + " - Motivo: " + event.getMotivoEliminacion());
    }

    /**
     * Método de ejemplo para enviar notificaciones
     */
    private void enviarNotificacionSimple(String mensaje) {
        // En un caso real, aquí podrías:
        // - Enviar emails
        // - Enviar push notifications
        // - Guardar en sistema de notificaciones
        // - Enviar a Slack/Discord/Teams

        logger.info("📢 NOTIFICACIÓN: {}", mensaje);
    }
}
