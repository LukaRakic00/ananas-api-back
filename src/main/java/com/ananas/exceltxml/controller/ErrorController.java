package com.ananas.exceltxml.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ErrorController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Excel to XML API");
        response.put("version", "0.0.1-SNAPSHOT");
        response.put("status", "running");
        response.put("endpoints", Map.of(
            "frontend", "/api/excel",
            "ananas", "/api/ananas/excel"
        ));
        response.put("documentation", "See README.md for API documentation");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }
}

