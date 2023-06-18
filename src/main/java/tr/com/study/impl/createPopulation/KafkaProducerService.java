package tr.com.study.impl.createPopulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tr.com.study.data.Gene;
import tr.com.study.data.Ugur;

@Component
public class KafkaProducerService {


   @Autowired
    KafkaTemplate<String, Ugur> kafkaTemplate;



    public void sendMessage(String topic, Ugur ugur) {
        kafkaTemplate.send(topic,ugur);
        System.out.println("Message sent to Kafka topic: " + topic);
    }
}