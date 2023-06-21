package tr.com.study.impl.createPopulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tr.com.study.data.Gene;

@Component
public class KafkaProducerService {


   @Autowired
    KafkaTemplate<String, Gene> kafkaTemplate;



    public void sendMessage(String topic, Gene gene) {
        kafkaTemplate.send(topic,gene);
        System.out.println("Message sent to Kafka topic: " + topic);
    }
}