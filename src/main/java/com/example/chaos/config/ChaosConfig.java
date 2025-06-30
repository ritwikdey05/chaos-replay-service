package com.example.chaos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "chaos")
public class ChaosConfig {
    private double dropProbability;
    private double duplicateProbability;
    private long maxDelayMs;
    private boolean corruptPayload;
    private int replayRatePerSec;
}
