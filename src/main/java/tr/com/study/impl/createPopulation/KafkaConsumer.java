package tr.com.study.impl.createPopulation;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tr.com.study.data.Gene;
import tr.com.study.data.Ugur;

@Component
public class KafkaConsumer {

    @KafkaListener(
            topics = "deneme",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(Ugur  ugur) {
        System.out.println("Received message: " + ugur);
        // Process the received User object as needed
    }
}