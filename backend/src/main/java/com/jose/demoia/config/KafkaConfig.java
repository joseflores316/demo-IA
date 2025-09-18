package com.jose.demoia.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false) // CAMBIO: No ejecutar por defecto
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}") // CORREGIDO: Usar puerto correcto
    private String bootstrapServers;

    @Value("${spring.kafka.properties.security.protocol:}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism:}")
    private String saslMechanism;

    @Value("${spring.kafka.properties.sasl.jaas.config:}")
    private String saslJaasConfig;

    // Producer Configuration - CORREGIDO para Railway/Confluent Cloud
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Configuraciones SASL/SSL para Confluent Cloud
        if (!securityProtocol.isEmpty()) {
            configProps.put("security.protocol", securityProtocol);
        }
        if (!saslMechanism.isEmpty()) {
            configProps.put("sasl.mechanism", saslMechanism);
        }
        if (!saslJaasConfig.isEmpty()) {
            configProps.put("sasl.jaas.config", saslJaasConfig);
        }

        // Configuraciones adicionales para mejor rendimiento y robustez
        configProps.put(ProducerConfig.ACKS_CONFIG, "1");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 5);
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 120000);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 300000);

        // Configurar el JsonSerializer para incluir información de tipo
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Consumer Configuration con ErrorHandlingDeserializer
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "actriz-events-group");

        // Configurar ErrorHandlingDeserializer para manejar errores de deserialización
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Configurar los deserializadores delegados
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // Configuraciones adicionales
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        configProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);

        // CONFIGURACIÓN CLAVE PARA SOLUCIONAR DESERIALIZACIÓN
        // Configurar JsonDeserializer para confiar en todos los paquetes y manejar tipos
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);  // CAMBIO CLAVE: true en lugar de false
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.jose.demoia.actriz.infrastructure.messaging.events.ActrizCreadaEvent");  // CAMBIO: usar el tipo específico

        // Configuración adicional para manejo de errores
        configProps.put(JsonDeserializer.TYPE_MAPPINGS,
            "ACTRIZ_CREADA:com.jose.demoia.actriz.infrastructure.messaging.events.ActrizCreadaEvent," +
            "ACTRIZ_ACTUALIZADA:com.jose.demoia.actriz.infrastructure.messaging.events.ActrizActualizadaEvent," +
            "ACTRIZ_ELIMINADA:com.jose.demoia.actriz.infrastructure.messaging.events.ActrizEliminadaEvent"
        );

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // Configurar manejo de errores - usar SeekToCurrentErrorHandler para versiones anteriores
        factory.setErrorHandler(new org.springframework.kafka.listener.SeekToCurrentErrorHandler());

        return factory;
    }
}
