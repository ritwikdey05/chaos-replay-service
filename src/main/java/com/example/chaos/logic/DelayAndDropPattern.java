package com.example.chaos.logic;

import com.example.chaos.config.ChaosConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component("delayAndDrop")
public class DelayAndDropPattern implements ChaosPattern {

    @Autowired private ChaosConfig config;

    @Override
    public List<ProducerRecord<String, String>> apply(ConsumerRecord<String, String> record) {
        List<ProducerRecord<String, String>> result = new ArrayList<>();
        if (Math.random() < config.getDropProbability()) return result;
        try { 
            Thread.sleep(ThreadLocalRandom.current().nextLong(config.getMaxDelayMs())); 
        } catch (InterruptedException ignored) {
            
        }
        result.add(new ProducerRecord<>("your-events-chaos", record.key(), record.value()));
        return result;
    }
}
