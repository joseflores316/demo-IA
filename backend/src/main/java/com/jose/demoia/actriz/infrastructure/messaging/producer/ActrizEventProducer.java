package com.jose.demoia.actriz.infrastructure.messaging.producer;

import com.jose.demoia.actriz.infrastructure.messaging.events.ActrizEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Servicio productor de eventos de Kafka para actrices
 */
@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class ActrizEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(ActrizEventProducer.class);
    private static final String TOPIC_NAME = "actriz-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ActrizEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publica un evento de actriz al topic de Kafka
     */
    public void publishEvent(ActrizEvent event) {
        try {
            logger.info("🚀 Publicando evento: {} para actriz ID: {}", event.getEventType(), event.getActrizId());

            // Enviar el evento al topic
            ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(TOPIC_NAME, event.getActrizId().toString(), event);

            // Callback para manejar el resultado
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    logger.info("✅ Evento enviado exitosamente: {} - Offset: {}",
                              event.getEventType(), result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.error("❌ Error al enviar evento: {} - Error: {}",
                               event.getEventType(), ex.getMessage());
                }
            });

        } catch (Exception e) {
            logger.error("💥 Error inesperado al publicar evento: {} - {}", event.getEventType(), e.getMessage(), e);
        }
    }

    /**
     * Publica un evento con un callback personalizado
     */
    public void publishEventWithCallback(ActrizEvent event, Runnable onSuccess, Runnable onFailure) {
        try {
            logger.info("🚀 Publicando evento con callback: {} para actriz ID: {}",
                       event.getEventType(), event.getActrizId());

            ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(TOPIC_NAME, event.getActrizId().toString(), event);

            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    logger.info("✅ Evento enviado exitosamente con callback: {} - Offset: {}",
                              event.getEventType(), result.getRecordMetadata().offset());
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.error("❌ Error al enviar evento con callback: {} - Error: {}",
                               event.getEventType(), ex.getMessage());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            });

        } catch (Exception e) {
            logger.error("💥 Error inesperado al publicar evento con callback: {} - {}",
                        event.getEventType(), e.getMessage(), e);
            if (onFailure != null) {
                onFailure.run();
            }
        }
    }
}
