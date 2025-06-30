package com.example.chaos.logic;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;

public interface ChaosPattern {
    List<ProducerRecord<String, String>> apply(ConsumerRecord<String, String> record);
}
