package com.example.springbootcrudapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller للتحقق من حالة التطبيق
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthController {

    @Autowired
    private DataSource dataSource;

    /**
     * Health Check Endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();

        try {
            // التحقق من الاتصال بقاعدة البيانات
            try (Connection connection = dataSource.getConnection()) {
                boolean isValid = connection.isValid(5); // 5 seconds timeout

                response.put("status", "UP");
                response.put("application", "Spring Boot JWT CRUD API");
                response.put("version", "1.0.0");
                response.put("database", isValid ? "Connected" : "Disconnected");
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("application", "Spring Boot JWT CRUD API");
            response.put("version", "1.0.0");
            response.put("database", "Error: " + e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(503).body(response);
        }
    }

    /**
     * Application Info Endpoint
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();

        response.put("application", "Spring Boot JWT CRUD API");
        response.put("description", "RESTful API with JWT Authentication for User and Transaction Management");
        response.put("version", "1.0.0");
        response.put("java.version", System.getProperty("java.version"));
        response.put("spring.boot.version", "3.1.2");
        response.put("database", "Oracle Database 12c");
        response.put("author", "Your Name");
        response.put("timestamp", System.currentTimeMillis());

        // معلومات عن الـ Endpoints
        Map<String, Object> endpoints = new HashMap<>();
        endpoints.put("auth", "/api/auth/*");
        endpoints.put("users", "/api/users/*");
        endpoints.put("transactions", "/api/transactions/*");
        endpoints.put("health", "/api/health");
        endpoints.put("info", "/api/info");

        response.put("endpoints", endpoints);

        return ResponseEntity.ok(response);
    }
}
