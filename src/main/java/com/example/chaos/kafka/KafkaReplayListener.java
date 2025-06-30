package com.example.chaos.kafka;

import com.example.chaos.config.ChaosConfig;
import com.example.chaos.logic.ChaosInjector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaReplayListener {

    @Autowired private KafkaTemplate<String, String> producer;
    @Autowired private ChaosInjector injector;
    @Autowired private ChaosConfig config;

    @KafkaListener(topics = "your-events-history", groupId = "chaos-replay")
    public void handle(ConsumerRecord<String, String> record) throws InterruptedException {
        Thread.sleep(1000 / config.getReplayRatePerSec());
        List<ProducerRecord<String, String>> outputs = injector.process(record);
        outputs.forEach(producer::send);
    }
}
