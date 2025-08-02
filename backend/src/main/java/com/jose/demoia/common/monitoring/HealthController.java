package com.jose.demoia.common.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/custom")
    public ResponseEntity<Map<String, Object>> customHealthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("timestamp", LocalDateTime.now());
        health.put("status", "UP");
        health.put("application", "Demo IA Application");

        // Check database connectivity
        try (Connection connection = dataSource.getConnection()) {
            health.put("database", "UP");
            health.put("database_url", connection.getMetaData().getURL());
        } catch (SQLException e) {
            health.put("database", "DOWN");
            health.put("database_error", e.getMessage());
        }

        // Check memory usage
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        Map<String, Object> memory = new HashMap<>();
        memory.put("max", maxMemory / 1024 / 1024 + " MB");
        memory.put("total", totalMemory / 1024 / 1024 + " MB");
        memory.put("used", usedMemory / 1024 / 1024 + " MB");
        memory.put("free", freeMemory / 1024 / 1024 + " MB");
        memory.put("usage_percentage", Math.round((double) usedMemory / maxMemory * 100));

        health.put("memory", memory);

        return ResponseEntity.ok(health);
    }

    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> getVersion() {
        Map<String, String> version = new HashMap<>();
        version.put("application", "Demo IA Application");
        version.put("version", "1.0.0");
        version.put("build_time", LocalDateTime.now().toString());
        version.put("java_version", System.getProperty("java.version"));
        return ResponseEntity.ok(version);
    }
}
