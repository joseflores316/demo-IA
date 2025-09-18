package com.jose.demoia.common.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/diagnostics")
public class NetworkDiagnosticsController {

    private static final Logger logger = LoggerFactory.getLogger(NetworkDiagnosticsController.class);

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
}
