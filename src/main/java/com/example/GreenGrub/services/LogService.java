package com.example.GreenGrub.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class LogService {

    @Value("${elk.elasticsearch.url:http://localhost:9200}")
    private String elasticsearchUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendApplicationLog(String level, String message, Map<String, Object> context) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> logEntry = Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "application", "spring-boot-app",
                    "level", level,
                    "message", message,
                    "context", context != null ? context : Map.of()
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(logEntry, headers);
            String indexUrl = elasticsearchUrl + "/spring-logs/_doc";

            restTemplate.postForEntity(indexUrl, request, String.class);
        } catch (Exception e) {
            // Fallback to standard logging
            System.err.println("Failed to send log to ELK: " + e.getMessage());
        }
    }
}
