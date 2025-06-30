package com.example.chaos.logic;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class ChaosInjector {

    private final Map<String, ChaosPattern> patternRegistry;
    private final AtomicReference<ChaosPattern> activePattern = new AtomicReference<>();

    @Autowired
    public ChaosInjector(ApplicationContext context) {
        this.patternRegistry = context.getBeansOfType(ChaosPattern.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.activePattern.set(patternRegistry.get("delayAndDrop")); // default
    }

    public List<ProducerRecord<String, String>> process(ConsumerRecord<String, String> record) {
        return activePattern.get().apply(record);
    }

    public Set<String> availablePatterns() {
        return patternRegistry.keySet();
    }

    public boolean switchPattern(String name) {
        ChaosPattern pattern = patternRegistry.get(name);
        if (pattern != null) {
            activePattern.set(pattern);
            return true;
        }
        return false;
    }

    public String getCurrentPattern() {
        return patternRegistry.entrySet().stream()
                .filter(e -> e.getValue() == activePattern.get())
                .map(Map.Entry::getKey)
                .findFirst().orElse("unknown");
    }
}
