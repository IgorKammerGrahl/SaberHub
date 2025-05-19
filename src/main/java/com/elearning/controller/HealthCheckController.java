package com.elearning.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheckController {

    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public HealthCheckController(JdbcTemplate jdbcTemplate,
                                 MongoTemplate mongoTemplate,
                                 RedisTemplate<String, String> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mongoTemplate = mongoTemplate;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        
        // PostgreSQL check
        try {
            status.put("postgres", jdbcTemplate.queryForObject("SELECT version()", String.class));
        } catch (Exception e) {
            status.put("postgres", "error: " + e.getMessage());
        }
        
        // MongoDB check
        try {
            status.put("mongodb", mongoTemplate.executeCommand("{ buildInfo: 1 }").getString("version"));
        } catch (Exception e) {
            status.put("mongodb", "error: " + e.getMessage());
        }

        // Redis check (versão corrigida e segura)
        try {
            // Verificação robusta usando PING/PONG
            String response = redisTemplate.execute((RedisCallback<String>) connection -> 
                connection.ping() // Não precisa fechar a conexão manualmente (gerenciada pelo Spring)
            );
            
            status.put("redis", "PONG".equals(response) ? "ok" : "error: Unexpected response");
            
        } catch (Exception e) {
            status.put("redis", "error: " + e.getMessage().replaceAll("pass.*?@", "***")); // Ofusca senha em logs
        }

        return ResponseEntity.ok(status);
    }
}