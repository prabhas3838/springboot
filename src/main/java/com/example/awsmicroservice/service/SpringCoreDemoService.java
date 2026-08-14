package com.example.awsmicroservice.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Pure Spring Boot @Service
 * This class handles business logic and has ZERO AWS dependencies.
 */
@Service
public class SpringCoreDemoService {

    // Simulating an in-memory database just to show how Spring manages data
    private final Map<Integer, String> database = new HashMap<>();

    public SpringCoreDemoService() {
        database.put(1, "Spring Boot is awesome!");
        database.put(2, "Dependency Injection makes life easy.");
    }

    public String getMessageById(int id) {
        if (!database.containsKey(id)) {
            // Throwing a standard Java exception that our Controller will catch
            throw new IllegalArgumentException("Message ID " + id + " not found!");
        }
        return database.get(id);
    }
}
