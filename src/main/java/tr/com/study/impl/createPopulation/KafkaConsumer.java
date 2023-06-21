package tr.com.study.impl.createPopulation;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tr.com.study.data.Gene;

@Component
public class KafkaConsumer {

    @KafkaListener(
            topics = "erik",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(Gene gene) {
        System.out.println("Received message: " + gene);
        // Process the received User object as needed
    }
}