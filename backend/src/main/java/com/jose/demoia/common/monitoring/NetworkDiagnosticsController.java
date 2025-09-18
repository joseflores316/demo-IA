package com.jose.demoia.common.monitoring;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/diagnostics")
public class NetworkDiagnosticsController {

    private static final Logger logger = LoggerFactory.getLogger(NetworkDiagnosticsController.class);

    @Value("${spring.kafka.bootstrap-servers:}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.sasl.jaas.config:}")
    private String saslJaasConfig;

    @Value("${spring.kafka.properties.security.protocol:}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism:}")
    private String saslMechanism;

    @GetMapping("/kafka-connectivity")
    public Map<String, Object> testKafkaConnectivity() {
        Map<String, Object> result = new HashMap<>();
        String kafkaHost = "pkc-nq356.eu-south-2.aws.confluent.cloud";
        int kafkaPort = 9092;

        try {
            logger.info("🔍 Probando conectividad TCP a {}:{} (Confluent Cloud, Railway)", kafkaHost, kafkaPort);
            Socket socket = new Socket();
            socket.setSoTimeout(10000); // 10 segundos
            long startTime = System.currentTimeMillis();
            socket.connect(new InetSocketAddress(kafkaHost, kafkaPort), 10000);
            long connectionTime = System.currentTimeMillis() - startTime;
            socket.close();
            result.put("status", "SUCCESS");
            result.put("message", "Conexión TCP exitosa a Confluent Cloud (Railway)");
            result.put("host", kafkaHost);
            result.put("port", kafkaPort);
            result.put("connectionTimeMs", connectionTime);
            result.put("timestamp", System.currentTimeMillis());
            logger.info("✅ Conectividad TCP exitosa en {}ms", connectionTime);
        } catch (SocketTimeoutException e) {
            result.put("status", "TIMEOUT");
            result.put("message", "Timeout de conexión - posible firewall bloqueando puerto " + kafkaPort);
            result.put("error", e.getMessage());
            result.put("diagnosis", "Railway puede estar bloqueando el puerto " + kafkaPort + ". Si usas Confluent Cloud, revisa la configuración de red y firewall.");
            logger.error("⏰ Timeout de conexión a {}:{}", kafkaHost, kafkaPort);
        } catch (IOException e) {
            result.put("status", "ERROR");
            result.put("message", "Error de conectividad de red");
            result.put("error", e.getMessage());
            result.put("diagnosis", "Posible restricción de firewall, red, o política de Railway. Si usas Confluent Cloud, revisa la configuración de red y credenciales.");
            logger.error("❌ Error de conectividad: {}", e.getMessage());
        } catch (Exception e) {
            result.put("status", "UNKNOWN_ERROR");
            result.put("message", "Error inesperado");
            result.put("error", e.getMessage());
            logger.error("💥 Error inesperado: {}", e.getMessage());
        }
        return result;
    }

    @GetMapping("/kafka-ssl-connectivity")
    public Map<String, Object> testKafkaSSLConnectivity() {
        Map<String, Object> result = new HashMap<>();
        String kafkaHost = "pkc-nq356.eu-south-2.aws.confluent.cloud";
        int kafkaSSLPort = 9092; // Confluent Cloud usa 9092 para SASL_SSL
        try {
            logger.info("🔍 Probando conectividad SSL a {}:{} (Confluent Cloud, Railway)", kafkaHost, kafkaSSLPort);
            javax.net.ssl.SSLSocketFactory factory = (javax.net.ssl.SSLSocketFactory) javax.net.ssl.SSLSocketFactory.getDefault();
            long startTime = System.currentTimeMillis();
            javax.net.ssl.SSLSocket sslSocket = (javax.net.ssl.SSLSocket) factory.createSocket(kafkaHost, kafkaSSLPort);
            sslSocket.setSoTimeout(15000);
            sslSocket.startHandshake();
            long connectionTime = System.currentTimeMillis() - startTime;
            sslSocket.close();
            result.put("status", "SUCCESS");
            result.put("message", "Conexión SSL exitosa a Confluent Cloud (Railway)");
            result.put("connectionTimeMs", connectionTime);
            logger.info("✅ Conectividad SSL exitosa en {}ms", connectionTime);
        } catch (Exception e) {
            result.put("status", "SSL_ERROR");
            result.put("message", "Error en handshake SSL (Railway puede estar bloqueando SSL o hay error de certificados)");
            result.put("error", e.getMessage());
            result.put("diagnosis", "Railway puede estar bloqueando SSL en puerto " + kafkaSSLPort + ". Si usas Confluent Cloud, revisa certificados y configuración de red.");
            logger.error("❌ Error SSL: {}", e.getMessage());
        }
        return result;
    }

    @GetMapping("/test-multiple-ports")
    public Map<String, Object> testMultiplePorts() {
        Map<String, Object> result = new HashMap<>();
        String kafkaHost = "pkc-nq356.eu-south-2.aws.confluent.cloud";
        int[] portsToTest = {9092, 9093, 443, 80};
        for (int port : portsToTest) {
            try {
                Socket socket = new Socket();
                socket.setSoTimeout(5000);
                long startTime = System.currentTimeMillis();
                socket.connect(new InetSocketAddress(kafkaHost, port), 5000);
                long connectionTime = System.currentTimeMillis() - startTime;
                socket.close();
                result.put("port_" + port, Map.of(
                    "status", "OPEN",
                    "connectionTimeMs", connectionTime
                ));
                logger.info("✅ Puerto {} ABIERTO ({}ms)", port, connectionTime);
            } catch (Exception e) {
                result.put("port_" + port, Map.of(
                    "status", "BLOCKED_OR_CLOSED",
                    "error", e.getMessage()
                ));
                logger.warn("❌ Puerto {} BLOQUEADO: {}", port, e.getMessage());
            }
        }
        return result;
    }

    @GetMapping("/kafka-auth-test")
    public Map<String, Object> testKafkaAuthentication() {
        Map<String, Object> result = new HashMap<>();

        try {
            logger.info("🔍 Probando autenticación SASL con Confluent Cloud");

            // Verificar configuración
            result.put("bootstrap_servers", bootstrapServers);
            result.put("security_protocol", securityProtocol);
            result.put("sasl_mechanism", saslMechanism);
            result.put("sasl_config_present", !saslJaasConfig.isEmpty());

            if (bootstrapServers.isEmpty() || saslJaasConfig.isEmpty()) {
                result.put("status", "CONFIG_ERROR");
                result.put("message", "Configuración de Kafka incompleta");
                result.put("missing", bootstrapServers.isEmpty() ? "bootstrap-servers" : "sasl-config");
                return result;
            }

            // Configurar producer de prueba
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put("security.protocol", securityProtocol);
            props.put("sasl.mechanism", saslMechanism);
            props.put("sasl.jaas.config", saslJaasConfig);

            // Configuración específica para diagnóstico
            props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "10000");
            props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "15000");
            props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, "5000");

            long startTime = System.currentTimeMillis();

            try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
                logger.info("🚀 Producer creado, enviando mensaje de prueba...");

                // Enviar mensaje de prueba
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    "actriz-events",
                    "test-key",
                    "{\"test\": \"auth-diagnostic\", \"timestamp\": " + System.currentTimeMillis() + "}"
                );

                var future = producer.send(record);
                var metadata = future.get(10, TimeUnit.SECONDS);

                long authTime = System.currentTimeMillis() - startTime;

                result.put("status", "SUCCESS");
                result.put("message", "Autenticación SASL exitosa - mensaje enviado");
                result.put("topic", metadata.topic());
                result.put("partition", metadata.partition());
                result.put("offset", metadata.offset());
                result.put("auth_time_ms", authTime);
                logger.info("✅ Autenticación SASL exitosa en {}ms", authTime);

            }

        } catch (org.apache.kafka.common.errors.SaslAuthenticationException e) {
            result.put("status", "SASL_AUTH_ERROR");
            result.put("message", "Error de autenticación SASL - credenciales incorrectas");
            result.put("error", e.getMessage());
            result.put("diagnosis", "Las credenciales SASL son incorrectas. Verifica KAFKA_USERNAME y KAFKA_PASSWORD en Railway.");
            logger.error("❌ Error de autenticación SASL: {}", e.getMessage());

        } catch (org.apache.kafka.common.errors.TopicAuthorizationException e) {
            result.put("status", "TOPIC_AUTH_ERROR");
            result.put("message", "Sin permisos para acceder al topic");
            result.put("error", e.getMessage());
            result.put("diagnosis", "El usuario no tiene permisos para escribir en el topic 'actriz-events'.");
            logger.error("❌ Error de autorización del topic: {}", e.getMessage());

        } catch (java.util.concurrent.TimeoutException e) {
            result.put("status", "TIMEOUT");
            result.put("message", "Timeout en autenticación SASL");
            result.put("error", e.getMessage());
            result.put("diagnosis", "La autenticación tardó demasiado - posible problema de red o configuración.");
            logger.error("⏰ Timeout en autenticación SASL: {}", e.getMessage());

        } catch (Exception e) {
            result.put("status", "UNKNOWN_ERROR");
            result.put("message", "Error inesperado en autenticación");
            result.put("error", e.getMessage());
            result.put("error_class", e.getClass().getSimpleName());
            logger.error("💥 Error inesperado en autenticación: {} - {}", e.getClass().getSimpleName(), e.getMessage());
        }

        return result;
    }

    @GetMapping("/kafka-consumer-test")
    public Map<String, Object> testKafkaConsumer() {
        Map<String, Object> result = new HashMap<>();

        try {
            logger.info("🔍 Probando Consumer de Kafka con Confluent Cloud");

            // Verificar configuración del consumer
            result.put("bootstrap_servers", bootstrapServers);
            result.put("security_protocol", securityProtocol);
            result.put("sasl_mechanism", saslMechanism);
            result.put("sasl_config_present", !saslJaasConfig.isEmpty());

            if (bootstrapServers.isEmpty() || saslJaasConfig.isEmpty()) {
                result.put("status", "CONFIG_ERROR");
                result.put("message", "Configuración de Kafka incompleta para Consumer");
                return result;
            }

            // Configurar consumer de prueba
            Properties props = new Properties();
            props.put("bootstrap.servers", bootstrapServers);
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("security.protocol", securityProtocol);
            props.put("sasl.mechanism", saslMechanism);
            props.put("sasl.jaas.config", saslJaasConfig);
            props.put("group.id", "diagnostic-consumer-group");
            props.put("auto.offset.reset", "latest");
            props.put("enable.auto.commit", "false");

            // Configuración específica para diagnóstico
            props.put("session.timeout.ms", "30000");
            props.put("heartbeat.interval.ms", "10000");
            props.put("request.timeout.ms", "120000");

            long startTime = System.currentTimeMillis();

            try (org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer =
                 new org.apache.kafka.clients.consumer.KafkaConsumer<>(props)) {

                logger.info("🚀 Consumer creado, suscribiéndose al topic...");

                // Suscribirse al topic
                consumer.subscribe(java.util.Arrays.asList("actriz-events"));

                // Intentar hacer poll para activar la conexión
                var records = consumer.poll(java.time.Duration.ofSeconds(10));

                long authTime = System.currentTimeMillis() - startTime;

                result.put("status", "SUCCESS");
                result.put("message", "Consumer conectado exitosamente a Confluent Cloud");
                result.put("records_received", records.count());
                result.put("partitions_assigned", consumer.assignment().size());
                result.put("auth_time_ms", authTime);
                logger.info("✅ Consumer conectado exitosamente en {}ms", authTime);

            }

        } catch (org.apache.kafka.common.errors.SaslAuthenticationException e) {
            result.put("status", "SASL_AUTH_ERROR");
            result.put("message", "Error de autenticación SASL en Consumer");
            result.put("error", e.getMessage());
            result.put("diagnosis", "Las credenciales SASL del Consumer son incorrectas.");
            logger.error("❌ Error de autenticación SASL en Consumer: {}", e.getMessage());

        } catch (org.apache.kafka.common.errors.TopicAuthorizationException e) {
            result.put("status", "TOPIC_AUTH_ERROR");
            result.put("message", "Sin permisos para leer del topic");
            result.put("error", e.getMessage());
            result.put("diagnosis", "El usuario no tiene permisos para leer del topic 'actriz-events'.");
            logger.error("❌ Error de autorización del topic en Consumer: {}", e.getMessage());

        } catch (Exception e) {
            result.put("status", "UNKNOWN_ERROR");
            result.put("message", "Error inesperado en Consumer");
            result.put("error", e.getMessage());
            result.put("error_class", e.getClass().getSimpleName());
            logger.error("💥 Error inesperado en Consumer: {} - {}", e.getClass().getSimpleName(), e.getMessage());
        }

        return result;
    }
}
