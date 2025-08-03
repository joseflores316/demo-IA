package com.jose.demoia.common.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/monitoring")
public class MonitoringDashboardController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "monitoring-dashboard";
    }

    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> getApplicationStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // Basic info
        status.put("timestamp", LocalDateTime.now());
        status.put("application", "Demo IA Application");
        status.put("version", "1.0.0");
        status.put("environment", "production");
        
        // Database status
        Map<String, Object> database = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            database.put("status", "UP");
            database.put("url", connection.getMetaData().getURL());
            database.put("driver", connection.getMetaData().getDriverName());
        } catch (SQLException e) {
            database.put("status", "DOWN");
            database.put("error", e.getMessage());
        }
        status.put("database", database);
        
        // Memory info
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        Map<String, Object> memory = new HashMap<>();
        memory.put("max_mb", maxMemory / 1024 / 1024);
        memory.put("total_mb", totalMemory / 1024 / 1024);
        memory.put("used_mb", usedMemory / 1024 / 1024);
        memory.put("free_mb", freeMemory / 1024 / 1024);
        memory.put("usage_percentage", Math.round((double) usedMemory / maxMemory * 100));
        status.put("memory", memory);
        
        // System info
        Map<String, Object> system = new HashMap<>();
        system.put("java_version", System.getProperty("java.version"));
        system.put("os_name", System.getProperty("os.name"));
        system.put("processors", Runtime.getRuntime().availableProcessors());
        status.put("system", system);
        
        return status;
    }
}
