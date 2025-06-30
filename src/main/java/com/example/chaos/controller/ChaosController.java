package com.example.chaos.controller;

import com.example.chaos.config.ChaosConfig;
import com.example.chaos.logic.ChaosInjector;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chaos")
public class ChaosController {

    @Autowired private ChaosConfig config;
    @Autowired private ChaosInjector injector;

    @GetMapping("/config")
    public Map<String, Object> getCurrentState() {
        return Map.of(
            "config", config,
            "activePattern", injector.getCurrentPattern(),
            "availablePatterns", injector.availablePatterns()
        );
    }

    @PostMapping("/pattern")
    public ResponseEntity<?> switchPattern(@RequestParam String name) {
        boolean ok = injector.switchPattern(name);
        return ok ? ResponseEntity.ok("Switched to: " + name)
                  : ResponseEntity.badRequest().body("Pattern not found: " + name);
    }

    @PostMapping("/config")
    public String updateChaos(@RequestBody ChaosConfig updated) {
        BeanUtils.copyProperties(updated, config);
        return "Chaos config updated";
    }
}
