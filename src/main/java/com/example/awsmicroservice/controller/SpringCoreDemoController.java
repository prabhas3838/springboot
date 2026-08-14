package com.example.awsmicroservice.controller;

import com.example.awsmicroservice.service.SpringCoreDemoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Pure Spring Boot @RestController
 * This class shows how Spring handles HTTP requests, Dependency Injection, 
 * Properties, and Error Handling with NO AWS code at all.
 */
@RestController
@RequestMapping("/api/core")
public class SpringCoreDemoController {

    // 1. Dependency Injection: Spring automatically creates the Service and injects it here
    private final SpringCoreDemoService demoService;

    // 2. Reading Properties: Spring grabs this directly from application.properties!
    @Value("${server.port}")
    private String serverPort;

    // The Constructor where Spring injects the dependencies
    public SpringCoreDemoController(SpringCoreDemoService demoService) {
        this.demoService = demoService;
    }

    // 3. Simple GET request mapped to a specific path variable
    @GetMapping("/message/{id}")
    public ResponseEntity<String> getMessage(@PathVariable int id) {
        // Calling our injected service
        String message = demoService.getMessageById(id);
        
        // Combining the service result with the property we read from application.properties
        return ResponseEntity.ok("Message: '" + message + "' (Served on port " + serverPort + ")");
    }

    // 4. Global Error Handling for this Controller
    // If ANY method in this controller throws an IllegalArgumentException, Spring intercepts it here!
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFoundException(IllegalArgumentException e) {
        // We gracefully return a 404 Not Found with the error message instead of crashing the server
        return ResponseEntity.status(404).body("Error intercepted by Spring: " + e.getMessage());
    }
}
